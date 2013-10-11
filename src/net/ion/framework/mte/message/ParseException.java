package net.ion.framework.mte.message;


@SuppressWarnings("serial")
public class ParseException extends MessageException {
	public ParseException(Message message) {
		super(message);
	}
}
