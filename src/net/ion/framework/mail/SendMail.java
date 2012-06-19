package net.ion.framework.mail;

import java.io.File;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import net.ion.framework.logging.LogBroker;
import net.ion.framework.util.CharsetMapper;
import net.ion.framework.util.StringUtil;

public class SendMail {
	final String smtpHost;
	final int smtpPort;
	final String smtpUser;
	final String smtpPass;

	boolean hasSendingThread;
	final MailExceptionReceiver exceptionReceiver;
	final LinkedList<WaitingTicket> waitLine;

	private static int uniqueId = 0;

	static Logger log = LogBroker.getLogger(SendMail.class);

	public SendMail(String smtpHost, int smtpPort, final String smtpUser, final String smtpPass) {
		this(smtpHost, smtpPort, smtpUser, smtpPass, new NoneMailExceptionReceiver());
	}

	public SendMail(String smtpHost, int smtpPort, final String smtpUser, final String smtpPass, MailExceptionReceiver exceptionReceiver) {
		this.smtpHost = smtpHost;
		this.smtpPort = smtpPort;
		this.smtpUser = smtpUser;
		this.smtpPass = smtpPass;
		this.hasSendingThread = false;
		this.exceptionReceiver = exceptionReceiver;
		this.waitLine = new LinkedList<WaitingTicket>();
		// this.log = LogBroker.getLogger(this);
	}

	// ibr_12133
	private static synchronized String getMsgId(String from) {
		SendMail.uniqueId++;
		return "<" + System.currentTimeMillis() + "." + SendMail.uniqueId + "." + from + ">";
	}

