package net.ion.framework.template.ref;

/**
 * �۾� ����
 * 
 * @author Kim Sanghoon wizest@i-on.net
 * @version 1.0
 */

public interface Context {
	/**
	 * <pre>
	 * context ���� ���� ������ attribute�� �߰��� ������ ã�´�.
	 * pageContext &gt; templateContext &gt; publishingContext
	 * </pre>
	 * 
	 * @param name
	 * @return
	 */
	Object findAttribute(String name);

	/**
	 * �Ӽ��� �����Ѵ�.
	 * 
	 * @param name
	 *            String ������ �Ӽ� �̸�
	 * @param obj
	 *            Object �Ӽ���
	 */
	void setAttribute(String name, Object obj);

	/**
	 * �Ӽ��� �����´�.
	 * 
	 * @param name
	 *            String �Ӽ� �̸�
	 * @return Object �Ӽ���
	 */
	Object getAttribute(String name);

	/**
	 * �Ӽ��� �����.
	 * 
	 * @param name
	 *            String �Ӽ� �̸�
	 */
	void removeAttribute(String name);

	/**
	 * �θ� ������ null
	 * 
	 * @return
	 */
	Context getParent();
}
