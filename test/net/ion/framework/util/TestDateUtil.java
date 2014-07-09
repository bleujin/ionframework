package net.ion.framework.util;

import java.util.Date;

import junit.framework.TestCase;

public class TestDateUtil extends TestCase {

	public void testToDate() throws Exception {
		Debug.line(DateUtil.dateToString(new Date(), "HH:mm:ss")) ;
	}
}
