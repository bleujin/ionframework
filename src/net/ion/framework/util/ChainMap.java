package net.ion.framework.util;

import java.util.Map;
import java.util.Map.Entry;

public class ChainMap<K, V> {

	private Map<K, V> store = MapUtil.newMap() ;
	
	public ChainMap<K, V> put(Map<K, ? extends V> map) {
		for (Entry<K, ? extends V> entry : map.entrySet()) {
			put(entry.getKey(), entry.getValue()) ;
		}
		return this ;
	}
	
	public ChainMap<K, V> put(K key, V value) {
		store.put(key, value) ;
		return this ;
	}
	
	public Map<K, V> toMap(){
		return store ;
	}
	
}
