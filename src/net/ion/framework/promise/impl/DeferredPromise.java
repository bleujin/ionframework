package net.ion.framework.promise.impl;

import net.ion.framework.promise.AlwaysCallback;
import net.ion.framework.promise.Deferred;
import net.ion.framework.promise.DoneCallback;
import net.ion.framework.promise.DoneFilter;
import net.ion.framework.promise.DonePipe;
import net.ion.framework.promise.FailCallback;
import net.ion.framework.promise.FailFilter;
import net.ion.framework.promise.FailPipe;
import net.ion.framework.promise.ProgressCallback;
import net.ion.framework.promise.ProgressFilter;
import net.ion.framework.promise.ProgressPipe;
import net.ion.framework.promise.Promise;

public class DeferredPromise<D, F, P> implements Promise<D, F, P> {
	private final Promise<D, F, P> promise;
	protected final Deferred<D, F, P> deferred;

	public DeferredPromise(Deferred<D, F, P> deferred) {
		this.deferred = deferred;
		this.promise = deferred.promise();
	}

	public net.ion.framework.promise.Promise.State state() {
		return promise.state();
	}

	public boolean isPending() {
		return promise.isPending();
	}

	public boolean isResolved() {
		return promise.isResolved();
	}

	public boolean isRejected() {
		return promise.isRejected();
	}

	public Promise<D, F, P> then(DoneCallback<D> doneCallback) {
		return promise.then(doneCallback);
	}

	public Promise<D, F, P> then(DoneCallback<D> doneCallback, FailCallback<F> failCallback) {
		return promise.then(doneCallback, failCallback);
	}

	public Promise<D, F, P> then(DoneCallback<D> doneCallback, FailCallback<F> failCallback, ProgressCallback<P> progressCallback) {
		return promise.then(doneCallback, failCallback, progressCallback);
	}

	public <D_OUT, F_OUT, P_OUT> Promise<D_OUT, F_OUT, P_OUT> then(DoneFilter<D, D_OUT> doneFilter) {
		return promise.then(doneFilter);
	}

	public <D_OUT, F_OUT, P_OUT> Promise<D_OUT, F_OUT, P_OUT> then(DoneFilter<D, D_OUT> doneFilter, FailFilter<F, F_OUT> failFilter) {
		return promise.then(doneFilter, failFilter);
	}

	public <D_OUT, F_OUT, P_OUT> Promise<D_OUT, F_OUT, P_OUT> then(DoneFilter<D, D_OUT> doneFilter, FailFilter<F, F_OUT> failFilter, ProgressFilter<P, P_OUT> progressFilter) {
		return promise.then(doneFilter, failFilter, progressFilter);
	}

	public Promise<D, F, P> done(DoneCallback<D> callback) {
		return promise.done(callback);
	}

	public Promise<D, F, P> fail(FailCallback<F> callback) {
		return promise.fail(callback);
	}

	public Promise<D, F, P> always(AlwaysCallback<D, F> callback) {
		return promise.always(callback);
	}

	public Promise<D, F, P> progress(ProgressCallback<P> callback) {
		return promise.progress(callback);
	}

	public void waitSafely() throws InterruptedException {
		promise.waitSafely();

	}

	public void waitSafely(long timeout) throws InterruptedException {
		promise.waitSafely(timeout);
	}

	public <D_OUT, F_OUT, P_OUT> Promise<D_OUT, F_OUT, P_OUT> then(DonePipe<D, D_OUT, F_OUT, P_OUT> doneFilter) {
		return promise.then(doneFilter);
	}

	public <D_OUT, F_OUT, P_OUT> Promise<D_OUT, F_OUT, P_OUT> then(DonePipe<D, D_OUT, F_OUT, P_OUT> doneFilter, FailPipe<F, D_OUT, F_OUT, P_OUT> failFilter) {
		return promise.then(doneFilter, failFilter);
	}

	public <D_OUT, F_OUT, P_OUT> Promise<D_OUT, F_OUT, P_OUT> then(DonePipe<D, D_OUT, F_OUT, P_OUT> doneFilter, FailPipe<F, D_OUT, F_OUT, P_OUT> failFilter, ProgressPipe<P, D_OUT, F_OUT, P_OUT> progressFilter) {
		return promise.then(doneFilter, failFilter, progressFilter);
	}
}
