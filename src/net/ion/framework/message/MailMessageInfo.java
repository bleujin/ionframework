package net.ion.framework.message;

import net.ion.framework.mail.MailException;

/**
 * Mail�� ������� ��Ű���� ��. �� �������̽��� �����Ѵ�. sendMail()�� MailMessageInfo�� ������ �ڵ带 �����Ѵ�..
 * 
 * @author bleujin
 * @version 1.0
 */

public interface MailMessageInfo {
	public void sendMail() throws MailException;
}
