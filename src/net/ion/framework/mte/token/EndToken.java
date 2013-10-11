package net.ion.framework.mte.token;

import net.ion.framework.mte.TemplateContext;


public class EndToken extends AbstractToken {
	public static final String END = "end";

	@Override
	public String getText() {
		if (text == null) {
			text = END;
		}
		return text;
	}

	public Object evaluate(TemplateContext context) {
		return "";
	}
}
