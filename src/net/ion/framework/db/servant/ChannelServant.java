package net.ion.framework.db.servant;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;


import net.ion.framework.util.ListUtil;

public class ChannelServant implements IExtraServant {

	private final int capacity ;
	private final BlockingQueue<AfterTask> queue;

	private ServantThread sthread;

	public ChannelServant(IExtraServant... handler) {
		this(1000, handler) ;
	}

	public ChannelServant(int capacity, IExtraServant... handler) {
		this.capacity = Math.max(capacity, 10) ;
		this.queue = new ArrayBlockingQueue<AfterTask>(this.capacity);
		this.sthread = new ServantThread(handler, this);
		this.sthread.start() ;
	}

	private void putEvent(AfterTask event) {
		try {
			queue.put(event);
		} catch (InterruptedException ignore) {
			// ignore.printStackTrace();
		}
	}

	public ChannelServant add(IExtraServant newServant){
		this.sthread.add(newServant) ;
		return this ;
	}

	public List<IExtraServant> getServantList() {
		List<IExtraServant> result = ListUtil.newList() ;
		for (IExtraServant servant : sthread.getServantList()) {
			if (servant instanceof ChannelServant) {
				result.addAll(((ChannelServant)servant).getServantList()) ;
			} else if (servant instanceof ServantChain) {
				result.addAll(((ServantChain)servant).getServants()) ;
			} else {
				result.add(servant) ;
			}
		}
		
		return Collections.unmodifiableList(result) ;
	}

	
	public void support(AfterTask event) {
		putEvent(event);
	}


	public AfterTask takeEvent() throws InterruptedException {
		return queue.take();
	}

	public void stopServant() {
		sthread.interrupt() ;
	}
	
	public ChannelServant(IExtraServant l1, int capacity) {
		this(capacity, l1) ;
	}
	public ChannelServant(IExtraServant l1, IExtraServant l2, int capacity) {
		this(capacity, l1, l2) ;
	}
	public ChannelServant(IExtraServant l1, IExtraServant l2, IExtraServant l3, int capacity) {
		this(capacity, l1, l2, l3) ;
	}
	public ChannelServant(IExtraServant l1, IExtraServant l2, IExtraServant l3, IExtraServant l4, int capacity) {
		this(capacity, l1, l2, l3, l4) ;
	}
	public ChannelServant(IExtraServant l1, IExtraServant l2, IExtraServant l3, IExtraServant l4, IExtraServant l5, int capacity) {
		this(capacity, l1, l2, l3, l4, l5) ;
	}


}
