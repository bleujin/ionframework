package net.ion.framework.promise.impl;

import net.ion.framework.promise.Deferred;
import net.ion.framework.promise.DoneCallback;
import net.ion.framework.promise.DonePipe;
import net.ion.framework.promise.FailCallback;
import net.ion.framework.promise.FailPipe;
import net.ion.framework.promise.ProgressCallback;
import net.ion.framework.promise.ProgressPipe;
import net.ion.framework.promise.Promise;

public class PipedPromise<D, F, P, D_OUT, F_OUT, P_OUT> extends DeferredObject<D_OUT, F_OUT, P_OUT> implements Promise<D_OUT, F_OUT, P_OUT>{
	public PipedPromise(final Promise<D, F, P> promise, final DonePipe<D, D_OUT, F_OUT, P_OUT> doneFilter, final FailPipe<F, D_OUT, F_OUT, P_OUT> failFilter, final ProgressPipe<P, D_OUT, F_OUT, P_OUT> progressFilter) {
		promise.done(new DoneCallback<D>() {
			@SuppressWarnings("unchecked")
			public void onDone(D result) {
				if (doneFilter != null) pipe(doneFilter.pipeDone(result));
				else PipedPromise.this.resolve((D_OUT) result);
				
			}
		}).fail(new FailCallback<F>() {
			@SuppressWarnings("unchecked")
			public void onFail(F result) {
				if (failFilter != null)  pipe(failFilter.pipeFail(result));
				else PipedPromise.this.reject((F_OUT) result);
			}
		}).progress(new ProgressCallback<P>() {
			@SuppressWarnings("unchecked")
			public void onProgress(P progress) {
				if (progressFilter != null) pipe(progressFilter.pipeProgress(progress));
				else PipedPromise.this.notify((P_OUT) progress);
			}
		});
	}
	
	protected Promise<D_OUT, F_OUT, P_OUT> pipe(Promise<D_OUT, F_OUT, P_OUT> promise) {
		promise.done(new DoneCallback<D_OUT>() {
			public void onDone(D_OUT result) {
				PipedPromise.this.resolve(result);
			}
		}).fail(new FailCallback<F_OUT>() {
			public void onFail(F_OUT result) {
				PipedPromise.this.reject(result);
			}
		}).progress(new ProgressCallback<P_OUT>() {
			public void onProgress(P_OUT progress) {
				PipedPromise.this.notify(progress);
			}
		});
		
		return promise;
	}
}