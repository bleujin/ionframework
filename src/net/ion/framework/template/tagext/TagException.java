package net.ion.framework.template.tagext;

public class TagException extends Exception {
	
	private static final long serialVersionUID = -7915114098346541562L;

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
