package net.ion.framework.template.tagext;

/**
 * body 를 가지는 tag model의 기본 구현 body tag를 작성시 이 클래스를 상속한다.
 * 
 * <pre>
 * 호출 순서
 * 1. setParent
 * 2. setPageContext
 * 3. doStartTag -[EVAL_BODY_BUFFERED]->4 / -[EVAL_BODY_INCLUDE]->6 /  -[SKIP_BODY]->7
 * 4. setBodycontent
 * 5. doInitBody
 * 6. doAfterBody -[EVAL_BODY_AGAIN]->6 / -[SKIP_BODY]->7
 * 7. doEndTag
 * 8. release
 * </pre>
 * 
 * @author Kim Sanghoon wizest@i-on.net
 * @version 1.0
 */

public abstract class BodyTagSupport extends TagSupport implements BodyTag {
	protected BodyContent bodyContent = null;

	public BodyTagSupport() {
	}

	/**
	 * start tag 위치에서
	 * 
	 * @throws TagException
	 * @return int { SKIP_BODY | EVAL_BODY_INCLUDE | EVAL_BODY_BUFFERED }
	 */
	public int doStartTag() throws TagException {
		return BodyTag.EVAL_BODY_BUFFERED;
	}

	/**
	 * end tag 위치에서
	 * 
	 * @throws TagException
	 * @return int { EVAL_PAGE | SKIP_PAGE }
	 */
	public int doEndTag() throws TagException {
		return super.doEndTag();
	}

	/**
	 * 새로운 body content가 시작될 때
	 * 
	 * @param b
	 *            BodyContent
	 */
	public void setBodyContent(BodyContent b) {
		bodyContent = b;
	}

	/**
	 * setBodyContent 이후 불러짐
	 * 
	 * @throws TagException
	 */
	public void doInitBody() throws TagException {
	}

	/**
	 * end tag 직전에
	 * 
	 * @throws TagException
	 * @return int { EVAL_BODY_AGAIN | SKIP_BODY }
	 */
	public int doAfterBody() throws TagException {
		return BodyTag.SKIP_BODY;
	}

	/**
	 * tag를 정리한다.
	 */
	public void release() {
		bodyContent = null;
		super.release();
	}

	public BodyContent getBodyContent() {
		return bodyContent;
	}

	/**
	 * doStartTag 가 EVAL_BODY_BUFFERED를 return 하였을 때 pageWriter가 한 layer 올려지는데 이전 layer의 pageWriter를 얻는다.
	 * 
	 * @return PageWriter
	 */
	public PageWriter getPreviousOut() {
		return bodyContent.getEnclosingWriter();
	}
}
