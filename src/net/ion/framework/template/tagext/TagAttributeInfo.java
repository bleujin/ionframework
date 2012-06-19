package net.ion.framework.template.tagext;

/**
 * Information on the attributes of a Tag, available at translation time. This class is instantiated from the Tag Library Descriptor file (TLD). Only the information needed to generate code is included here. Other information like SCHEMA for
 * validation belongs elsewhere.
 * 
 * <p>
 * Tag�� ���� attribute �� ���� ����
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
	 *            String �Ӽ� �̸�
	 * @param required
	 *            boolean �ʼ� �Ӽ� ����
	 * @param type
	 *            String �Ӽ� �� type
	 * @param rtexprvalue
	 *            boolean �ǽð� �� �� ���� ([% %] ���� ���� �κ��� �ؼ��� ���ΰ� ����)
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
