package net.ion.framework.promise;

/**
 * @see Deferred#then(DoneFilter, FailFilter, ProgressFilter)
 * @param <P>
 *            Type of the input
 * @param <P_OUT>
 *            Type of the output from this filter
 */
public interface ProgressFilter<P, P_OUT> {
	public P_OUT filterProgress(final P progress);
}
