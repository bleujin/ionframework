package net.ion.framework.promise;

import junit.framework.Assert;

public class ValueHolder<T> {
	private T value;
	
	public ValueHolder() {
	}
	
	public ValueHolder(T value) {
		this.value = value;
	}
	
	public void set(T value) {
		this.value = value;
	}
	
	public T get() {
		return this.value;
	}
	
	public void clear() {
		this.value = null;
	}
	
	public void assertEquals(T other) {
		Assert.assertEquals(other, value);
	}
}

