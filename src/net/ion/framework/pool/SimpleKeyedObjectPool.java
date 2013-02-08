package net.ion.framework.pool;

import java.util.Enumeration;
import java.util.Hashtable;

/**
 * 간단하게 구현된 KeyedObjectPool
 * 
 * @author Kim Sanghoon wizest@i-on.net
 * @version 1.0
 */

public final class SimpleKeyedObjectPool implements KeyedObjectPool {
	Hashtable<Object, ObjectPool> poolMap = null;
	int maxObject;

	/**
	 * @param maxObject
	 *            각 key마다 최대 object 크기
	 */
	public SimpleKeyedObjectPool(int maxObject) {
		this.poolMap = new Hashtable<Object, ObjectPool>();
		this.maxObject = maxObject;
	}

	public synchronized void addObject(Object key, Object o) {
		if (poolMap.contains(key)) {
			ObjectPool pool = (ObjectPool) poolMap.get(key);
			pool.addObject(o);
		} else {
			ObjectPool pool = new SimpleObjectPool(this.maxObject);
			pool.addObject(o);
			poolMap.put(key, pool);
		}
	}

	public synchronized Object getObject(Object key) {
		if (poolMap.contains(key)) {
			ObjectPool pool = (ObjectPool) poolMap.get(key);
			return pool.getObject();
		} else {
			return null;
		}
	}

	public void releaseObject(Object key, Object o) {
		addObject(key, o);
	}

	public synchronized void clear() {
		Enumeration<ObjectPool> e = poolMap.elements();
		while (e.hasMoreElements()) {
			ObjectPool pool = (ObjectPool) e.nextElement();
			pool.destroy();
		}
		poolMap.clear();
	}

	public synchronized void destroy() {
		Enumeration<ObjectPool> e = poolMap.elements();
		while (e.hasMoreElements()) {
			ObjectPool pool = (ObjectPool) e.nextElement();
			pool.destroy();
		}
		poolMap = null;
	}
}
