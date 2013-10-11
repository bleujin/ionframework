package net.ion.framework.mte.token;

import net.ion.framework.mte.AnnotationProcessor;
import net.ion.framework.mte.TemplateContext;

public class AnnotationToken extends AbstractToken {
	private final String receiver;
	private final String arguments;

	public AnnotationToken(String receiver, String arguments) {
		super();
		this.receiver = receiver;
		this.arguments = arguments;
	}

	public String getReceiver() {
		return receiver;
	}

	public String getArguments() {
		return arguments;
	}

	public Object evaluate(TemplateContext context) {
		AnnotationProcessor<?> annotationProcessor = context
				.resolveAnnotationProcessor(receiver);
		if (annotationProcessor != null) {
			Object value = annotationProcessor.eval(this, context);
			if (value != null) {
				return value;
			}
		}
		return "";
	}
}
