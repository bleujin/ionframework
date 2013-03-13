package net.ion.framework.template.tagext;

public class TagRuntimeException extends RuntimeException {
	
	private static final long serialVersionUID = -7915114098346541562L;

	public TagRuntimeException() {
		super();
	}

	public TagRuntimeException(Throwable cause) {
		super(cause);
	}

	public TagRuntimeException(String message) {
		super(message);
	}

	public TagRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}
}
