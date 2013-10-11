package net.ion.framework.mte.message;

import java.util.Map;

import net.ion.framework.mte.ErrorHandler;
import net.ion.framework.mte.token.Token;


public class InternalErrorHandler extends AbstractErrorHandler implements
		ErrorHandler {

	public void error(String messageKey, Token token,
			Map<String, Object> parameters) throws ParseException {
		logger.warning(String.format(
				"Internal error '%s' on '%s'(%d:%d) with parameters %s",
				messageKey, token.getText(), token.getLine(),
				token.getColumn(), parameters != null ? parameters.toString()
						: ""));
	}

}
