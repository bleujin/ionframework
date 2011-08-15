package net.ion.framework.message;

import net.ion.framework.mail.MailException;

/**
 * MailMessageInfo�� ���� WrapperŬ����. handle() �޼ҵ�� �޼����� �����ϰ� �ִ� Mail�۾��� �����Ų��.
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
	 * MailMessageInfo�� �����Ѵ�.
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
