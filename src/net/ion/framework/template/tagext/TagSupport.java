package net.ion.framework.template.tagext;

import java.io.Serializable;

/**
 * body�� ������ �ʴ� tag�� �⺻ ����
 * 
 * <pre>
 * ȣ�� ����
 * 1. setParent
 * 2. setPageContext
 * 3. doStartTag
 * 4. doEndTag
 * 5. release
 * </pre>
 * 
 * @author Kim Sanghoon wizest@i-on.net
 * @version 1.0
 */

public abstract class TagSupport extends BaseTagSupport implements IterationTag, Serializable {
	// reserved replace words..
	protected final String SPACE = "#SPACE#";
	protected final String SPACE_VALUE = " ";
	protected final String NEWLINE = "#NEWLINE#";
	protected final String NEWLINE_VALUE = "\r\n";
	protected final String TAB = "#TAB#";
	protected final String TAB_VALUE = "\t";
	protected final String BLANK = "#BLANK#";
	protected final String BLANK_VALUE = "";

	protected PageContext pageContext = null;

	public TagSupport() {
	}

	public int doStartTag() throws TagException {
		return UniPageTag.SKIP_BODY;
	}

	public int doEndTag() throws TagException {
		return UniPageTag.EVAL_PAGE;
	}

	public int doAfterBody() throws TagException {
		return UniPageTag.SKIP_BODY;
	}

	public void release() {
		pageContext = null;
		super.release();
	}

	public void setPageContext(PageContext pageContext) {
		this.pageContext = pageContext;
	}

	/**
	 * Ư���� ������ ���ٸ� pageContext�� �ٷ� �����ϱ� �ٶ�
	 * 
	 * @return
	 */
	public PageContext _getPageContext() {
		return this.pageContext;
	}

}
