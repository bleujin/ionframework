package net.ion.framework.template;

import java.util.ArrayList;
import java.util.Stack;

import net.ion.framework.template.parse.Marker;
import net.ion.framework.template.parse.Parser;
import net.ion.framework.template.parse.ParserException;
import net.ion.framework.template.parse.TagNameParser;
import net.ion.framework.template.parse.TagParser;
import net.ion.framework.template.tagext.BodyTagSupport;
import net.ion.framework.template.tagext.PageTagSupport;
import net.ion.framework.template.tagext.TagAttributeInfo;
import net.ion.framework.template.tagext.TagInfo;
import net.ion.framework.template.tagext.TagLibraryInfo;
import net.ion.framework.template.tagext.TagSupport;
import net.ion.framework.util.InstanceCreationException;
import net.ion.framework.util.InstanceCreator;

/**
 * text template을 code와 data로 분리하여 각각 operationCode, operationData를 만들어 compiledTemplate을 생성한다. 컴파일된 템플릿은 templateRuntime으로 실행시킬 수 있다.
 * 
 * @author Kim Sanghoon wizest@i-on.net
 * @version 1.0
 */

public class TemplateCompiler {
	/**
	 * COMPILE_MODE<br/>
	 * OUTER_TEMPLATE : 모든 tag를 다 사용할 수 있다. INNER_TEMPLATE : page tag를 제외한 모든 tag를 사용할 수 있다.
	 * 
	 * 주) Template은 단 1개의 page tag를 가질 수 있는데 template이 타 template을 포함할 경우 타 템플릿이 이미 page tag를 가질 경우 현재 템플릿에도 page tag가 있으므로 문제가 된다. 때문에 포함할 수 있는 template은 inner template 밖에 없다.
	 */
	public static final int COMPILE_MODE_OUTER_TEMPLATE = Template.TYPE_OUTER_TEMPLATE;
	public static final int COMPILE_MODE_INNER_TEMPLATE = Template.TYPE_INNER_TEMPLATE;

	private TagLibraryInfo libraryInfo = null;

	private ParserHost tagParserHost = null;
	private ParserHost tagNameParserHost = null;
	private ParserHost attrParserHost = null;
	private ParserHost attrNameParserHost = null;
	private ParserHost attrValueParserHost = null;

	public TemplateCompiler(RenderingSupport rs) {
		this.libraryInfo = rs.getTagLibraryInfo();

		this.tagParserHost = rs.getTagParserHost();
		this.tagNameParserHost = rs.getTagNameParserHost();
		this.attrParserHost = rs.getAttrParserHost();
		this.attrNameParserHost = rs.getAttrNameParserHost();
		this.attrValueParserHost = rs.getAttrValueParserHost();
	}

