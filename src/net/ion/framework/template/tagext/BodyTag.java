package net.ion.framework.template.tagext;

/**
 * Body �� ������ tag model
 * 
 * @author Kim Sanghoon wizest@i-on.net
 * @version 1.0
 * 
 * @see javax.servlet.jsp.tagext.BodyTag
 */

public interface BodyTag extends IterationTag {
	public static final int EVAL_BODY_BUFFERED = 2;

	/**
	 * doStartTag�� BodyTag.EVAL_BODY_BUFFERED �� ������ ��� template runtime�� bodycontent �� �ʱ�ȭ ���� ����� �ѱ��.
	 * 
	 * @param bodycontent
	 *            BodyContent
	 */
	public abstract void setBodyContent(BodyContent bodycontent);

	/**
	 * setBodyContent �� �θ� ���� �ҷ�����.
	 * 
	 * @throws TagException
	 */
	public abstract void doInitBody() throws TagException;
}
