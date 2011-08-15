package net.ion.framework.xml.excetion;

public class XmlBindException extends XmlException {
	public XmlBindException() {
		super();
	}

	public XmlBindException(String message) {
		super(message);
	}

	public XmlBindException(Throwable t) {
		super(t);
	}

	public XmlBindException(String message, Throwable t) {
		super(message, t);
	}

}
