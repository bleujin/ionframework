package net.ion.framework.promise;

import java.util.concurrent.Callable;

import net.ion.framework.promise.DeferredManager.StartPolicy;
import net.ion.framework.promise.impl.DeferredObject;

/**
 * Use this as superclass in case you need to be able to return a result and notify progress. If you don't need to notify progress, you can simply use {@link Callable}
 * 
 * @see #notify(Object)
 * @author Ray Tsang
 * 
 * @param <D>
 *            Type used as return type of {@link Callable#call()}, and {@link Deferred#resolve(Object)}
 * @param <P>
 *            Type used for {@link Deferred#notify(Object)}
 */
public abstract class DeferredCallable<D, P> implements Callable<D> {
	private final Deferred<D, Throwable, P> deferred = new DeferredObject<D, Throwable, P>();
	private final StartPolicy startPolicy;

	public DeferredCallable() {
		this.startPolicy = StartPolicy.DEFAULT;
	}

	public DeferredCallable(StartPolicy startPolicy) {
		this.startPolicy = startPolicy;
	}

	/**
	 * @see Deferred#notify(Object)
	 * @param progress
	 */
	protected void notify(P progress) {
		deferred.notify(progress);
	}

	protected Deferred<D, Throwable, P> getDeferred() {
		return deferred;
	}

	public StartPolicy getStartPolicy() {
		return startPolicy;
	}
}
