package net.ion.framework.exception;

import java.util.logging.Logger;

import net.ion.framework.util.StackTrace;

/**
 * Exception�� ���� �ʿ��� ������ �����Ͽ� ����� ���� ���ش�. �⺻������ Exception �ν��Ͻ�, logger �ν��Ͻ�, exception �޼����� �ʿ��ϴ�.
 * 
 * @author Choi sei hwan <a href="mailto:sehan@i-on.net">Choi sei hwan</a>
 * @version 1.0
 */

public class ExceptionInfo {
	private Throwable exception = null;

	private Logger logger = null;

	private String userMessage = null;

	public ExceptionInfo(Throwable ex) {
		this(ex, null);
	}

	public ExceptionInfo(Throwable ex, Logger logger) {
		this(ex, logger, null);
	}

	public ExceptionInfo(Throwable ex, Logger logger, String userMessage) {
		this.exception = ex;
		this.logger = logger;
		this.userMessage = userMessage;
	}

	public Logger getLogger() {
		return this.logger;
	}

	public String getUserMessage() {
		return this.userMessage;
	}

	public String getExceptionLocation() {
		return new String();
	}

	public Throwable getException() {
		return exception;
	}

	public String getExceptionClassName() {
		return exception.getClass().getName();
	}

	/**
	 * Exception �� ���� Trace�� ����Ѵ�. �⺻ stdout
	 */
	public void recordException() {
		exception.printStackTrace();
	}

	public String getMessage() {
		return exception.getMessage();
	}

	/**
	 * Exception�� trace�� �����´�.
	 * 
	 * @return String
	 */
	public String getStackTrace() {
		return StackTrace.trace(this.exception);
	}

}
