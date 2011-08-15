package net.ion.framework.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.security.AccessController;
import java.text.MessageFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

import sun.security.action.GetPropertyAction;

/**
 * log entry formater
 * 
 * @author Kim, Sanghoon (wizest@i-on.net)
 * @version 1.0
 * 
 * @see java.util.logging.Formatter
 */

public class SimpleFormatter extends Formatter {
	Date dat = new Date();
	private final static String format = "{0,date} {0,time}";
	private MessageFormat formatter;

	private Object args[] = new Object[1];

	// Line separator string. This is the value of the line.separator
	// property at the moment that the SimpleFormatter was created.
	@SuppressWarnings("unchecked")
	private String lineSeparator = (String) AccessController.doPrivileged(new GetPropertyAction("line.separator"));

	public SimpleFormatter() {
		super();
	}

	/**
	 * Format the given LogRecord.
	 * 
	 * @param record
	 *            the log record to be formatted.
	 * @return a formatted log record
	 */
	public synchronized String format(LogRecord record) {
		StringBuffer sb = new StringBuffer();
		// Minimize memory allocations here.
		dat.setTime(record.getMillis());
		args[0] = dat;
		StringBuffer text = new StringBuffer();
		if (formatter == null) {
			formatter = new MessageFormat(format);
		}
		formatter.format(args, text, null);
		sb.append(text);
		sb.append(" ");
		if (record.getSourceClassName() != null) {
			sb.append(record.getSourceClassName());
		} else {
			sb.append(record.getLoggerName());
		}
		if (record.getSourceMethodName() != null) {
			sb.append(" ");
			sb.append(record.getSourceMethodName());
		}
		sb.append(lineSeparator);
		String message = formatMessage(record);
		sb.append(record.getLevel().getLocalizedName());
		sb.append(": ");
		sb.append(message);

		// 파라미터 출력
		{
			Object[] ps = record.getParameters();
			if (ps != null) {
				sb.append(" {");
				for (int i = 0, length = ps.length; i < length; ++i) {
					sb.append(ps[i]);
					sb.append(',');
				}
				sb.append('}');
			}
		}

		sb.append(lineSeparator);
		if (record.getThrown() != null) {
			try {
				StringWriter sw = new StringWriter();
				PrintWriter pw = new PrintWriter(sw);
				record.getThrown().printStackTrace(pw);
				pw.close();
				sb.append(sw.toString());
			} catch (Exception ex) {
			}
		}
		return sb.toString();
	}
}
