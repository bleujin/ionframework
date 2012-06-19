package net.ion.framework.template.tagext;

/**
 * �������� page�� �����ϴ� tag model (page driving)<br/>
 * <br/>
 * Template�� �� 1���� MultiPageTag�� �����ؾ� �Ѵ�.
 * 
 * @author Kim Sanghoon wizest@i-on.net
 * @version 1.0
 * 
 * @see UniPageTag
 */

public interface MultiPageTag extends Tag {
	public static final int EVAL_PAGE_AGAIN = 7;
	public static final int EVAL_PAGE_AGAIN_WITHOUT_PAGE_GENERATION = 77;
	public static final int SKIP_PAGE_WITHOUT_PAGE_GENERATION = 55;

	public abstract void setTemplateContext(TemplateContext templatecontext);

	/**
	 * @throws TagException
	 * @return int { EVAL_PAGE | SKIP_PAGE }
	 */
	public abstract int doStartTemplate() throws TagException;

	public abstract void doInitPage() throws TagException;

	/**
	 * iteration�� ���۵Ǳ� ���� �ڽ� UniPageTag���� ����ϴ� page context�� parameter�� ���� PageInfo�� �����Ѵ�.
	 * 
	 * @return
	 * @throws TagException
	 */
	public abstract PageInfo createPageInfo() throws TagException;

	/**
	 * @throws TagException
	 * @return int { EVAL_PAGE_AGAIN | SKIP_PAGE | EVAL_PAGE_AGAIN_WITHOUT_PAGE_GENERATION | SKIP_PAGE_WITHOUT_PAGE_GENERATION }
	 */
	public abstract int doAfterPage() throws TagException;

	// iteration ����

	public abstract void doEndTemplate() throws TagException;
}
