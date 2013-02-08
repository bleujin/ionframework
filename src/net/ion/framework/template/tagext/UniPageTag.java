package net.ion.framework.template.tagext;

/**
 * page를 driving 하는 tag(multi tag)가 아닌 tag의 model
 * 
 * @author Kim Sanghoon wizest@i-on.net
 * @version 1.0
 * @see MultiPageTag
 */

public interface UniPageTag extends Tag {
	public static final int SKIP_BODY = 0;
	public static final int EVAL_BODY_INCLUDE = 1;

	/**
	 * template runtime이 tag 사용하기 전에 pageContext를 초기화 하여 여기로 던진다.
	 * 
	 * @param pagecontext
	 *            PageContext
	 */
	public abstract void setPageContext(PageContext pagecontext);

	/**
	 * Tag.EVAL_BODY_INCLUDE (BodyTag.EVAL_BODY_BUFFERED) 또는 Tag.SKIP_BODY 를 리턴한다.
	 * 
	 * @return
	 * @throws TagException
	 */
	public abstract int doStartTag() throws TagException;

	/**
	 * Tag.EVAL_PAGE 또는 Tag.SKIP_PAGE 를 리턴한다.
	 * 
	 * @return
	 * @throws TagException
	 */
	public abstract int doEndTag() throws TagException;

}
