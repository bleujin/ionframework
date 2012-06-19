package net.ion.framework.util;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import junit.framework.TestCase;

public class DateFormatUtilTest extends TestCase{

	public void testFormat() throws Exception {
		//Debug.debug(DateFormatUtil.getDateIfMatchedType("20000101"), DateFormatUtil.getDateIfMatchedType("2000.a3.35")) ;
		//Debug.debug(DateFormatUtil.currentDate2String("F"));
		
		//Debug.debug(DateFormatUtil.date2String(DateFormatUtil.string2Date("20111124-000001"), "F"));
		Calendar cal = Calendar.getInstance();
		String[] week = {"Mon","Tue","Wed","Thu","Fri","Sat","Sun"};
		for (int i = 20; i < 30; i++) {
			cal.set(2011, 10, i);
			Debug.line(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
			Debug.debug(week[cal.get(Calendar.DAY_OF_WEEK)-1]);
			//  7,2
			Debug.debug(cal.get(Calendar.DAY_OF_WEEK));
			Debug.debug( 67 & (int)Math.pow(2, cal.get(Calendar.DAY_OF_WEEK)-1));
		}
		
	}
	
	public void testPP() throws Exception {
		Debug.debug(66 & (int)Math.pow(2, 2));
		Calendar cal = Calendar.getInstance();
		cal.set(2011, 10, 22, 4, 6);
		
		//Debug.debug(cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE));
		Debug.debug(DateFormatUtil.date2String(cal.getTime(), "HHmm"));
		
		
		
		
	}
}
