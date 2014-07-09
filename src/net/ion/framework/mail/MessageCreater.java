package net.ion.framework.mail;

import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

public interface MessageCreater {
	public MimeMessage makeMessage(MimeMessage initMessage) throws MessagingException, UnsupportedEncodingException ;
}
