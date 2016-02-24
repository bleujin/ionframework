package net.ion.framework.promise;

/**
 * @see Deferred#reject(Object)
 * @see Promise#fail(FailCallback)
 * @param <F>
 */
public interface FailCallback<F> {
	public void onFail(final F result);
}
