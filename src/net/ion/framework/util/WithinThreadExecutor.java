package net.ion.framework.util;

import java.util.List;
import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.TimeUnit;

public class WithinThreadExecutor  extends AbstractExecutorService {
	private volatile boolean shutDown;

	public WithinThreadExecutor() {
		shutDown = false;
	}

	public void execute(Runnable command) {
		command.run();
	}

	public void shutdown() {
		shutDown = true;
	}

	public List shutdownNow() {
		shutDown = true;
		return ListUtil.EMPTY;
	}

	public boolean isShutdown() {
		return shutDown;
	}

	public boolean isTerminated() {
		return shutDown;
	}

	public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
		return shutDown;
	}

}