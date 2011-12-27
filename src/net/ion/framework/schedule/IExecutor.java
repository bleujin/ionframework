package net.ion.framework.schedule;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.ion.framework.logging.LogBroker;

public class IExecutor {

	private ExecutorService eservice;
	private ScheduledExecutorService sservice;

	public IExecutor(int ecount, int scount) {
		this.eservice = (ecount <= 2) ? Executors.newCachedThreadPool() : Executors.newFixedThreadPool(ecount);
		this.sservice = Executors.newScheduledThreadPool(Math.max(5, scount));
	}

	public static IExecutor test() {
		return new IExecutor(0, 3);
	}

	public <T> Future<T> submitTask(Callable<T> task) {
		return eservice.submit(task);
	}

	public void runTask(Runnable task) {
		Logger logger = LogBroker.getLogger(task);
		try {
			logger.log(Level.INFO, "Thread started: " + task);
			Future<?> future = eservice.submit(task);
			logger.log(Level.INFO, "Thread finished: " + task);
		} catch (Throwable t) {
			logger.log(Level.SEVERE, "unexpected error", t);
		}
	}

	public <T> ScheduledFuture<T> schedule(Callable<T> callable, long delay, TimeUnit unit) {
		return sservice.schedule(callable, delay, unit);
	}

	public ScheduledFuture<?> scheduleWithFixedDelay(Runnable callable, long initialDelay, long delay, TimeUnit unit) {
		return sservice.scheduleWithFixedDelay(callable, initialDelay, delay, unit);
	}

	public void doRelease() {
		awaitUnInterupt(100, TimeUnit.MILLISECONDS);
	}

	public void awaitUnInterupt(long timeout, TimeUnit unit) {
		try {
			eservice.awaitTermination(timeout, unit);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		try {
			sservice.awaitTermination(timeout, unit);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void shutdown() {
		eservice.shutdown();
		sservice.shutdown();
	}

	public ScheduledExecutorService getScheduleService() {
		return sservice;
	}

	public ExecutorService getService() {
		return eservice;
	}

}
