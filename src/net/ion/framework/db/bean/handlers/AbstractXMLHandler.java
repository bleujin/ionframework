package net.ion.framework.db.bean.handlers;

public abstract class AbstractXMLHandler {

	public String getCDATASection(String string) {
		return "<![CDATA[" + string + "]]>";
	}
}
