package net.ion.framework.configuration;

/**
 * Configuration 시 발생되는 모든 에러
 * 
 * @author Choi sei hwan <a href="mailto:sehan@i-on.net">Choi sei hwan</a>
 * @version 1.0
 */

public class ConfigurationException extends Exception {
	public ConfigurationException() {
		super();
	}

	public ConfigurationException(String message) {
		super(message);
	}

	public ConfigurationException(Throwable t) {
		super(t);
	}

	public ConfigurationException(String message, Throwable t) {
		super(message, t);
	}

}
