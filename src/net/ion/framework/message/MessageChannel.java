package net.ion.framework.message;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MessageChannel {
	private final ExecutorService eservice;
	private ScheduledExecutorService ses ;
	
	public MessageChannel() {
		this.eservice = Executors.newCachedThreadPool();
	}
	public MessageChannel(int threads) {
		this.eservice = Executors.newCachedThreadPool() ;
	}

	public synchronized void putMessage(Runnable message) {
		eservice.submit(message);
	}

	public synchronized <T> Future<T> putMessage(Callable<T> message) {
		return eservice.submit(message) ;
	}

	public synchronized void destroy() {
		try {
			eservice.shutdown();
			eservice.awaitTermination(2, TimeUnit.SECONDS);
		} catch (InterruptedException ignore) {
			ignore.printStackTrace();
		}
	}

	public ExecutorService getExecutorService(){
		return eservice ;
	}
	
	public synchronized ScheduledExecutorService loadScheduleService(){
		if (ses != null){
			return ses ;
		}
		this.ses = Executors.newScheduledThreadPool(5) ;
		return loadScheduleService() ;
	}
}
