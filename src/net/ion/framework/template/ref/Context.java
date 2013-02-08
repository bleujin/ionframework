package net.ion.framework.template.ref;

/**
 * 작업 공간
 * 
 * @author Kim Sanghoon wizest@i-on.net
 * @version 1.0
 */

public interface Context {
	/**
	 * <pre>
	 * context 계층 구조 순서로 attribute를 발견할 때까지 찾는다.
	 * pageContext &gt; templateContext &gt; publishingContext
	 * </pre>
	 * 
	 * @param name
	 * @return
	 */
	Object findAttribute(String name);

	/**
	 * 속성을 저장한다.
	 * 
	 * @param name
	 *            String 저장할 속성 이름
	 * @param obj
	 *            Object 속성값
	 */
	void setAttribute(String name, Object obj);

	/**
	 * 속성을 가져온다.
	 * 
	 * @param name
	 *            String 속성 이름
	 * @return Object 속성값
	 */
	Object getAttribute(String name);

	/**
	 * 속성을 지운다.
	 * 
	 * @param name
	 *            String 속성 이름
	 */
	void removeAttribute(String name);

	/**
	 * 부모가 없으면 null
	 * 
	 * @return
	 */
	Context getParent();
}
