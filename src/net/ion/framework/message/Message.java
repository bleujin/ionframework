package net.ion.framework.message;

/**
 * 임의의 일을 처리하는 단위. handle() 메소드는 메세지를 처리하는 코드를 구현해야한다.
 * 
 * @author bleujin
 * @version 1.0
 */

public abstract class Message {
	private final String name;

	public Message() {
		this.name = "None";
	}

	public Message(String name) {
		this.name = name;
	}

	/**
	 * 메세지를 처리한다.
	 * 
	 * @throws Exception
	 */
	public abstract void handle() throws Exception;

	public String getName() {
		return name;
	}

	public String toString() {
		return "[ Message from " + getName() + " ]";
	}
}
