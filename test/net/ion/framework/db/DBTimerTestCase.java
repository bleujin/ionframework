package net.ion.framework.db;

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

public class DBTimerTestCase extends DBTestCase {
	private StopWatch watch = new StopWatch();

	public void start() {
		watch.reset();
		watch.start();
	}

	public void end(String name) {
		watch.stop();
		System.out.println(name + " Test : " + watch.getTime());
	}

}
