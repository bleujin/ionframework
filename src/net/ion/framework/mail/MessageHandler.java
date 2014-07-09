package net.ion.framework.mail;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Date;

import javax.mail.Address;
import javax.mail.Flags.Flag;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;


public interface MessageHandler<T> {

	public T handle(Message[] msgs) throws Exception ;
	
	public static final MessageHandler<Void> PRINTER = new MessageHandler<Void>() {
		
		private PrintStream pout = System.out ;
		public Void handle(Message[] msgs) throws Exception {
			printAllMessages(msgs);
			return null;
		}

		private void printAllMessages(Message[] msgs) throws Exception {
			for (int i = 0; i < msgs.length; i++) {
				pout.println("MESSAGE #" + (i + 1) + ":");
				printEnvelope(msgs[i]);
				msgs[i].setFlag(Flag.SEEN, true);  
			}
		}

		/* Print the envelope(FromAddress,ReceivedDate,Subject) */
		private void printEnvelope(Message message) throws MessagingException, IOException {
			Address[] a;
			if ((a = message.getFrom()) != null) { // FROM
				for (int j = 0; j < a.length; j++) {
					pout.println("FROM: " + a[j].toString());
				}
			}
			if ((a = message.getRecipients(Message.RecipientType.TO)) != null) { // TO
				for (int j = 0; j < a.length; j++) {
					pout.println("TO: " + a[j].toString());
				}
			}
			pout.println("messageId :"+ message.getHeader("Message-ID")[0]) ;
			String subject = message.getSubject();
			Date receivedDate = message.getReceivedDate();
			String content = message.getContent().toString();
			pout.println("Subject : " + subject);
			pout.println("Received Date : " + receivedDate);
			pout.println("Content : " + content);
			getContent(message);
		}

		private void getContent(Message msg) throws MessagingException, IOException {
			String contentType = msg.getContentType();
			pout.println("Content Type : " + contentType);
			if (msg.getContent() instanceof String) {
				pout.println(msg.getContent());
			} else { // attached file
				Multipart mp = (Multipart) msg.getContent();
				int count = mp.getCount();
				for (int i = 0; i < count; i++) {
					dumpPart(mp.getBodyPart(i));
				}
			}
		}
		
		private void dumpPart(Part p) throws IOException, MessagingException {
			InputStream is = p.getInputStream();
			// If "is" is not already buffered, wrap a BufferedInputStream around it.
			if (!(is instanceof BufferedInputStream)) {
				is = new BufferedInputStream(is);
			}
			int c;
			pout.println("Message : ");
			while ((c = is.read()) != -1) {
				pout.write(c);
			}
		}

	} ;


	
}
