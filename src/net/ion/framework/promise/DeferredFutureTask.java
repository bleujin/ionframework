package net.ion.framework.promise;

import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import net.ion.framework.promise.DeferredManager.StartPolicy;
import net.ion.framework.promise.impl.DeferredObject;

/**
 * FutureTask can wrap around {@link Callable} and {@link Runnable}. In these two cases, a new {@link Deferred} object will be created. This class will override {@link FutureTask#done} to trigger the appropriate {@link Deferred} actions.
 * 
 * Note, type used for {@link Deferred#reject(Object)} is always {@link Throwable}.
 * 
 * When the task is completed successfully, {@link Deferred#resolve(Object)} will be called. When a task is canceled, {@link Deferred#reject(Object)} will be called with an instance of {@link CancellationException} If any Exception occured, {@link Deferred#reject(Object)} will be called with the Exception instance.
 * 
 * @param <D>
 *            Type used for {@link Deferred#resolve(Object)}
 * @param <P>
 *            Type used for {@link Deferred#notify(Object)}
 */
public class DeferredFutureTask<D, P> extends FutureTask<D> {
	protected final Deferred<D, Throwable, P> deferred;
	protected final StartPolicy startPolicy;

	public DeferredFutureTask(Callable<D> callable) {
		super(callable);
		this.deferred = new DeferredObject<D, Throwable, P>();
		this.startPolicy = StartPolicy.DEFAULT;
	}

	public DeferredFutureTask(Runnable runnable) {
		super(runnable, null);
		this.deferred = new DeferredObject<D, Throwable, P>();
		this.startPolicy = StartPolicy.DEFAULT;
	}

	public DeferredFutureTask(DeferredCallable<D, P> callable) {
		super(callable);
		this.deferred = callable.getDeferred();
		this.startPolicy = callable.getStartPolicy();
	}

	@SuppressWarnings("unchecked")
	public DeferredFutureTask(DeferredRunnable<P> runnable) {
		super(runnable, null);
		this.deferred = (Deferred<D, Throwable, P>) runnable.getDeferred();
		this.startPolicy = runnable.getStartPolicy();
	}

	public Promise<D, Throwable, P> promise() {
		return deferred.promise();
	}

	@Override
	protected void done() {
		try {
			if (isCancelled()) {
				deferred.reject(new CancellationException());
			}
			D result = get();
			deferred.resolve(result);
		} catch (InterruptedException e) {
		} catch (ExecutionException e) {
			deferred.reject(e.getCause());
		}
	}

	public StartPolicy getStartPolicy() {
		return startPolicy;
	}
}
