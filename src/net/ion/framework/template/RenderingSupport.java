package net.ion.framework.template;

import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.logging.Logger;

import net.ion.framework.configuration.Configuration;
import net.ion.framework.configuration.ConfigurationException;
import net.ion.framework.configuration.NotFoundXmlTagException;
import net.ion.framework.logging.LogBroker;
import net.ion.framework.template.parse.AttributeNameParser;
import net.ion.framework.template.parse.AttributeParser;
import net.ion.framework.template.parse.AttributeValueParser;
import net.ion.framework.template.parse.TagNameParser;
import net.ion.framework.template.parse.TagParser;
import net.ion.framework.template.tagext.BodyTagSupport;
import net.ion.framework.template.tagext.PageTagSupport;
import net.ion.framework.template.tagext.TagAttributeInfo;
import net.ion.framework.template.tagext.TagInfo;
import net.ion.framework.template.tagext.TagLibraryInfo;
import net.ion.framework.template.tagext.TagSupport;

/**
 * rendering 지원 클래스 parser host, handler host, tag registry
 * 
 * @author Kim, Sanghoon (wizest@i-on.net)
 * @version 1.0
 */

public class RenderingSupport {
	private TagLibraryInfo libraryInfo = null;
	private TagHandlerHost handlerHost = null;

	private ParserHost tagParserHost = null;
	private ParserHost tagNameParserHost = null;
	private ParserHost attrParserHost = null;
	private ParserHost attrNameParserHost = null;
	private ParserHost attrValueParserHost = null;

	private Logger log = null;

