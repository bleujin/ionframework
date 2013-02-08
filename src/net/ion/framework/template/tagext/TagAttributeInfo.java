package net.ion.framework.template.tagext;

/**
 * Information on the attributes of a Tag, available at translation time. This class is instantiated from the Tag Library Descriptor file (TLD). Only the information needed to generate code is included here. Other information like SCHEMA for
 * validation belongs elsewhere.
 * 
 * <p>
 * Tag에 대한 attribute 한 개의 정보
 * </p>
 * 
 * @author Kim Sanghoon wizest@i-on.net
 * @version 1.0
 */

public class TagAttributeInfo {

	public static final String ID = "id";
	private String name = null;
	private String type = null;
	private boolean required = false;
	private boolean rtexprvalue = false;

	/**
	 * @param name
	 *            String 속성 이름
	 * @param required
	 *            boolean 필수 속성 여부
	 * @param type
	 *            String 속성 값 type
	 * @param rtexprvalue
	 *            boolean 실시간 값 평가 여부 ([% %] 으로 묶인 부분을 해석할 것인가 여부)
	 */
	public TagAttributeInfo(String name, boolean required, String type, boolean rtexprvalue) {
		this.name = name;
		this.required = required;
		this.type = type;
		this.rtexprvalue = rtexprvalue;
	}

	public String getName() {
		return name;
	}

	public String getTypeName() {
		return type;
	}

	public boolean isRequired() {
		return required;
	}

	public boolean isRtexprvalue() {
		return rtexprvalue;
	}

	public String toString() {
		StringBuffer b = new StringBuffer();
		b.append("{name=" + name);
		b.append(",type=" + type);
		b.append(",rtexprvalue=" + rtexprvalue);
		b.append(",required=" + required + "}");
		return b.toString();
	}
}
