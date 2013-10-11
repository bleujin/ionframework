package net.ion.framework.mte.message;

@SuppressWarnings("serial")
public class MessageException extends RuntimeException {
	private final Message message;

	public MessageException(Message message) {
		this.message = message;
	}
	
	@Override
	public String getMessage() {
		return message.format();
	}
}
