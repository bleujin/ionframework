package net.ion.framework.configuration;

public class ConfigurationRuntimeException extends RuntimeException {
	private static final long serialVersionUID = -6214304009550515783L;

	public ConfigurationRuntimeException() {
		super();
	}

	public ConfigurationRuntimeException(String message) {
		super(message);
	}

	public ConfigurationRuntimeException(Throwable t) {
		super(t);
	}

	public ConfigurationRuntimeException(String message, Throwable t) {
		super(message, t);
	}

}