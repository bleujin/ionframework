package net.ion.framework.mail;

import java.io.File;

import net.ion.framework.util.StringUtil;

/**
 * 실제 메일을 보내지 않고 console에 보여준다.
 */
public class OutSendMail extends SendMail {
	public OutSendMail(String smtpHost, int smtpPort, final String smtpUser, final String smtpPass) {
		super(smtpHost, smtpPort, smtpUser, smtpPass);
	}

	public OutSendMail(String smtpHost, int smtpPort, final String smtpUser, final String smtpPass, MailExceptionReceiver exceptionReceiver) {
		super(smtpHost, smtpPort, smtpUser, smtpPass, exceptionReceiver);
	}

	public void enqueueSend(String from, String[] to, String[] cc, String[] bcc, String charset, String contentType, String subject, String text,
			File[] attachments) {
		try {
			send(smtpHost, smtpPort, smtpUser, smtpPass, from, to, cc, bcc, charset, contentType, subject, text, attachments);
		} catch (MailException ex) {
			sendOut("This Exception Is Not Catched");
		}
	}

	public void enqueueSend(String from, String to, String subject, String text) {
		enqueueSend(from, StringUtil.split(to), null, null, null, null, subject, text, null);
	}

	public void send(String from, String[] to, String[] cc, String[] bcc, String charset, String contentType, String subject, String text, File[] attachments)
			throws MailException {

		send(smtpHost, smtpPort, smtpUser, smtpPass, from, to, cc, bcc, charset, contentType, subject, text, attachments);

	}

	public static void send(String smtpHost, int smtpPort, final String smtpUser, final String smtpPass, String from, String[] to, String[] cc, String[] bcc,
			String charset, String contentType, String subject, String text, File[] attachments) throws MailException {
		String tos = StringUtil.join(to, ',') + "\ncc : " + StringUtil.join(cc, ',') + "\nbcc : " + StringUtil.join(bcc, ',');

		String fileName = (attachments == null) ? "" : "Attachment :" + attachments.toString();
		send(smtpHost, smtpPort, smtpUser, smtpPass, from, tos, charset, contentType, subject, text + "\n" + fileName);
	}

	public static void send(String smtpHost, int smtpPort, final String smtpUser, final String smtpPass, String from, String to, String charset,
			String contentType, String subject, String text) throws MailException {
		StringBuffer info = new StringBuffer();

		info.append("=======================================\n");
		info.append("smtpHost : " + smtpHost + "\n");
		info.append("smtpPort : " + smtpPort + "\n");
		info.append("smtpUser : " + smtpUser + "\n");
		info.append("smtpPass : " + "************" + "\n");
		info.append("charset : " + charset + "\n");
		info.append("content-type : " + contentType + "\n");
		info.append("from : " + from + "\n");
		info.append("to : " + to + "\n");
		info.append("subject : " + subject + "\n");
		info.append("body : " + text + "\n");
		info.append("=======================================\n");

		// OutSendMail self = new OutSendMail(smtpHost, smtpPort, smtpUser, smtpPass, null);
		OutSendMail.sendOut(info.toString());
	}

	private static void sendOut(String info) {
		System.out.println(info);
	}
}
