package net.ion.framework.template;

import net.ion.framework.template.parse.Marker;

/**
 * @author Kim, Sanghoon (wizest@i-on.net)
 * @version 1.0
 */

public class TemplateRuntimeException extends Exception {
	private Marker mark = null;

	public TemplateRuntimeException() {
		super();
	}

	public TemplateRuntimeException(String msg) {
		super(msg);
	}

	public TemplateRuntimeException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public TemplateRuntimeException(Throwable cause) {
		super(cause);
	}

	public TemplateRuntimeException(Marker mark, String msg, Throwable cause) {
		super(msg, cause);
		this.mark = mark;
	}

	public Marker getMark() {
		return this.mark;
	}

}
