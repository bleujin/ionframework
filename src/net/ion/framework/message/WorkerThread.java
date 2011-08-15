package net.ion.framework.message;

import java.util.logging.Level;
import java.util.logging.Logger;

import net.ion.framework.logging.LogBroker;

/**
 * MessageChannel 에 속한 모든 메세지를 처리하는 Thread
 * 
 * @author bleujin
 * @version 1.0
 */

// Worker Thread Pattern
public class WorkerThread extends Thread {
	private final MessageChannel channel;
	private final Logger log;

	public WorkerThread(String name, MessageChannel channel) {
		super(name);
		this.channel = channel;
		this.log = LogBroker.getLogger(this);
		this.setDaemon(true);
	}

	public void run() {
		while (!channel.isCompleted()) {
			try {
				Message message = channel.takeMessage();
				message.handle();
			} catch (Exception ex) {
				log.log(Level.WARNING, "Message Not handled..", ex);
			}
		}
	}
}
