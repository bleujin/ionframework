package net.ion.framework.template;

import java.util.Stack;

import net.ion.framework.template.parse.AttributeValueEvalParser;
import net.ion.framework.template.parse.Marker;
import net.ion.framework.template.parse.Parser;
import net.ion.framework.template.parse.ParserException;
import net.ion.framework.template.ref.Context;
import net.ion.framework.template.tagext.BodyTag;
import net.ion.framework.template.tagext.MultiPageTag;
import net.ion.framework.template.tagext.PageContext;
import net.ion.framework.template.tagext.PageInfo;
import net.ion.framework.template.tagext.PageWriter;
import net.ion.framework.template.tagext.Tag;
import net.ion.framework.template.tagext.TagAttributeInfo;
import net.ion.framework.template.tagext.TagException;
import net.ion.framework.template.tagext.TagInfo;
import net.ion.framework.template.tagext.TagLibraryInfo;
import net.ion.framework.template.tagext.TemplateContext;
import net.ion.framework.template.tagext.UniPageTag;
import net.ion.framework.util.InstanceCreationException;
import net.ion.framework.util.InstanceCreator;

/**
 * CompiledTemplate을 실행한다.
 * 
 * @author Kim Sanghoon wizest@i-on.net
 * @version 1.0
 */

public class TemplateRuntime {
	private final long WATCHDOG_THRESHOLD;

	private PageReceiver pageReceiver;

	private TagLibraryInfo libraryInfo;
	private TagHandlerHost handlerHost;

	private boolean breakRequest;

	/**
	 * @param rs
	 *            RenderingSupport tag handler, parser 정보
	 * @param pageReceiver
	 *            PageReceiver 생성된 page를 보내줄 곳
	 * @param pageTimeoutMsec
	 *            long 한 페이지당 제한시간
	 */
	public TemplateRuntime(RenderingSupport rs, PageReceiver pageReceiver, long pageTimeoutMsec) {
		this.pageReceiver = pageReceiver;

		this.libraryInfo = rs.getTagLibraryInfo();
		this.handlerHost = rs.getTagHandlerHost();

		this.WATCHDOG_THRESHOLD = pageTimeoutMsec;
	}

	private static int getLineNumber(String templateText, int beginIndex) {
		int length = Math.min(templateText.length(), beginIndex);
		int line = 1;
		for (int i = 0; i < length; ++i) {
			if (templateText.charAt(i) == '\n')
				++line;
		}
		return line;
	}

	/**
	 * attribute value에 사용한 run-time 변수를 결정한다.
	 * 
	 * @param tagInfo
	 * @param context
	 *            null일 경우 context로 부터 값을 찾지 않는다. 그러므로 [%변수%] -> null로 치환한다.
	 * @param evalValueParser
	 * @param map
	 *            TemplateCompiler 가 처리한 reflectionMap
	 * @param parentRuntimeTag
	 *            현재 template runtime 을 실행시키고 있는 tag (baseTagSupport의 getParentRuntimeTag()에 의해 접근할 tag를 지정), 별도 지정사항이 없을 경우 null
	 * @return
	 * @throws RuntimeException
	 */
	public static ReflectionMap evalReflectionMap(TagInfo tagInfo, Parser evalValueParser, final ReflectionMap staticMap, Context context, Tag parentRuntimeTag)
			throws TemplateRuntimeException {
		ReflectionMap map = staticMap.copy();
		map.append("parentRuntimeTag", parentRuntimeTag);

		String[] names = map.getNames();
		int length = names.length;
		if (length == 3)
			return map; // 현재 reflectionMap에 기본적으로 3개의 정보를 가지고 있다(id,tagMarker,parentRuntimeTag) 3개일 경우 run-time 처리가 필요없다.

		int i = 0;

		Object[] values = new Object[length];// map.getValues();
		System.arraycopy(map.getValues(), 0, values, 0, length);

		TagAttributeInfo attrInfo = null;
		try {
			for (; i < length; ++i) {
				attrInfo = tagInfo.getAttribute(names[i]);
				if (attrInfo != null && attrInfo.isRtexprvalue()) {
					StringBuffer evalString = new StringBuffer();

					// evaluate - context로 부터 name을 찾아서 치환한다.
					{
						String src = values[i].toString();
						evalValueParser.initialize(src);
						int pivot = 0;

						for (;;) {
							Marker mark = evalValueParser.parseNext();
							if (mark == null) {
								evalString.append(src.substring(pivot));
								break;
							}
							evalString.append(src.substring(pivot, mark.getBeginIndex()));
							if (context == null) {
								// evalString.append("null"); ibr_1295..
								evalString.append("");
							} else {
								Object obj = context.findAttribute(mark.getValue());
								if (obj == null)
									throw new TemplateRuntimeException("not found the value in contexts, name=" + mark.getValue());
								evalString.append(obj);
							}
							pivot = mark.getEndIndex();
						}
					}

					values[i] = InstanceCreator.getObjectInstance(attrInfo.getTypeName(), evalString.toString());
				}
			}
			evalValueParser.release();
		} catch (ParserException ex) {
			throw new java.lang.RuntimeException(ex);
		} catch (InstanceCreationException ex) {
			throw new TemplateRuntimeException("incorrect attribute type at '" + names[i] + "'", ex);
		} catch (ClassNotFoundException ex) {
			throw new TemplateRuntimeException("incorrect attribute type. class not found:" + attrInfo.getTypeName() + " at '" + names[i] + "'", ex);
		}

		return new ReflectionMap(names, values);
	}

