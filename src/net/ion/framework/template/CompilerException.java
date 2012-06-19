package net.ion.framework.template;

import net.ion.framework.template.parse.Marker;

/**
 * compile �� �� �߻��ϴ� ����
 * 
 * @author Kim Sanghoon wizest@i-on.net
 * @version 1.0
 */

public class CompilerException extends Exception {
	/**
	 * ���ܰ� �߻��� ����
	 */
	private Marker mark = null;

	/**
	 * @param mark
	 *            ���� �߻��� ����
	 * @param message
	 *            ���� �޼���
	 */
	public CompilerException(Marker mark, String message) {
		super(message);
		this.mark = mark;
	}

	/**
	 * @param mark
	 *            Marker ���� �߻� ����
	 * @param message
	 *            String ���� �޼���
	 * @param cause
	 *            Throwable ���� ����
	 */
	public CompilerException(Marker mark, String message, Throwable cause) {
		super(message, cause);
		this.mark = mark;
	}

	/**
	 * @param mark
	 *            Marker ���� �߻� ����
	 * @param cause
	 *            Throwable ���� ����
	 */
	public CompilerException(Marker mark, Throwable cause) {
		super(cause);
		this.mark = mark;
	}

	/**
	 * @return Marker ���� �߻� ����
	 */
	public Marker getMark() {
		return mark;
	}

	// public String getMessage()
	// {
	// return super.getMessage()+" in tag mark="+mark;
	// }
}
