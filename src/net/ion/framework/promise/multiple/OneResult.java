package net.ion.framework.promise.multiple;

import net.ion.framework.promise.Promise;

/**
 * 
 * @author Ray Tsang
 *
 */
@SuppressWarnings("rawtypes")
public class OneResult {
	private final int index;
	private final Promise promise;
	private final Object result;
	
	public OneResult(int index, Promise promise, Object result) {
		super();
		this.index = index;
		this.promise = promise;
		this.result = result;
	}
	public int getIndex() {
		return index;
	}
	public Promise getPromise() {
		return promise;
	}
	public Object getResult() {
		return result;
	}
	@Override
	public String toString() {
		return "OneResult [index=" + index + ", promise=" + promise
				+ ", result=" + result + "]";
	}
}
