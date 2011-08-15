package net.ion.framework.template.tagext;

public class TagException extends Exception {
	public TagException() {
		super();
	}

	public TagException(Throwable cause) {
		super(cause);
	}

	public TagException(String message) {
		super(message);
	}

	public TagException(String message, Throwable cause) {
		super(message, cause);
	}
}
