package net.ion.framework.mte.message;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import net.ion.framework.mte.Engine;
import net.ion.framework.mte.EngineConfig;
import net.ion.framework.mte.token.Token;

public class ResourceBundleMessage implements Message {

	protected static String getTemplate(ResourceBundle bundle, String key, String fallback) {
		return key != null && (bundle.getObject(key) != null) ? bundle.getString(key) : fallback;
	}

	private final String messageCode;

	public ResourceBundleMessage(String messageCode) {
		this.messageCode = messageCode;
	}

	private String locationCode = "location";
	private String prefixCode = "prefix";
	private String frameCode = "frame";
	private Map<String, Object> argumentModel = new HashMap<String, Object>();
	private String baseName = "net.ion.framework.mte.message.messages";

	public ResourceBundleMessage useLocationCode(String locationCode) {
		this.locationCode = locationCode;
		return this;
	}

	public ResourceBundleMessage onToken(Token token) {
		this.argumentModel.put("token", token);
		return this;
	}

	public ResourceBundleMessage usePrefixCode(String prefixCode) {
		this.prefixCode = prefixCode;
		return this;
	}

	public ResourceBundleMessage useFrameCode(String frameCode) {
		this.frameCode = frameCode;
		return this;
	}

	public ResourceBundleMessage withModel(Map<String, Object> model) {
		if (model != null) {
			this.argumentModel.putAll(model);
		}
		return this;
	}

	public ResourceBundleMessage withBaseName(String baseName) {
		this.baseName = baseName;
		return this;
	}

	public String getMessageCode() {
		return messageCode;
	}

	public String getLocationCode() {
		return locationCode;
	}

	public String getPrefixCode() {
		return prefixCode;
	}

	public String getFrameCode() {
		return frameCode;
	}

	public Map<String, Object> getArgumentModel() {
		return argumentModel;
	}

	public String getBaseName() {
		return baseName;
	}

	public String format() {
		return format(new Locale("en"));
	}

	public String format(Locale locale) {
		final ResourceBundle messages = ResourceBundle.getBundle(baseName, locale);
		final String frameTemplate = getTemplate(messages, frameCode, "${prefix} ${location}: ${message}");
		final String prefixTemplate = getTemplate(messages, prefixCode, "");
		final String locationTemplate = getTemplate(messages, locationCode, "");
		final String messageTemplate = getTemplate(messages, messageCode, "");

		Engine engine = EngineConfig.newBuilder().errorHandler(new InternalErrorHandler()).createEngine() ;
		argumentModel.put("prefix", engine.transform(prefixTemplate, argumentModel));
		argumentModel.put("location", engine.transform(locationTemplate, argumentModel));
		argumentModel.put("message", engine.transform(messageTemplate, argumentModel));

		String transformed = engine.transform(frameTemplate, argumentModel);
		return transformed;

	}

	public String formatPlain() {
		return formatPlain(new Locale("en"));
	}

	public String formatPlain(Locale locale) {
		final ResourceBundle messages = ResourceBundle.getBundle(baseName, locale);
		final String messageTemplate = getTemplate(messages, messageCode, "");

		Engine engine = EngineConfig.newBuilder().errorHandler(new InternalErrorHandler()).createEngine() ;
		String transformed = engine.transform(messageTemplate, argumentModel);
		return transformed;
	}
}