	/**
	 * handler에 reflection으로 값을 집어 넣을 때 사용할 parameter들을 추출한다.
	 * 
	 * @param tagName
	 *            TagSupport 의 id 필드에 저장할 값 (template에서 사용한 tag이름), 주의: tag handler 이름이 아니다.
	 * @param tagInfo
	 * @param tagMark
	 * @return
	 * @throws CompilerException
	 */
	private ReflectionMap retrieveParametersToReflectAttributes(String tagName, TagInfo tagInfo, Marker tagMark) throws CompilerException {
		Parser ap = this.attrParserHost.getParser();
		Parser np = this.attrNameParserHost.getParser();
		Parser vp = this.attrValueParserHost.getParser();
		try {
			ArrayList<String> names = new ArrayList<String>();
			ArrayList<Object> values = new ArrayList<Object>();

			String name;
			Object value;

			Marker attrMark = null;
			Marker nameMark = null;
			Marker valueMark = null;

			boolean existsDefaultAttribute = false;
			int numberOfRequiredAttributes = 0;

			ap.initialize(tagMark);

			try {
				// tagMark를 저장한다.
				names.add("tagMarker");
				values.add(tagMark);

				// tag name을 TagSupport.id 필드에 저장한다.
				// Tag handler에서 template에서 어떤 이름으로 handler를 invoke했는지 알기 위해
				// (default tag 같은 경우 동일한 handler를 다양한 이름으로 부른다.)
				names.add("id");
				values.add(tagName);

				while (true) {
					// reset
					name = null;
					value = null;

					// set attrMark
					attrMark = ap.parseNext();
					if (attrMark == null) {
						break; // ending condition
					}
					// end of attrMark

					np.initialize(attrMark);
					vp.initialize(attrMark);

					// set name
					try {
						nameMark = np.parseNext();
						name = nameMark.getValue();
					} catch (NullPointerException ex) {
						// default attribute 처리
						if (name == null) {
							if (existsDefaultAttribute) {
								throw new CompilerException(tagMark, "duplicated default attribute at '" + attrMark.getValue() + "'");
							} else {
								existsDefaultAttribute = true;
								name = tagInfo.getDefaultAttributeName(); // default attribute

								// default attribute가 지정되어 있지 않을 경우
								if (name == null) {
									throw new CompilerException(tagMark, "does not allow the default attribute at '" + attrMark.getValue() + "'");
								}
							}
						}
					}
					// end of name

					// set value
					valueMark = vp.parseNext();
					if (valueMark == null) {
						throw new CompilerException(tagMark, "null value at '" + attrMark.getValue() + "'");
					}
					String valueStr = valueMark.getValue();

					TagAttributeInfo attrInfo = tagInfo.getAttribute(name);
					if (attrInfo == null) {
						throw new CompilerException(tagMark, "unknown attribute name at '" + attrMark.getValue() + "'");
					}
					if (attrInfo.isRequired()) {
						numberOfRequiredAttributes++;

					}

					String className = attrInfo.getTypeName();

					// if run-time value
					if (attrInfo.isRtexprvalue()) {
						value = valueStr; // template run-time environment에서 이 값을 재처리해줘야한다!!!
					}
					// if compile-time value
					else {
						try {
							value = InstanceCreator.getObjectInstance(className, valueStr);
						} catch (InstanceCreationException ex) {
							throw new CompilerException(tagMark, "incorrect attribute type at '" + attrMark.getValue() + "'", ex);
						} catch (ClassNotFoundException ex) {
							throw new CompilerException(tagMark,
									"incorrect attribute type. class not found:" + className + " at '" + attrMark.getValue() + "'", ex);
						}
					}

					names.add(name);
					values.add(value);
				}

				if (numberOfRequiredAttributes < tagInfo.getNumberOfRequiredAttributes())
					throw new CompilerException(tagMark, "too few required attributes");

				ReflectionMap rm = new ReflectionMap(names.toArray(new String[0]), values.toArray());
				// System.out.println(rm);
				return rm;
			} catch (ParserException pex) {
				throw new CompilerException(tagMark, "parsing exception at '" + attrMark.getValue() + "'", pex);
			}
		} finally {
			this.attrParserHost.releaseParser(ap);
			this.attrNameParserHost.releaseParser(np);
			this.attrValueParserHost.releaseParser(vp);
		}
	}

	/**
	 * 현재 tag parser가 속해 있는 body의 end tag 바로 앞으로 건너뛴다. (단 tag의 body content가 tag dependent라고 가정한다)
	 * 
	 * @param tagParser
	 * @throws ParserException
	 */
	private void parseToEndTagOfCurrentTagDependentBody(Marker nameStartMark, Parser tagParser) throws ParserException {
		/**
		 * 원리) start tag가 나타나면 depth를 1씩 증가하고 end tag가 나타나면 depth를 1씩 감소한다.
		 * 
		 * 만일 계층 구조가 제대로 되어 있다면 depth가 0이 되는 순간 tag가 닫히는 순간이다.
		 */

		int depth = 1; // 이 메소드를 부른 상태에 이미 열려져 있는 상태(start tag를 지나쳤음) 이므로 1로 설정
		Marker mark = null;

		String tagName = TagNameParser.getRealTagName(nameStartMark);
		Parser nameParser = this.tagNameParserHost.getParser();

		try {
			do {
				mark = tagParser.parseNext();
				if (mark == null) {
					throw new ParserException("could not find the corresponding end tag.");
				}

				nameParser.initialize(mark);
				Marker nameMark = nameParser.parseNext();

				if (tagName.equals(TagNameParser.getRealTagName(nameMark)))
					if (TagNameParser.isBodyTag(nameMark)) {
						if (TagNameParser.isStartTag(nameMark))
							++depth;
						else
							--depth;
					}
			} while (depth > 0);

			// end tag의 시작 부분을 다음 parsing point로 설정한다.
			tagParser.setParsingPoint(mark.getBeginIndex());
		} finally {
			this.tagNameParserHost.releaseParser(nameParser);
		}
	}

