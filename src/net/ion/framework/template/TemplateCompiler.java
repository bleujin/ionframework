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
 * text template�� code�� data�� �и��Ͽ� ���� operationCode, operationData�� ����� compiledTemplate�� �����Ѵ�. �����ϵ� ���ø��� templateRuntime���� �����ų �� �ִ�.
 * 
 * @author Kim Sanghoon wizest@i-on.net
 * @version 1.0
 */

public class TemplateCompiler {
	/**
	 * COMPILE_MODE<br/>
	 * OUTER_TEMPLATE : ��� tag�� �� ����� �� �ִ�. INNER_TEMPLATE : page tag�� ������ ��� tag�� ����� �� �ִ�.
	 * 
	 * ��) Template�� �� 1���� page tag�� ���� �� �ִµ� template�� Ÿ template�� ������ ��� Ÿ ���ø��� �̹� page tag�� ���� ��� ���� ���ø����� page tag�� �����Ƿ� ������ �ȴ�. ������ ������ �� �ִ� template�� inner template �ۿ� ����.
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
	 * handler�� reflection���� ���� ���� ���� �� ����� parameter���� �����Ѵ�.
	 * 
	 * @param tagName
	 *            TagSupport �� id �ʵ忡 ������ �� (template���� ����� tag�̸�), ����: tag handler �̸��� �ƴϴ�.
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
				// tagMark�� �����Ѵ�.
				names.add("tagMarker");
				values.add(tagMark);

				// tag name�� TagSupport.id �ʵ忡 �����Ѵ�.
				// Tag handler���� template���� � �̸����� handler�� invoke�ߴ��� �˱� ����
				// (default tag ���� ��� ������ handler�� �پ��� �̸����� �θ���.)
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
						// default attribute ó��
						if (name == null) {
							if (existsDefaultAttribute) {
								throw new CompilerException(tagMark, "duplicated default attribute at '" + attrMark.getValue() + "'");
							} else {
								existsDefaultAttribute = true;
								name = tagInfo.getDefaultAttributeName(); // default attribute

								// default attribute�� �����Ǿ� ���� ���� ���
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
						value = valueStr; // template run-time environment���� �� ���� ��ó��������Ѵ�!!!
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
	 * ���� tag parser�� ���� �ִ� body�� end tag �ٷ� ������ �ǳʶڴ�. (�� tag�� body content�� tag dependent��� �����Ѵ�)
	 * 
	 * @param tagParser
	 * @throws ParserException
	 */
	private void parseToEndTagOfCurrentTagDependentBody(Marker nameStartMark, Parser tagParser) throws ParserException {
		/**
		 * ����) start tag�� ��Ÿ���� depth�� 1�� �����ϰ� end tag�� ��Ÿ���� depth�� 1�� �����Ѵ�.
		 * 
		 * ���� ���� ������ ����� �Ǿ� �ִٸ� depth�� 0�� �Ǵ� ���� tag�� ������ �����̴�.
		 */

		int depth = 1; // �� �޼ҵ带 �θ� ���¿� �̹� ������ �ִ� ����(start tag�� ��������) �̹Ƿ� 1�� ����
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

			// end tag�� ���� �κ��� ���� parsing point�� �����Ѵ�.
			tagParser.setParsingPoint(mark.getBeginIndex());
		} finally {
			this.tagNameParserHost.releaseParser(nameParser);
		}
	}

	/**
	 * ���ø��� ������ �Ѵ�.
	 * 
	 * @param template
	 *            Template �������� ���ø�
	 * @throws CompilerException
	 * @return CompiledTemplate �����ϵ� ���ø�
	 */
	public CompiledTemplate compile(Template template) throws CompilerException {
		// ����!!!
		// template�� template type�� compile mode �� ������ ���� �����ٰ� ����
		return compile(template.getTemplateText(), template.getTemplateType());
	}

	/**
	 * ���ø��� ������ �Ѵ�.
	 * 
	 * @param templateText
	 *            String Outer Template�̶�� ������ template text
	 * @throws CompilerException
	 * @return CompiledTemplate �����ϵ� ���ø�
	 */
	public CompiledTemplate compile(String templateText) throws CompilerException {
		return compile(templateText, COMPILE_MODE_OUTER_TEMPLATE);
	}

	/**
	 * ���ø��� ������ �Ѵ�.
	 * 
	 * @param templateText
	 *            String ���ø� �ؽ�Ʈ
	 * @param COMPILE_MODE
	 *            int ������ ���
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

			// Page Tag ó��
			boolean existsCustomPageTagHandler = false;
			int addrPageTagHandlerName = 0; // data addr
			int addrPageTagHandlerReflectionMap = 0; // data addr

			// reset vector : entry point
			int addrResetVector = code.add(OperationCode.OPCODE_TERMINATE);

			// address
			int addrTagHandlerName; // tag handler name�� ����Ǿ� �ִ� �ּ� - data addr
			// special address table (�Ʒ��� ������ �ſ� �߿���!!!!!!!!!! - �����ϰ� �����ϱ� �ٶ� (code�� �ִ� ����)
			int addrGenericException = code.add(OperationCode.OPCODE_EXCEPTION, data.add("incompleted compiled template.")); // code addr
			int addrEnd = code.add(OperationCode.OPCODE_NO_OPERATION); // page�� ������ ���� - code addr
			int addrTermination = code.add(OperationCode.OPCODE_TERMINATE); // translation�� ����Ǵ� ���� - code addr
			int addrStart = code.add(OperationCode.OPCODE_NO_OPERATION); // page�� ���� �Ǵ� ���� - code addr
			// end of address

			// body ó���� ���� stack
			Stack<String> tagNameStack = new Stack<String>();
			IntStack addrAfterBodyStack = new IntStack();
			IntStack addrDoEndTagStack = new IntStack();

			// core start
			while (true) {
				tagMark = tagP.parseNext();

				// print <- ending condition ����
				if (tagMark == null) { // �� �̻� tag�� ���� �� text�� ������
					code.add(OperationCode.OPCODE_PRINT,
					// data.add(TagParser.filterComments(templateText.substring(prevParsedPoint))));
							data.add(templateText.substring(prevParsedPoint)));

					code.add(OperationCode.OPCODE_GOTO, addrEnd); // goto end point
					break; // loop�� ����
				} else { // ���� tag�� ã���� �� parsing �� �� ����
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

						// SKIP_BODY�� ���� �޾��� �� ���� end tag�� parsing���� �ʾ����Ƿ� ���� jump���� �𸥴�. �׷��� gerneric exception�� ������ �ӽ÷� �ΰ� ���߿� �ּҸ� update ��Ų��.
						// EVAL_BODY_INCLUDE�� ���� �޾��� �� body content�� �缳������ �����Ƿ� body content�� push ���� �ʱ� ���� +1�� skip�Ѵ�
						int addrDoStartTag = code.add(OperationCode.OPCODE_DO_START_TAG, addrGenericException, // SKIP_BODY �� ��
								addrGenericException, // EVAL_BODY_INCLUDE �� ��
								addrGenericException); // EVAL_BODY_BUFFERED �� ��

						int addrDoAfterBody = code.add(OperationCode.OPCODE_DO_AFTER_BODY, addrGenericException, // SKIP_BODY �� ��
								addrGenericException); // EVAL_BODY_AGAIN �� ��

						int addrDoEndTag = code.add(OperationCode.OPCODE_DO_END_TAG, addrEnd, // SKIP_PAGE �� ��
								addrGenericException); // EVAL_PAGE �� ��

						int addrBody = code.add(OperationCode.OPCODE_NO_OPERATION); // (body�� ���� ������ ǥ���� �� ������) compiling�� ���� �Ϸ���

						// �� ���� generic exception�� ó�� �ߴ� �κ��� ���� �ּҷ� update��Ų��.
						// doStartTag
						code.updateCodeAt(addrDoStartTag, 1, addrDoEndTag);
						code.updateCodeAt(addrDoStartTag, 2, addrBody);
						code.updateCodeAt(addrDoStartTag, 3, addrBody);
						// doAfterTag
						code.updateCodeAt(addrDoAfterBody, 1, addrDoEndTag);
						code.updateCodeAt(addrDoAfterBody, 2, addrBody);
						// doEndTag
						// doEndTag�� generic exception �� ���߿� end tag�� parsing�Ͽ� update�Ѵ�.

						// endTag ���� ������ �κ��� ó���ϱ� ���� stack�� ���� �ִ´�
						tagNameStack.push(tagName);
						addrAfterBodyStack.pushInt(addrDoAfterBody);
						addrDoEndTagStack.pushInt(addrDoEndTag);

						// BODY CONTENT �� TAGDEPENDERNT�� ��� body���� action tag�� �������� �ʰ� skip �ع�����.
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
					} else { // if ( !TagNameParser.isBodyTag( nameMark ) ) // body tag�ε� body�� ���� ����� ���
					// throw new CompilerException( tagMark, "not found the body of '" + nameMark.getValue() + "'" ); //tagInfo.getTagName()

						code.add(OperationCode.OPCODE_LOAD_HANDLER, addrTagHandlerName, data.add(retrieveParametersToReflectAttributes(tagName, tagInfo,
								tagMark)));

						// do start & end
						int addrGotoDoStart = code.add(OperationCode.OPCODE_GOTO, addrGenericException); // -(1) : ���� jump���� ���������·� �д�(generic exception)

						// int addrEx1 = code.add( OperationCode.OPCODE_EXCEPTION, data.add( "a handler returned EVAL_BODY_INCLUDE, but not allowed at '" + tagMark.getValue()+"'" ) );
						// int addrEx2 = code.add( OperationCode.OPCODE_EXCEPTION, data.add( "a handler returned EVAL_BODY_BUFFERED, but not allowed at '" + tagMark.getValue()+"'" ) );

						int addrDoStart = code.add(OperationCode.OPCODE_DO_START_TAG, addrGenericException, // SKIP_BODY �� ��
								addrGenericException, // EVAL_BODY_INCLUDE �� ��
								addrGenericException); // EVAL_BODY_BUFFERED �� ��

						int addrDoAfterBody = code.add(OperationCode.OPCODE_DO_AFTER_BODY, addrGenericException, // SKIP_BODY �� ��
								addrGenericException); // EVAL_BODY_AGAIN �� ��

						int addrDoEnd = code.add(OperationCode.OPCODE_DO_END_TAG, addrEnd, // SKIP_PAGE �� ��
								code.getAfterAddressToAdd()); // EVAL_PAGE �� ��

						code.add(OperationCode.OPCODE_RELEASE_HANDLER);

						code.updateCodeAt(addrGotoDoStart, 1, addrDoStart); // (1)�� �������� goto target�� exception �κ��� �ǳʶٰ� do start�� �����Ѵ�.

						// doStartTag
						code.updateCodeAt(addrDoStart, 1, addrDoEnd); // addrDoStart�� �������� �ּ� ����
						code.updateCodeAt(addrDoStart, 2, addrDoAfterBody);
						code.updateCodeAt(addrDoStart, 3, addrDoAfterBody);

						// doAfterTag
						code.updateCodeAt(addrDoAfterBody, 1, addrDoEnd);
						code.updateCodeAt(addrDoAfterBody, 2, addrDoAfterBody);
					}
				} else if (TagSupport.class.isAssignableFrom(handlerCls)) { // non-body tag
					code.add(OperationCode.OPCODE_LOAD_HANDLER, addrTagHandlerName, data.add(retrieveParametersToReflectAttributes(tagName, tagInfo, tagMark)));

					// do start & end
					int addrGotoDoStart = code.add(OperationCode.OPCODE_GOTO, addrGenericException); // -(1) : ���� jump���� ���������·� �д�(generic exception)

					int addrEx1 = code.add(OperationCode.OPCODE_EXCEPTION, data.add("a handler returned EVAL_BODY_INCLUDE, but not allowed at '"
							+ tagMark.getValue() + "'"));
					int addrEx2 = code.add(OperationCode.OPCODE_EXCEPTION, data.add("a handler returned EVAL_BODY_BUFFERED, but not allowed at '"
							+ tagMark.getValue() + "'"));

					int addrDoStart = code.add(OperationCode.OPCODE_DO_START_TAG, code.getAfterAddressToAdd(), // SKIP_BODY �� ��
							addrEx1, // EVAL_BODY_INCLUDE �� ��
							addrEx2); // EVAL_BODY_BUFFERED �� ��

					code.add(OperationCode.OPCODE_DO_END_TAG, addrEnd, // SKIP_PAGE �� ��
							code.getAfterAddressToAdd()); // EVAL_PAGE �� ��

					code.add(OperationCode.OPCODE_RELEASE_HANDLER);

					code.updateCodeAt(addrGotoDoStart, 1, addrDoStart); // (1)�� �������� goto target�� exception �κ��� �ǳʶٰ� do start�� �����Ѵ�.
				} else if (PageTagSupport.class.isAssignableFrom(handlerCls)) { // page tag
					// PageTag�� Body ����̰� �ƴϰ� ������� �ʴ´�.
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

			// ������ ������ ���� ���� �ױװ� �ִ� ���̴�.
			if (!tagNameStack.isEmpty()) {
				throw new CompilerException(null, "incorrect tag hierarchy, too few end tag.");
			}

			// custom page tag handler�� �����Ǿ� ���� ������ default page tag handler�� �����Ѵ�.
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

				int addrStartTemplate = code.add(OperationCode.OPCODE_DO_START_TEMPLATE, addrGenericException, // SKIP_PAGE �� ���
						addrStart); // EVAL_PAGE �� ���

				int addrAfterTemplate = code.add(OperationCode.OPCODE_DO_AFTER_PAGE, addrGenericException, // SKIP_PAGE �� ���
						addrStart); // EVAL_PAGE_AGAIN �� ���

				int addrEndPage = code.add(OperationCode.OPCODE_DO_END_TEMPLATE);

				// int addrReleasePageTagHandler = code.add( OperationCode.OPCODE_RELEASE_HANDLER );

				code.add(OperationCode.OPCODE_GOTO, addrTermination); // jump to terminate.

				// addrGenericException���� binding ���� �� ���� Ȯ�� ���´�.
				code.updateCodeAt(addrStartTemplate, 1, addrEndPage); // doStartTemplate()�� skip_page�� ��� jump �� �� ����
				code.updateCodeAt(addrAfterTemplate, 1, addrEndPage); // doAfterTemplate()�� skip_page�� ��� jump �� �� ����

				// bind reset vector
				code.updateCodeAt(addrResetVector, new int[] { OperationCode.OPCODE_GOTO, addrLoadPageTagHandler }); // doStartTemplate()�� ����

				// bind start point
				code.updateCodeAt(addrStart, new int[] { OperationCode.OPCODE_DO_CREATE_PAGEINFO }); // updatePageInfo()�� invoke�Ѵ�.

				// bind end point
				code.updateCodeAt(addrEnd, new int[] { OperationCode.OPCODE_GOTO, addrAfterTemplate }); // doAfterTemplate()�� ����
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
