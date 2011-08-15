package net.ion.framework.res;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Locale;

public class Messages {
	private Hashtable<String, String> map = new Hashtable<String, String>();
	private Locale locale;

	public Messages() {
		locale = new Locale("en");
	}

	public Enumeration<String> getKeys() {
		return map.keys();
	}

	public void addMessage(Message message) {
		map.put(message.getKey(), message.getValue());
	}

	public String get(String key) {
		return (String) map.get(key);
	}

	public String getDefault() {
		return "en";
	}

	public void setLocale(String localeStr) {
		locale = new Locale(localeStr);
	}

	public Locale getLocale() {
		return locale;
	}

}
