package net.ion.framework.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CalendarUtils {
	private static Calendar calendar = null;
	private static int[] lastDayArr = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
	private static final String SIMPLE_FORMAT = "yyyyMMdd";
	private static final String DETAIL_FORMAT = "yyyyMMdd-HHmmss";
	private static final String INQUIRY_FORMAT = "yyyy.MM.dd";

	public static final String YEAR = "year";
	public static final String MONTH = "month";
	public static final String DAY = "day";

	public static final int LAST_MONTH = 12;
	public static final int LAST_DATE = 31;
	public static final int LAST_TIME = 24;

	public static String[] yearList = null;
	public static final String[] monthList = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12" };
	public static final String[] dayList = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20",
			"21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31" };
	public static final String[] timeList = { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18",
			"19", "20", "21", "22", "23" };

	private static int getYear() {
		return calendar.get(Calendar.YEAR);
	}

	private static int getMonth() {
		return calendar.get(Calendar.MONTH) + 1;
	}

	public static int getCurrentYear() {
		calendar = Calendar.getInstance();
		return getYear();
	}

	public static int getCurrentMonth() {
		calendar = Calendar.getInstance();
		return getMonth();
	}

	public static String getCurrentDate() {
		return getCurrentDate(SIMPLE_FORMAT);
	}

	public static String getCurrentDateTime() {
		return getCurrentDate(DETAIL_FORMAT);
	}

	public static String getCurrentDate(String formatString) {
		if (StringUtil.isEmpty(formatString)) {
			return new SimpleDateFormat().format(Calendar.getInstance().getTime());
		} else {
			return new SimpleDateFormat(formatString).format(Calendar.getInstance().getTime());
		}
	}

	public static int lastdayByMonth(int year, int month) {
		calendar = Calendar.getInstance();
		if (month == 2) {
			lastDayArr[1] = (year % 4 == 0) && !(year % 100 == 0) || (year % 400 == 0) ? 29 : 28;
		}
		return lastDayArr[month - 1];
	}

	public static String getYesterday() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, -1);
		return new SimpleDateFormat(SIMPLE_FORMAT).format(calendar.getTime());
	}

	public static String getBeforeday(String day, int i) throws ParseException {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new SimpleDateFormat(SIMPLE_FORMAT).parse(day));
		calendar.add(Calendar.DATE, i);
		return new SimpleDateFormat(SIMPLE_FORMAT).format(calendar.getTime());
	}

	public static String getInquiry(String date) {
		Calendar cal = Calendar.getInstance();
		cal.set(substring2int(date, 0, 4), substring2int(date, 4, 6) - 1, substring2int(date, 6));
		return new SimpleDateFormat(INQUIRY_FORMAT).format(cal.getTime());
	}

	public static int substring2int(String obj, int i) {
		return Integer.parseInt(obj.substring(i));
	}

	public static int substring2int(String obj, int i, int j) {
		return Integer.parseInt(obj.substring(i, j));
	}

	public static void main(String[] args) throws ParseException {
		String day = "20070301";
		String before = CalendarUtils.getBeforeday(day, -6);
		System.out.println(before);

	}
}
