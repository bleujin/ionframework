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
 * rendering ���� Ŭ���� parser host, handler host, tag registry
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
	 * -. Configuration ����
	 * 
	 * <pre>
	 * +: �ݵ�� �ʿ�
	 *  -: ��� �������
	 *      [+actiontag]
	 *          +[+short-name]
	 *          +[+default-tag-name]                            : default�� ����� tag handler �̸�
	 *          +[+default-page-tag-name]                       : default�� ����� page tag �̸� (page tag�� ���� template�� ���� �⺻ ó�� �ڵ鷯 ����)
	 *          +[-decription]
	 *          +[-tag]                                         : Tag ���� (������ ���� ����)
	 *             +[+name]                        : Tag �̸�
	 *             +[-alias]                       : Tag ���� (�̸��� ���� ȿ��) ',' �� �����Ͽ� ������ ��������
	 *             +[+tag-class]                   : Tag handler (the fully qualified name)
	 *             +[+body-content]                : Tag �� �ѷ����� �κ��� ���� -> [ ACTIONTAG | TAGDEPENDENT | EMPTY ] -> ACTIONTAG: body�ȿ� �� ActionTag�� �ִ�, TAGDEPENDENT:TAG handler�� å�� ���� ó���� ���̹Ƿ� Action Tag ������ ���� �� ��, EMPTY:body�� �������� ���� ���
	 *             +[-default-attribute-name]      : attribute �̸��� ���� ���� �ִ� attribute�� ��� ������ ������ ���� (�������� ���� ��� default attribute�� ����� �� ����.)
	 *             +[-description]
	 *             +[-attribute]                   : Tag �� attribute ���� (������ ���� ����)
	 *                 +[+name]
	 *                 +[-required]    : attribute�� �ʿ俩�� (true/false)
	 *                 +[-type]        : attribute�� Ÿ�� (the fully qualified class name, type name) ex) java.lang.String, int
	 *                 +[-description]
	 * </pre>
	 * 
	 * -. Configuration �ۼ� ����
	 * 
	 * <pre>
	 * <taglib>
	 *          <short-name>action tag library</short-name>
	 *          <default-tag-name>tag library �����ڿ� ���� tag�� �߰ߵǾ��� ��� �⺻ ó���� tag name</default-tag-name>
	 *          <default-page-tag-name>template���� page tag�� �������� ���� ��� �⺻ ó���� page tag name</default-page-tag-name>
	 *          <inner-page-tag-name>_InnerPage</inner-page-tag-name>
	 *          <description>tag library�� ���� ����</description>
	 *          <tag>
	 *              <name>Action Tag�� ����� �̸�</name>
	 *              <alias>����1,����2,...</alias>
	 *              <tag-class>tag handler�� fully qualified class name (ex) net.cms.ion.publish.actiontag.ActionTag</tag-class>
	 *              <body-content>
	 *                  EMPTY :body�� �������� ����,
	 *                  TAGDEPENDENT :body�� ���������� body ������ actiontag�� �������� ����,
	 *                  ACTIONTAG :body�� �����ϰ� body ������ actiontag�� ������,
	 *                  PAGE :body�� ������ page�� ������ų ��
	 *                  �� �� �� �ִ�.
	 *              </body-content>
	 *              <description>tag�� ���� ��ü���� ����</description>
	 *              <default-attribute-name>attribute�� �̸��� ���� ��� ������ name</default-attribute-name>
	 *              <attribute>
	 *                  <name>attribute name</name>
	 *                  <required>true/false :�ݵ�� �ʿ��� attribute �ΰ�? </required>
	 *                  <type>
	 *                      attribute�� java type : primitive,object���� ������� �����Ӱ� �� �� �ִ�.
	 *                      (ex)int,boolean,java.lang.String,long,java.util.Date,...
	 *                  </type>
	 *                  <rtexprvalue>true/false :�Ķ���� ���� ���̳����ϰ� �Է¹��� ���ΰ� ����</rtexprvalue>
	 *                  <description>attribute�� ���� ����</description>
	 *              </attribute>
	 *              ... �߰��� <attribute/> ����
	 *          </tag>
	 *          ... �߰��� <tag/> ����
	 *      </taglib>
	 * </pre>
	 * 
	 * @param taglib
	 *            Configuration tag library configuration
	 * @param handlerHostSize
	 *            int �� handler�� pool size
	 * @param parserHostSize
	 *            int parser pool size
	 * @throws InvalidSetupException
	 *             configuration�� �ùٸ��� ���� ���
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

					// alias ����
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
	 * rendering support�� �����Ѵ�.
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
