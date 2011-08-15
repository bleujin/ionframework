package net.ion.framework.util;

import java.util.Date;

import junit.framework.TestCase;

public class DateFormatUtilTest extends TestCase{

	public void testFormat() throws Exception {
		Debug.debug(DateFormatUtil.getDateIfMatchedType("20000101"), DateFormatUtil.getDateIfMatchedType("2000.a3.35")) ;
		
		
	}
}
