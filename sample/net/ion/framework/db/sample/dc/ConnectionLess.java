package net.ion.framework.db.sample.dc;

import net.ion.framework.db.DBController;
import net.ion.framework.db.sample.TestBaseDB;
import net.ion.framework.util.Debug;

public class ConnectionLess extends TestBaseDB {

	public void testThread() throws Exception {

		Debug.debug(dc.getDBManager());

		Thread[] ts = new Thread[5];
		for (int i = 0; i < ts.length; i++) {
			ts[i] = new Thread(new ThreadRunner(dc));
		}

		for (int i = 0; i < ts.length; i++) {
			ts[i].start();
		}

		for (int i = 0; i < ts.length; i++) {
			ts[i].join();
		}

	}
}

class ThreadRunner implements Runnable {

	private DBController dc;
	private int splitMs = 5;

	ThreadRunner(DBController dc) {
		this.dc = dc;

	}

	public void run() {
		try {
			int i = 0;
			while (i++ < 10) {
				dc.getRows("select * from update_sample");
				Thread.sleep(splitMs);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

}