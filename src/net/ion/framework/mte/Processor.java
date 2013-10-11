package net.ion.framework.mte;

/**
 * Dynamic element to be placed into model.
 * 
 * @param <T>
 *            what the processor produces
 */
public interface Processor<T> {

	/**
	 * Produces a value based on the given context.
	 * 
	 * @param context
	 *            current context during template evaluation
	 * @return the produced value
	 */
	T eval(TemplateContext context);

}
