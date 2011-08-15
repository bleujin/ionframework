package net.ion.framework.schedule;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.StringTokenizer;
import java.util.TimeZone;

import net.ion.framework.util.StackTrace;

/**
 * �����ٷ��� ������ �ð��� �����Ѵ�.
 * 
 * @author Kim Sanghoon wizest@i-on.net
 * @version 1.0
 */

public class AtTime implements Serializable {
	public final static int ALL = -1;
	public final static int INFINITE = -1;

	String crontab = null;
	TimeZone zone = null;

	int matchTimes = INFINITE;
	int currTimes = 0;

	int[] mins;
	int[] hours;
	int[] days;
	int[] months;
	int[] weeks;
	int[] years;

	/**
	 * unix�� crontab ��Ÿ�Ϸ� �ð��� �����Ѵ�. (��,TimeZone�� �߰� �Ͽ���.)
	 * 
	 * <pre>
	 *  {��} {��} {��} {��} {����} [�� ���� Ƚ��] [��] [TimeZone]
	 * 
	 *  1.����
	 *      {��} : 0~59
	 *      {��} : 0~23
	 *      {��} : 1~31
	 *      {��} : 1~12
	 *      {����} : 0~7 (�� 0�� 7�� ��� '�Ͽ���'�� �ǹ� �Ѵ�. ��, ��0 ��1 ȭ2 ��3 ��4 ��5 ��6 ��7)
	 *      [�� ���� Ƚ��] : 0~ (��, * �� ��� ���� ���� ������ ����. (defualt))
	 *      [��] : 1970~2200 , �������� ������ ��� �ų� ����ȴ�.
	 *      [TimeZone] : java.util.TimeZone�� ����� ������ ���ڿ��� ���� ex) GMT+9 , ���� �����ϰ� ������ ��� System Default Time Zone�� ����Ѵ�.
	 * 
	 *  2.����
	 *      ����Ʈ���� : , �� �̿��Ͽ� ���� ��츦 ������ �� �ִ�.
	 *          ex) 1,2,3,7,8
	 * 
	 *      �������� : - �� �̿��Ͽ� ������ ������ �� �ִ�.
	 *          ex) 2-11        -> 2,3,4,5,6,7,8,9,10,11
	 * 
	 *      ���ܿ��� : / �� �̿��Ͽ� ��������� ������ ������ �� �ִ�.
	 *          ex) 3-7/2       -> 3,5,7 (2ĭ��)
	 *         ��, ������ 0�� ��� �ƹ� �͵� ���õ��� �ʴ´�.
	 * 
	 *      ���ǽð� : * �� �̿��Ͽ� �־��� ��� ������ ������ �� �ִ�.
	 *          ex) *           -> {��} �� ��� 0-59 �� ������ �ǹ�
	 * 
	 *  3.����
	 * 
	 *      AtTime("20 4 1,15 1-12/2 6 * * GMT+9")
	 *          �ų�
	 *          �� 1,3,5,7,9,11��
	 *          1��,15�� �Ǵ� �ݿ���   <- ����!!! : ���� �׸��� ������ "�Ͽ� ����" OR ���� �ۿ�ȴ�. �������� ��� AND��.
	 *          ���� 4�� 20��
	 *          Ƚ�� ���� ����
	 * 
	 *      AtTime("20 4 * * 1 * *");
	 *          �ų�
	 *          �Ŵ�
	 *          ��� ����
	 *          ��� ���� OR �����Ͽ� -> �ᱹ ��� ����       <- ����!!! : ������ "�Ͽ� ����" OR �̹Ƿ� �� �Ŀ��� ���� 1�� �ǹ̰� ����.
	 *          ���� 4�� 20��
	 *          Ƚ�� ���� ����
	 * 
	 *      AtTime("20 4 0 * 1 3 *");
	 *          �ų�
	 *          �Ŵ�
	 *          ��� ����
	 *          0 ���� OR �����Ͽ� -> �ᱹ ��� �����Ͽ� (0�� ���� �����Ƿ�)    <- ����!!! : ���Ͽ� ���ؼ� �����Ϸ��� �̿� ���� �ؾ��Ѵ�.
	 *          ���� 4�� 20��
	 *          �� ��쿡 3���� (���Ŀ��� false)
	 * </pre>
	 * 
	 * @param crontab
	 */
	public AtTime(String crontab) {
		init(crontab);
	}

