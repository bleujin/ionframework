package net.ion.framework.mail;

import java.io.File;

/**
 * 아무 것도 하지 않는 (메일을 보내지 않는) SendMail
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: I-ON Communications
 * </p>
 * 
 * @author Kim Sanghoon wizest@i-on.net
 * @version 1.0
 */
public class NoneSendMail extends SendMail {
	public NoneSendMail(String smtpHost, int smtpPort, final String smtpUser, final String smtpPass) {
		super(smtpHost, smtpPort, smtpUser, smtpPass);
	}

	public NoneSendMail(String smtpHost, int smtpPort, final String smtpUser, final String smtpPass, MailExceptionReceiver exceptionReceiver) {
		super(smtpHost, smtpPort, smtpUser, smtpPass, exceptionReceiver);
	}

	public void enqueueSend(String from, String[] to, String[] cc, String[] bcc, String charset, String subject, String text, File[] attachments) {
	}

	public void enqueueSend(String from, String to, String subject, String text) {
	}

	public void send(String from, String[] to, String[] cc, String[] bcc, String charset, String subject, String text, File[] attachments) throws MailException {
	}

	public void send(String from, String[] to, String[] cc, String[] bcc, String charset, String contentType, String subject, String text, File[] attachments)
			throws MailException {

	}

	public static void send(String smtpHost, int smtpPort, final String smtpUser, final String smtpPass, String from, String to, String charset,
			String subject, String text) throws MailException {
	}

	public static void send(String smtpHost, int smtpPort, final String smtpUser, final String smtpPass, String from, String[] to, String[] cc, String[] bcc,
			String charset, String subject, String text, File[] attachments) throws MailException {
	}
}
