package net.ion.framework.template.tagext;

/**
 * body �� ������ tag model�� �⺻ ���� body tag�� �ۼ��� �� Ŭ������ ����Ѵ�.
 * 
 * <pre>
 * ȣ�� ����
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
	 * start tag ��ġ����
	 * 
	 * @throws TagException
	 * @return int { SKIP_BODY | EVAL_BODY_INCLUDE | EVAL_BODY_BUFFERED }
	 */
	public int doStartTag() throws TagException {
		return BodyTag.EVAL_BODY_BUFFERED;
	}

	/**
	 * end tag ��ġ����
	 * 
	 * @throws TagException
	 * @return int { EVAL_PAGE | SKIP_PAGE }
	 */
	public int doEndTag() throws TagException {
		return super.doEndTag();
	}

	/**
	 * ���ο� body content�� ���۵� ��
	 * 
	 * @param b
	 *            BodyContent
	 */
	public void setBodyContent(BodyContent b) {
		bodyContent = b;
	}

	/**
	 * setBodyContent ���� �ҷ���
	 * 
	 * @throws TagException
	 */
	public void doInitBody() throws TagException {
	}

	/**
	 * end tag ������
	 * 
	 * @throws TagException
	 * @return int { EVAL_BODY_AGAIN | SKIP_BODY }
	 */
	public int doAfterBody() throws TagException {
		return BodyTag.SKIP_BODY;
	}

	/**
	 * tag�� �����Ѵ�.
	 */
	public void release() {
		bodyContent = null;
		super.release();
	}

	public BodyContent getBodyContent() {
		return bodyContent;
	}

	/**
	 * doStartTag �� EVAL_BODY_BUFFERED�� return �Ͽ��� �� pageWriter�� �� layer �÷����µ� ���� layer�� pageWriter�� ��´�.
	 * 
	 * @return PageWriter
	 */
	public PageWriter getPreviousOut() {
		return bodyContent.getEnclosingWriter();
	}
}