	public void send(String from, String[] to, String[] cc, String[] bcc, String charset, String contentType, String subject, String text, File[] attachments) throws MailException {
		if (to == null || to.length == 0)
			return;
		boolean doAuth = (smtpPass == null) ? false : true;
		if (charset == null) {
			charset = CharsetMapper.getDefaultCharset(Locale.getDefault());
		}

		// Get system properties
		Properties props = new Properties();

		// Setup mail server
		props.put("mail.transport.protocol", "smtp");
		props.put("mail.smtp.host", smtpHost);
		props.put("mail.smtp.port", new Integer(smtpPort));
		props.put("mail.smtp.user", smtpUser);

		// Get session
		Session session = null;
		if (doAuth) {
			props.put("mail.smtp.auth", "true");
			Authenticator auth = new Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(smtpUser, smtpPass);
				}
			};
			session = Session.getInstance(props, auth);
		} else {
			session = Session.getInstance(props);
		}

		// Define message
		MimeMessage message = new UserMimeMessage(session, getMsgId(from));

		// set header
		try {
			message.setHeader("X-Mailer", "I-ON FX Mailer");
		} catch (MessagingException ex7) {
			throw new MailException("could not set the headers", ex7);
		}

		try {
			message.setFrom(new InternetAddress(from));
		} catch (MessagingException ex11) {
		}

		for (int i = 0, length = to.length; i < length; ++i) {
			try {
				message.addRecipient(Message.RecipientType.TO, new InternetAddress(to[i]));
			} catch (MessagingException ex8) {
			}
		}
		if (cc != null) {
			for (int i = 0, length = cc.length; i < length; ++i) {
				try {
					message.addRecipient(Message.RecipientType.CC, new InternetAddress(cc[i]));
				} catch (MessagingException ex9) {
				}
			}
		}
		if (bcc != null) {
			for (int i = 0, length = bcc.length; i < length; ++i) {
				try {
					message.addRecipient(Message.RecipientType.BCC, new InternetAddress(bcc[i]));
				} catch (MessagingException ex10) {
				}
			}
		}

		try {
			message.setSubject(subject, charset);
		} catch (IllegalStateException ex3) {
			throw new MailException("the message is obtained from a READ_ONLY folder.", ex3);
		} catch (MessagingException ex10) {
		}

		// Create the multi-part
		Multipart multipart = new MimeMultipart();

		// Create part one
		MimeBodyPart messageBodyPart = new MimeBodyPart();

		// Fill the message
		try {
			messageBodyPart.setText(text, charset);
			multipart.addBodyPart(messageBodyPart);
		} catch (MessagingException ex4) {
			throw new MailException("could not fill the message", ex4);
		}
		// attachment
		if (attachments != null) {
			try {
				DataSource source = null;
				for (int i = 0, length = attachments.length; i < length; ++i) {
					messageBodyPart = new MimeBodyPart();
					source = new FileDataSource(attachments[i]);

					messageBodyPart.setDataHandler(new DataHandler(source));
					messageBodyPart.setFileName(attachments[i].getName());

					multipart.addBodyPart(messageBodyPart);
				}
			} catch (MessagingException ex5) {
				throw new MailException("could not attach the files", ex5);
			}
		}

		try {
			// Put parts in message

			if (contentType == null || "".equals(contentType)) {
				message.setContent(multipart);
			} else {
				message.setContent(text, contentType); // HTML ���
				// message.setContent(text,"text/html; charset=euc-kr"); // HTML
			}

			// System.out.println(message.getEncoding());
			// System.out.println(message.getContentType());

			// Send message
			Transport.send(message);

			// Transport trans = session.getTransport("smtp");
			// trans.connect(smtpHost,smtpPort,smtpUser,smtpPass);
			// trans.send(message);
			// trans.close();
			log.log(Level.INFO, "From :" + from + " To : " + StringUtil.join(to, ',') + ", mail is sent");
		} catch (MessagingException ex6) {
			log.log(Level.WARNING, "From :" + from + " To : " + StringUtil.join(to, ',') + ", mail is not sent");
			throw new MailException("could not send the mail", ex6);
		}
	}

	public void sendWithSendMail(String from, String[] to, String[] cc, String[] bcc, String charset, String subject, String text, File[] attachments) throws MailException {
		SendMail sm = new SendMail(smtpHost, smtpPort, smtpUser, smtpPass, null);
		sm.send(from, to, cc, bcc, charset, null, subject, text, attachments);
		sm = null;
	}

	public void enqueueSend(String from, String to, String subject, String text) {
		enqueueSend(from, new String[] { to }, null, null, null, subject, text, null);
	}

	public void enqueueSend(String from, String[] to, String[] cc, String[] bcc, String charset, String subject, String text, File[] attachments) {
		WaitingTicket ticket = new WaitingTicket(from, to, cc, bcc, charset, subject, text, attachments);
		Thread sendingThread = null;
		synchronized (waitLine) {
			waitLine.addLast(ticket);
			// log.info("sending mail request enqueued, ticket="+ticket);
			if (!hasSendingThread) {
				sendingThread = new Thread(new SendingRunner(this));
				hasSendingThread = true;
			}
		}

		if (sendingThread != null) {
			sendingThread.setDaemon(true);
			sendingThread.start();
		}
	}

	public static void send(String smtpHost, int smtpPort, final String smtpUser, final String smtpPass, String from, String to, String charset, String subject, String text) throws MailException {
		SendMail.send(smtpHost, smtpPort, smtpUser, smtpPass, from, new String[] { to }, null, null, charset, subject, text, null);
	}

	public static void send(String smtpHost, int smtpPort, final String smtpUser, final String smtpPass, String from, String[] to, String[] cc, String[] bcc, String charset, String subject,
			String text, File[] attachments) throws MailException {
		SendMail sm = new SendMail(smtpHost, smtpPort, smtpUser, smtpPass, null);
		sm.send(from, to, cc, bcc, charset, null, subject, text, attachments);
		sm = null;
	}

}

class SendingRunner implements Runnable {
	private final SendMail owner;
	private final Logger log;

	protected SendingRunner(SendMail owner) {
		this.owner = owner;
		this.log = LogBroker.getLogger(this);
	}

	public void run() {
		for (;;) {
			try {
				WaitingTicket t = null;
				synchronized (owner.waitLine) {
					if (owner.waitLine.isEmpty())
						break;
					t = (WaitingTicket) owner.waitLine.removeFirst();
				}
				owner.send(t.from, t.to, t.cc, t.bcc, t.charset, null, t.subject, t.text, t.attachments);

			} catch (MailException me) {
				if (owner.exceptionReceiver != null)
					owner.exceptionReceiver.receive(me);

				log.warning(me.getMessage());
			} catch (Throwable t) {
				if (owner.exceptionReceiver != null)
					owner.exceptionReceiver.receive(new MailException("unexpected error", t));

				log.severe(t.getMessage());
			}
		}

		owner.hasSendingThread = false;
	}
}

class WaitingTicket {
	public final String from;
	public final String[] to;
	public final String[] cc;
	public final String[] bcc;
	public final String charset;
	public final String subject;
	public final String text;
	public final File[] attachments;

	public WaitingTicket(String from, String[] to, String[] cc, String[] bcc, String charset, String subject, String text, File[] attachments) {
		this.from = from;
		this.to = to;
		this.cc = cc;
		this.bcc = bcc;
		this.charset = charset;
		this.subject = subject;
		this.text = text;
		this.attachments = attachments;
	}
}