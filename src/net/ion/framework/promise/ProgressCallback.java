package net.ion.framework.promise;

/**
 * @see Deferred#notify(Object)
 * @see Promise#progress(ProgressCallback)
 * @param <P>
 */
public interface ProgressCallback<P> {
	public void onProgress(final P progress);
}
