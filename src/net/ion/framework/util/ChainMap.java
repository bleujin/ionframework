package net.ion.framework.util;

import java.util.Map;

public class ChainMap<K, V> {

	private Map<K, V> store = MapUtil.newMap();

	public ChainMap<K, V> put(K key, V value) {
		store.put(key, value);
		return this;
	}

	public Map<K, V> toMap() {
		return store;
	}

}