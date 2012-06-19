package net.ion.framework.template.tagext;

/**
 * Body 를 가지는 tag model
 * 
 * @author Kim Sanghoon wizest@i-on.net
 * @version 1.0
 * 
 * @see javax.servlet.jsp.tagext.BodyTag
 */

public interface BodyTag extends IterationTag {
	public static final int EVAL_BODY_BUFFERED = 2;

	/**
	 * doStartTag가 BodyTag.EVAL_BODY_BUFFERED 를 리턴할 경우 template runtime이 bodycontent 를 초기화 시켜 여기로 넘긴다.
	 * 
	 * @param bodycontent
	 *            BodyContent
	 */
	public abstract void setBodyContent(BodyContent bodycontent);

	/**
	 * setBodyContent 를 부른 직후 불러진다.
	 * 
	 * @throws TagException
	 */
	public abstract void doInitBody() throws TagException;
}
