package net.ion.framework.promise.impl;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * A default implementation that runs deferred tasks using an {@link ExecutorService}.
 * Also, by default, deferred tasks are executed (submitted to the ExecutorService) automatically
 * when it's passed into {@link DeferredManager}'s when(...) methods.  This behavior can be changed
 * by setting {@link #setAutoSubmit(boolean)}.
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public class DefaultDeferredManager extends AbstractDeferredManager {
	/**
	 * By default, {@link #autoSubmit} will be set to true
	 * You can set it to false by using {@link #setAutoSubmit(boolean)}
	 * If you set it to false, that means you'll be responsible to make sure any
	 * {@link Runnable} or {@link Callable} are executed.
	 */
	public static final boolean DEFAULT_AUTO_SUBMIT = true;
	
	private final ExecutorService executorService;
	private boolean autoSubmit = DEFAULT_AUTO_SUBMIT;

	/**
	 * Equivalent to {@link #DefaultDeferredManager(ExecutorService)} using
	 * {@link Executors#newCachedThreadPool()}
	 */
	public DefaultDeferredManager() {
		this.executorService = Executors.newCachedThreadPool();
	}

	/**
	 * 
	 * @param executorService
	 */
	public DefaultDeferredManager(ExecutorService executorService) {
		this.executorService = executorService;
	}

	public ExecutorService getExecutorService() {
		return executorService;
	}

	public boolean awaitTermination(long timeout, TimeUnit unit)
			throws InterruptedException {
		return executorService.awaitTermination(timeout, unit);
	}

	public boolean isShutdown() {
		return executorService.isShutdown();
	}

	public boolean isTerminated() {
		return executorService.isTerminated();
	}

	public void shutdown() {
		executorService.shutdown();
	}

	public List<Runnable> shutdownNow() {
		return executorService.shutdownNow();
	}

	@Override
	protected void submit(Runnable runnable) {
		executorService.submit(runnable);
	}
	
	@Override
	protected void submit(Callable callable) {
		executorService.submit(callable);
	}

	@Override
	public boolean isAutoSubmit() {
		return autoSubmit;
	}

	public void setAutoSubmit(boolean autoSubmit) {
		this.autoSubmit = autoSubmit;
	}
	
}
