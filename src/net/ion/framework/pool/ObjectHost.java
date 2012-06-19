package net.ion.framework.pool;

/**
 * Pool�� ����ѵ� pool�� ��� object�� ã�� ���ϸ� null�� ���������� host�� ���� �����Ͽ� �����Ѵ�.
 * 
 * @author Kim, Sanghoon (wizest@i-on.net)
 * @version 1.0
 */

public abstract class ObjectHost {
	private ObjectPool pool = null;
	private Class<?> objectClass = null;

	/**
	 * object host�� �����Ѵ�.
	 * 
	 * @param pool
	 *            ObjectPool host ���ο��� ����� pooling ��ü
	 * @param objectClass
	 *            Class pooling�� object�� class (pool�� ���� object�� ���� ��� �� class�� instantiation�Ѵ�.)
	 */
	public ObjectHost(ObjectPool pool, Class<?> objectClass) {
		this.pool = pool;
		this.objectClass = objectClass;
	}

	/**
	 * object host�� �����Ѵ�.
	 */
	public void destroy() {
		this.pool.destroy();
	}

	/**
	 * pooling �ϰ� �ִ� object�� �����Ѵ�. �����ִ� object�� ���� ��� �⺻�����ڷ� �����Ͽ� �����ϴ�.
	 * 
	 * @return Object
	 */
	public final Object askObject() {
		Object o = this.pool.getObject();
		if (o == null) {
			try {
				o = objectClass.newInstance();
			} catch (Exception ex) {
				throw new RuntimeException("could not instantiate the class, class name=" + objectClass.getName(), ex);
			}
		}

		beforeAskObject(o);
		return o;
	}

	/**
	 * ����� ���� object�� ��ȯ�޴´�.
	 * 
	 * @param o
	 *            Object
	 */
	public final void releaseObject(Object o) {
		beforeReleaseObject(o);
		this.pool.releaseObject(o);
	}

	/**
	 * askObject()���� ���� object�� return�Ǳ� ���� �ҷ�����.
	 * 
	 * @param o
	 *            Object ���ϵ� object
	 */
	public abstract void beforeAskObject(Object o);

	/**
	 * releaseObject()���� ���� pool�� ���ư����� �ҷ�����.
	 * 
	 * @param o
	 *            Object pool�� release�� object
	 */
	public abstract void beforeReleaseObject(Object o);
}
