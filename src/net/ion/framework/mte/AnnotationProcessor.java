package net.ion.framework.mte;

import net.ion.framework.mte.token.AnnotationToken;

/**
 * Processor for an annotation, like ${@type String simple}.
 * 
 * @param <T>
 *            what the processor produces
 */
public interface AnnotationProcessor<T> {

	String getType();

	/**
	 * Processes the annotation and possible returns a value.
	 * 
	 * @param token
	 *            the annotation token triggering this processor
	 * 
	 * @param context
	 *            current context during template evaluation
	 * @return the produced value
	 */
	T eval(AnnotationToken token, TemplateContext context);

}
