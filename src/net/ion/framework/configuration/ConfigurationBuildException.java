package net.ion.framework.configuration;

/**
 * ConfigurationFactory ���� build �� �߻��Ǵ� ����
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
