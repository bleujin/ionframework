package net.ion.framework.util.nio;

import junit.framework.TestCase;

import org.apache.commons.lang.time.StopWatch;

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

public class TimerTestCase extends TestCase {
	private StopWatch watch = new StopWatch();

	public TimerTestCase() {
	}

	public TimerTestCase(String name) {
		super(name);
	}

	protected void start() {
		watch.reset();
		watch.start();
	}

	protected void end(String name) {
		watch.stop();
		System.out.println(name + " Test : " + watch.getTime());
		watch.reset();
	}

}
