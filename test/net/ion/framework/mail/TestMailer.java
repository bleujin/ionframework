package net.ion.framework.mail;

import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;
import java.util.Date;
import java.util.concurrent.Executors;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Flags.Flag;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import junit.framework.TestCase;
import net.ion.framework.mail.ReceiveConfigBuilder.Protocol;
import net.ion.framework.util.Debug;
import net.ion.framework.util.StringUtil;

public class TestMailer extends TestCase {

    private String userPwd = StringUtil.defaultIfEmpty(System.getProperty("mail.password"), "password");

    public void xtestSendMail() throws Exception {
        Mailer mailer = MailConfigBuilder.create().sendConfig().server("smtp.i-on.net").mailUserId("bleujin@i-on.net").mailUserPwd("bleujin7").buildConfig().confirmValidOfSendMailConfig().createMailer();

        mailer.sendMail(new MessageCreater() {
            public MimeMessage makeMessage(MimeMessage msg) throws MessagingException, UnsupportedEncodingException {
                msg.addRecipient(Message.RecipientType.TO, new InternetAddress("bleujin@i-on.net"));
                msg.setSubject(MimeUtility.encodeText("한글 제목!", "UTF-8", "B"));
                msg.setContentLanguage(new String[]{"UTF-8"});
                msg.setSentDate(new Date());
                msg.setHeader("Content-Type", "text/html; charset=UTF-8");
                msg.setContent("<h1>한글 내용2</h1>", "text/html; charset=UTF-8");
                return msg;
            }
        }).get();
    }

    public void xtestReceiveMail() throws Exception {
        Mailer mailer = MailConfigBuilder.create().receiveConfig().server("smtp.i-on.net").mailUserId("bleujin@i-on.net").mailUserPwd(userPwd).protocol(Protocol.POP3).buildConfig().confirmValidOfReceiveMailConfig().createMailer();

        mailer.unreadMessage(MessageHandler.PRINTER);
        mailer.unreadMessage(MessageHandler.PRINTER);
    }

    public void testDate() throws Exception {
		Debug.line(new Date(1403759916210L)) ;
	}
    
    public void xtestMessageWriteHandler() {
        Mailer mailer = MailConfigBuilder.create().receiveConfig().server("smtp.i-on.net").mailUserId("bleujin@i-on.net").mailUserPwd(userPwd).protocol(Protocol.POP3).buildConfig().confirmValidOfReceiveMailConfig().createMailer();

        StringWriter sw = new StringWriter();
		try {
			mailer.unreadMessage(new MessageWriteHandler(sw, 3, 0)).get();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Debug.line(sw.toString());
    }
    
    public void testAttachFile() throws Exception {
        Mailer mailer = MailConfigBuilder.create().sendConfig().server("smtp.i-on.net").mailUserId("bleujin@i-on.net").mailUserPwd(userPwd).buildConfig().confirmValidOfSendMailConfig().createMailer();

        mailer.sendMail(new MessageCreater() {
            public MimeMessage makeMessage(MimeMessage initMsg) throws MessagingException {
                initMsg.addRecipient(Message.RecipientType.TO, new InternetAddress("bleujin@i-on.net"));
                initMsg.setSubject("This is the Subject Line!");

                Multipart multipart = new MimeMultipart(); // Create a multipart message

                BodyPart textPart = new MimeBodyPart();
                textPart.setContent("<h1>This is attached message</h1>", "text/html"); // Fill the message
                multipart.addBodyPart(textPart); // Set text message part

                // Part two is attachment
                MimeBodyPart filePart = new MimeBodyPart();
                filePart.setDataHandler(new DataHandler(new FileDataSource("./README.md")));
                filePart.setFileName("file.txt");
                multipart.addBodyPart(filePart);

                initMsg.setContent(multipart);
                return initMsg;
            }
        });
    }

    public void testAsync() throws Exception {
        Mailer mailer = MailConfigBuilder.create().sendConfig().server("smtp.i-on.net").mailUserId("bleujin@i-on.net").mailUserPwd(userPwd).buildConfig().confirmValidOfSendMailConfig().createMailer().executors(Executors.newSingleThreadExecutor());

        mailer.sendMail(new MessageCreater() {
            public MimeMessage makeMessage(MimeMessage msg) throws MessagingException {
                msg.addRecipient(Message.RecipientType.TO, new InternetAddress("bleujin@i-on.net"));
                msg.setSubject("This is the Subject Line!");
                msg.setContent("<h1>This is actual message</h1>", "text/html");
                return msg;
            }
        }).get();
    }

    public void xtestMessageFormat() throws Exception {
        Debug.line(MessageFormat.format("Hello {0}, and bye {1}{0}", "bleujin", 3));

    }

    public void xtestIfIMap() throws Exception {
        Mailer mailer = MailConfigBuilder.create().sendConfig().server("smtp.i-on.net").mailUserId("bleujin@i-on.net")
                .mailUserPwd(userPwd).receiveConfig().server("smtp.i-on.net").mailUserId("bleujin@i-on.net").mailUserPwd(userPwd).protocol(Protocol.IMAP).buildConfig()
                .confirmValidOfSendMailConfig().confirmValidOfReceiveMailConfig().createMailer();

        Integer readCount = mailer.unreadMessage(new MessageHandler<Integer>() {
            public Integer handle(Message[] msgs) throws Exception {
                for (Message msg : msgs) {
                    msg.setFlag(Flag.SEEN, true);
                    msg.saveChanges();
                }

                return msgs.length;
            }
        }).get() ;

        assertEquals(1, readCount.intValue());
    }




}
