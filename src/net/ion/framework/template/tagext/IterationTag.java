package net.ion.framework.template.tagext;

/**
 * iteration ������ tag model
 * 
 * @author Kim Sanghoon wizest@i-on.net
 * @version 1.0
 * 
 * @see javax.servlet.jsp.tagext.IterationTag
 */
public interface IterationTag extends UniPageTag {
	public static final int EVAL_BODY_AGAIN = 2;

	/**
	 * IterationTag.EVAL_BODY_AGAIN �Ǵ� IterationTag.SKIP_BODY �� �����Ѵ�.
	 * 
	 * @return
	 * @throws TagException
	 */
	public abstract int doAfterBody() throws TagException;
}
