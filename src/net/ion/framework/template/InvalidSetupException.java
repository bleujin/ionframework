package net.ion.framework.template;

public class InvalidSetupException extends Exception {
	public InvalidSetupException() {
		super();
	}

	public InvalidSetupException(String msg) {
		super(msg);
	}

	public InvalidSetupException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public InvalidSetupException(Throwable cause) {
		super(cause);
	}
}
