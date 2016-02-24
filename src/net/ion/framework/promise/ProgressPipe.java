package net.ion.framework.promise;

/**
 * @see Promise#then(ProgressPipe, FailFilter)
 * @param <P>
 *            Type of the input
 * @param <P_OUT>
 *            Type of the output from this filter
 */
public interface ProgressPipe<P, D_OUT, F_OUT, P_OUT> {
	public Promise<D_OUT, F_OUT, P_OUT> pipeProgress(final P result);
}
