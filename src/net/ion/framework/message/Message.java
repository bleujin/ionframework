package net.ion.framework.message;

/**
 * ������ ���� ó���ϴ� ����. handle() �޼ҵ�� �޼����� ó���ϴ� �ڵ带 �����ؾ��Ѵ�.
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
	 * �޼����� ó���Ѵ�.
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
