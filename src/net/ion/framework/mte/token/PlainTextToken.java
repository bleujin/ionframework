package net.ion.framework.mte.token;

import net.ion.framework.mte.TemplateContext;

public class PlainTextToken extends AbstractToken {
	public PlainTextToken(String text) {
		setText(text);
	}

	public Object evaluate(TemplateContext context) {
		return getText();
	}

	@Override
	public String emit() {
		return getText();
	}
}
