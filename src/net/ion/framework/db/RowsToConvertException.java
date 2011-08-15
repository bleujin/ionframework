package net.ion.framework.db;

public class RowsToConvertException extends Exception {
	public RowsToConvertException() {
		super();
	}

	public RowsToConvertException(String message) {
		super(message);
	}

	public RowsToConvertException(Throwable t) {
		super(t);
	}

	public RowsToConvertException(String message, Throwable t) {
		super(message, t);
	}
}
