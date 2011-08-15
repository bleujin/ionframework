package net.ion.framework.db.servant;

import java.util.logging.Level;
import java.util.logging.Logger;

import net.ion.framework.logging.LogBroker;

public abstract class ExtraServant extends Thread {
	private ExtraServant next = null;
	private ServantChannel channel = null;
	private Logger log = LogBroker.getLogger("Servant");

	// public final static ExtraServant NONE = new NoneServant() ;

	public void setChannel(ServantChannel channel) {
		this.channel = channel;
	}

	public ExtraServant setNext(ExtraServant next) {
		this.next = next;
		this.next.setChannel(this.channel);
		return next;
	}

	protected Logger getLogger() {
		return log;
	}

	public final void run() {
		while (!terminated) {
			try {
				AfterTask atask = channel.takeTask();
				support(atask);
			} catch (InterruptedException ex) {
				terminated = true;
			} catch (Exception ex) {
				log.log(Level.WARNING, "Task Not handled : " + ex.getMessage(), ex);
			}
		}
	}

	public final void support(AfterTask atask) {

		if (isDealWith(atask)) {
			handle(atask);
		}

		if (next != null) {
			next.support(atask);
		}
	}

	public final ExtraServant getNextServant() {
		return next;
	}

	protected abstract boolean isDealWith(AfterTask atask);

	protected abstract void handle(AfterTask atask);

	private volatile boolean terminated = false;

	void stopServant() {
		// if (this.next != null) {
		// this.next.stopServant();
		// }

		terminated = true;
		interrupt();
	}

	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append(this.getName() + "(" + this.getClass() + ")");
		if (this.next != null) {
			str.append("[" + this.next.toString() + "]");
		}
		return str.toString();
	}

	public abstract ExtraServant newCloneInstance();

	public void restart() {
		this.terminated = false;
		this.start();

	}
}
