package net.ion.framework.template;

import net.ion.framework.template.parse.Marker;

/**
 * compile 할 때 발생하는 예외
 * 
 * @author Kim Sanghoon wizest@i-on.net
 * @version 1.0
 */

public class CompilerException extends Exception {
	/**
	 * 예외가 발생한 지점
	 */
	private Marker mark = null;

	/**
	 * @param mark
	 *            예외 발생한 지점
	 * @param message
	 *            예외 메세지
	 */
	public CompilerException(Marker mark, String message) {
		super(message);
		this.mark = mark;
	}

	/**
	 * @param mark
	 *            Marker 예외 발생 지점
	 * @param message
	 *            String 예외 메세지
	 * @param cause
	 *            Throwable 예외 원인
	 */
	public CompilerException(Marker mark, String message, Throwable cause) {
		super(message, cause);
		this.mark = mark;
	}

	/**
	 * @param mark
	 *            Marker 예외 발생 지점
	 * @param cause
	 *            Throwable 예외 원인
	 */
	public CompilerException(Marker mark, Throwable cause) {
		super(cause);
		this.mark = mark;
	}

	/**
	 * @return Marker 예외 발생 지점
	 */
	public Marker getMark() {
		return mark;
	}

	// public String getMessage()
	// {
	// return super.getMessage()+" in tag mark="+mark;
	// }
}
