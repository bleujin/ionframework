package net.ion.framework.res;

import java.text.MessageFormat;
import java.util.Enumeration;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import net.ion.framework.logging.LogBroker;
import net.ion.framework.util.StringUtil;

public final class XMLResources extends Resources {

	private static Logger log = LogBroker.getLogger(XMLResources.class);
	private Messages messages;

	public XMLResources(Messages messages) {
		this.messages = messages;
	}

	public void setBundle(ResourceBundle bundle) {
		throw new IllegalArgumentException("");
	}

	public Enumeration<String> getKeys() {
		return messages.getKeys();
	}

	public Locale getLocale() {
		return messages.getLocale();
	}

	public String get(String key, Object[] args) {
		try {
			String s = messages.get(key);
			if (s == null) {
				log.warning("Not Found Key : " + key);
				return "[[key=" + key + "]]";
			}
			return MessageFormat.format(s, args);
		} catch (Exception e) {
			log.warning(e.getMessage());
			return "[[key=" + key + ",value=" + StringUtil.join(args, " ") + "]]";
		}
	}

	public static Resources getResources(String baseName) {
		return XMLResourceFactory.getResource(baseName);
	}

	public static Resources getResources(String baseName, Locale locale) {
		return XMLResourceFactory.getResource(baseName, locale.getLanguage());
	}

}