	/**
	 * 템플릿을 컴파일 한다.
	 * 
	 * @param template
	 *            Template 컴파일할 템플릿
	 * @throws CompilerException
	 * @return CompiledTemplate 컴파일된 템플릿
	 */
	public CompiledTemplate compile(Template template) throws CompilerException {
		// 가정!!!
		// template의 template type랑 compile mode 가 동일한 값을 가진다고 가정
		return compile(template.getTemplateText(), template.getTemplateType());
	}

	/**
	 * 템플릿을 컴파일 한다.
	 * 
	 * @param templateText
	 *            String Outer Template이라고 가정한 template text
	 * @throws CompilerException
	 * @return CompiledTemplate 컴파일된 템플릿
	 */
	public CompiledTemplate compile(String templateText) throws CompilerException {
		return compile(templateText, COMPILE_MODE_OUTER_TEMPLATE);
	}

	/**
	 * 템플릿을 컴파일 한다.
	 * 
	 * @param templateText
	 *            String 템플릿 텍스트
	 * @param COMPILE_MODE
	 *            int 컴파일 모드
	 * @throws CompilerException
	 * @return CompiledTemplate
	 */
	public CompiledTemplate compile(String templateText, final int COMPILE_MODE) throws CompilerException {
		OperationCode code = new OperationCode();
		OperationData data = new OperationData();

		Marker tagMark = null;
		Marker nameMark = null;

		Parser tagP = this.tagParserHost.getParser();
		Parser nameP = this.tagNameParserHost.getParser();
		// String templateText = TagParser.filterComments(tplText);

		try {
			tagP.initialize(templateText);

			int prevParsedPoint = 0;

			TagInfo tagInfo = null;
			String tagName = null;
			String tagHandlerName = null;

			// Page Tag 처리
			boolean existsCustomPageTagHandler = false;
			int addrPageTagHandlerName = 0; // data addr
			int addrPageTagHandlerReflectionMap = 0; // data addr

			// reset vector : entry point
			int addrResetVector = code.add(OperationCode.OPCODE_TERMINATE);

			// address
			int addrTagHandlerName; // tag handler name이 저장되어 있는 주소 - data addr
			// special address table (아래는 순서가 매우 중요함!!!!!!!!!! - 신중하게 변동하길 바람 (code에 넣는 순서)
			int addrGenericException = code.add(OperationCode.OPCODE_EXCEPTION, data.add("incompleted compiled template.")); // code addr
			int addrEnd = code.add(OperationCode.OPCODE_NO_OPERATION); // page가 끝나는 지점 - code addr
			int addrTermination = code.add(OperationCode.OPCODE_TERMINATE); // translation이 종료되는 지점 - code addr
			int addrStart = code.add(OperationCode.OPCODE_NO_OPERATION); // page가 시작 되는 지점 - code addr
			// end of address

			// body 처리를 위한 stack
			Stack<String> tagNameStack = new Stack<String>();
			IntStack addrAfterBodyStack = new IntStack();
			IntStack addrDoEndTagStack = new IntStack();

			// core start
			while (true) {
				tagMark = tagP.parseNext();

				// print <- ending condition 포함
				if (tagMark == null) { // 더 이상 tag가 없을 때 text의 끝까지
					code.add(OperationCode.OPCODE_PRINT,
					// data.add(TagParser.filterComments(templateText.substring(prevParsedPoint))));
							data.add(templateText.substring(prevParsedPoint)));

					code.add(OperationCode.OPCODE_GOTO, addrEnd); // goto end point
					break; // loop를 종료
				} else { // 다음 tag를 찾았을 때 parsing 된 곳 까지
					code.add(OperationCode.OPCODE_PRINT,
					// data.add(TagParser.filterComments(templateText.substring(prevParsedPoint,tagMark.getBeginIndex()))));
							data.add(templateText.substring(prevParsedPoint, tagMark.getBeginIndex())));
					prevParsedPoint = tagMark.getEndIndex(); // update point
				}

				// string evaluating index
				code.add(OperationCode.OPCODE_EVAL_MARK, data.add(tagMark));

				nameP.initialize(tagMark);
				nameMark = nameP.parseNext();

				tagName = TagNameParser.getRealTagName(nameMark);
				tagInfo = this.libraryInfo.getTag(tagName);
				if (tagInfo == null) {
					tagInfo = this.libraryInfo.getTag(this.libraryInfo.getDefaultTagName()); // default tag handler
				}
				tagHandlerName = tagInfo.getTagName();
				addrTagHandlerName = data.add(tagHandlerName);

				Class<?> handlerCls = Class.forName(tagInfo.getTagClassName());
				if (BodyTagSupport.class.isAssignableFrom(handlerCls)) { // body tag
					if (TagNameParser.isStartTag(nameMark)) { // startTag
						code.add(OperationCode.OPCODE_LOAD_HANDLER, addrTagHandlerName, data.add(retrieveParametersToReflectAttributes(tagName, tagInfo,
								tagMark)));

						// SKIP_BODY를 리턴 받았을 때 아직 end tag를 parsing하지 않았으므로 어디로 jump할지 모른다. 그래서 gerneric exception을 내도록 임시로 두고 나중에 주소를 update 시킨다.
						// EVAL_BODY_INCLUDE를 리턴 받았을 때 body content를 재설정하지 않으므로 body content를 push 하지 않기 위해 +1로 skip한다
						int addrDoStartTag = code.add(OperationCode.OPCODE_DO_START_TAG, addrGenericException, // SKIP_BODY 일 때
								addrGenericException, // EVAL_BODY_INCLUDE 일 때
								addrGenericException); // EVAL_BODY_BUFFERED 일 때

						int addrDoAfterBody = code.add(OperationCode.OPCODE_DO_AFTER_BODY, addrGenericException, // SKIP_BODY 일 때
								addrGenericException); // EVAL_BODY_AGAIN 일 때

						int addrDoEndTag = code.add(OperationCode.OPCODE_DO_END_TAG, addrEnd, // SKIP_PAGE 일 때
								addrGenericException); // EVAL_PAGE 일 때

						int addrBody = code.add(OperationCode.OPCODE_NO_OPERATION); // (body의 시작 지점을 표시할 수 있으니) compiling을 쉽게 하려고

						// 위 에서 generic exception로 처리 했던 부분을 실제 주소로 update시킨다.
						// doStartTag
						code.updateCodeAt(addrDoStartTag, 1, addrDoEndTag);
						code.updateCodeAt(addrDoStartTag, 2, addrBody);
						code.updateCodeAt(addrDoStartTag, 3, addrBody);
						// doAfterTag
						code.updateCodeAt(addrDoAfterBody, 1, addrDoEndTag);
						code.updateCodeAt(addrDoAfterBody, 2, addrBody);
						// doEndTag
						// doEndTag의 generic exception 은 나중에 end tag를 parsing하여 update한다.

						// endTag 에서 나머지 부분을 처리하기 위해 stack에 집에 넣는다
						tagNameStack.push(tagName);
						addrAfterBodyStack.pushInt(addrDoAfterBody);
						addrDoEndTagStack.pushInt(addrDoEndTag);

						// BODY CONTENT 가 TAGDEPENDERNT일 경우 body내의 action tag는 번역하지 않고 skip 해버린다.
						if (tagInfo.getBodyContent().equals(TagInfo.BODY_CONTENT_TAG_DEPENDENT)) {
							parseToEndTagOfCurrentTagDependentBody(nameMark, tagP);
						}
					} else if (TagNameParser.isEndTag(nameMark)) { // endTag
						if (tagNameStack.isEmpty()) {
							throw new CompilerException(tagMark, "incorrect tag hierarchy, too much end tag.");
						}

						String tagNameToCompare = (String) tagNameStack.pop();
						int addrAfterBody = addrAfterBodyStack.popInt();
						int addrDoEndTag = addrDoEndTagStack.popInt();

						if (!tagName.equals(tagNameToCompare)) {
							throw new CompilerException(tagMark, "incorrect tag hierarchy, check the start and end of a tag.");
						}

						code.add(OperationCode.OPCODE_GOTO, addrAfterBody);
						int addrReleaseHandler = code.add(OperationCode.OPCODE_RELEASE_HANDLER);

						code.updateCodeAt(addrDoEndTag, 2, addrReleaseHandler);
					} else { // if ( !TagNameParser.isBodyTag( nameMark ) ) // body tag인데 body가 없이 사용한 경우
					// throw new CompilerException( tagMark, "not found the body of '" + nameMark.getValue() + "'" ); //tagInfo.getTagName()

						code.add(OperationCode.OPCODE_LOAD_HANDLER, addrTagHandlerName, data.add(retrieveParametersToReflectAttributes(tagName, tagInfo,
								tagMark)));

						// do start & end
						int addrGotoDoStart = code.add(OperationCode.OPCODE_GOTO, addrGenericException); // -(1) : 어디로 jump할지 미지정상태로 둔다(generic exception)

						// int addrEx1 = code.add( OperationCode.OPCODE_EXCEPTION, data.add( "a handler returned EVAL_BODY_INCLUDE, but not allowed at '" + tagMark.getValue()+"'" ) );
						// int addrEx2 = code.add( OperationCode.OPCODE_EXCEPTION, data.add( "a handler returned EVAL_BODY_BUFFERED, but not allowed at '" + tagMark.getValue()+"'" ) );

						int addrDoStart = code.add(OperationCode.OPCODE_DO_START_TAG, addrGenericException, // SKIP_BODY 일 때
								addrGenericException, // EVAL_BODY_INCLUDE 일 때
								addrGenericException); // EVAL_BODY_BUFFERED 일 때

						int addrDoAfterBody = code.add(OperationCode.OPCODE_DO_AFTER_BODY, addrGenericException, // SKIP_BODY 일 때
								addrGenericException); // EVAL_BODY_AGAIN 일 때

						int addrDoEnd = code.add(OperationCode.OPCODE_DO_END_TAG, addrEnd, // SKIP_PAGE 일 때
								code.getAfterAddressToAdd()); // EVAL_PAGE 일 때

						code.add(OperationCode.OPCODE_RELEASE_HANDLER);

						code.updateCodeAt(addrGotoDoStart, 1, addrDoStart); // (1)의 미지정된 goto target을 exception 부분을 건너뛰고 do start로 지정한다.

						// doStartTag
						code.updateCodeAt(addrDoStart, 1, addrDoEnd); // addrDoStart의 미지정된 주소 결정
						code.updateCodeAt(addrDoStart, 2, addrDoAfterBody);
						code.updateCodeAt(addrDoStart, 3, addrDoAfterBody);

						// doAfterTag
						code.updateCodeAt(addrDoAfterBody, 1, addrDoEnd);
						code.updateCodeAt(addrDoAfterBody, 2, addrDoAfterBody);
					}
				} else if (TagSupport.class.isAssignableFrom(handlerCls)) { // non-body tag
					code.add(OperationCode.OPCODE_LOAD_HANDLER, addrTagHandlerName, data.add(retrieveParametersToReflectAttributes(tagName, tagInfo, tagMark)));

					// do start & end
					int addrGotoDoStart = code.add(OperationCode.OPCODE_GOTO, addrGenericException); // -(1) : 어디로 jump할지 미지정상태로 둔다(generic exception)

					int addrEx1 = code.add(OperationCode.OPCODE_EXCEPTION, data.add("a handler returned EVAL_BODY_INCLUDE, but not allowed at '"
							+ tagMark.getValue() + "'"));
					int addrEx2 = code.add(OperationCode.OPCODE_EXCEPTION, data.add("a handler returned EVAL_BODY_BUFFERED, but not allowed at '"
							+ tagMark.getValue() + "'"));

					int addrDoStart = code.add(OperationCode.OPCODE_DO_START_TAG, code.getAfterAddressToAdd(), // SKIP_BODY 일 때
							addrEx1, // EVAL_BODY_INCLUDE 일 때
							addrEx2); // EVAL_BODY_BUFFERED 일 때

					code.add(OperationCode.OPCODE_DO_END_TAG, addrEnd, // SKIP_PAGE 일 때
							code.getAfterAddressToAdd()); // EVAL_PAGE 일 때

					code.add(OperationCode.OPCODE_RELEASE_HANDLER);

					code.updateCodeAt(addrGotoDoStart, 1, addrDoStart); // (1)의 미지정된 goto target을 exception 부분을 건너뛰고 do start로 지정한다.
				} else if (PageTagSupport.class.isAssignableFrom(handlerCls)) { // page tag
					// PageTag는 Body 모양이건 아니건 상관하지 않는다.
					if (TagNameParser.isBodyTag(nameMark)) {
						if (TagNameParser.isStartTag(nameMark)) { // start form
							if (existsCustomPageTagHandler)
								throw new CompilerException(tagMark, "too much page tag. only one page tag allowed.");

							if (COMPILE_MODE == COMPILE_MODE_INNER_TEMPLATE)
								throw new CompilerException(tagMark, "not support a page tag in a INNER template (custom action tag or article content)");

							addrPageTagHandlerName = data.add(tagHandlerName);
							addrPageTagHandlerReflectionMap = data.add(retrieveParametersToReflectAttributes(tagName, tagInfo, tagMark));
							existsCustomPageTagHandler = true;
						} else { // end form
							if (!existsCustomPageTagHandler)
								throw new CompilerException(tagMark, "the corresponding start tag for this does not exist.");
						}
					} else {
						if (existsCustomPageTagHandler)
							throw new CompilerException(tagMark, "too much page tag. only one page tag allowed.");

						addrPageTagHandlerName = data.add(tagHandlerName);
						addrPageTagHandlerReflectionMap = data.add(retrieveParametersToReflectAttributes(tagName, tagInfo, tagMark));
						existsCustomPageTagHandler = true;
					}
				} else {
					throw new CompilerException(tagMark, "incorrect handler, handler name=" + tagInfo.getTagName());
				}
			}
			// end of core

			// 스택이 남으면 닫지 않은 테그가 있는 것이다.
			if (!tagNameStack.isEmpty()) {
				throw new CompilerException(null, "incorrect tag hierarchy, too few end tag.");
			}

			// custom page tag handler가 설정되어 있지 않으면 default page tag handler로 설정한다.
			if (!existsCustomPageTagHandler) {
				try {
					String pageTagName = null;
					if (COMPILE_MODE == COMPILE_MODE_OUTER_TEMPLATE)
						pageTagName = this.libraryInfo.getDefaultPageTagName();
					else if (COMPILE_MODE == COMPILE_MODE_INNER_TEMPLATE)
						pageTagName = this.libraryInfo.getInnerPageTagName();
					else
						throw new IllegalArgumentException("unknown compile mode.");

					TagInfo pageTagInfo = this.libraryInfo.getTag(pageTagName);

					Class<?> pageTagCls = Class.forName(pageTagInfo.getTagClassName());
					if (!PageTagSupport.class.isAssignableFrom(pageTagCls))
						throw new CompilerException(new Marker(0, 0, ""), "incorrect handler for page tag");

					String pageTagParsingString = TagParser.TAG_OPEN + pageTagName + TagParser.TAG_CLOSE;
					Marker pageTagMark = new Marker(0, pageTagParsingString.length(), pageTagParsingString);

					addrPageTagHandlerName = data.add(pageTagName);
					addrPageTagHandlerReflectionMap = data.add(retrieveParametersToReflectAttributes(pageTagName, pageTagInfo, pageTagMark));
				} catch (CompilerException ce) {
					throw new CompilerException(null, "it was in a default page tag handler that the compiler exception occured. :" + ce.getMessage(), ce);
				}
			}

			// bind 'addrResetVector' , 'addrStart' , 'addrEnd'
			{
				int addrLoadPageTagHandler = code.add(OperationCode.OPCODE_LOAD_PAGE_HANDLER, addrPageTagHandlerName, addrPageTagHandlerReflectionMap);

				int addrStartTemplate = code.add(OperationCode.OPCODE_DO_START_TEMPLATE, addrGenericException, // SKIP_PAGE 일 경우
						addrStart); // EVAL_PAGE 일 경우

				int addrAfterTemplate = code.add(OperationCode.OPCODE_DO_AFTER_PAGE, addrGenericException, // SKIP_PAGE 일 경우
						addrStart); // EVAL_PAGE_AGAIN 일 경우

				int addrEndPage = code.add(OperationCode.OPCODE_DO_END_TEMPLATE);

				// int addrReleasePageTagHandler = code.add( OperationCode.OPCODE_RELEASE_HANDLER );

				code.add(OperationCode.OPCODE_GOTO, addrTermination); // jump to terminate.

				// addrGenericException으로 binding 시켜 둔 것을 확정 짓는다.
				code.updateCodeAt(addrStartTemplate, 1, addrEndPage); // doStartTemplate()가 skip_page일 경우 jump 할 곳 지정
				code.updateCodeAt(addrAfterTemplate, 1, addrEndPage); // doAfterTemplate()가 skip_page일 경우 jump 할 곳 지정

				// bind reset vector
				code.updateCodeAt(addrResetVector, new int[] { OperationCode.OPCODE_GOTO, addrLoadPageTagHandler }); // doStartTemplate()로 연결

				// bind start point
				code.updateCodeAt(addrStart, new int[] { OperationCode.OPCODE_DO_CREATE_PAGEINFO }); // updatePageInfo()를 invoke한다.

				// bind end point
				code.updateCodeAt(addrEnd, new int[] { OperationCode.OPCODE_GOTO, addrAfterTemplate }); // doAfterTemplate()로 연결
			}

		} catch (CompilerException ce) {
			throw ce;
		} catch (ClassNotFoundException cfe) {
			throw new CompilerException(tagMark, "the propery handler does not exists:" + cfe.getMessage(), cfe);
		} catch (ParserException pe) {
			throw new CompilerException(tagMark, "parsing error:" + pe.getMessage(), pe);
		} catch (Throwable t) {
			throw new CompilerException(tagMark, "unexpected error:" + t.getMessage(), t);
		} finally {
			this.tagParserHost.releaseParser(tagP);
			this.tagNameParserHost.releaseParser(nameP);
		}

		return new CompiledTemplate(code, data);
	}

	class IntStack extends Stack<Integer> {
		public IntStack() {
			super();
		}

		public synchronized int popInt() {
			Integer i = (Integer) pop();
			return i.intValue();
		}

		public synchronized int peekInt() {
			Integer i = (Integer) peek();
			return i.intValue();
		}

		public int pushInt(int item) {
			push(new Integer(item));
			return item;
		}

		public synchronized int searchInt(int i) {
			return super.search(new Integer(i));
		}
	}
}
