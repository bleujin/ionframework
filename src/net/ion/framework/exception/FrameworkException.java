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
