package net.ion.framework.mail;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;

/**
 * SendMail이 메일을 보낼 때 message-ID를 지정할 수 있게 사용하는 message 클래스<br/>
 * 받는 메일 서버가 message-ID에 따라 받는 스펨분류를 할 수 있으므로 발송시 적당히 message-ID를 지정해주어야한다. (자바메일 api의 기본 값으로는 일반적으로 스펨처리된다.)
 * 
 * @author Kim, Sanghoon (wizest@i-on.net)
 * @version 1.0
 */

class UserMimeMessage extends MimeMessage {
	private String msgId;

	public UserMimeMessage(Session session, String msgId) {
		super(session);
		this.msgId = msgId;
	}

	protected void updateHeaders() throws MessagingException {
		super.updateHeaders();
		setHeader("Message-ID", msgId);
	}
}
