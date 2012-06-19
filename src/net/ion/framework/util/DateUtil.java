package net.ion.framework.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.Locale;
import java.util.TimeZone;

public class DateUtil {

	public static final String DEFAULT_FORMAT = "yyyyMMdd-HHmmss";
	  /** Formatter for Date based on OS Locale */
	  private static final DateFormat DATE_FORMAT = DateFormat.getDateInstance(DateFormat.SHORT);

	  /** Formatter for Date (long) based on OS Locale */
	  private static final DateFormat DATE_LONG_FORMAT = DateFormat.getDateInstance(DateFormat.FULL);

	  /** Formatter for Date and Time based on OS Locale */
	  private static final DateFormat DATE_TIME_FORMAT = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);

	  /** Formatter for Date and Time (long) based on OS Locale */
	  private static final DateFormat DATE_TIME_LONG_FORMAT = DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.SHORT);
	
	
	  private static SimpleDateFormat[] dateFormats = null;
	  static {
		    final String[] possibleDateFormats = {

		    "EEE, dd MMM yy HH:mm:ss z", /** RFC 1123 with 2-digit Year */
		    "EEE, dd MMM yyyy HH:mm:ss z", /** RFC 1123 with 4-digit Year */
		    "EEE, dd MMM yy HH:mm:ss", /** RFC 1123 with no Timezone */
		    "EEE, MMM dd yy HH:mm:ss", /** Variant of RFC 1123 */
		    "yyyy-MM-dd'T'HH:mm:ssZ", /** ISO 8601 slightly modified */
		    "yyyy-MM-dd'T'HH:mm:ss'Z'", /** ISO 8601 slightly modified */
		    "yyyy-MM-dd'T'HH:mm:sszzzz", /** ISO 8601 slightly modified */

		    "yyyy-MM-dd'T'HH:mm:ss z", /** ISO 8601 slightly modified */
		    "yyyy-MM-dd'T'HH:mm:ssz", /** ISO 8601 */
		    "yyyy-MM-dd'T'HH:mm:ss", /** ISO 8601 slightly modified */
		    "yyyy-MM-dd'T'HHmmss.SSSz", /** ISO 8601 slightly modified */
		    "yyyy-MM-dd'T'HH:mm:ss", /** ISO 8601 slightly modified */
		    "yyyy-MM-dd HH:mm:ss",

		    "yyyy-MM-dd'T'HH:mmZ", /** ISO 8601 w/o seconds */
		    "yyyy-MM-dd'T'HH:mm'Z'", /** ISO 8601 w/o seconds */
		    "dd-mm-yyyy HH:mm:ss", /** European Date Format */
		    "dd MMM yyyy HH:mm:ss z", /** RFC 1123 without Day Name */
		    "dd MMM yyyy HH:mm:ss", /** RFC 1123 without Day Name and Timezone */
		    "dd MMM yyyy HH:mm z", /** RFC 1123 without Day Name and Seconds */
		    
		    "yyyy-MM-dd", /** Simple Date Format */
		    "yyyy-MM-dd'T'HH:mm:ssz",
		    "yyyy-MM-dd'T'HH:mmz",
		    "yyyy-MM",
		    "yyyy",
		    "EEE, dd MMM yy HH:mm:ss z",
		    "EEE, dd MMM yy HH:mm z",
		    "dd MMM yy HH:mm:ss z",
		    "dd MMM yy HH:mm z"

		    };

		    /** Create the dateformats */
		    dateFormats = new SimpleDateFormat[possibleDateFormats.length];
		    TimeZone gmtTZ = TimeZone.getTimeZone("GMT");
		    for (int i = 0; i < possibleDateFormats.length; i++) {
		      dateFormats[i] = new SimpleDateFormat(possibleDateFormats[i], Locale.ENGLISH);
		      dateFormats[i].setTimeZone(gmtTZ);
		    }
	  }
	  

	public DateUtil() {
	}

	public static String toHTTPDateFormat(Date date){
		SimpleDateFormat sf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.ENGLISH);
		sf.setTimeZone(TimeZone.getTimeZone("GMT")) ;
		return sf.format(date) ;		
	}
	
	public static String calendarToString(Calendar c) {
		return calendarToString(c, "yyyyMMdd-HHmmss");
	}

	public static String calendarToDayString(Calendar c) {
		return calendarToString(c, "yyyyMMdd");
	}

	public static String dateToString(Date d, String format) {
		if (d == null)
			d = new Date();
		DateFormat dateformatter = getDateFormat(format);
		return dateformatter.format(d);
	}

	public static String calendarToString(Calendar c, String format) {
		Date d = new Date();
		d.setYear(c.get(1) - 1900);
		d.setMonth(c.get(2));
		d.setDate(c.get(5));
		d.setHours(c.get(11));
		d.setMinutes(c.get(12));
		d.setSeconds(c.get(13));
		return dateToString(d, format);
	}

	public static String currentSeoulToString() {
		return calendarToString(Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul")));
	}

	public static String currentDateToString(String format) {
		return dateToString(new Date(), format);
	}

	public static Date stringToDate(String s) throws ParseException {
		if (s.endsWith("+02"))
			s = s.substring(0, s.length() - 3);
		if (s.endsWith("+01"))
			s = s.substring(0, s.length() - 3);
		DateFormat dateformatter = getDateFormat("yyyyMMdd-HHmmss");
		if (s == null || s.length() < 1)
			return new Date();
		else
			return dateformatter.parse(s);
	}

	public static Calendar stringToCalendar(String s) {
		try {
			Calendar c = Calendar.getInstance();
			c.setTime(getDateFormat("yyyyMMdd-HHmmss").parse(s));
			return c;
		} catch (NullPointerException e) {
			throw new IllegalArgumentException(e);
		} catch (ParseException e) {
			throw new IllegalArgumentException(e);
		}
	}

	private static DateFormat getDateFormat(String format) {
		return new SimpleDateFormat(format); // multi thread..
	}

	public static Date stringToDate(String s, String format) {
		if (s.endsWith("+02"))
			s = s.substring(0, s.length() - 3);
		if (s.endsWith("+01"))
			s = s.substring(0, s.length() - 3);
		try {
			DateFormat df = new SimpleDateFormat(format);
			return df.parse(s);
		} catch (Exception e) {
			throw new IllegalArgumentException(e);
		}
	}

	public Timestamp getTimestamp(Calendar cal) {
		if (cal == null) {
			return null;
		} else {
			long timeval = cal.getTime().getTime();
			return new Timestamp(timeval);
		}
	}

	public Calendar getCalendar(Timestamp t) {
		if (t == null) {
			return null;
		} else {
			Calendar cal = new GregorianCalendar();
			cal.setTimeInMillis(t.getTime());
			return cal;
		}
	}

	public Calendar getCurrentCalendar() {
		Calendar now = new GregorianCalendar();
		return now;
	}

	public static Calendar timeToCalendar(Timestamp t) {
		if (t == null) {
			return null;
		} else {
			Calendar c = new GregorianCalendar();
			c.setTimeInMillis(t.getTime());
			return c;
		}
	}

	public static Timestamp calendarToTime(Calendar c) {
		if (c == null)
			return null;
		else
			return new Timestamp(c.getTimeInMillis());
	}

	public static Calendar dateToCalendar(Date d) {
		if (d == null) {
			return null;
		} else {
			Calendar c = new GregorianCalendar();
			c.setTime(d);
			return c;
		}
	}

	public static Date calendarToDate(Calendar c) {
		if (c == null)
			return null;
		else
			return new Date(c.getTimeInMillis());
	}

	public static Calendar longToCalendar(long misec) {
		Calendar c = new GregorianCalendar();
		c.setTimeInMillis(misec);
		return c;
	}

	public static long calendarToLong(Calendar c) {
		if (c == null)
			return 0L;
		else
			return c.getTimeInMillis();
	}

	public static String currentGMTToString() {
		Calendar c = new GregorianCalendar();
		return calendarToString(c);
	}

	public static String calendarToString(Calendar c, TimeZone tz) {
		c.setTimeZone(tz);
		return calendarToString(c);
	}

	public static String gmtStringToString(String utcDate, TimeZone tz) {
		return calendarToString(stringToCalendar(utcDate), tz);
	}

	public static String calendarToGMTString(Calendar c) {
		return calendarToString(c, TimeZone.getTimeZone("Etc/GMT"));
	}

	public static String calendarToSeoulString(Calendar c) {
		return calendarToString(c, TimeZone.getTimeZone("Asia/Seoul"));
	}

	public static String timeMilliesToDay(long longValue) {
		Date date = new Date(longValue);
		SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_FORMAT);

		return sdf.format(date);
	}

	private static ThreadLocal<SimpleDateFormat> httpFormatter = new ThreadLocal<SimpleDateFormat>();

    public static SimpleDateFormat getHttpDateFormatter() {
        if (httpFormatter.get() == null) {
            httpFormatter.set(new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.US));
            httpFormatter.get().setTimeZone(TimeZone.getTimeZone("GMT"));
        }
        return httpFormatter.get();
    }
	
	private static Hashtable dateFormatter = new Hashtable();
	private static final String STD_FORMAT = "yyyyMMdd-HHmmss";

}
