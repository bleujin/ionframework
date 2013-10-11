package net.ion.framework.mte;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.ion.framework.mte.encoder.Encoder;
import net.ion.framework.mte.template.Template;
import net.ion.framework.mte.template.TemplateCompiler;
import net.ion.framework.mte.util.Util;

public class EngineConfig implements RendererRegistry{

	private String startToken ;
	private String endToken ;
	private double expansionSizeFactor ;
	private ErrorHandler errorHandler ;
	private boolean useCompilation ;
	private boolean useInterpretedTemplateCache ;
	private ModelAdaptor modelAdaptor ;
	private Encoder encoder;

	// compiler plus all compiled classes live as long as this engine
	private TemplateCompiler compiler ;

	
	
	
	private final Map<Class<?>, Renderer<?>> renderers = new HashMap<Class<?>, Renderer<?>>();
	private final Map<Class<?>, Renderer<?>> resolvedRendererCache = new HashMap<Class<?>, Renderer<?>>();

	private final Map<String, AnnotationProcessor<?>> annotationProcessors = new HashMap<String, AnnotationProcessor<?>>();

	private final Map<String, NamedRenderer> namedRenderers = new HashMap<String, NamedRenderer>();
	private final Map<Class<?>, Set<NamedRenderer>> namedRenderersForClass = new HashMap<Class<?>, Set<NamedRenderer>>();
	
	EngineConfig(String startToken, String endToken, double expansionSizeFactor, ErrorHandler errorHandler, boolean useCompilation, boolean useInterpretedTemplateCache, 
			ModelAdaptor modelAdaptor, Encoder encoder, TemplateCompiler compiler) {
		this.startToken = startToken ;
		this.endToken = endToken ;
		this.expansionSizeFactor = expansionSizeFactor ;
		this.errorHandler = errorHandler ;
		this.useCompilation = useCompilation ;
		this.useInterpretedTemplateCache = useInterpretedTemplateCache ;
		this.modelAdaptor = modelAdaptor ;
		this.encoder = encoder ;
		this.compiler = compiler ;
	}
	
	
	public static EngineConfigBuilder newBuilder(){
		return new EngineConfigBuilder() ;
	}
	

	
	

	public Encoder encoder() {
		return encoder;
	}

	public ErrorHandler errorHandler() {
		return errorHandler;
	}

	public String exprStartToken() {
		return startToken;
	}

	public String exprEndToken() {
		return endToken;
	}

	public double expansionSizeFactor() {
		return expansionSizeFactor;
	}

	public boolean isUseCompilation() {
		return useCompilation;
	}

	public ModelAdaptor modelAdaptor() {
		return modelAdaptor;
	}

	public boolean enabledInterpretedTemplateCache() {
		return useInterpretedTemplateCache;
	}

	public TemplateCompiler compiler() {
		return compiler;
	}

	
	
	
	
	
	


	public synchronized EngineConfig registerNamedRenderer(NamedRenderer renderer) {
		namedRenderers.put(renderer.getName(), renderer);
		Set<Class<?>> supportedClasses = Util.asSet(renderer.getSupportedClasses());
		for (Class<?> clazz : supportedClasses) {
			Class<?> classInHierarchy = clazz;
			while (classInHierarchy != null) {
				addSupportedRenderer(classInHierarchy, renderer);
				classInHierarchy = classInHierarchy.getSuperclass();
			}
		}
		return this;
	}

	public synchronized EngineConfig deregisterNamedRenderer(NamedRenderer renderer) {
		namedRenderers.remove(renderer.getName());
		Set<Class<?>> supportedClasses = Util.asSet(renderer.getSupportedClasses());
		for (Class<?> clazz : supportedClasses) {
			Class<?> classInHierarchy = clazz;
			while (classInHierarchy != null) {
				Set<NamedRenderer> renderers = namedRenderersForClass.get(classInHierarchy);
				renderers.remove(renderer);
				classInHierarchy = classInHierarchy.getSuperclass();
			}
		}
		return this;
	}

	private void addSupportedRenderer(Class<?> clazz, NamedRenderer renderer) {
		Collection<NamedRenderer> compatibleRenderers = getCompatibleRenderers(clazz);
		compatibleRenderers.add(renderer);
	}

	public synchronized Collection<NamedRenderer> getCompatibleRenderers(Class<?> inputType) {
		Set<NamedRenderer> renderers = namedRenderersForClass.get(inputType);
		if (renderers == null) {
			renderers = new HashSet<NamedRenderer>();
			namedRenderersForClass.put(inputType, renderers);
		}
		return renderers;
	}

	
	
	public synchronized EngineConfig registerAnnotationProcessor(AnnotationProcessor<?> annotationProcessor) {
		annotationProcessors.put(annotationProcessor.getType(), annotationProcessor);
		return this;
	}

	public synchronized EngineConfig deregisterAnnotationProcessor(AnnotationProcessor<?> annotationProcessor) {
		annotationProcessors.remove(annotationProcessor.getType());
		return this;
	}

	public synchronized <C> EngineConfig registerRenderer(Class<C> clazz, Renderer<C> renderer) {
		renderers.put(clazz, renderer);
		resolvedRendererCache.clear();
		return this;
	}

	public synchronized EngineConfig deregisterRenderer(Class<?> clazz) {
		renderers.remove(clazz);
		resolvedRendererCache.clear();
		return this;
	}
	
	
	
	
	
	public Collection<NamedRenderer> getAllNamedRenderers() {
		Collection<NamedRenderer> values = namedRenderers.values();
		return values;
	}

	public NamedRenderer resolveNamedRenderer(String rendererName) {
		return namedRenderers.get(rendererName);
	}

	AnnotationProcessor<?> resolveAnnotationProcessor(String type) {
		return annotationProcessors.get(type);
	}
	
	@SuppressWarnings( { "unchecked", "rawtypes" })
	public <C> Renderer<C> resolveRendererForClass(Class<C> clazz) {
		Renderer resolvedRenderer = resolvedRendererCache.get(clazz);
		if (resolvedRenderer != null) {
			return resolvedRenderer;
		}

		resolvedRenderer = renderers.get(clazz);
		if (resolvedRenderer == null) {
			Class<?>[] interfaces = clazz.getInterfaces();
			for (Class<?> interfaze : interfaces) {
				resolvedRenderer = resolveRendererForClass(interfaze);
				if (resolvedRenderer != null) {
					break;
				}
			}
		}
		if (resolvedRenderer == null) {
			Class<?> superclass = clazz.getSuperclass();
			if (superclass != null) {
				resolvedRenderer = resolveRendererForClass(superclass);
			}
		}
		if (resolvedRenderer != null) {
			resolvedRendererCache.put(clazz, resolvedRenderer);
		}
		return resolvedRenderer;
	}

	
	
	
	
}
