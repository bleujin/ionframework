package net.ion.framework.mte.token;

import net.ion.framework.mte.TemplateContext;

public class InvalidToken extends AbstractToken {
	public Object evaluate(TemplateContext context) {
		context.engine.errorHandler().error("invalid-expression", this);
		return "";
	}
}
