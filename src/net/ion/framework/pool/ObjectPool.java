package net.ion.framework.pool;

/**
 * Object pool
 * 
 * @author Kim Sanghoon wizest@i-on.net
 * @version 1.0
 */

public interface ObjectPool {
	/**
	 * pool에 object를 넣는다.
	 * 
	 * @param o
	 */
	public void addObject(Object o);

	/**
	 * pool에서 object를 가져온다.
	 * 
	 * @return
	 */
	public Object getObject();

	/**
	 * 사용한 object를 반납한다.
	 * 
	 * @param o
	 */
	public void releaseObject(Object o);

	/**
	 * pool의 모든 object를 정리한다.
	 */
	public void clear();

	/**
	 * pool을 소멸시킨다.
	 */
	public void destroy();
}
