package net.ion.framework.template.tagext;

/**
 * page�� driving �ϴ� tag(multi tag)�� �ƴ� tag�� model
 * 
 * @author Kim Sanghoon wizest@i-on.net
 * @version 1.0
 * @see MultiPageTag
 */

public interface UniPageTag extends Tag {
	public static final int SKIP_BODY = 0;
	public static final int EVAL_BODY_INCLUDE = 1;

	/**
	 * template runtime�� tag ����ϱ� ���� pageContext�� �ʱ�ȭ �Ͽ� ����� ������.
	 * 
	 * @param pagecontext
	 *            PageContext
	 */
	public abstract void setPageContext(PageContext pagecontext);

	/**
	 * Tag.EVAL_BODY_INCLUDE (BodyTag.EVAL_BODY_BUFFERED) �Ǵ� Tag.SKIP_BODY �� �����Ѵ�.
	 * 
	 * @return
	 * @throws TagException
	 */
	public abstract int doStartTag() throws TagException;

	/**
	 * Tag.EVAL_PAGE �Ǵ� Tag.SKIP_PAGE �� �����Ѵ�.
	 * 
	 * @return
	 * @throws TagException
	 */
	public abstract int doEndTag() throws TagException;

}
