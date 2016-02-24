package net.ion.framework.promise;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import junit.framework.TestCase;
import net.ion.framework.promise.impl.DefaultDeferredManager;

public class TestBasePromise extends TestCase {

	protected DefaultDeferredManager deferredManager;

	protected void createDeferredManager() {
		this.deferredManager = new DefaultDeferredManager();
	}

	public void setUp() throws Exception {
		this.createDeferredManager();
	}

	public void tearDown() throws Exception {
		waitForCompletion();
	}

	protected <R> Callable<R> successCallable(final R result, final int waitMs) {
		return new Callable<R>() {
			public R call() throws Exception {
				if (waitMs > 0) {
					Thread.sleep(waitMs);
				}

				return result;
			}
		};
	}

	protected Callable<Void> failedCallable(final Exception exception, final int waitMs) {
		return new Callable<Void>() {
			public Void call() throws Exception {
				if (waitMs > 0) {
					Thread.sleep(waitMs);
				}

				throw exception;
			}
		};
	}

	protected void waitForCompletion() {
		deferredManager.shutdown();
		while (!deferredManager.isTerminated()) {
			try {
				deferredManager.awaitTermination(1, TimeUnit.SECONDS);
			} catch (InterruptedException e) {
			}
		}
	}
}
