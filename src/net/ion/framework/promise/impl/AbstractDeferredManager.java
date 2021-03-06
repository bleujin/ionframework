package net.ion.framework.promise.impl;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.logging.Logger;

import net.ion.framework.logging.LogBroker;
import net.ion.framework.promise.DeferredCallable;
import net.ion.framework.promise.DeferredFutureTask;
import net.ion.framework.promise.DeferredManager;
import net.ion.framework.promise.DeferredRunnable;
import net.ion.framework.promise.Promise;
import net.ion.framework.promise.multiple.MasterDeferredObject;
import net.ion.framework.promise.multiple.MasterProgress;
import net.ion.framework.promise.multiple.MultipleResults;
import net.ion.framework.promise.multiple.OneReject;

@SuppressWarnings({ "rawtypes", "unchecked" })
public abstract class AbstractDeferredManager implements DeferredManager {
	final protected Logger log = LogBroker.getLogger(AbstractDeferredManager.class);

	protected abstract void submit(Runnable runnable);

	protected abstract void submit(Callable callable);

	/**
	 * Should {@link Runnable} or {@link Callable} be submitted for execution automatically when any of the following are called
	 * <ul>
	 * <li>{@link #when(Runnable...)}</li>
	 * <li>{@link #when(Callable...)}</li>
	 * <li>{@link #when(DeferredFutureTask...))}</li>
	 * <li>{@link #when(DeferredCallable)}</li>
	 * <li>{@link #when(DeferredRunnable)}</li>
	 * <li>{@link #when(DeferredFutureTask))}</li>
	 * </ul>
	 * 
	 * @return
	 */
	public abstract boolean isAutoSubmit();

	public Promise<MultipleResults, OneReject, MasterProgress> when(Runnable... runnables) {
		assertNotEmpty(runnables);

		Promise[] promises = new Promise[runnables.length];

		for (int i = 0; i < runnables.length; i++) {
			if (runnables[i] instanceof DeferredRunnable)
				promises[i] = when((DeferredRunnable) runnables[i]);
			else
				promises[i] = when(runnables[i]);
		}

		return when(promises);
	}

	public Promise<MultipleResults, OneReject, MasterProgress> when(Callable<?>... callables) {
		assertNotEmpty(callables);

		Promise[] promises = new Promise[callables.length];

		for (int i = 0; i < callables.length; i++) {
			if (callables[i] instanceof DeferredCallable)
				promises[i] = when((DeferredCallable) callables[i]);
			else
				promises[i] = when(callables[i]);
		}

		return when(promises);
	}

	public Promise<MultipleResults, OneReject, MasterProgress> when(DeferredRunnable<?>... runnables) {
		assertNotEmpty(runnables);

		Promise[] promises = new Promise[runnables.length];

		for (int i = 0; i < runnables.length; i++) {
			promises[i] = when(runnables[i]);
		}

		return when(promises);
	}

	public Promise<MultipleResults, OneReject, MasterProgress> when(DeferredCallable<?, ?>... callables) {
		assertNotEmpty(callables);

		Promise[] promises = new Promise[callables.length];

		for (int i = 0; i < callables.length; i++) {
			promises[i] = when(callables[i]);
		}

		return when(promises);
	}

	public Promise<MultipleResults, OneReject, MasterProgress> when(DeferredFutureTask<?, ?>... tasks) {
		assertNotEmpty(tasks);

		Promise[] promises = new Promise[tasks.length];

		for (int i = 0; i < tasks.length; i++) {
			promises[i] = when(tasks[i]);
		}
		return when(promises);
	}

	public Promise<MultipleResults, OneReject, MasterProgress> when(Future<?>... futures) {
		assertNotEmpty(futures);

		Promise[] promises = new Promise[futures.length];

		for (int i = 0; i < futures.length; i++) {
			promises[i] = when(futures[i]);
		}
		return when(promises);
	}

	public Promise<MultipleResults, OneReject, MasterProgress> when(Promise... promises) {
		assertNotEmpty(promises);
		return new MasterDeferredObject(promises).promise();
	}

	public <D, F, P> Promise<D, F, P> when(Promise<D, F, P> promise) {
		return promise;
	}

	public <P> Promise<Void, Throwable, P> when(DeferredRunnable<P> runnable) {
		return when(new DeferredFutureTask<Void, P>(runnable));
	}

	public <D, P> Promise<D, Throwable, P> when(DeferredCallable<D, P> runnable) {
		return when(new DeferredFutureTask<D, P>(runnable));
	}

	public Promise<Void, Throwable, Void> when(Runnable runnable) {
		return when(new DeferredFutureTask<Void, Void>(runnable));
	}

	public <D> Promise<D, Throwable, Void> when(Callable<D> callable) {
		return when(new DeferredFutureTask<D, Void>(callable));
	}

	/**
	 * This method is delegated by at least the following methods
	 * <ul>
	 * <li>{@link #when(Callable)}</li>
	 * <li>{@link #when(Callable...)}</li>
	 * <li>{@link #when(Runnable)}</li>
	 * <li>{@link #when(Runnable..)}</li>
	 * <li>{@link #when(java.util.concurrent.Future)}</li>
	 * <li>{@link #when(java.util.concurrent.Future...)}</li>
	 * <li>{@link #when(net.ion.framework.promise.DeferredRunnable...)}</li>
	 * <li>{@link #when(net.ion.framework.promise.DeferredRunnable)}</li>
	 * <li>{@link #when(net.ion.framework.promise.DeferredCallable...)}</li>
	 * <li>{@link #when(net.ion.framework.promise.DeferredCallable)}</li>
	 * <li>{@link #when(DeferredFutureTask...)}</li>
	 * </ul>
	 */
	public <D, P> Promise<D, Throwable, P> when(DeferredFutureTask<D, P> task) {
		if (task.getStartPolicy() == StartPolicy.AUTO || (task.getStartPolicy() == StartPolicy.DEFAULT && isAutoSubmit()))
			submit(task);

		return task.promise();
	}

	public <D> Promise<D, Throwable, Void> when(final Future<D> future) {
		// make sure the task is automatically started

		return when(new DeferredCallable<D, Void>(StartPolicy.AUTO) {
			public D call() throws Exception {
				try {
					return future.get();
				} catch (InterruptedException e) {
					throw e;
				} catch (ExecutionException e) {
					if (e.getCause() instanceof Exception)
						throw (Exception) e.getCause();
					else
						throw e;
				}
			}
		});
	}

	protected void assertNotEmpty(Object[] objects) {
		if (objects == null || objects.length == 0)
			throw new IllegalArgumentException("Arguments is null or its length is empty");
	}
}
