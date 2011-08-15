package net.ion.framework.mail;

/**
 * Mail ฐüทร exception
 * 
 * @author Kim, Sanghoon (wizest@i-on.net)
 * @version 1.0
 */

public class MailException extends Exception {
	public MailException() {
		super();
	}

	public MailException(String msg) {
		super(msg);
	}

	public MailException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public MailException(Throwable cause) {
		super(cause);
	}

}
