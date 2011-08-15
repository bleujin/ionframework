package net.ion.framework.schedule;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.StringTokenizer;
import java.util.TimeZone;

import net.ion.framework.util.StackTrace;

/**
 * 스케줄러가 동작할 시각을 지정한다.
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
	 * unix의 crontab 스타일로 시각을 지정한다. (년,TimeZone을 추가 하였다.)
	 * 
	 * <pre>
	 *  {분} {시} {일} {월} {요일} [총 실행 횟수] [년] [TimeZone]
	 * 
	 *  1.범위
	 *      {분} : 0~59
	 *      {시} : 0~23
	 *      {일} : 1~31
	 *      {월} : 1~12
	 *      {요일} : 0~7 (단 0과 7은 모두 '일요일'을 의미 한다. 즉, 일0 월1 화2 수3 목4 금5 토6 일7)
	 *      [총 실행 횟수] : 0~ (단, * 일 경우 실행 수의 제한이 없다. (defualt))
	 *      [년] : 1970~2200 , 생략가능 생략할 경우 매년 적용된다.
	 *      [TimeZone] : java.util.TimeZone에 사용할 생성자 문자열과 동일 ex) GMT+9 , 생략 가능하고 생략할 경우 System Default Time Zone을 사용한다.
	 * 
	 *  2.연산
	 *      리스트연산 : , 를 이용하여 여러 경우를 지정할 수 있다.
	 *          ex) 1,2,3,7,8
	 * 
	 *      범위연산 : - 을 이용하여 범위를 지정할 수 있다.
	 *          ex) 2-11        -> 2,3,4,5,6,7,8,9,10,11
	 * 
	 *      스텝연산 : / 을 이용하여 범위연산시 스텝을 지정할 수 있다.
	 *          ex) 3-7/2       -> 3,5,7 (2칸씩)
	 *         단, 스텝이 0일 경우 아무 것도 선택되지 않는다.
	 * 
	 *      임의시간 : * 을 이용하여 주어진 모든 범위를 선택할 수 있다.
	 *          ex) *           -> {분} 일 경우 0-59 와 동일한 의미
	 * 
	 *  3.예제
	 * 
	 *      AtTime("20 4 1,15 1-12/2 6 * * GMT+9")
	 *          매년
	 *          매 1,3,5,7,9,11월
	 *          1일,15일 또는 금요일   <- 주의!!! : 요일 항목은 예외적 "일에 대해" OR 으로 작용된다. 나머지는 모두 AND다.
	 *          오전 4시 20분
	 *          횟수 제한 없음
	 * 
	 *      AtTime("20 4 * * 1 * *");
	 *          매년
	 *          매달
	 *          모든 달의
	 *          모든 날에 OR 월요일에 -> 결국 모든 날에       <- 주의!!! : 요일은 "일에 대해" OR 이므로 위 식에서 요일 1은 의미가 없다.
	 *          오전 4시 20분
	 *          횟수 제한 없음
	 * 
	 *      AtTime("20 4 0 * 1 3 *");
	 *          매년
	 *          매달
	 *          모든 달의
	 *          0 날에 OR 월요일에 -> 결국 모든 월요일에 (0인 일은 없으므로)    <- 주의!!! : 요일에 대해서 적용하려면 이와 같이 해야한다.
	 *          오전 4시 20분
	 *          의 경우에 3번만 (이후에는 false)
	 * </pre>
	 * 
	 * @param crontab
	 */
	public AtTime(String crontab) {
		init(crontab);
	}

	/**
	 * calendar에 지정된 날의 지정된 시각에 한번 실행 시킨다.
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
	 * AtTime을 생성한다.
	 * 
	 * 각 항목에서 모든 경우를 표현할 때는 AtTime.ALL 을 사용한다.
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
		// * 로 했을 경우 범위
		// 분 0~59
		// 시 0~23
		// 일 1~31
		// 월 1~12
		// 요일 0~7 (0,7 -> sun)
		// 년 1970~2200
		// TimeZone ex) "GMT+9"
		//
		// * -> first-last
		//
		// range -
		// list ,
		// step / (,보다 우선순위가 높다)

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
						// 그냥 숫자 하나일 경우
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
	 * AtTime 에 설정된 값이 현재 시간과 match 되는지 알아본다.
	 * 
	 * match()가 true일 경우 match times 가 증가하므로 한 번 true 난 이후 설정 확인을 위해 계속 이 메소드를 콜하면 안된다. (실행 횟수의 제한에 영향을 준다.)
	 * 
	 * @return
	 */
	protected boolean match() {
		// match 되는 횟수 제한
		if (wasExpired()) {
			return false;
		}

		Calendar cal = Calendar.getInstance(this.zone);

		int min = cal.get(Calendar.MINUTE);
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		int day = cal.get(Calendar.DATE);
		int month = cal.get(Calendar.MONTH) + 1;
		int year = cal.get(Calendar.YEAR);
		int week = cal.get(Calendar.DAY_OF_WEEK) - 1; // 0 부터 시작하게
		int week2 = (week == 0) ? 7 : 0; // 0 과 7은 동일 -> AtTime 생성시 둘다 허용했으므로 두번 검색 ㅡ.ㅡ;;

		if (Arrays.binarySearch(mins, min) >= 0
				&& Arrays.binarySearch(hours, hour) >= 0
				&& ((Arrays.binarySearch(days, day) >= 0 || (Arrays.binarySearch(weeks, week) >= 0 || Arrays.binarySearch(weeks, week2) >= 0))
						&& Arrays.binarySearch(months, month) >= 0 && Arrays.binarySearch(years, year) >= 0)) {
			this.currTimes++; // match 되는 횟수 제한을 위해
			return true;
		} else {
			return false;
		}
	}

	/**
	 * job이 실행되는 횟수가 초과 되었는지 test
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
