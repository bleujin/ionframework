package net.ion.framework.message;

import net.ion.framework.mail.MailException;

/**
 * MailMessageInfo를 감싼 Wrapper클래스. handle() 메소드는 메세지가 포함하고 있는 Mail작업을 실행시킨다.
 * 
 * @author bleujin
 * @version 1.0
 */

public class MailMessage extends Message {

	MailMessageInfo mailMessageInfo;

	public MailMessage(String name, MailMessageInfo mailMessageInfo) {
		super(name);
		this.mailMessageInfo = mailMessageInfo;
	}

	/**
	 * MailMessageInfo를 수행한다.
	 * 
	 * @throws MailException
	 */
	public void handle() throws MailException {
		mailMessageInfo.sendMail();
	}

	public String toString() {
		return "[ Mail Message from " + getName() + mailMessageInfo + " ]";
	}

}
