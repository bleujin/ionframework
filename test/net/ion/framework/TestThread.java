package net.ion.framework;

import junit.framework.TestCase;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: I-ON Communications
 * </p>
 * 
 * @author bleujin
 * @version 1.0
 */

public class TestThread extends TestCase {
	public void testWait() throws Exception {
		Thread t = new InnerThread();
		t.start();

		try {
			t.join();
		} catch (InterruptedException ex) {
		}
	}
}

class InnerThread extends Thread {

	InnerThread() {
	}

	public void run() {
		int i = 0;
		while (true) {
			try {
				outPrint(i++);
			} catch (InterruptedException ex) {
			}
		}
	}

	synchronized void outPrint(int i) throws InterruptedException {
		System.out.println("i=" + i);
		wait(1000);
	}

}
