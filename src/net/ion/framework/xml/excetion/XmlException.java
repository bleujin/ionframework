package net.ion.framework.xml.excetion;

public class XmlException extends Exception {
	public XmlException() {
		super();
	}

	public XmlException(String message) {
		super(message);
	}

	public XmlException(Throwable t) {
		super(t);
	}

	public XmlException(String message, Throwable t) {
		super(message, t);
	}

}
