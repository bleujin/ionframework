package net.ion.framework.exception;

public class ExecutionRuntimeException extends RuntimeException {
	private static final long serialVersionUID = 1943492039493304299L;

	public ExecutionRuntimeException(Throwable cause) {
		super(cause.getMessage(), cause);
	}

	public ExecutionRuntimeException(Throwable cause, String message) {
		super(message, cause);
	}

	public ExecutionRuntimeException(String message) {
		super(message);
	}

}
