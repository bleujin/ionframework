package net.ion.framework.util;

import org.apache.commons.collections.map.LRUMap;

/**
 * 범용 cache 구현 - 제한 크기를 초과할 경우 LRU 방법에 의해 과거의 값이 제거된다. 사용 방법은 java.util.Map과 유사
 * 
 * @author Kim Sanghoon wizest@i-on.net
 * @version 1.0
 * 
 * @see java.util.Map
 * @see org.apache.commons.collections.map.LRUMap;
 */
public class GenericCache {
	private ICache cache;

	public GenericCache(int cacheSize) {
		// this.cache=new HashMapCache(cacheSize);

		// use jakarta collection's LRUMap
		this.cache = new LRUCache(cacheSize);
	}

	public void put(Object key, Object value) {
		synchronized (cache) {
			cache.put(key, value);
		}
	}

	public Object get(Object key) {
		synchronized (cache) {
			return cache.get(key);
		}
	}

	public void clear() {
		synchronized (cache) {
			cache.clear();
		}
	}

	public Object remove(Object key) {
		synchronized (cache) {
			return cache.remove(key);
		}
	}

	public boolean containsKey(Object key) {
		return cache.containsKey(key);
	}
}

interface ICache {
	Object put(Object key, Object value);

	Object get(Object key);

	Object remove(Object o);

	void clear();

	boolean containsKey(Object key);
}

class LRUCache extends LRUMap implements ICache {
	public LRUCache(int cacheSize) {
		super(cacheSize);
	}

}
