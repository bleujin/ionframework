package net.ion.framework.template;

import net.ion.framework.template.parse.Marker;

/**
 * ���ܰ� �߻��Ǿ��� �� ������ ����� ���� ���� Ŭ����
 * 
 * @author Kim, Sanghoon (wizest@i-on.net)
 * @version 1.0
 */
class ExceptionCause {
	private Template template;
	private Marker mark;
	private Throwable cause;

	/**
	 * @param template
	 *            Template ���ܰ� �߻��� ���ø�
	 * @param cause
	 *            Throwable ���� ����
	 */
	public ExceptionCause(Template template, Throwable cause) {
		this.template = template;
		this.cause = cause;

		if (cause instanceof CompilerException)
			this.mark = ((CompilerException) cause).getMark();
		else if (cause instanceof TemplateRuntimeException)
			this.mark = ((TemplateRuntimeException) cause).getMark();
	}

	/**
	 * @return Template ���ܰ� �߻��� ���ø�
	 */
	public Template getTemplate() {
		return template;
	}

	/**
	 * @return Marker ���� ����
	 */
	public Marker getMark() {
		return mark;
	}

	/**
	 * @return Throwable ���� ����
	 */
	public Throwable getCause() {
		return cause;
	}
}
