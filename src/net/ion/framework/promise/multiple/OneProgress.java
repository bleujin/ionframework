package net.ion.framework.promise.multiple;

import net.ion.framework.promise.Promise;

/**
 * Progress update by one of the {@link Promise}.
 * 
 * @author Ray Tsang
 *
 */
@SuppressWarnings("rawtypes")
public class OneProgress extends MasterProgress {
	private final int index;
	
	private final Promise promise;
	private final Object progress;
	
	public OneProgress(int done, int fail, int total, int index, Promise promise, Object progress) {
		super(done, fail, total);
		this.index = index;
		this.promise = promise;
		this.progress = progress;
	}

	public int getIndex() {
		return index;
	}
	
	public Promise getPromise() {
		return promise;
	}

	public Object getProgress() {
		return progress;
	}

	@Override
	public String toString() {
		return "OneProgress [index=" + index + ", promise=" + promise
				+ ", progress=" + progress + ", getDone()=" + getDone()
				+ ", getFail()=" + getFail() + ", getTotal()=" + getTotal()
				+ "]";
	}
}
