package net.ion.framework.mte.message;

import java.util.Map;

import net.ion.framework.mte.ErrorHandler;
import net.ion.framework.mte.token.Token;


public class NoLogErrorHandler extends AbstractErrorHandler implements ErrorHandler {

	public void error(String messageKey, Token token, Map<String, Object> parameters) throws ParseException {
		// Silent by design
		Message message = new ResourceBundleMessage(messageKey).withModel(parameters).onToken(token);
		throw new ParseException(message);
	}
}
