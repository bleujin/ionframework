package net.ion.framework.mte.message;

import java.util.Map;

import net.ion.framework.mte.ErrorHandler;
import net.ion.framework.mte.token.Token;


public class DefaultErrorHandler extends AbstractErrorHandler implements ErrorHandler {
	/**
	 * {@inheritDoc}
	 */
	public void error(String messageKey, Token token,
			Map<String, Object> parameters) throws ParseException {
		Message message = new ResourceBundleMessage(messageKey).withModel(
				parameters).onToken(token);
		logger.severe(message.format(locale));
		throw new ParseException(message);
	}
}
