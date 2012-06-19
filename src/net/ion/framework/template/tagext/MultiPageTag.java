package net.ion.framework.template.tagext;

/**
 * 여러장의 page를 생성하는 tag model (page driving)<br/>
 * <br/>
 * Template당 단 1개의 MultiPageTag가 존재해야 한다.
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
	 * iteration이 시작되기 전에 자식 UniPageTag에서 사용하는 page context의 parameter로 사용될 PageInfo를 생성한다.
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

	// iteration 종료

	public abstract void doEndTemplate() throws TagException;
}
