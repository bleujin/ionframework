package net.ion.framework.util;

import java.io.File;

import junit.framework.TestCase;

public class TestShell extends TestCase {

	public void testForceMkdir() throws Exception {
		int maxCount = 100;
		Thread[] ts = new Thread[maxCount];
		for (int i = 0, last = ts.length; i < last; i++) {
			ts[i] = new UserThread(i + " thread", "F:/a/b/c/d/e/f/g/h/i/j/k/l/m/n/o/p/q/r/s/t/u/v/w/x/y/z");
		}

		for (Thread thread : ts) {
			thread.start();
		}

		for (Thread thread : ts) {
			thread.join();
		}

	}
}

class UserThread extends Thread {
	private String dirPath;

	public UserThread(String name, String dirPath) {
		super(name);
		this.dirPath = dirPath;
	}

	public void run() {
		File directory = new File(dirPath);
		if (!Shell.forceMkdir(directory)) {
			Debug.debug("### mkdirs false...");
		}
	}
}