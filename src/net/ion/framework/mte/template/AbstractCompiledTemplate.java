package net.ion.framework.mte.template;

import java.util.Locale;
import java.util.Map;
import java.util.Set;

import net.ion.framework.mte.Engine;
import net.ion.framework.mte.ModelAdaptor;
import net.ion.framework.mte.ProcessListener;
import net.ion.framework.mte.ScopedMap;
import net.ion.framework.mte.TemplateContext;
import net.ion.framework.mte.util.Util;

public abstract class AbstractCompiledTemplate extends AbstractTemplate {

	public AbstractCompiledTemplate() {
	}

	public AbstractCompiledTemplate(Engine engine) {
		this.engine = engine;
	}

	@Override
	public Set<String> getUsedVariables() {
		return usedVariables;
	}

	@Override
	public synchronized String transform(Map<String, Object> model, Locale locale, ModelAdaptor modelAdaptor, ProcessListener processListener) {
		TemplateContext context = new TemplateContext(template, locale, sourceName, new ScopedMap(model), modelAdaptor, engine, engine.errorHandler(), processListener);

		String transformed = transformCompiled(context);
		return transformed;

	}

	protected abstract String transformCompiled(TemplateContext context);

	public void setEngine(Engine engine) {
		this.engine = engine;
	}

	public Engine getEngine() {
		return engine;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

	public String getTemplate() {
		return template;
	}

	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}

	public String getSourceName() {
		return sourceName;
	}

	@Override
	public String toString() {
		return template;
	}

}
