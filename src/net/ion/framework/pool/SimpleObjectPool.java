package net.ion.framework.pool;

/**
 * 간단하게 구현된 object pool
 * 
 * @author Kim Sanghoon wizest@i-on.net
 * @version 1.0
 */

public final class SimpleObjectPool implements ObjectPool {
	final static int INIT_PTR = -1;

	private Object pool[];

	private int max;
	private int current = INIT_PTR;

	Object lock;
	public static final int DEFAULT_SIZE = 16;

	public SimpleObjectPool() {
		this.max = DEFAULT_SIZE;
		pool = new Object[max];
		lock = new Object();
	}

	public SimpleObjectPool(int max) {
		this.max = max;
		pool = new Object[max];
		lock = new Object();
	}

	public void addObject(Object o) {
		releaseObject(o);
	}

	/**
	 * Add the object to the pool, silent nothing if the pool is full
	 */
	public void releaseObject(Object o) {
		synchronized (lock) {
			if (current < (max - 1)) {
				current += 1;
				pool[current] = o;
			}
		}
	}

	/**
	 * Get an object from the pool, null if the pool is empty.
	 */
	public Object getObject() {
		Object item = null;
		synchronized (lock) {
			if (current >= 0) {
				item = pool[current];
				current -= 1;
			}
		}
		return item;
	}

	/**
	 * Return the size of the pool
	 */
	public int getMaxObject() {
		return max;
	}

	public synchronized void clear() {
		// this.max=max;
		pool = new Object[max];
		lock = new Object();

		this.current = INIT_PTR;
	}

	public void destroy() {
		pool = null;
	}

	// public static void main( String[] args )
	// {
	// SimpleObjectPool pool = new SimpleObjectPool();
	//
	// pool.addObject( SimpleObjectPool.class );
	// int i = 1000000;
	//
	// long begin = System.currentTimeMillis();
	// while ( --i > 0 )
	// {
	// pool.releaseObject( pool.getObject() );
	// }
	// System.out.println( System.currentTimeMillis() - begin );
	//
	// }
}
