package net.ion.framework.configuration;

/**
 * Configuration �� �߻��Ǵ� ��� ����
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
