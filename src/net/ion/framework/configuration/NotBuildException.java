package net.ion.framework.configuration;

/**
 * ConfigurationFactory ���� build ���� ���� ���¿��� getConfiguration �Ҷ� �߻��Ǵ� ����
 * 
 * @author Choi sei hwan <a href="mailto:sehan@i-on.net">Choi sei hwan</a>
 * @version 1.0
 */

public class NotBuildException extends ConfigurationException {
	public NotBuildException() {
		super();
	}

	public NotBuildException(String message) {
		super(message);
	}

	public NotBuildException(Throwable t) {
		super(t);
	}

	public NotBuildException(String message, Throwable t) {
		super(message, t);
	}
}
