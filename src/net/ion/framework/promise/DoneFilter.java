package net.ion.framework.promise;

/**
 * @see Promise#then(DoneFilter, FailFilter)
 * @param <P>
 *            Type of the input
 * @param <P_OUT>
 *            Type of the output from this filter
 */
public interface DoneFilter<D, D_OUT> {
	public D_OUT filterDone(final D result);
}
