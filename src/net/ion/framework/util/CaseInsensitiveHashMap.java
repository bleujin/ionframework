package net.ion.framework.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CaseInsensitiveHashMap<V> extends HashMap<String, V> {

	private static final long serialVersionUID = 560261785825477651L;

	public boolean containsKey(Object key) {
		return super.containsKey(convertKey((String) key));
	}

	public V get(Object key) {
		return super.get(convertKey((String) key));
	}

	public V put(String key, V value) {
		return super.put(convertKey(key), value);
	}

	private String convertKey(String key) {
		if (key == null)
			return null;
		return key.toLowerCase();
	}

	public void putAll(Map<? extends String, ? extends V> m) {
		Set<? extends String> entrys = m.keySet();
		for (String key : entrys) {
			this.put(key, m.get(key));
		}
	}

	public V remove(Object key) {
		return super.remove(convertKey((String) key));
	}

}