	/**
	 * calendar�� ������ ���� ������ �ð��� �ѹ� ���� ��Ų��.
	 * 
	 * @param cal
	 */
	public AtTime(Calendar cal) {
		int min = cal.get(Calendar.MINUTE);
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		int day = cal.get(Calendar.DATE);
		int month = cal.get(Calendar.MONTH) + 1;
		int year = cal.get(Calendar.YEAR);

		String crontab = min + " " + hour + " " + day + " " + month + " * " + year;
		init(crontab);

		this.zone = cal.getTimeZone();
		this.matchTimes = 1;
	}

	/**
	 * AtTime�� �����Ѵ�.
	 * 
	 * �� �׸񿡼� ��� ��츦 ǥ���� ���� AtTime.ALL �� ����Ѵ�.
	 * 
	 * @param min
	 * @param hour
	 * @param day
	 * @param month
	 * @param weekday
	 * @param matchTimes
	 * @param year
	 * @param zone
	 */
	public AtTime(int min, int hour, int day, int month, int weekday, int matchTimes, int year, TimeZone zone) {
		String sMin = (min == ALL) ? "*" : String.valueOf(min);
		String sHour = (hour == ALL) ? "*" : String.valueOf(hour);

		String sDay = (day == ALL) ? "*" : String.valueOf(day);
		String sMonth = (month == ALL) ? "*" : String.valueOf(month);
		String sWeekday = (weekday == ALL) ? "*" : String.valueOf(weekday);

		String sMatchTimes = (matchTimes == INFINITE) ? "*" : String.valueOf(matchTimes);
		String sYear = (year == ALL) ? "*" : String.valueOf(year);

		String crontab = sMin + " " + sHour + " " + sDay + " " + sMonth + " " + sWeekday + " " + sMatchTimes + " " + sYear;

		init(crontab);

		this.zone = zone;
	}

	private void init(String crontab) {
		// * �� ���� ��� ����
		// �� 0~59
		// �� 0~23
		// �� 1~31
		// �� 1~12
		// ���� 0~7 (0,7 -> sun)
		// �� 1970~2200
		// TimeZone ex) "GMT+9"
		//
		// * -> first-last
		//
		// range -
		// list ,
		// step / (,���� �켱������ ����)

		try {
			this.crontab = crontab;
			StringTokenizer tokenizer = new StringTokenizer(crontab, " ");

			String min = tokenizer.nextToken();
			mins = parseScheduledTable(0, 59, min);
			String hour = tokenizer.nextToken();
			hours = parseScheduledTable(0, 23, hour);
			String day = tokenizer.nextToken();
			days = parseScheduledTable(1, 31, day);
			String month = tokenizer.nextToken();
			months = parseScheduledTable(1, 12, month);
			String week = tokenizer.nextToken();
			weeks = parseScheduledTable(0, 7, week);

			String matchTimes = tokenizer.hasMoreTokens() ? tokenizer.nextToken() : "*";
			if (matchTimes.equals("*")) {
				this.matchTimes = INFINITE;
			} else {
				this.matchTimes = Integer.parseInt(matchTimes);
			}

			String year = tokenizer.hasMoreTokens() ? tokenizer.nextToken() : "*";
			years = parseScheduledTable(1970, 2200, year);

			this.zone = tokenizer.hasMoreTokens() ? TimeZone.getTimeZone(tokenizer.nextToken()) : TimeZone.getDefault();
		} catch (Throwable t) {
			throw new IllegalArgumentException("invalid arguments:mins hours days months weeks matchTimes years timezone :StackTrace=" + StackTrace.trace(t));
		}
	}

	private static int[] parseScheduledTable(int min, int max, String table) {
		try {
			if (table.endsWith("*")) {
				int[] result = new int[max - min + 1];
				for (int i = 0; i <= max - min; ++i) {
					result[i] = i + min;
				}
				return result;
			} else {
				ArrayList<Integer> l = new ArrayList<Integer>();
				String[] blocks = table.split(",");

				// list operation
				for (int i = 0; i < blocks.length; ++i) {
					String block = blocks[i];
					if (block.indexOf("-") > 0) {
						// step & range operation
						int step = 1; // default step:1
						int div = block.length();

						if (block.indexOf("/") > 0) {
							// step
							div = block.indexOf("/");
							step = Integer.parseInt(block.substring(div + 1));
						}
						min = Integer.parseInt(block.substring(0, block.indexOf("-")));
						max = Integer.parseInt(block.substring(block.indexOf("-") + 1, div));

						if (step != 0)
							for (int j = min; j <= max; j += step) {
								Integer in = new Integer(j);
								if (!l.contains(in)) {
									l.add(in);
								}
							}
					} else {
						// �׳� ���� �ϳ��� ���
						Integer in = new Integer(block.trim());
						if (!l.contains(in)) {
							l.add(in);
						}
					}
				}

				int[] result = new int[l.size()];
				for (int i = 0; i < result.length; ++i) {
					result[i] = ((Integer) l.get(i)).intValue();
				}
				return result;
			}
		} catch (Exception e) {
			throw new IllegalArgumentException(StackTrace.trace(e));
		}
	}

