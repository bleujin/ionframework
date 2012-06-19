package net.ion.framework.mail;

/**
 * <p>
 * Title: EV
 * </p>
 * <p>
 * Description: I-ON CMS 4
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: I-ON Communications
 * </p>
 * 
 * @author Kim, Sanghoon (wizest@i-on.net)
 * @version 1.0
 */

public interface MailExceptionReceiver {
	void receive(MailException ex);
}