package net.ion.framework.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class MapUtil {
	public final static Map EMPTY = Collections.EMPTY_MAP ;
	
	public final static <K, V> Map<K, V> newMap(){
		return new HashMap<K, V>() ;
	}

	public final static <K, V> Map<K, V> newSyncMap(){
		return Collections.synchronizedMap(new HashMap<K, V>()) ;
	}


	public final static <V> Map<String, V> newCaseInsensitiveMap(){
		return new CaseInsensitiveHashMap<V>() ;
	}

	public final static <K, V> Map<K, V> newOrdereddMap(){
		return new LinkedHashMap<K, V>() ;
	}
	
	public static<K, T> Map<K, T> create(K key, T value) {
		Map<K, T> result = new HashMap<K, T>() ;
		result.put(key, value) ;
		return result;
	}


	public static Map<String, Object> stringMap(String key, Object value) {
		Map<String, Object> result = new HashMap<String, Object>() ;
		result.put(key, value) ;
		return result;
	}	
	
	public static <K, V> ChainMap<K, V> chainMap(){
		return new ChainMap<K, V>() ;
	}

	public static <V> ChainMap<String, V> chainKeyMap(){
		return new ChainMap<String, V>() ;
	}
	
	public static <K, V> Map<K, V> filterMap(Map<K, V> map, String keypattern) {
        try {
            Map<K, V> filtered = map.getClass().newInstance();
            for (Map.Entry<K, V> entry : map.entrySet()) {
                K key = entry.getKey();
                if (key.toString().matches(keypattern)) {
                    filtered.put(key, entry.getValue());
                }
            }
            return filtered;
        } catch (IllegalAccessException iex) {
            throw new IllegalArgumentException(iex) ;
        } catch (InstantiationException iex) {
        	throw new IllegalArgumentException(iex) ;
		}
    }

	public static Map<String, Object> toFlat(Map<String, Object> map) {
		return toFlat(map, '.') ;
	}
	
	public static Map<String, Object> toFlat(Map<String, Object> map, char div) {
		Map<String, Object> result = newMap() ;
		for (Entry<String, Object> entry : map.entrySet()) {
			recursiveObject(result, entry.getKey(), div, entry.getValue()) ;
		}

		return result ;
	}
	
	private static void recursiveObject(Map<String, Object> parent, String parentPath, char div, Object value){
		if (value instanceof Map){
			Map<String, Object> that = (Map) value ;
			for (Entry<String, Object> entry : that.entrySet()) {
				recursiveObject(parent, parentPath +  div + entry.getKey(), div, entry.getValue()) ;
			}
		} else if (value instanceof List){
			parent.put(parentPath, value) ;
		} else {
			parent.put(parentPath, value) ;
		}
	}

	
}
