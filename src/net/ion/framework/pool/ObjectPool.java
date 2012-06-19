package net.ion.framework.pool;

/**
 * Object pool
 * 
 * @author Kim Sanghoon wizest@i-on.net
 * @version 1.0
 */

public interface ObjectPool {
	/**
	 * pool�� object�� �ִ´�.
	 * 
	 * @param o
	 */
	public void addObject(Object o);

	/**
	 * pool���� object�� �����´�.
	 * 
	 * @return
	 */
	public Object getObject();

	/**
	 * ����� object�� �ݳ��Ѵ�.
	 * 
	 * @param o
	 */
	public void releaseObject(Object o);

	/**
	 * pool�� ��� object�� �����Ѵ�.
	 */
	public void clear();

	/**
	 * pool�� �Ҹ��Ų��.
	 */
	public void destroy();
}
