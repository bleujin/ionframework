package net.ion.framework.message;

import net.ion.framework.mail.MailException;

/**
 * Mail로 어떠한일을 시키려할 때. 이 인터페이스를 구현한다. sendMail()는 MailMessageInfo가 수행할 코드를 구현한다..
 * 
 * @author bleujin
 * @version 1.0
 */

public interface MailMessageInfo {
	public void sendMail() throws MailException;
}
