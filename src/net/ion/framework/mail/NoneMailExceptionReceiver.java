package net.ion.framework.mail;

import java.util.logging.Level;
import java.util.logging.Logger;

import net.ion.framework.logging.LogBroker;

public class NoneMailExceptionReceiver implements MailExceptionReceiver {
	public NoneMailExceptionReceiver() {
	}

	public void receive(MailException ex) {
		Logger log = LogBroker.getLogger(this);
		log.log(Level.INFO, "Send Mail Failed");
	}
}
