package net.ion.framework.util;

public class InfinityThread extends Thread {

	public void run() {
		try {
			while (true) {
				Thread.sleep(1000);
			}
		} catch (InterruptedException ignore) {
			
		}
	}

	public void startNJoin() {
		this.start();
		try {
			this.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
