package net.ion.framework.mte.message;

import java.util.Locale;
import java.util.logging.Logger;

import net.ion.framework.mte.ErrorHandler;
import net.ion.framework.mte.token.Token;


public abstract class AbstractErrorHandler implements ErrorHandler {
	protected final Logger logger = Logger
			.getLogger(getClass().getName());

	protected Locale locale = new Locale("en");

	public void error(String messageKey, Token token) throws ParseException {
		error(messageKey, token, null);
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}
}
