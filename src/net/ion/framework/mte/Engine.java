package net.ion.framework.mte;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import net.ion.framework.mte.message.SilentErrorHandler;
import net.ion.framework.mte.renderer.DefaultCollectionRenderer;
import net.ion.framework.mte.renderer.DefaultIterableRenderer;
import net.ion.framework.mte.renderer.DefaultMapRenderer;
import net.ion.framework.mte.renderer.DefaultObjectRenderer;
import net.ion.framework.mte.template.InterpretedTemplate;
import net.ion.framework.mte.template.Template;
import net.ion.framework.mte.token.IfToken;
import net.ion.framework.mte.token.Token;


public final class Engine  {

	public final static String VERSION = "@version@";

	public static Engine createCachingEngine() {
		Engine engine = new Engine(EngineConfig.newBuilder().enabledInterpretedTemplateCache(true).build());
		return engine;
	}

	public static Engine createNonCachingEngine() {
		Engine engine = new Engine(EngineConfig.newBuilder().enabledInterpretedTemplateCache(false).build());
		return engine;
	}

	public static Engine createCompilingEngine() {
		Engine engine = new Engine(EngineConfig.newBuilder().useCompilation(true).build());
		return engine;
	}

	public static Engine createDefaultEngine() {
		Engine engine = new Engine(EngineConfig.newBuilder().build());
		return engine;
	}

	private EngineConfig config;

	// compiled templates cache lives as long as this engine
	private final Map<String, Template> compiledTemplates = new HashMap<String, Template>();
	private final Map<String, Template> interpretedTemplates = new HashMap<String, Template>();

	/**
	 * Creates a new engine having <code>${</code> and <code>}</code> as start and end strings for expressions.
	 */
	Engine(EngineConfig config) {
		this.config = config;
		init();
	}

	private void init() {
		config.registerRenderer(Object.class, new DefaultObjectRenderer());
		config.registerRenderer(Map.class, new DefaultMapRenderer());
		config.registerRenderer(Collection.class, new DefaultCollectionRenderer());
		config.registerRenderer(Iterable.class, new DefaultIterableRenderer());
	}

	/**
	 * Checks if all given variables are there and if so, that they evaluate to true inside an if.
	 */
	public boolean variablesAvailable(Map<String, Object> model, String... vars) {
		final TemplateContext context = new TemplateContext(null, null, null, new ScopedMap(model), config.modelAdaptor(), this, new SilentErrorHandler(), null);
		for (String var : vars) {
			final IfToken token = new IfToken(var, false);
			if (!(Boolean) token.evaluate(context)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Transforms a template into an expanded output using the given model.
	 * 
	 * @param template
	 *            the template to expand
	 * @param locale
	 *            the locale being passed into renderers in {@link TemplateContext}
	 * @param sourceName
	 *            the name of the current template (if there is anything like that)
	 * @param model
	 *            the model used to evaluate expressions inside the template
	 * @return the expanded output
	 */
	public String transform(String template, Locale locale, String sourceName, Map<String, Object> model, ProcessListener processListener) {
		return transformInternal(template, locale, sourceName, model, config.modelAdaptor(), processListener);
	}

	public String transform(String template, String sourceName, Map<String, Object> model, ProcessListener processListener) {
		return transformInternal(template, sourceName, model, config.modelAdaptor(), processListener);
	}

	public String transform(String template, Locale locale, String sourceName, Map<String, Object> model) {
		return transformInternal(template, locale, sourceName, model, config.modelAdaptor(), null);
	}

	public String transform(String template, String sourceName, Map<String, Object> model) {
		return transformInternal(template, sourceName, model, config.modelAdaptor(), null);
	}

	public String transform(String template, Locale locale, Map<String, Object> model) {
		return transformInternal(template, locale, null, model, config.modelAdaptor(), null);
	}

	public String transform(String template, Map<String, Object> model) {
		return transformInternal(template, null, model, config.modelAdaptor(), null);
	}

	public String transform(String template, Map<String, Object> model, ProcessListener processListener) {
		return transformInternal(template, null, model, config.modelAdaptor(), processListener);
	}

	public String transform(String template, Locale locale, Map<String, Object> model, ProcessListener processListener) {
		return transformInternal(template, locale, null, model, config.modelAdaptor(), processListener);
	}

	String transformInternal(String template, String sourceName, Map<String, Object> model, ModelAdaptor modelAdaptor, ProcessListener processListener) {
		Locale locale = Locale.getDefault();
		return transformInternal(template, locale, sourceName, model, modelAdaptor, processListener);
	}

	String transformInternal(String template, Locale locale, String sourceName, Map<String, Object> model, ModelAdaptor modelAdaptor, ProcessListener processListener) {
		Template templateImpl = getTemplate(template, sourceName);
		String output = templateImpl.transform(model, locale, modelAdaptor, processListener);
		return output;
	}

	/**
	 * Replacement for {@link java.lang.String.format}. All arguments will be put into the model having their index starting from 1 as their name.
	 * 
	 * @param pattern
	 *            the template
	 * @param args
	 *            any number of arguments
	 * @return the expanded template
	 */
	public String format(final String pattern, final Object... args) {
		Map<String, Object> model = Collections.emptyMap();
		ModelAdaptor modelAdaptor = new ModelAdaptor() {

			public Object getValue(TemplateContext context, Token token, List<String> segments, String expression) {
				int index = Integer.parseInt(expression) - 1;
				return args[index];
			}

		};

		String output = transformInternal(pattern, null, model, modelAdaptor, null);
		return output;
	}

	/**
	 * Gets all variables used in the given template.
	 */
	public Set<String> getUsedVariables(String template) {
		Template templateImpl = getTemplate(template, null);
		return templateImpl.getUsedVariables();
	}
	
	
	/**
	 * Gets a template for a certain source.
	 * 
	 * @param templateSource
	 *            the template source
	 * @return the prepared template
	 */
	public Template getTemplate(String templateSource) {
		return getTemplate(templateSource, null);
	}

	/**
	 * Gets a template for a certain source.
	 * 
	 * @param templateSource
	 *            the template source
	 * @param sourceName
	 *            the template name
	 * @return the prepared template
	 */
	public Template getTemplate(String templateSource, String sourceName) {

		if (config.isUseCompilation()) {
			synchronized (compiledTemplates) {
				Template template = compiledTemplates.get(templateSource);
				if (template == null) {
					template = config.compiler().compile(templateSource, sourceName, this);
					compiledTemplates.put(templateSource, template);
				}
				return template;
			}
		}

		if (config.enabledInterpretedTemplateCache()) {
			synchronized (interpretedTemplates) {
				Template template = interpretedTemplates.get(templateSource);
				if (template == null) {
					template = new InterpretedTemplate(templateSource, sourceName, this);
					interpretedTemplates.put(templateSource, template);
				}
				return template;
			}
		}
		return new InterpretedTemplate(templateSource, sourceName, this);
	}

	public EngineConfig config() {
		return config;
	}

	public ErrorHandler errorHandler() {
		return config.errorHandler();
	}

}
