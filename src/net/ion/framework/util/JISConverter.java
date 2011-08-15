package net.ion.framework.util;

import java.util.HashMap;

public class JISConverter {

	private static HashMap<String, String> charMap = new HashMap<String, String>();

	private static void makeMap() {
		// charMap.put("\u2014", String.valueOf((char)0x2014)); // ¡ª 0x2014
		charMap.put("\u2015", String.valueOf((char) 0x2014)); // ¡ª 0x2014
		charMap.put("\u2225", String.valueOf((char) 0x2016)); // ¡« 0x2016
		charMap.put("\u22EF", String.valueOf((char) 0x2026)); // ¡¦ 0x2026
		charMap.put("\uFF0D", String.valueOf((char) 0x2212)); // £­ 0x2212
		charMap.put("\uFF5E", String.valueOf((char) 0x301C)); // ¢¦ 0x301C
		charMap.put("\uFFE0", String.valueOf((char) 0x00A2)); // ¡Ë 0x00A2
		charMap.put("\uFFE1", String.valueOf((char) 0x00A3)); // ¡Ì 0x00A3
		charMap.put("\uFFE2", String.valueOf((char) 0x00AC)); // ¡þ 0x00AC

		// / http://www.ingrid.org/java/i18n/encoding/ja-conv.html
		// / http://www.haible.de/bruno/charsets/conversion-tables/
	}

	static {
		makeMap();
	}

	public static String toConverter(String charset, String fullWidhCahr) {
		if ("Shift_JIS".equalsIgnoreCase(charset) || "iso-2022-jp".equalsIgnoreCase(charset) || "euc-jp".equalsIgnoreCase(charset)) {
			char charVal[] = fullWidhCahr.toCharArray();
			int sizeChar = charVal.length;
			String strTemp = "";
			String strAdd = "";
			StringBuffer strRet = new StringBuffer();
			for (int i = 0; i < sizeChar; i++) {
				strTemp = String.valueOf(charVal[i]);
				if ((strAdd = (String) charMap.get(strTemp)) != null)
					strRet.append(strAdd);
				else
					strRet.append(strTemp);
			}
			return strRet.toString();
		} else {
			return fullWidhCahr;
		}
	}

}
