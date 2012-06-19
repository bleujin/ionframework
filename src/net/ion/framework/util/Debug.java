package net.ion.framework.util;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import net.htmlparser.jericho.Segment;

public class Debug extends OutputStream {

	public final static String PROPERTY_KEY = "framework.debug.out";
//	private static final String DEBUG = "debug";
//	private final static String INFO = "info";
//	private static final Object WARN = "warn";
//	private static final String ERROR = "error";
//	
	public enum Level {
		Info(0), Debug (1), Warn(4), Error(7) ;
		
		private int code ;
		private Level(int code){
			this.code = code ;
		}
		public int getLevelCode(){
			return code ;
		}
	} ;

	private static final LimitedList<String> imsi = new LimitedList<String>(500);
	private static final Map<Integer, String> INDENTS = new HashMap<Integer, String>();
	static {
		INDENTS.put(0, "");
		INDENTS.put(1, "\t");
		INDENTS.put(2, "\t\t");
		INDENTS.put(3, "\t\t\t");
		INDENTS.put(4, "\t\t\t\t");
		INDENTS.put(5, "\t\t\t\t\t");
	}
	private final static Integer ZERO = new Integer(0);

	private static void out(CharSequence message) {
		out(System.out, ZERO, message);
	}

	private static void out(Level level, CharSequence message) {
		if (level.getLevelCode() < PrintLevel.getLevelCode()) return ;
		out(System.out, ZERO, message);
	}

	private static void err(CharSequence message) {
		out(System.err, ZERO, message);
	}

	private static Level PrintLevel = Level.Info ; 
	public static void setPrintLevel(Level level){
		PrintLevel = level;
	}
	
	
	
	private static void out(final PrintStream out, final int level, CharSequence message) {

		if ("off".equals(System.getProperty(PROPERTY_KEY)))
			return;
		else if ("system".equals(System.getProperty(PROPERTY_KEY))) {
			System.out.print(message);
			return;
		}

		StackTraceElement[] stackTraces = new Exception().getStackTrace() ; //Thread.currentThread().getStackTrace() ;//  new Exception().getStackTrace();
		String thisName = Debug.class.getName();
		for (StackTraceElement st : stackTraces) {
			String className = st.getClassName();
			if (!thisName.equals(className) && (!className.contains("Debug") && (!className.startsWith("org.apache")))) {

				String callerClass = st.getClassName();
				String callerMethod = st.getMethodName();
				String currDay = CalendarUtils.getCurrentDateTime();
				String outMessage = INDENTS.get(level) + message + " by " + callerClass + ":" + callerMethod + "[" + currDay + "]";
				synchronized (imsi) {
					imsi.add(outMessage);
				}
				out.println(outMessage);
				break;
			}
		}
	}

	// last
	private static void print(Object... objs) {
		out(Arrays.deepToString(objs));
	}

	private static void print(Level level, Object... objs) {
		out(level, Arrays.deepToString(objs));
	}

	private static void printErr(Object... objs) {
		err(Arrays.deepToString(objs));
	}

	public static void line() {
		line("");
	}

	public void write(int b) throws IOException {
		System.out.print(b);
	}

	public static boolean isInfoEnabled() {
		return true;
	}

	public static void happy(Object... objs) {
		print(objs);
	}

	public static void warn(Object... objs) {
		printErr(Arrays.deepToString(objs));
	}

	public static void line(Object... objs) {
		line('=', objs);
	}

	public static void line(char c, Object... objs) {
		String s = StringUtil.repeat(String.valueOf(c), 20);
		print(s, objs);
	}

	public static void error(Object... objs) {
		printErr(Level.Error, objs);
	}

	public static void warn(CharSequence message) {
		printErr(Level.Warn, message);
	}

	public static void warn(CharSequence message, IOException e) {
		printErr(Level.Warn, message, e);
	}

	public static void debug(Object... objs) {
		print(Level.Debug, Arrays.deepToString(objs));
	}

	public static void debug(CharSequence message) {
		print(Level.Debug, message);
	}

	public static void info(CharSequence message) {
		print(Level.Info, message);
	}

	public static void info(Object... objs) {
		print(Level.Info, Arrays.deepToString(objs));
	}

	public static void info(CharSequence message, InterruptedException e) {
		print(Level.Info, message, e);
	}

	public static String[] getRecentMessage() {
		return imsi.toArray(new String[0]);
	}

	public static void println(Object... obj) {
		StringBuilder sb = new StringBuilder() ;
		for (Object object : obj) {
			sb.append(object).append(" ") ;
		}
		System.out.println(sb) ;
	}

	public static void trace(String message) {
		debug(message) ;
	}

}
