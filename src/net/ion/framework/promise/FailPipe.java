package net.ion.framework.promise;

/**
 * @see Promise#then(DonePipe, FailPipe)
 * @param <P>
 *            Type of the input
 * @param <P_OUT>
 *            Type of the output from this filter
 */
public interface FailPipe<F, D_OUT, F_OUT, P_OUT> {
	public Promise<D_OUT, F_OUT, P_OUT> pipeFail(final F result);
}
