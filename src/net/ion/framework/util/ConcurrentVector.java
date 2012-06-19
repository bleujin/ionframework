package net.ion.framework.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

public class ConcurrentVector<T> implements Collection<T> {

	private Vector<T> inner ;
	
	public ConcurrentVector(){
		inner = new Vector<T>();
	}  
	
	public ConcurrentVector(int initialCapacity){
		inner = new Vector<T>(initialCapacity);
	}

	public int size() {
		return inner.size();
	}

	public boolean isEmpty() {
		return inner.isEmpty();
	}

	public boolean contains(Object elem) {
		return inner.contains(elem);
	}

	public Iterator<T> iterator() {
		return inner.iterator();
	}

	public Object[] toArray() {
		return toCollection().toArray();
	}

	public <T> T[] toArray(T[] a) {
		synchronized (inner) {
			return inner.toArray(a);
		}
	}

	public Collection<T> toCollection() {
		synchronized (inner) {
			return new ArrayList<T>(inner);
		}
	}

	public boolean add(T o) {
		return inner.add(o);
	}

	public boolean containsAll(Collection<?> c) {
		return inner.containsAll(c);
	}

	public boolean addAll(Collection<? extends T> c) {
		return inner.addAll(c);
	}

	public boolean removeAll(Collection<?> c) {
		return inner.removeAll(c);
	}

	public void removeAllElements() {
		inner.removeAllElements();
	}
	
	public boolean retainAll(Collection<?> c) {
		return inner.retainAll(c);
	}

	public boolean remove(Object o) {
		return inner.remove(o);
	}

	public void clear() {
		inner.clear();
	}

}