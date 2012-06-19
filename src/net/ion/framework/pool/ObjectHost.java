package net.ion.framework.pool;

/**
 * Pool과 비슷한데 pool의 경우 object를 찾지 못하면 null을 리턴하지만 host는 새로 생성하여 리턴한다.
 * 
 * @author Kim, Sanghoon (wizest@i-on.net)
 * @version 1.0
 */

public abstract class ObjectHost {
	private ObjectPool pool = null;
	private Class<?> objectClass = null;

	/**
	 * object host를 생성한다.
	 * 
	 * @param pool
	 *            ObjectPool host 내부에서 사용할 pooling 객체
	 * @param objectClass
	 *            Class pooling할 object의 class (pool에 남는 object가 없을 경우 이 class를 instantiation한다.)
	 */
	public ObjectHost(ObjectPool pool, Class<?> objectClass) {
		this.pool = pool;
		this.objectClass = objectClass;
	}

	/**
	 * object host를 정리한다.
	 */
	public void destroy() {
		this.pool.destroy();
	}

	/**
	 * pooling 하고 있는 object를 리턴한다. 여유있는 object가 없을 경우 기본생성자로 생성하여 리턴하다.
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
	 * 사용이 끝난 object를 반환받는다.
	 * 
	 * @param o
	 *            Object
	 */
	public final void releaseObject(Object o) {
		beforeReleaseObject(o);
		this.pool.releaseObject(o);
	}

	/**
	 * askObject()에서 최종 object가 return되기 전에 불러진다.
	 * 
	 * @param o
	 *            Object 리턴될 object
	 */
	public abstract void beforeAskObject(Object o);

	/**
	 * releaseObject()에서 최종 pool로 돌아가기전 불러진다.
	 * 
	 * @param o
	 *            Object pool에 release될 object
	 */
	public abstract void beforeReleaseObject(Object o);
}
