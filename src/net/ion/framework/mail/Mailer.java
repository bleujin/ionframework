package net.ion.framework.mail;

import java.util.Properties;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import javax.mail.FetchProfile;
import javax.mail.Flags;
import javax.mail.Flags.Flag;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.search.FlagTerm;

import net.ion.framework.util.WithinThreadExecutor;


public class Mailer {

    private MailConfig mconfig;
    private ExecutorService eservice;

    Mailer(MailConfig mconfig) {
        this.mconfig = mconfig;
        this.eservice = new WithinThreadExecutor() ;
    }


    public Mailer executors(ExecutorService eservice){
        this.eservice = eservice ;
        return this ;
    }

    public <T> Future<T> unreadMessage(final MessageHandler<T> messageHandler) {
        return eservice.submit(new Callable<T>() {
            public T call() throws Exception {
                Session session = Session.getDefaultInstance(mconfig.prop(), null);

                Store store = null ;
                Folder inbox = null ;
                try {
                    store = session.getStore(mconfig.recPortocol().stringValue());
                    store.connect(mconfig.recServerHost(), mconfig.recMailUserId(), mconfig.recMailUserPwd());

					/* Mention the folder name which you want to read. */
                    inbox = store.getFolder("Inbox");

					/* Open the inbox using store. */
                    inbox.open(Folder.READ_WRITE);

					/* Get the messages which is unread in the Inbox */
                    Message messages[] = inbox.search(new FlagTerm(new Flags(Flag.SEEN), false));
//                    Message messages[] = inbox.getMessages() ;

					/* Use a suitable FetchProfile */
                    FetchProfile fp = new FetchProfile();
                    fp.add(FetchProfile.Item.ENVELOPE);
                    fp.add(FetchProfile.Item.CONTENT_INFO);
                    inbox.fetch(messages, fp);

                    T result = messageHandler.handle(messages);
                    return result ;
                } finally {
                    if (inbox != null) try {inbox.close(true) ;} catch(MessagingException ignore){};
                    if (store != null) try {store.close();} catch(MessagingException ignore){};
                }
            }
        }) ;
    }

    public Future<Void> sendMail(final MessageCreater creater) {
        return eservice.submit(new Callable<Void>(){
            public Void call() throws Exception {
                Properties props = System.getProperties();
                props.setProperty("mail.smtp.host", mconfig.sendServerHost());
                props.setProperty("mail.user", mconfig.sendMailUserId()) ;
                props.setProperty("mail.password", mconfig.sendMailUserPwd()) ;

                Session session = Session.getDefaultInstance(props);

                MimeMessage initMessage = new MimeMessage(session);
                // Set From: header field of the header.
                initMessage.setFrom(new InternetAddress(mconfig.sendMailUserId())); // Sender's email ID needs to be mentioned

                MimeMessage makedMessage = creater.makeMessage(initMessage) ;
                Transport.send(makedMessage);
                return null;
            }

        }) ;
    }
}