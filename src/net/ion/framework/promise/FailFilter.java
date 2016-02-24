package net.ion.framework.promise;

/**
 * @see Promise#then(DoneFilter, FailFilter)
 * @param <P>
 *            Type of the input
 * @param <P_OUT>
 *            Type of the output from this filter
 */
public interface FailFilter<F, F_OUT> {
	public F_OUT filterFail(final F result);
}
