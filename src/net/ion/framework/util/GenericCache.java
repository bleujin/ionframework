package net.ion.framework.util;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import org.apache.commons.collections.map.LRUMap;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.util.concurrent.UncheckedExecutionException;

import net.ion.framework.collection.map.ConcurrentLinkedHashMap;
import net.ion.framework.exception.ExecutionRuntimeException;

/**
 * 범용 cache 구현 - 제한 크기를 초과할 경우 LRU 방법에 의해 과거의 값이 제거된다. 사용 방법은 java.util.Map과 유사
 * 
 * @author Kim Sanghoon wizest@i-on.net
 * @version 1.0
 * 
 * @see java.util.Map
 * @see org.apache.commons.collections.LRUMAP
 */
public interface GenericCache<K, V> {

	public final static class Builder<K, V> {
		private int cacheSize;
		private CacheLoader<K, V> loader;

		public Builder(int cacheSize) {
			this.cacheSize = cacheSize;
		}

		public Builder<K, V> loader(CacheLoader<K, V> loader) {
			this.loader = loader;
			return this;
		}

		public GenericCache<K, V> build() {
			CacheBuilder builder = CacheBuilder.newBuilder().maximumSize(cacheSize);
			if (loader != null)
				return new GuavaCache<K, V>(builder.build(loader));
			return new GuavaCache<K, V>(builder.build());
		}

		public GenericCache<K, V> old() {
//			return build();
			return new GenericCacheOld<K, V>(this.cacheSize);
		}
	}

	public void put(K key, V value);

	public V get(K key);

	public V get(K key, Callable<? extends V> call);

	public void clear();

	public void remove(K key);

	public boolean containsKey(K key);
}

class GuavaCache<K, V> implements GenericCache<K, V> {

	private Cache<K, V> cache;

	public GuavaCache(Cache<K, V> cache) {
		this.cache = cache;
	}

	public void clear() {
		cache.cleanUp();
	}

	public boolean containsKey(K key) {
		return cache.getIfPresent(key) != null;
	}

	public V get(K key) {
		return cache.getIfPresent(key);
	}

	public void put(K key, V value) {
		cache.put(key, value);
	}

	public void remove(K key) {
		cache.invalidate(key);
	}

	public V get(K key, Callable<? extends V> call){
		try {
			return cache.get(key, call);
		} catch (ExecutionException ex) {
			throw new ExecutionRuntimeException(ex.getCause(), ex.getCause().getMessage()) ;
		} catch (UncheckedExecutionException ex) {
			throw new ExecutionRuntimeException(ex.getCause(), ex.getCause().getMessage()) ;
		}
	}

}

class GenericCacheOld<K, V> implements GenericCache<K, V> {

//	private ICache cache;
	private ConcurrentLinkedHashMap<K, V> cache;

	public GenericCacheOld(int cacheSize) {
		// this.cache=new HashMapCache(cacheSize); 
		cache = new ConcurrentLinkedHashMap.Builder<K, V>().maximumWeightedCapacity(cacheSize).build();
	}

	public void put(K key, V value) {
		cache.put(key, value);
	}

	public V get(K key) {
		return (V) cache.get(key);
	}

	public void clear() {
		cache.clear();
	}

	public void remove(K key) {
		cache.remove(key);
	}

	public boolean containsKey(K key) {
		return cache.containsKey(key);
	}

	public V get(K key, Callable<? extends V> call) {
		V value = get(key);
		try {
			if (value == null) {
				synchronized (this) {
					value = get(key) ; // Double Get
					if (value == null) {
						value = call.call();
						cache.put(key, value) ;
					}
				}
			}
		} catch (Throwable ex) {
			throw new ExecutionRuntimeException(ex, ex.getMessage()) ;
		}
		return value;
	}
}

interface ICache {
	Object put(Object key, Object value);

	Object get(Object key);

	Object remove(Object o);

	void clear();

	boolean containsKey(Object key);
}

@Deprecated
class LRUCache<K, V> extends LRUMap implements ICache {
	public LRUCache(int cacheSize) {
		super(cacheSize);
	}

}
