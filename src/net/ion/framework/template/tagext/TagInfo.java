package net.ion.framework.template.tagext;

import java.util.HashMap;

/**
 * Tag information for a tag in a Tag Library;
 * 
 * <p>
 * tld ���Ͽ��� ���� �� �ִ� tag �Ѱ��� ����
 * </p>
 * 
 * @author Kim Sanghoon wizest@i-on.net
 * @version 1.0
 */
public class TagInfo {
	public static final String BODY_CONTENT_ACTION_TAG = "ACTIONTAG";
	public static final String BODY_CONTENT_TAG_DEPENDENT = "TAGDEPENDENT";
	public static final String BODY_CONTENT_EMPTY = "EMPTY";
	public static final String BODY_CONTENT_PAGE = "PAGE";

	private String tagName = null;
	private String tagClassName = null;
	private String bodyContent = null;
	private String infoString = null;
	private String defaultAttrName = null;

	private TagAttributeInfo[] attributeInfo = null;
	private HashMap<String, TagAttributeInfo> attributeInfoMap = null;

	// attribute�� ���� tag handler�� reflection���� ���� ���� �� required �� attribute�� ��� ������ Ȯ���� �� ����Ѵ�.
	private int numberOfRequiredAttributes;

	/**
	 * tag�� ����
	 * 
	 * @param tagName
	 *            String tag handler �̸�
	 * @param tagClassName
	 *            String tag class �̸�
	 * @param bodycontent
	 *            String body content Ÿ�� { ACTIONTAG | TAGDEPENDENT | EMPTY | PAGE }
	 * @param defaultAttrName
	 *            String �⺻ �Ӽ� �̸�
	 * @param infoString
	 *            String ���� ���ڿ�
	 * @param attributeInfo
	 *            TagAttributeInfo[] �Ӽ�����
	 */
	public TagInfo(String tagName, String tagClassName, String bodycontent, String defaultAttrName, String infoString, TagAttributeInfo[] attributeInfo)

	{
		this.tagName = tagName;
		this.tagClassName = tagClassName;
		this.infoString = infoString;
		this.attributeInfo = attributeInfo;
		this.bodyContent = bodycontent;
		this.defaultAttrName = defaultAttrName;

		this.attributeInfoMap = new HashMap<String, TagAttributeInfo>();

		this.numberOfRequiredAttributes = 0;
		for (int i = 0; i < attributeInfo.length; ++i) {
			TagAttributeInfo attrInfo = attributeInfo[i];
			this.attributeInfoMap.put(attrInfo.getName(), attrInfo);

			if (attrInfo.isRequired()) {
				++numberOfRequiredAttributes;
			}
		}
	}

	public String getTagName() {
		return tagName;
	}

	public TagAttributeInfo[] getAttributes() {
		return attributeInfo;
	}

	public TagAttributeInfo getAttribute(String attributeName) {
		return (TagAttributeInfo) this.attributeInfoMap.get(attributeName);
	}

	public String getTagClassName() {
		return tagClassName;
	}

	public String getBodyContent() {
		return bodyContent;
	}

	public String getInfoString() {
		return infoString;
	}

	public String getDefaultAttributeName() {
		return defaultAttrName;
	}

	public int getNumberOfRequiredAttributes() {
		return numberOfRequiredAttributes;
	}

	public String toString() {
		StringBuffer b = new StringBuffer();
		b.append("{name=" + tagName);
		b.append(",class=" + tagClassName);
		b.append(",body=" + bodyContent);
		b.append(",default attribute name=" + defaultAttrName);
		b.append(",info=" + infoString);
		b.append(",attributes = {");
		for (int i = 0; i < attributeInfo.length; i++) {
			b.append(attributeInfo[i].toString());

		}
		b.append("}");
		return b.toString();
	}
}
