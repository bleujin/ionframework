package net.ion.framework.template;

import net.ion.framework.template.parse.Marker;

/**
 * 예외가 발생되었을 때 보고서를 만들기 위한 정보 클래스
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
	 *            Template 예외가 발생한 템플릿
	 * @param cause
	 *            Throwable 예외 원인
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
	 * @return Template 예외가 발생한 템플릿
	 */
	public Template getTemplate() {
		return template;
	}

	/**
	 * @return Marker 예외 지점
	 */
	public Marker getMark() {
		return mark;
	}

	/**
	 * @return Throwable 예외 원인
	 */
	public Throwable getCause() {
		return cause;
	}
}
