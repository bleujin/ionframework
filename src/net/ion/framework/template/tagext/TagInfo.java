package net.ion.framework.template.tagext;

import java.util.HashMap;

/**
 * Tag information for a tag in a Tag Library;
 * 
 * <p>
 * tld 파일에서 얻을 수 있는 tag 한개의 정보
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

	// attribute의 값을 tag handler에 reflection으로 집어 넣을 때 required 한 attribute가 모두 들어갔는지 확인할 때 사용한다.
	private int numberOfRequiredAttributes;

	/**
	 * tag의 정보
	 * 
	 * @param tagName
	 *            String tag handler 이름
	 * @param tagClassName
	 *            String tag class 이름
	 * @param bodycontent
	 *            String body content 타입 { ACTIONTAG | TAGDEPENDENT | EMPTY | PAGE }
	 * @param defaultAttrName
	 *            String 기본 속성 이름
	 * @param infoString
	 *            String 설명 문자열
	 * @param attributeInfo
	 *            TagAttributeInfo[] 속성정보
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
