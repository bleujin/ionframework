package net.ion.framework.license;

/**
 * license serialization / deserialization / validation 등에 발생한다.
 * 
 * @author Kim, Sanghoon (wizest@i-on.net)
 * @version 1.0
 */

public class LicenseException extends Exception {
	public LicenseException(String message) {
		super(message);
	}

	public LicenseException(String message, Throwable cause) {
		super(message, cause);
	}
}
