package net.ion.framework.promise.multiple;

import net.ion.framework.promise.Promise;

/**
 * Progress fail by one of the {@link Promise}.
 * @author Ray Tsang
 *
 */
@SuppressWarnings("rawtypes")
public class OneReject {
	private final int index;
	private final Promise promise;
	private final Object reject;
	
	public OneReject(int index, Promise promise, Object reject) {
		super();
		this.index = index;
		this.promise = promise;
		this.reject = reject;
	}

	public int getIndex() {
		return index;
	}

	public Promise getPromise() {
		return promise;
	}

	public Object getReject() {
		return reject;
	}

	@Override
	public String toString() {
		return "OneReject [index=" + index + ", promise=" + promise
				+ ", reject=" + reject + "]";
	}
	
	
	
}
