package net.ion.framework.mail;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Date;

import javax.mail.Address;
import javax.mail.Flags;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;

import net.ion.framework.util.ArrayUtil;
import net.ion.framework.util.ObjectUtil;

public class MessageWriteHandler implements MessageHandler<Integer> {
	
	private Writer writer;
	private int limit;
	private int skip;

	public MessageWriteHandler(PrintStream pstream, int offset, int skip){
		this(new PrintWriter(pstream), offset, skip) ;
	}

	public MessageWriteHandler(Writer writer, int offset, int skip){
		this.writer = writer ;
		this.limit = offset + skip - 1;
		this.skip = skip ;
	}
	
	public Integer handle(Message[] msgs) throws Exception {
		return printAllMessages(msgs);
	}

	private MessageWriteHandler println(String msg) throws IOException{
		writer.write(msg + "\n");
		return this ;
	}
	
	private Integer printAllMessages(Message[] msgs) throws Exception {
		
		ArrayUtil.reverse(msgs);
		
		for (int i = 0; i < msgs.length; i++) {
			if (skip-- > 0) continue ;
			if (limit < i) return msgs.length;
			println("MESSAGE #" + (i + 1) + ":");
			printEnvelope(msgs[i]);
		}
		return msgs.length ;
	}

	/* Print the envelope(FromAddress,ReceivedDate,Subject) */
	private void printEnvelope(Message message) throws MessagingException, IOException {
		Address[] a;
		if ((a = message.getFrom()) != null) { // FROM
			for (int j = 0; j < a.length; j++) {
				println("FROM: " + a[j].toString());
			}
		}
		if ((a = message.getRecipients(Message.RecipientType.TO)) != null) { // TO
			for (int j = 0; j < a.length; j++) {
				println("TO: " + a[j].toString());
			}
		}
		
		String subject = message.getSubject();
		Date receivedDate = message.getReceivedDate();
		println("Subject : " + subject);
		println("Received Date : " + receivedDate);
		printContent(message);
	}

	private void printContent(Message msg) throws MessagingException, IOException {
		String contentType = msg.getContentType();
		println("Content Type : " + contentType);
		println("Content : ");
		if (msg.getContent() instanceof String) {
			println(ObjectUtil.toString(msg.getContent()));
		} else { // attached file
			Multipart mp = (Multipart) msg.getContent();
			int count = mp.getCount();
			for (int i = 0; i < count; i++) {
				dumpPart(mp.getBodyPart(i));
			}
		}
	}
	
	private void dumpPart(Part p) throws IOException, MessagingException {
		InputStream is = new BufferedInputStream(p.getInputStream());
		int c;
		if (p.getContentType().startsWith("text/")) {
			println("Message : ");
			while ((c = is.read()) != -1) {
				writer.write(c);
			}
		} else {
			println("Message : " + p.getContent());
		}
	}

}
