package net.ion.framework.promise;

/**
 * @see Deferred#resolve(Object)
 * @see Promise#done(DoneCallback)
 * @param <D>
 */
public interface DoneCallback<D> {
	public void onDone(final D result);
}