	/**
	 * -. Configuration 구성
	 * 
	 * <pre>
	 * +: 반드시 필요
	 *  -: 없어도 상관없음
	 *      [+actiontag]
	 *          +[+short-name]
	 *          +[+default-tag-name]                            : default로 사용할 tag handler 이름
	 *          +[+default-page-tag-name]                       : default로 사용할 page tag 이름 (page tag가 없는 template에 대한 기본 처리 핸들러 지정)
	 *          +[-decription]
	 *          +[-tag]                                         : Tag 정의 (여러개 존재 가능)
	 *             +[+name]                        : Tag 이름
	 *             +[-alias]                       : Tag 별명 (이름과 같은 효과) ',' 로 구분하여 여러개 지정가능
	 *             +[+tag-class]                   : Tag handler (the fully qualified name)
	 *             +[+body-content]                : Tag 로 둘러싸인 부분의 역할 -> [ ACTIONTAG | TAGDEPENDENT | EMPTY ] -> ACTIONTAG: body안에 또 ActionTag가 있다, TAGDEPENDENT:TAG handler가 책임 지고 처리할 것이므로 Action Tag 번역을 하지 말 것, EMPTY:body가 존재하지 않을 경우
	 *             +[-default-attribute-name]      : attribute 이름이 없이 값만 있는 attribute는 어디에 저장할 것인지 지정 (지정하지 않을 경우 default attribute를 사용할 수 없다.)
	 *             +[-description]
	 *             +[-attribute]                   : Tag 의 attribute 정의 (여러개 존재 가능)
	 *                 +[+name]
	 *                 +[-required]    : attribute의 필요여부 (true/false)
	 *                 +[-type]        : attribute의 타입 (the fully qualified class name, type name) ex) java.lang.String, int
	 *                 +[-description]
	 * </pre>
	 * 
	 * -. Configuration 작성 예제
	 * 
	 * <pre>
	 * <taglib>
	 *          <short-name>action tag library</short-name>
	 *          <default-tag-name>tag library 설명자에 없는 tag가 발견되었을 경우 기본 처리할 tag name</default-tag-name>
	 *          <default-page-tag-name>template내에 page tag가 존재하지 않을 경우 기본 처리할 page tag name</default-page-tag-name>
	 *          <inner-page-tag-name>_InnerPage</inner-page-tag-name>
	 *          <description>tag library에 대한 설명</description>
	 *          <tag>
	 *              <name>Action Tag로 사용할 이름</name>
	 *              <alias>별명1,별명2,...</alias>
	 *              <tag-class>tag handler의 fully qualified class name (ex) net.cms.ion.publish.actiontag.ActionTag</tag-class>
	 *              <body-content>
	 *                  EMPTY :body가 존재하지 않음,
	 *                  TAGDEPENDENT :body가 존재하지만 body 내부의 actiontag를 번역하지 않음,
	 *                  ACTIONTAG :body가 존재하고 body 내부의 actiontag를 번역함,
	 *                  PAGE :body의 내용이 page를 생성시킬 때
	 *                  가 올 수 있다.
	 *              </body-content>
	 *              <description>tag에 대한 구체적인 설명</description>
	 *              <default-attribute-name>attribute의 이름이 없을 경우 간주할 name</default-attribute-name>
	 *              <attribute>
	 *                  <name>attribute name</name>
	 *                  <required>true/false :반드시 필요한 attribute 인가? </required>
	 *                  <type>
	 *                      attribute의 java type : primitive,object형에 관계없이 자유롭게 쓸 수 있다.
	 *                      (ex)int,boolean,java.lang.String,long,java.util.Date,...
	 *                  </type>
	 *                  <rtexprvalue>true/false :파라미터 값을 다이나믹하게 입력받을 것인가 결정</rtexprvalue>
	 *                  <description>attribute에 대한 설명</description>
	 *              </attribute>
	 *              ... 추가로 <attribute/> 설정
	 *          </tag>
	 *          ... 추가로 <tag/> 설정
	 *      </taglib>
	 * </pre>
	 * 
	 * @param taglib
	 *            Configuration tag library configuration
	 * @param handlerHostSize
	 *            int 각 handler의 pool size
	 * @param parserHostSize
	 *            int parser pool size
	 * @throws InvalidSetupException
	 *             configuration이 올바르지 않을 경우
	 */
	public RenderingSupport(Configuration taglib, int handlerHostSize, int parserHostSize) throws InvalidSetupException {
		this.log = LogBroker.getLogger(this);

		// setup libraryInfo from 'taglib' configuration
		{
			if (!taglib.getTagName().equals("taglib")) {
				throw new InvalidSetupException("invalid configuration.");
			}
			try {
				Configuration[] tags = taglib.getChildren("tag");
				ArrayList<TagInfo> tagInfoList = new ArrayList<TagInfo>();

				for (int i = 0; i < tags.length; ++i) {
					Configuration tag = tags[i];
					String tagName = tag.getChild("name").getValue().toUpperCase();
					String tagClass = tag.getChild("tag-class").getValue();
					String bodyContent = tag.getChild("body-content").getValue();

					// validate tag-class, body-content
					try {
						Class<?> cls = Class.forName(tagClass);
						if (PageTagSupport.class.isAssignableFrom(cls)) {
							if (!(bodyContent.equals(TagInfo.BODY_CONTENT_PAGE))) {
								throw new InvalidSetupException("incorrect body-content at " + tagName + " tag");
							}
						} else if (BodyTagSupport.class.isAssignableFrom(cls)) {
							if (!(bodyContent.equals(TagInfo.BODY_CONTENT_ACTION_TAG) || bodyContent.equals(TagInfo.BODY_CONTENT_TAG_DEPENDENT))) {
								throw new InvalidSetupException("incorrect body-content at " + tagName + " tag");
							}
						} else if (TagSupport.class.isAssignableFrom(cls)) {
							if (!bodyContent.equals(TagInfo.BODY_CONTENT_EMPTY)) {
								throw new InvalidSetupException("incorrect body-content at " + tagName + " tag");
							}
						} else {
							throw new InvalidSetupException("invalid tag-class at " + tagName);
						}
					} catch (ClassNotFoundException cfe) {
						throw new InvalidSetupException("invalid tag-class at " + tagName, cfe);
					}

					String defaultAttrName;
					try {
						defaultAttrName = tag.getChild("default-attribute-name").getValue().toUpperCase();
					} catch (NotFoundXmlTagException ex) {
						defaultAttrName = null;
					}

					String infoString;
					try {
						infoString = tag.getChild("description").getValue();
					} catch (NotFoundXmlTagException ex3) {
						infoString = "";
					}

					TagAttributeInfo[] tagAttributeInfo;

					Configuration[] attributes = null;
					try {
						attributes = tag.getChildren("attribute");
						tagAttributeInfo = new TagAttributeInfo[attributes.length];

						for (int j = 0; j < attributes.length; ++j) {
							Configuration attribute = attributes[j];

							String attrName = attribute.getChild("name").getValue().toUpperCase();
							boolean required;
							String type;
							boolean rtexprvalue;

							try {
								required = attribute.getChild("required").getValueAsBoolean();
							} catch (NotFoundXmlTagException ex1) {
								required = false;
							}
							try {
								type = attribute.getChild("type").getValue();
							} catch (NotFoundXmlTagException ex2) {
								type = "java.lang.String";
							}
							try {
								rtexprvalue = attribute.getChild("rtexprvalue").getValueAsBoolean(false);
							} catch (NotFoundXmlTagException ex2) {
								rtexprvalue = false;
							}

							tagAttributeInfo[j] = new TagAttributeInfo(attrName, required, type, rtexprvalue);
						}
					} catch (NotFoundXmlTagException ex) {
						tagAttributeInfo = new TagAttributeInfo[0];
					}

					tagInfoList.add(new TagInfo(tagName, tagClass, bodyContent, defaultAttrName, infoString, tagAttributeInfo));

					// alias 지정
					try {
						String alias = tag.getChild("alias").getValue().toUpperCase();
						StringTokenizer tokener = new StringTokenizer(alias, ",");
						while (tokener.hasMoreTokens()) {
							tagInfoList.add(new TagInfo(tokener.nextToken().trim(), tagClass, bodyContent, defaultAttrName, infoString, tagAttributeInfo));
						}
					} catch (NotFoundXmlTagException nf) {
					}
				}

				String libShortName = taglib.getChild("short-name").getValue();
				String defaultTagName = null;
				String defaultPageTagName = null;
				String innerPageTagName = null;
				try {
					defaultTagName = taglib.getChild("default-tag-name").getValue().toUpperCase();
				} catch (NotFoundXmlTagException ex5) {
					log.warning("not found <default-tag-name/>");
				}
				try {
					defaultPageTagName = taglib.getChild("default-page-tag-name").getValue().toUpperCase();
				} catch (NotFoundXmlTagException ex6) {
					log.warning("not found <default-page-tag-name/>");
				}
				try {
					innerPageTagName = taglib.getChild("inner-page-tag-name").getValue().toUpperCase();
				} catch (NotFoundXmlTagException ex7) {
					log.warning("not found <inner-page-tag-name/>");
				}

				TagInfo[] tagInfo = tagInfoList.toArray(new TagInfo[0]);
				TagLibraryInfo tagLibraryInfo = new TagLibraryInfo(libShortName, defaultPageTagName, defaultTagName, innerPageTagName, tagInfo);
				this.libraryInfo = tagLibraryInfo;

			} catch (ConfigurationException ex) {
				log.throwing(this.getClass().getName(), "<init>", ex);
				throw new InvalidSetupException(ex);
			}
		}

		// setup Hosts
		this.handlerHost = new TagHandlerHost(this.libraryInfo, handlerHostSize);

		this.tagParserHost = new ParserHost(TagParser.class, parserHostSize);
		this.tagNameParserHost = new ParserHost(TagNameParser.class, parserHostSize);
		this.attrParserHost = new ParserHost(AttributeParser.class, parserHostSize);
		this.attrNameParserHost = new ParserHost(AttributeNameParser.class, parserHostSize);
		this.attrValueParserHost = new ParserHost(AttributeValueParser.class, parserHostSize);
	}

