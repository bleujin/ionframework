package net.ion.framework.mte.token;

import net.ion.framework.mte.TemplateContext;


public class ElseToken extends AbstractToken {
	public static final String ELSE = "else";

	protected IfToken ifToken = null;

	@Override
	public String getText() {
		if (text == null) {
			text = ELSE + getIfToken() != null ? "(" + getIfToken().getText()
					+ ")" : "";
		}
		return text;
	}

	public Object evaluate(TemplateContext context) {
		Boolean evaluated = !(Boolean) getIfToken().evaluate(context);
		return evaluated;
	}

	public void setIfToken(IfToken ifToken) {
		this.ifToken = ifToken;
	}

	public IfToken getIfToken() {
		if (ifToken == null) {
			throw new IllegalStateException(
					"An else token can only be evaluated using an associated if token - which is missing");
		}
		return ifToken;
	}

	@Override
	public String emit() {
		return ELSE;
	}
}
