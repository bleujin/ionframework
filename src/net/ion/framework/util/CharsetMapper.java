package net.ion.framework.util;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

/**
 * Ư�� locale�� �⺻ charset�� �˷��ش�.
 * 
 * @author Kim Sanghoon wizest@i-on.net
 * @version 1.0
 * 
 * @see net.ion.framework.util.CharserMapperDefault.properties
 */

public class CharsetMapper {
	private static HashMap<String, String> charsets = new HashMap<String, String>(); // cached charset

	private static final String DEFAULT_CHARSET_PROPERTY_RESOURCE = "net.ion.framework.util.CharsetMapperDefault";
	private static ResourceBundle defaultCharSet;
	static {
		defaultCharSet = PropertyResourceBundle.getBundle(DEFAULT_CHARSET_PROPERTY_RESOURCE);
	}

	private CharsetMapper() {
	}

	/**
	 * locale�� default charset�� �����Ѵ�.
	 * 
	 * @param locale
	 * @return
	 */
	public static String getDefaultCharset(Locale locale) {
		if (locale == null) {
			System.out.println(CharsetMapper.class.getName() + ": null locale.");
			return "ISO-8859-1";
		} else {
			String localeString = locale.toString();
			return getDefaultCharset(localeString);
		}
	}

	/**
	 * locale�� default charset�� �����Ѵ�.
	 * 
	 * ����) localeString�� ������ ����. ko_KR,ja_JP,en_US... �Ǵ� ko,ja,en,zh...
	 * 
	 * @param localeString
	 *            ISO Language Code - http://www.ics.uci.edu/pub/ietf/http/related/iso639.txt
	 * @return
	 */
	public static String getDefaultCharset(String localeString) {
		if (charsets.get(localeString) != null) {
			return (String) charsets.get(localeString);
		}

		String scanLocale = localeString;
		String charset = null;

		try {
			while (true) {
				try {
					charset = defaultCharSet.getString(scanLocale);
				} catch (MissingResourceException ex) {
				}

				if (charset != null) {
					break;
				}

				scanLocale = scanLocale.substring(0, scanLocale.lastIndexOf("_"));
			}
		} catch (Exception e) {
			System.out.println(CharsetMapper.class.getName() + ": unknown the default charset for language:" + localeString);
			charset = "ISO-8859-1";
		}

		synchronized (charsets) {
			charsets.put(localeString, charset);
		}

		return charset;
	}

	/**
	 * iso-8859-1 ���ڿ��� �ش� locale�� charset���� encoding
	 * 
	 * @param locale
	 * @param iso_8859_1_string
	 * @return
	 */
	public static String localizedString(Locale locale, String iso_8859_1_string) {
		try {
			return new String(iso_8859_1_string.getBytes("ISO-8859-1"), getDefaultCharset(locale));
		} catch (UnsupportedEncodingException ex) {
			return iso_8859_1_string;
		}
	}

	// ibr_11723
	public static String changeUtf8xCharset(String charset) {
		if ("utf-8x".equalsIgnoreCase(charset)) {
			return "utf-8";
		}
		return charset;
	}

	// public static void main(String[] args)
	// {
	// System.out.println(getDefaultCharset(Locale.KOREA));
	// }
}
