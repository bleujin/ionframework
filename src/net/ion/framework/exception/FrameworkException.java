package net.ion.framework.exception;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: I-ON Communications
 * </p>
 * 
 * @author bleujin
 * @version 1.0
 */

public abstract class FrameworkException extends RuntimeException {
	private static final long serialVersionUID = 1943492039493304299L;

	public FrameworkException(Throwable cause) {
		super(cause.getMessage(), cause);
	}

	public FrameworkException(Throwable cause, String message) {
		super(message, cause);
	}

	public FrameworkException(String message) {
		super(message);
	}

}
