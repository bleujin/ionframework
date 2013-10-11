package net.ion.framework.mte.template;

import net.ion.framework.mte.Engine;

/**
 * Compiles template source to an executable template.
 * 
 */
public interface TemplateCompiler {
	public Template compile(String template, String sourceName, Engine engine);
}
