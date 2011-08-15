package net.ion.framework.util;

/**
 * Throwable에 대한 stack tracing 결과 문자열을 얻는다.
 * 
 * @author Kim Sanghoon wizest@i-on.net
 * @version 1.0
 */

public class StackTrace {
	// private static final int TRACING_DEPTH=5;

	/**
	 * 예외에 대한 상세 내역을 e.printStackTrace() 처럼 작성하여 리턴한다.
	 * 
	 * @param t
	 * @return
	 */
	public static String trace(Throwable t) {
		return trace(t, Integer.MAX_VALUE);
	}

	/**
	 * 예외에 대한 상세 내역을 e.printStackTrace() 처럼 작성하여 리턴한다.
	 * 
	 * @param t
	 * @param tracingDepth
	 *            tracing하는 깊이 (너무 깊이 추적할 필요 없다)
	 * @return
	 */
	public static String trace(Throwable t, int tracingDepth) {
		if (t == null) {
			return "null";
		}

		int length;
		StringBuffer buff = new StringBuffer();

		buff.append(t.toString());
		// buff.append( "\n" );

		StackTraceElement[] trace = null;

		trace = t.getStackTrace();
		length = Math.min(trace.length, tracingDepth);
		for (int i = 0; i < length; ++i) {
			buff.append("\n    at ");
			buff.append(trace[i].toString());
		}
		if (trace.length != length) {
			buff.append("\n    ...");

			// cause
		}
		while (t.getCause() != null) {
			t = t.getCause();

			buff.append("\nCaused by: ");
			buff.append(t.toString());
			// buff.append( "\n" );

			trace = t.getStackTrace();
			length = Math.min(trace.length, tracingDepth);
			for (int i = 0; i < length; ++i) {
				buff.append("\n    at ");
				buff.append(trace[i].toString());
			}
			if (trace.length != length) {
				buff.append("\n    ...");
			}
		}

		return buff.toString();
	}
	// public static void main(String[] args)
	// {
	//
	// }
}
