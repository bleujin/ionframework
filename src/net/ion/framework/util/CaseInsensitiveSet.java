package net.ion.framework.util;

import java.io.Serializable;
import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class CaseInsensitiveSet<E> extends AbstractSet<E> implements Set<E>, Serializable {

	private static final long serialVersionUID = -4787197864449172206L;
	private transient Map<E, Object> map;
	private static final Object PRESENT = new Object();

	public CaseInsensitiveSet() {
		map = new CaseInsensitiveHashMap();
	}

	public CaseInsensitiveSet(E[] values) {
		this();
		if (values == null)
			return;
		for (E value : values) {
			add(value);
		}
	}

	public Iterator<E> iterator() {
		return map.keySet().iterator();
	}

	public int size() {
		return map.size();
	}

	public boolean isEmpty() {
		return map.isEmpty();
	}

	public boolean contains(Object o) {
		return map.containsKey(o);
	}

	public boolean add(E o) {
		return map.put(o, PRESENT) == null;
	}

	public boolean remove(Object o) {
		return map.remove(o) == PRESENT;
	}

	public void clear() {
		map.clear();
	}
}
