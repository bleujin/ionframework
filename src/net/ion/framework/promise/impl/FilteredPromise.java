package net.ion.framework.promise.impl;

import net.ion.framework.promise.DoneCallback;
import net.ion.framework.promise.DoneFilter;
import net.ion.framework.promise.FailCallback;
import net.ion.framework.promise.FailFilter;
import net.ion.framework.promise.ProgressCallback;
import net.ion.framework.promise.ProgressFilter;
import net.ion.framework.promise.Promise;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class FilteredPromise<D, F, P, D_OUT, F_OUT, P_OUT> extends DeferredObject<D_OUT, F_OUT, P_OUT> implements Promise<D_OUT, F_OUT, P_OUT>{
	protected static final NoOpDoneFilter NO_OP_DONE_FILTER = new NoOpDoneFilter();
	protected static final NoOpFailFilter NO_OP_FAIL_FILTER = new NoOpFailFilter();
	protected static final NoOpProgressFilter NO_OP_PROGRESS_FILTER = new NoOpProgressFilter();
	
	private final DoneFilter<D, D_OUT> doneFilter;
	private final FailFilter<F, F_OUT> failFilter;
	private final ProgressFilter<P, P_OUT> progressFilter;
	
	public FilteredPromise(final Promise<D, F, P> promise, final DoneFilter<D, D_OUT> doneFilter, final FailFilter<F, F_OUT> failFilter, final ProgressFilter<P, P_OUT> progressFilter) {
		this.doneFilter = doneFilter == null ? NO_OP_DONE_FILTER : doneFilter;
		this.failFilter = failFilter == null ? NO_OP_FAIL_FILTER : failFilter;
		this.progressFilter = progressFilter == null ? NO_OP_PROGRESS_FILTER : progressFilter;
		
		promise.done(new DoneCallback<D>() {
			public void onDone(D result) {
				FilteredPromise.this.resolve(FilteredPromise.this.doneFilter.filterDone(result));
			}
		}).fail(new FailCallback<F>() {
			public void onFail(F result) {
				FilteredPromise.this.reject(FilteredPromise.this.failFilter.filterFail(result));
			}
		}).progress(new ProgressCallback<P>() {
			public void onProgress(P progress) {
				FilteredPromise.this.notify(FilteredPromise.this.progressFilter.filterProgress(progress));
			}
		});
	}
	
	public static final class NoOpDoneFilter<D> implements DoneFilter<D, D> {
		public D filterDone(D result) {
			return result;
		}
	}
	
	public static final class NoOpFailFilter<F> implements FailFilter<F, F> {
		public F filterFail(F result) {
			return result;
		}
	}
	
	public static final class NoOpProgressFilter<P> implements ProgressFilter<P, P> {
		public P filterProgress(P progress) {
			return progress;
		}
	}
}