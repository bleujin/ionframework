package net.ion.framework.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

/**
 * 두개의 key를 가지는 map collection<br/>
 * <br/>
 * 주의: mapping 데이터를 추가(put),삭제(remove)하는 것은 반드시 이 클래스를 통해야 하며 이 클래스로 부터 얻은 collection에 직접 추가, 삭제 하면 안된다.
 * 
 * @author Kim Sanghoon wizest@i-on.net
 * @version 1.0
 * 
 * @see java.util.Map
 */

public class DoubleKeyHashMap<K1, K2, V> {
	private HashMap<K1, HashMap<K2, V>> map;

	public DoubleKeyHashMap() {
		map = new HashMap<K1, HashMap<K2, V>>();
	}

	public int size() {
		int size = 0;
		Iterator<HashMap<K2, V>> i = map.values().iterator();

		while (i.hasNext()) {
			size += (i.next()).size();

		}
		return size;
	}

	public boolean isEmpty() {
		if (map.isEmpty()) {
			return true;
		}

		// int size=0;
		Iterator<HashMap<K2, V>> i = map.values().iterator();

		while (i.hasNext()) {
			if (!(i.next()).isEmpty()) {
				return false;
			}
		}

		return true;
	}

	public boolean containsKey(K1 key1, K2 key2) {
		if (map.containsKey(key1)) {
			if ((map.get(key1)).containsKey(key2)) {
				return true;
			}
		}

		return false;
	}

	public boolean containsValue(V value) {
		Iterator<HashMap<K2, V>> i = map.values().iterator();

		while (i.hasNext()) {
			if ((i.next()).containsValue(value)) {
				return true;
			}
		}

		return false;
	}

	public V get(K1 key1, K2 key2) {
		HashMap<K2, V> inner = map.get(key1);
		if (inner == null) {
			return null;
		} else {
			return inner.get(key2);
		}
	}

	private HashMap<K2, V> get(K1 key1) {
		return map.get(key1);
	}

	public V put(K1 key1, K2 key2, V value) {
		// return a previous value
		synchronized (map) {
			HashMap<K2, V> inner = map.get(key1);
			if (inner == null) {
				inner = new HashMap<K2, V>();
				inner.put(key2, value);
				map.put(key1, inner);

				return null;
			} else {
				return inner.put(key2, value);
			}
		}
	}

	public V remove(K1 key1, K2 key2) {
		HashMap<K2, V> inner = map.get(key1);
		if (inner == null) {
			map.remove(key1);
			return null;
		} else {
			V previous = inner.remove(key2);
			if (inner.isEmpty()) {
				map.remove(key1);

			}
			return previous;
		}
	}

	/**
	 * key1으로 시작하는 모든 entry를 지운다.
	 * 
	 * @param key1
	 * @return 지워진 innerHashMap
	 */
	public HashMap<K2, V> remove(K1 key1) {
		return map.remove(key1);
	}

	public void putAll(K1 key1, Map<K2, V> t) {
		HashMap<K2, V> inner = map.get(key1);
		if (inner == null) {
			inner = new HashMap<K2, V>(t);
			map.put(key1, inner);
		} else {
			inner.putAll(t);
		}
	}

	public void clear() {
		map.clear();
	}

	/**
	 * 첫번째 key집합을 리턴
	 * 
	 * @return Set
	 */
	public Set<K1> key1Set() {
		return map.keySet();
	}

	/**
	 * 첫번째 키가 key1인 두번째 key집합을 리턴
	 * 
	 * @param key1
	 *            Object
	 * @return Set
	 */
	public Set<K2> key2Set(K1 key1) {
		HashMap<K2, V> inner = map.get(key1);
		if (inner == null) {
			return new HashMap<K2, V>().keySet();
		} else {
			return inner.keySet();
		}
	}

	/**
	 * key1을 가지는 value set을 리턴한다.
	 * 
	 * @param key1
	 *            Object
	 * @return Set
	 */
	public Collection<V> values(K1 key1) {
		HashMap<K2, V> inner = map.get(key1);
		if (inner == null) {
			return new HashMap<K2, V>().values();
		} else {
			return inner.values();
		}
	}

	/**
	 * key1을 가지는 entry set을 리턴한다.
	 * 
	 * @param key1
	 *            Object
	 * @return Set
	 */
	public Set<Entry<K2, V>> entrySet(K1 key1) {
		HashMap<K2, V> inner = map.get(key1);
		if (inner == null) {
			return new HashMap<K2, V>().entrySet();
		} else {
			return inner.entrySet();
		}
	}

	public boolean equals(Object o) {
		if (o instanceof DoubleKeyHashMap) {
			DoubleKeyHashMap<K1, K2, V> c = (DoubleKeyHashMap<K1, K2, V>) o;

			if (!c.key1Set().equals(map.keySet())) {
				return false;
			}

			Set<K1> keys = map.keySet();
			Iterator<K1> i = keys.iterator();

			while (i.hasNext()) {
				K1 k = i.next();

				HashMap<K2, V> h1 = map.get(k);
				HashMap<K2, V> h2 = c.get(k);

				if (!h1.equals(h2)) {
					return false;
				}
			}

			return true;
		} else {
			return false;
		}
	}

	/**
	 * key1을 가지는 map을 리턴한다.
	 * 
	 * 주의! : key1에 해당하는 innerHashMap이 없을 경우 새로운 HashMap을 생성해서 리턴한다. 그러므로 얻은 innerHashMap에 직접 entry를 추가하면 안된다!
	 * 
	 * @param key1
	 * @return
	 */
	public HashMap<K2, V> innerHashMap(K1 key1) {
		HashMap<K2, V> inner = map.get(key1);
		if (inner == null) {
			return new HashMap<K2, V>();
		} else {
			return inner;
		}
	}

	public String toString() {
		return map.toString();
	}
}
