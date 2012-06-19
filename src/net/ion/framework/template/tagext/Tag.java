package net.ion.framework.template.tagext;

/**
 * tag�� �⺻ ��
 * 
 * @author Kim Sanghoon wizest@i-on.net
 * @version 1.0
 */

public interface Tag {
	public static final int SKIP_PAGE = 5;
	public static final int EVAL_PAGE = 6;

	/**
	 * template �� tag ���� ���� ���踦 �����Ѵ�.
	 * 
	 * @param tag
	 *            Tag
	 */
	public abstract void setParent(Tag tag);

	public abstract Tag getParent();

	/**
	 * ����� tag�� ������ �� �ҷ�����.
	 */
	public abstract void release();
}
