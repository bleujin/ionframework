package net.ion.framework.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;

public class DateFormatUtil {

	private static String DATE_FORMATE = "yyyyMMdd-HHmmss";
	private static String DAY_FORMATE = "yyyyMMdd";
	private static String DIR_FORMATE = "yyyy/MM/dd";

	public static String date2String(Date d, String format) {
		if (d == null) {
			d = new Date();
		}
		return DateFormatUtils.format(d, format);
	}

	public static String date2String(Date d) {
		return date2String(d, DATE_FORMATE);
	}

	public static String day2String(Date d) {
		return date2String(d, DAY_FORMATE);
	}

	public static String currentDate2String() {
		return date2String(new Date());
	}

	public static String currentDay2String() {
		return day2String(new Date());
	}

	public static String currentDate2String(String format) {
		return date2String(new Date(), format);
	}

	public static Date string2Date(String s) throws ParseException {
		return string2Date(s, DATE_FORMATE);
	}

	public static Date string2Date(String s, String format) throws ParseException {
		DateFormat dateformatter = new SimpleDateFormat(format);
		return dateformatter.parse(s);
	}

	private static String[] PREDEFINED_DATEFORMAT = new String[] { "yyyy/MM/dd", "yyyy.MM.dd", "yyyy-MM-dd", "yyyyMMdd", "yyyyMMdd-HHmmss",
			"yyyy.MM.dd HH:mm:ss", "yyyy/MM/dd HH:mm:ss", "yyyy-MM-dd HH:mm:ss" };

	public static boolean isDateFormat(String str) {
		try {
			DateUtils.parseDate(str, PREDEFINED_DATEFORMAT);
			return true;
		} catch (IllegalArgumentException e) {
			return false;
		} catch (Exception e) {
			return false;
		}
	}

	public static Date getDateIfMatchedType(String str) {
		try {
			Date date = DateUtils.parseDate(str, PREDEFINED_DATEFORMAT);
			return date;
		} catch (IllegalArgumentException e) {
			return null;
		} catch (Exception e) {
			return null;
		}
	}

	public static String getDirFormat() {
		SimpleDateFormat formatter = new SimpleDateFormat(DIR_FORMATE);
		return formatter.format(new Date());
	}
}
