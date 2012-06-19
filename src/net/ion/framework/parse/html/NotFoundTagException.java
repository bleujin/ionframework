package net.ion.framework.parse.html;

import java.io.IOException;

public class NotFoundTagException extends IOException {

	public NotFoundTagException(Throwable cause) {
		super(cause.getMessage());
	}

	public NotFoundTagException(String message) {
		super(message);
	}

}