	public void exec(TemplateContext templateContext, CompiledTemplate compiledTemplate) throws TemplateRuntimeException {
		exec(templateContext, compiledTemplate, null, null);
	}

	public void exec(TemplateContext templateContext, CompiledTemplate compiledTemplate, String templateText) throws TemplateRuntimeException {
		exec(templateContext, compiledTemplate, templateText, null);
	}

	/**
	 * 템플릿을 실행한다.
	 * 
	 * @param templateContext
	 *            TemplateContext 한개의 템플릿 작업 공간
	 * @param compiledTemplate
	 *            CompiledTemplate 처리할 템플릿
	 * @param templateText
	 *            String 템플릿 내용 (에러가 났을 경우 라인번호를 표시하기 위해 필요)
	 * @param parentRuntimeTag
	 *            Tag 부모테그 (별도 지정할 것이 없을 경우 null 입력)
	 * @throws TemplateRuntimeException
	 */
	public void exec(TemplateContext templateContext, CompiledTemplate compiledTemplate, String templateText, Tag parentRuntimeTag)
			throws TemplateRuntimeException {
		// 파라미터로 templateText를 받는 이유는 Error가 발생했을 때 compiledTemplate에 들어있는 eval mark를 참고하여 line번호를 찾기 위해서다. -> 단지 이 하나의 이유만으로.. 억울하다. -_-"

		resetBreakRequest();

		CompiledTemplate binary = compiledTemplate;

		OperationCode code = binary.getOperationCode();
		OperationData data = binary.getOperationData();

		Tag handler = parentRuntimeTag;
		Tag parentHandler = (handler == null) ? null : handler.getParent();

		PageContext pageContext = new PageContext();
		PageWriter writer = new PageWriter();
		PageInfo pageInfo = null;

		Marker tagMark = null;
		Parser evalValueP = new AttributeValueEvalParser();

		long watchDogCounter = System.currentTimeMillis();
		try {
			DECODING: {
				int cp = 0; // code pointer
				int[] opcode = null;
				Object opdata = null;

				Stack<Tag> handlerStack = new Stack<Tag>();
				Stack<Boolean> isBodyBufferedStack = new Stack<Boolean>();

				String handlerName = null;
				ReflectionMap reflectionMap = null;

				while (true) {
					if (breakRequest)
						throw new TemplateRuntimeException("User requested template-execution break.");

					opcode = code.getCodeAt(cp);

					// decoder
					switch (opcode[0]) {
					case OperationCode.OPCODE_TERMINATE:
						break DECODING;

					case OperationCode.OPCODE_EXCEPTION:
						opdata = data.getDataAt(opcode[1]);
						throw new TemplateRuntimeException(opdata.toString());

					case OperationCode.OPCODE_GOTO:
						cp = opcode[1];
						break;

					case OperationCode.OPCODE_NO_OPERATION:
						cp++;
						break;

					case OperationCode.OPCODE_PRINT:
						opdata = data.getDataAt(opcode[1]);
						pageContext.getOut().print(opdata);
						cp++;

						{
							// runtime자신을 보호하기 위해 page 한장을 얻는데 시간이 너무 오래 걸리면 exception 내버리고 종료한다.
							// 원래 loop의 최상단에 있었지만 비교를 너무 하면 좋을께 없으니 적당히 걸리는 부분으로 옮김
							if ((System.currentTimeMillis() - watchDogCounter) > WATCHDOG_THRESHOLD)
								throw new TemplateRuntimeException("time-out to generate a page, threshold:" + WATCHDOG_THRESHOLD + "ms per page");
						}

						break;

					case OperationCode.OPCODE_LOAD_HANDLER: // uni page tag handler
						handlerStack.push(handler); // push a handler
						handlerName = (String) data.getDataAt(opcode[1]);

						parentHandler = handler;
						handler = handlerHost.getTagHandler(handlerName);
						handler.setParent(parentHandler);

						((UniPageTag) handler).setPageContext(pageContext); // !!!!!

						reflectionMap = evalReflectionMap(libraryInfo.getTag(handlerName), evalValueP, (ReflectionMap) data.getDataAt(opcode[2]), pageContext,
								parentRuntimeTag);
						InstanceCreator.setPropertyValues(handler, reflectionMap.getNames(), reflectionMap.getValues());
						cp++;
						break;

					case OperationCode.OPCODE_LOAD_PAGE_HANDLER: // multi page tag handler
						handlerStack.push(handler); // push a handler
						handlerName = (String) data.getDataAt(opcode[1]);

						parentHandler = handler;
						handler = handlerHost.getTagHandler(handlerName);
						handler.setParent(parentHandler);

						((MultiPageTag) handler).setTemplateContext(templateContext); // !!!!!

						reflectionMap = evalReflectionMap(libraryInfo.getTag(handlerName), evalValueP, (ReflectionMap) data.getDataAt(opcode[2]),
								templateContext, parentRuntimeTag);
						InstanceCreator.setPropertyValues(handler, reflectionMap.getNames(), reflectionMap.getValues());
						cp++;
						break;

					case OperationCode.OPCODE_RELEASE_HANDLER:
						handler.release();
						handlerHost.releaseTagHandler(handlerName, handler);
						handler = (Tag) handlerStack.pop(); // pop a handler
						if (handler == null) {
							handlerName = null;
							parentHandler = null;
						} else {
							handlerName = libraryInfo.getTag(handler).getTagName();
							parentHandler = handler.getParent();
						}
						cp++;
						break;

					case OperationCode.OPCODE_DO_START_TAG:
						switch (((UniPageTag) handler).doStartTag()) {
						case UniPageTag.SKIP_BODY:
							isBodyBufferedStack.push(Boolean.FALSE);
							cp = opcode[1];
							break;
						case UniPageTag.EVAL_BODY_INCLUDE:
							isBodyBufferedStack.push(Boolean.FALSE);
							cp = opcode[2];
							break;
						case BodyTag.EVAL_BODY_BUFFERED:
							((BodyTag) handler).setBodyContent(pageContext.pushBody());
							((BodyTag) handler).doInitBody();

							isBodyBufferedStack.push(Boolean.TRUE);
							cp = opcode[3];
							break;
						default:
							throw new TemplateRuntimeException("incorrect return value of doStartTag() at handlerName=" + handlerName);
						}
						break;

					case OperationCode.OPCODE_DO_AFTER_BODY:
						switch (((BodyTag) handler).doAfterBody()) {
						case UniPageTag.SKIP_BODY:
							cp = opcode[1];
							break;
						case BodyTag.EVAL_BODY_AGAIN:
							cp = opcode[2];
							break;
						default:
							throw new TemplateRuntimeException("incorrect return value of doAfterBody() at handlerName=" + handlerName);
						}
						break;

					case OperationCode.OPCODE_DO_END_TAG:
						if (((Boolean) isBodyBufferedStack.pop()).booleanValue()) {
							pageContext.popBody();

						}
						switch (((UniPageTag) handler).doEndTag()) {
						case Tag.SKIP_PAGE:
							cp = opcode[1];
							break;
						case Tag.EVAL_PAGE:
							cp = opcode[2];
							break;
						default:
							throw new TemplateRuntimeException("incorrect return value of doEndTag() at handlerName=" + handlerName);
						}
						break;

					case OperationCode.OPCODE_DO_START_TEMPLATE:
						switch (((MultiPageTag) handler).doStartTemplate()) {
						case MultiPageTag.SKIP_PAGE:
							cp = opcode[1];
							break;
						case MultiPageTag.EVAL_PAGE:
							cp = opcode[2];
							((MultiPageTag) handler).doInitPage();
							break;
						default:
							throw new TemplateRuntimeException("incorrect return value of doStartPage() at handlerName=" + handlerName);
						}
						break;

					case OperationCode.OPCODE_DO_CREATE_PAGEINFO:
						// initialize page context for uni page tags
						pageInfo = ((MultiPageTag) handler).createPageInfo();
						pageContext.initialize(templateContext, writer, pageInfo);
						cp++;
						break;

					case OperationCode.OPCODE_DO_AFTER_PAGE:
						boolean doGenerate = true;
						pageContext.release();
						switch (((MultiPageTag) handler).doAfterPage()) {
						case MultiPageTag.SKIP_PAGE:
							cp = opcode[1];
							break;
						case MultiPageTag.SKIP_PAGE_WITHOUT_PAGE_GENERATION:
							doGenerate = false;
							cp = opcode[1];
							break;
						case MultiPageTag.EVAL_PAGE_AGAIN:
							cp = opcode[2];
							break;
						case MultiPageTag.EVAL_PAGE_AGAIN_WITHOUT_PAGE_GENERATION:
							doGenerate = false;
							cp = opcode[2];
							break;
						default:
							throw new TemplateRuntimeException("incorrect return value of doAfterPage() at handlerName=" + handlerName);
						}

						// G E N E R A T E D P A G E --------------------------------------------------------------
						if (doGenerate) {
							String generatedPage = writer.toString();
							Page page = new Page(pageInfo, generatedPage);
							this.pageReceiver.receivePage(page); // page가 생성되는 신성한 곳!!
						}
						writer.clear();
						watchDogCounter = System.currentTimeMillis(); // reset a watchDogCounter
						break;

					case OperationCode.OPCODE_DO_END_TEMPLATE:
						((MultiPageTag) handler).doEndTemplate();
						cp++;
						break;

					case OperationCode.OPCODE_EVAL_MARK:
						tagMark = (Marker) data.getDataAt(opcode[1]);
						cp++;
						break;

					default:
						throw new TemplateRuntimeException("incorrect operation code at code address=" + cp);
					}
				}
			}
		} catch (InstanceCreationException ie) {
			String line = (tagMark == null || templateText == null) ? "" : " in Line " + getLineNumber(templateText, tagMark.getBeginIndex());
			throw new TemplateRuntimeException(tagMark, "could not reflect tag attributes for the handler" + line, ie);
		} catch (TagException te) {
			String line = (tagMark == null || templateText == null) ? "" : " in Line " + getLineNumber(templateText, tagMark.getBeginIndex());
			throw new TemplateRuntimeException(tagMark, te.getMessage() + line, te);
		} catch (TemplateRuntimeException re) {
			if (tagMark == null || templateText == null)
				throw re;
			String line = " in Line " + getLineNumber(templateText, tagMark.getBeginIndex());
			throw new TemplateRuntimeException(tagMark, re.getMessage() + line, re);
		} catch (RuntimeException ex) { // 이 에러는 반드시 나면 안된다!! 릴리즈 전에 다 잡아야 한다. -> report에 unexpected exception으로 기록된다.
			if (tagMark == null || templateText == null)
				throw ex;
			String line = " in Line " + getLineNumber(templateText, tagMark.getBeginIndex());
			String msg = "unexpected exception occured : " + (ex.getMessage() == null ? ex.getClass().getName() : ex.getMessage());
			throw new RuntimeException(msg + line, ex);
		} finally {
			if (handler != null && parentRuntimeTag != handler) {
				handler.release();
				handlerHost.releaseTagHandler(libraryInfo.getTag(handler).getTagName(), handler);
			}
			pageContext = null;
			writer = null;
			pageInfo = null;
		}
	}

	/**
	 * 현재 exec 작업을 종료한다.
	 */
	public void breakExec() {
		breakRequest = true;
	}

	private void resetBreakRequest() {
		breakRequest = false;
	}
}
