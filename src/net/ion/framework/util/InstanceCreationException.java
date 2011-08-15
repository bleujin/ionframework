package net.ion.framework.util;

/**
 * <p>
 * Title: ICS EV
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: I-ON Communications
 * </p>
 * 
 * @author Kim Sanghoon wizest@i-on.net
 * @version 1.0
 */

public class InstanceCreationException extends Exception {
	public InstanceCreationException() {
		super();
	}

	public InstanceCreationException(String msg) {
		super(msg);
	}

	public InstanceCreationException(Throwable cause) {
		super(cause);
	}

	public InstanceCreationException(String msg, Throwable cause) {
		super(msg, cause);
	}
}