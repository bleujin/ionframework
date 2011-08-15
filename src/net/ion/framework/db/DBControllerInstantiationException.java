package net.ion.framework.db;

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

public class DBControllerInstantiationException extends Exception {
	public DBControllerInstantiationException() {
		super();
	}

	public DBControllerInstantiationException(String msg) {
		super(msg);
	}

	public DBControllerInstantiationException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public DBControllerInstantiationException(Throwable cause) {
		super(cause);
	}
}