	/**
	 * rendering support를 종료한다.
	 */
	public void destroy() {
		this.handlerHost.destroy();

		this.tagParserHost.destroy();
		this.tagNameParserHost.destroy();
		this.attrParserHost.destroy();
		this.attrNameParserHost.destroy();
		this.attrValueParserHost.destroy();
	}

	/**
	 * @return TagLibraryInfo tag library information
	 */
	public final TagLibraryInfo getTagLibraryInfo() {
		return this.libraryInfo;
	}

	/**
	 * @return TagHandlerHost handler pool
	 */
	public final TagHandlerHost getTagHandlerHost() {
		return this.handlerHost;
	}

	/**
	 * @return ParserHost name parser pool
	 */
	public final ParserHost getTagNameParserHost() {
		return tagNameParserHost;
	}

	/**
	 * @return ParserHost tag parser pool
	 */
	public final ParserHost getTagParserHost() {
		return tagParserHost;
	}

	/**
	 * @return ParserHost attribute name parser pool
	 */
	public final ParserHost getAttrNameParserHost() {
		return attrNameParserHost;
	}

	/**
	 * @return ParserHost attribute parser pool
	 */
	public final ParserHost getAttrParserHost() {
		return attrParserHost;
	}

	/**
	 * @return ParserHost attribute value parser pool
	 */
	public final ParserHost getAttrValueParserHost() {
		return attrValueParserHost;
	}
}
