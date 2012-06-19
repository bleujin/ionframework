package net.ion.framework.configuration;

/**
 * ConfigurationFactory 에서 build 시 발생되는 에러
 * 
 * @author Choi sei hwan <a href="mailto:sehan@i-on.net">Choi sei hwan</a>
 * @version 1.0
 */

public class ConfigurationBuildException extends ConfigurationException {
	public ConfigurationBuildException() {
		super();
	}

	public ConfigurationBuildException(String message) {
		super(message);
	}

	public ConfigurationBuildException(Throwable t) {
		super(t);
	}

	public ConfigurationBuildException(String message, Throwable t) {
		super(message, t);
	}

}
