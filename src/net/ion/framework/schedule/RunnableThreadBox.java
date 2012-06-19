package net.ion.framework.schedule;

import java.util.logging.Level;
import java.util.logging.Logger;

import net.ion.framework.logging.LogBroker;

/**
 * scheduler �� runnable�� ������ �� log�� ��������� ������ Thread�� ����Ѵ�.
 * 
 * @author Kim Sanghoon wizest@i-on.net
 * @version 1.0
 */

@Deprecated
class RunnableThreadBox extends Thread {
	Logger logger = null;
	Runnable runner = null;

	public RunnableThreadBox(Runnable r) {
		super(r);
		this.logger = LogBroker.getLogger(r);
		this.runner = r;
	}

	public void run() {
		logger.log(Level.INFO, "Thread started: " + runner);
		try {
			super.run();
		} catch (Throwable t) {
			logger.log(Level.SEVERE, "unexpected error", t);
		}
		logger.log(Level.INFO, "Thread finished: " + runner);
	}
}
