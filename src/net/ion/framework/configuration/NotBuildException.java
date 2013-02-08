package net.ion.framework.configuration;

/**
 * ConfigurationFactory 에서 build 하지 않은 상태에서 getConfiguration 할때 발생되는 에러
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
