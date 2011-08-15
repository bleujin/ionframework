package net.ion.framework.exception;

import java.util.logging.Logger;

import net.ion.framework.util.StackTrace;

/**
 * Exception에 대해 필요한 정보를 가공하여 사용을 쉽게 해준다. 기본적으로 Exception 인스턴스, logger 인스턴스, exception 메세지가 필요하다.
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
	 * Exception 에 대한 Trace를 기록한다. 기본 stdout
	 */
	public void recordException() {
		exception.printStackTrace();
	}

	public String getMessage() {
		return exception.getMessage();
	}

	/**
	 * Exception의 trace를 가져온다.
	 * 
	 * @return String
	 */
	public String getStackTrace() {
		return StackTrace.trace(this.exception);
	}

}
