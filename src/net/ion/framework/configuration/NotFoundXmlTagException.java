package net.ion.framework.configuration;

/**
 * Configuration 을 얻어올때 해당 xml tag 가 존재 하지 않을때 발생되는 에러
 * 
 * @author Choi sei hwan <a href="mailto:sehan@i-on.net">Choi sei hwan</a>
 * @version 1.0
 */

public class NotFoundXmlTagException extends ConfigurationException {
	public NotFoundXmlTagException() {
		super();
	}

	public NotFoundXmlTagException(String message) {
		super("Not Found XML tag : " + message);
	}

	public NotFoundXmlTagException(Throwable t) {
		super(t);
	}

	public NotFoundXmlTagException(String message, Throwable t) {
		super("Not Found XML tag : " + message, t);
	}
}
