package net.ion.framework.template.tagext;

import java.io.Serializable;

/**
 * page를 생성하는 tag의 기본 구현
 * 
 * <pre>
 * 호출 순서
 * 1. setParent
 * 2. setTemplateContext
 * 3. doStartTemplate -[EVAL_PAGE]->4 / -[SKIP_PAGE]->7
 * 4. doInitPage
 * 5. createPageInfo
 * 6. doAfterPage -[EVAL_PAGE_AGAIN]->5 / -[SKIP_PAGE]->7
 * 7. doEndTemplate
 * 8. release
 * </pre>
 * 
 * @author Kim Sanghoon wizest@i-on.net
 * @version 1.0
 */

public abstract class PageTagSupport extends BaseTagSupport implements MultiPageTag, Serializable {
	protected TemplateContext templateContext;

	public PageTagSupport() {
	}

	public void release() {
		this.templateContext = null;
		super.release();
	}

	public void setTemplateContext(TemplateContext templatecontext) {
		this.templateContext = templatecontext;
	}

	/**
	 * @return SKIP_PAGE,EVAL_PAGE
	 * @throws TagException
	 */
	public int doStartTemplate() throws TagException {
		return EVAL_PAGE;
	}

	/**
	 * doStartPage 가 EVAL_PAGE를 리턴했을 경우 PAGE를 시작하면서 불러진다.
	 * 
	 * @throws TagException
	 */
	public void doInitPage() throws TagException {
	}

	/**
	 * @return SKIP_PAGE,EVAL_PAGE_AGAIN
	 * @throws TagException
	 */
	public int doAfterPage() throws TagException {
		return SKIP_PAGE;
	}

	public void doEndTemplate() throws TagException {
	}

	public PageInfo createPageInfo() throws TagException {
		return null;
		// {
		// TranslationRequester tr=templateContext.getTranslationRequester();
		// Template tpl=tr.getTemplate();
		//
		// PageInfo pageInfo=new PageInfo(tpl.getCategoryId(),tpl.getTemplateId(),PageInfo.CONTENT_TYPE_NONE,null);
		//
		// return pageInfo;
		// }

	}

	/**
	 * 특별한 이유가 없다면 templateContext를 바로 접근하기 바람
	 * 
	 * @return
	 */
	public TemplateContext _getTemplateContext() {
		return this.templateContext;
	}
}
