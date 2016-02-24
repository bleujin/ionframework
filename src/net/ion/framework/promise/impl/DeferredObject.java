package net.ion.framework.promise.impl;

import net.ion.framework.promise.Deferred;
import net.ion.framework.promise.DoneCallback;
import net.ion.framework.promise.FailCallback;
import net.ion.framework.promise.ProgressCallback;
import net.ion.framework.promise.Promise;

/**
 * An implementation of {@link Deferred} interface.
 * 
 * <pre>
 * <code>
 * final {@link Deferred} deferredObject = new {@link DeferredObject}
 * 
 * {@link Promise} promise = deferredObject.promise();
 * promise
 *   .done(new DoneCallback() { ... })
 *   .fail(new FailCallback() { ... })
 *   .progress(new ProgressCallback() { ... });
 *   
 * {@link Runnable} runnable = new {@link Runnable}() {
 *   public void run() {
 *     int sum = 0;
 *     for (int i = 0; i < 100; i++) {
 *       // something that takes time
 *       sum += i;
 *       deferredObject.notify(i);
 *     }
 *     deferredObject.resolve(sum);
 *   }
 * }
 * // submit the task to run
 * 
 * </code>
 * </pre>
 * 
 * @see DoneCallback
 * @see FailCallback
 * @see ProgressCallback
 */
public class DeferredObject<D, F, P> extends AbstractPromise<D, F, P> implements Deferred<D, F, P> {
	
	public Deferred<D, F, P> resolve(final D resolve) {
		synchronized (this) {
			if (!isPending())
				throw new IllegalStateException("Deferred object already finished, cannot resolve again");
			
			this.state = State.RESOLVED;
			this.resolveResult = resolve;
			
			try {
				triggerDone(resolve);
			} finally {
				triggerAlways(state, resolve, null);
			}
		}
		return this;
	}

	public Deferred<D, F, P> notify(final P progress) {
		synchronized (this) {
			if (!isPending())
				throw new IllegalStateException("Deferred object already finished, cannot notify progress");
			
			triggerProgress(progress);
		}
		return this;
	}

	public Deferred<D, F, P> reject(final F reject) {
		synchronized (this) {
			if (!isPending())
				throw new IllegalStateException("Deferred object already finished, cannot reject again");
			this.state = State.REJECTED;
			this.rejectResult = reject;
			
			try {
				triggerFail(reject);
			} finally {
				triggerAlways(state, null, reject);
			}
		}
		return this;
	}

	public Promise<D, F, P> promise() {
		return this;
	}
}