	/**
	 * AtTime �� ������ ���� ���� �ð��� match �Ǵ��� �˾ƺ���.
	 * 
	 * match()�� true�� ��� match times �� �����ϹǷ� �� �� true �� ���� ���� Ȯ���� ���� ��� �� �޼ҵ带 ���ϸ� �ȵȴ�. (���� Ƚ���� ���ѿ� ������ �ش�.)
	 * 
	 * @return
	 */
	protected boolean match() {
		// match �Ǵ� Ƚ�� ����
		if (wasExpired()) {
			return false;
		}

		Calendar cal = Calendar.getInstance(this.zone);

		int min = cal.get(Calendar.MINUTE);
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		int day = cal.get(Calendar.DATE);
		int month = cal.get(Calendar.MONTH) + 1;
		int year = cal.get(Calendar.YEAR);
		int week = cal.get(Calendar.DAY_OF_WEEK) - 1; // 0 ���� �����ϰ�
		int week2 = (week == 0) ? 7 : 0; // 0 �� 7�� ���� -> AtTime ������ �Ѵ� ��������Ƿ� �ι� �˻� ��.��;;

		if (Arrays.binarySearch(mins, min) >= 0
				&& Arrays.binarySearch(hours, hour) >= 0
				&& ((Arrays.binarySearch(days, day) >= 0 || (Arrays.binarySearch(weeks, week) >= 0 || Arrays.binarySearch(weeks, week2) >= 0))
						&& Arrays.binarySearch(months, month) >= 0 && Arrays.binarySearch(years, year) >= 0)) {
			this.currTimes++; // match �Ǵ� Ƚ�� ������ ����
			return true;
		} else {
			return false;
		}
	}

	/**
	 * job�� ����Ǵ� Ƚ���� �ʰ� �Ǿ����� test
	 * 
	 * @return
	 */
	protected boolean wasExpired() {
		if (this.matchTimes == INFINITE) {
			return false;
		} else {
			if (this.currTimes < this.matchTimes) {
				return false;
			} else {
				return true;
			}
		}
	}

	private String getIntArrayString(int[] is) {
		StringBuffer buff = new StringBuffer();

		if (is.length > 0) {
			buff.append("{");
			int length = is.length - 1;
			for (int i = 0; i < length; ++i) {
				buff.append(is[i]);
				buff.append(",");
			}
			buff.append(is[is.length - 1]);
			buff.append("}");
		} else {
			buff.append("{}");

		}
		return buff.toString();
	}

	public String toString() {

		// return "cron style scheduled table:"+this.crontab;
		StringBuffer buff = new StringBuffer();

		buff.append("At time: ");
		buff.append(this.crontab);
		buff.append("\n");

		// mins
		buff.append("\tmins:");
		buff.append(getIntArrayString(mins));
		buff.append("\n");

		// hours
		buff.append("\thours:");
		buff.append(getIntArrayString(hours));
		buff.append("\n");

		// days
		buff.append("\tdays:");
		buff.append(getIntArrayString(days));
		buff.append("\n");

		// months
		buff.append("\tmonths:");
		buff.append(getIntArrayString(months));
		buff.append("\n");

		// weeks
		buff.append("\tweeks:");
		buff.append(getIntArrayString(weeks));
		buff.append("\n");

		// matchTimes
		buff.append("\tmatchTimes:(-1 means infinite)");
		buff.append(this.matchTimes);
		buff.append(" / current:");
		buff.append(this.currTimes);
		buff.append("\n");

		// years
		buff.append("\tyears:");
		buff.append(getIntArrayString(years));
		buff.append("\n");

		// TimeZone
		buff.append("\tTimeZone:");
		buff.append(this.zone);
		buff.append("\n");

		return buff.toString();
	}

	/*
	 * public static void main(String[] args) { // AtTime at = new AtTime("20 4 1,15 1-12/2 6 * * GMT+9"); AtTime at = new AtTime("* 16 * 2 2 * * GMT+9"); // AtTime at = new AtTime(Calendar.getInstance()); System.out.println(at);
	 * System.out.println(at.match()); // System.out.println(System.currentTimeMillis()); // System.out.println(Calendar.getInstance(TimeZone.getTimeZone("GMT-1")).getTimeInMillis()); }
	 */
}
