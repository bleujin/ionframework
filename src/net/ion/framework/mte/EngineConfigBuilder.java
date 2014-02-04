package net.ion.framework.mte;

import net.ion.framework.mte.encoder.Encoder;
import net.ion.framework.mte.message.DefaultErrorHandler;
import net.ion.framework.mte.template.DynamicBytecodeCompiler;
import net.ion.framework.mte.template.TemplateCompiler;

public class EngineConfigBuilder {

	private String startToken = "${";
	private String endToken = "}";
	private double expansionSizeFactor = 2;
	private ErrorHandler errorHandler = new DefaultErrorHandler();
	private boolean useCompilation = false;
	private boolean enabledInterpretedTemplateCache = true;
	private ModelAdaptor modelAdaptor = new DefaultModelAdaptor();
	private Encoder encoder = null;

	// compiler plus all compiled classes live as long as this engine
	private TemplateCompiler compiler = new DynamicBytecodeCompiler();
	
	
	
	
	
	
	public EngineConfig build(){
		return new EngineConfig(this.startToken, this.endToken, this.expansionSizeFactor, this.errorHandler, this.useCompilation, this.enabledInterpretedTemplateCache, this.modelAdaptor, this.encoder, this.compiler) ;
	}
	
	public Engine createEngine() {
		return new Engine(build());
	}

	
	public EngineConfigBuilder exprStartToken(String startToken) {
		this.startToken = startToken;
		return this;
	}

	public EngineConfigBuilder exprEndToken(String endToken) {
		this.endToken = endToken;
		return this;
	}

	public EngineConfigBuilder expansionSizeFactor(double expansionSizeFactor) {
		this.expansionSizeFactor = expansionSizeFactor;
		return this;
	}

	public EngineConfigBuilder compiler(TemplateCompiler compiler) {
		this.compiler = compiler;
		return this;
	}

	public EngineConfigBuilder useCompilation(boolean useCompilation) {
		this.useCompilation = useCompilation;
		return this;
	}

	public EngineConfigBuilder modelAdaptor(ModelAdaptor modelAdaptor) {
		this.modelAdaptor = modelAdaptor;
		return this;
	}

	public EngineConfigBuilder encoder(Encoder encoder) {
		this.encoder = encoder;
		return this;
	}

	public EngineConfigBuilder errorHandler(ErrorHandler errorHandler) {
		this.errorHandler = errorHandler;
		return this;
	}

	public EngineConfigBuilder enabledInterpretedTemplateCache(boolean enabledInterpretedTemplateCache) {
		this.enabledInterpretedTemplateCache = enabledInterpretedTemplateCache;
		return this;
	}


}
