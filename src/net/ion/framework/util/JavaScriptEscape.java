package net.ion.framework.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Locale;

import net.ion.framework.util.chardet.HtmlCharsetDetector;
import net.ion.framework.util.chardet.nsDetector;
import net.ion.framework.util.chardet.nsICharsetDetectionObserver;
import net.ion.framework.util.chardet.nsPSMDetector;

public class JavaScriptEscape {
	private final static String[] hex = { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "0A", "0B", "0C", "0D", "0E", "0F", "10", "11", "12",
			"13", "14", "15", "16", "17", "18", "19", "1A", "1B", "1C", "1D", "1E", "1F", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "2A",
			"2B", "2C", "2D", "2E", "2F", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "3A", "3B", "3C", "3D", "3E", "3F", "40", "41", "42",
			"43", "44", "45", "46", "47", "48", "49", "4A", "4B", "4C", "4D", "4E", "4F", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59", "5A",
			"5B", "5C", "5D", "5E", "5F", "60", "61", "62", "63", "64", "65", "66", "67", "68", "69", "6A", "6B", "6C", "6D", "6E", "6F", "70", "71", "72",
			"73", "74", "75", "76", "77", "78", "79", "7A", "7B", "7C", "7D", "7E", "7F", "80", "81", "82", "83", "84", "85", "86", "87", "88", "89", "8A",
			"8B", "8C", "8D", "8E", "8F", "90", "91", "92", "93", "94", "95", "96", "97", "98", "99", "9A", "9B", "9C", "9D", "9E", "9F", "A0", "A1", "A2",
			"A3", "A4", "A5", "A6", "A7", "A8", "A9", "AA", "AB", "AC", "AD", "AE", "AF", "B0", "B1", "B2", "B3", "B4", "B5", "B6", "B7", "B8", "B9", "BA",
			"BB", "BC", "BD", "BE", "BF", "C0", "C1", "C2", "C3", "C4", "C5", "C6", "C7", "C8", "C9", "CA", "CB", "CC", "CD", "CE", "CF", "D0", "D1", "D2",
			"D3", "D4", "D5", "D6", "D7", "D8", "D9", "DA", "DB", "DC", "DD", "DE", "DF", "E0", "E1", "E2", "E3", "E4", "E5", "E6", "E7", "E8", "E9", "EA",
			"EB", "EC", "ED", "EE", "EF", "F0", "F1", "F2", "F3", "F4", "F5", "F6", "F7", "F8", "F9", "FA", "FB", "FC", "FD", "FE", "FF" };
	private final static byte[] val = { 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
			0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
			0x3F, 0x3F, 0x3F, 0x3F, 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x0A, 0x0B, 0x0C,
			0x0D, 0x0E, 0x0F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
			0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x0A, 0x0B, 0x0C, 0x0D, 0x0E, 0x0F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
			0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
			0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
			0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
			0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
			0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
			0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F };

	public static String escape(String s) {
		StringBuffer sbuf = new StringBuffer();
		int len = s.length();
		for (int i = 0; i < len; i++) {
			int ch = s.charAt(i);
			if (ch == ' ') { // space : map to '+'
				sbuf.append('+');
			} else if ('A' <= ch && ch <= 'Z') { // 'A'..'Z' : as it was
				sbuf.append((char) ch);
			} else if ('a' <= ch && ch <= 'z') { // 'a'..'z' : as it was
				sbuf.append((char) ch);
			} else if ('0' <= ch && ch <= '9') { // '0'..'9' : as it was
				sbuf.append((char) ch);
			} else if (ch == '-' || ch == '_' // unreserved : as it was
					|| ch == '.' || ch == '!' || ch == '~' || ch == '*' || ch == '\'' || ch == '(' || ch == ')') {
				sbuf.append((char) ch);
			} else if (ch <= 0x007F) { // other ASCII : map to %XX
				sbuf.append('%');
				sbuf.append(hex[ch]);
			} else { // unicode : map to %uXXXX
				sbuf.append('%');
				sbuf.append('u');
				sbuf.append(hex[(ch >>> 8)]);
				sbuf.append(hex[(0x00FF & ch)]);
			}
		}
		return sbuf.toString();
	}

	public static String unescape(String s) {
		StringBuffer sbuf = new StringBuffer();
		int i = 0;
		int len = s.length();
		while (i < len) {
			int ch = s.charAt(i);
			if (ch == '+') { // + : map to ' '
				sbuf.append(' ');
			} else if ('A' <= ch && ch <= 'Z') { // 'A'..'Z' : as it was
				sbuf.append((char) ch);
			} else if ('a' <= ch && ch <= 'z') { // 'a'..'z' : as it was
				sbuf.append((char) ch);
			} else if ('0' <= ch && ch <= '9') { // '0'..'9' : as it was
				sbuf.append((char) ch);
			} else if (ch == '-' || ch == '_' // unreserved : as it was
					|| ch == '.' || ch == '!' || ch == '~' || ch == '*' || ch == '\'' || ch == '(' || ch == ')') {
				sbuf.append((char) ch);
			} else if (ch == ':' || ch == '/' // http://, ?, &, =
					|| ch == '?' || ch == '&' || ch == '=') {
				sbuf.append((char) ch);
			} else if (ch == '%') {
				int cint = 0;
				if ('u' != s.charAt(i + 1)) { // %XX : map to ascii(XX)
					cint = (cint << 4) | val[s.charAt(i + 1)];
					cint = (cint << 4) | val[s.charAt(i + 2)];
					i += 2;
				} else { // %uXXXX : map to unicode(XXXX)
					cint = (cint << 4) | val[s.charAt(i + 2)];
					cint = (cint << 4) | val[s.charAt(i + 3)];
					cint = (cint << 4) | val[s.charAt(i + 4)];
					cint = (cint << 4) | val[s.charAt(i + 5)];
					i += 5;
				}
				sbuf.append((char) cint);
			}
			i++;
		}
		return sbuf.toString();
	}

	// @SuppressWarnings("finally")
	// private static boolean isCharset(String str, int langth, String charset) {
	// try {
	// if (str.indexOf("%&") > -1) return true;
	// // utf-8은 3개가 1글자..
	// if (str.length() >= langth) {
	// int one;
	// try {
	// one = Integer.parseInt(str.substring(str.indexOf("%") + 1, str.indexOf("%") + 2));
	// } catch (Exception e) {
	// one = 10;
	// }
	//
	// if (one < 8) {
	// return isCharset(str.substring(str.indexOf("%") + 3), langth, charset);
	// } else {
	// if (str.indexOf("%") > -1) {
	// String utf = str.substring(str.indexOf("%"), str.indexOf("%") + langth);
	// utf = java.net.URLDecoder.decode(utf, charset);
	// if (utf.length() == 1) {
	// return true;
	// }
	// } else return true;
	// }
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	// return true;
	// }
	// return false;
	// }

	// public static String decode(String s, Locale defaultLocale) {
	// if (StringUtils.isEmpty(s)) return "";
	// else if (s.indexOf("%") == -1) return s.trim();
	// else if (s.indexOf("%u") > -1) return unescape(s.trim());
	// else {
	// // 다른문자(한글이나 기타문자) 포함 될 경우 Decode 안함.
	// if (!Pattern.matches("[0-9a-zA-Z&:/?#%+=._-]+", s)) return s.trim();
	// try {
	// s = s.replaceAll("%*&", "&");
	//
	// if (isCharset(s, 9, "utf-8")) {
	// return java.net.URLDecoder.decode(s, "utf-8");
	// }
	//
	// String charset = CharsetMapper.getDefaultCharset(defaultLocale);
	// if (isCharset(s, 6, charset)) {
	// return java.net.URLDecoder.decode(s, charset);
	// }
	//
	// return java.net.URLDecoder.decode(s, System.getProperty("file.encoding"));
	// } catch (UnsupportedEncodingException e) {
	// e.printStackTrace();
	// }
	// }
	// return s.trim();
	// }

	private static String decode(String url, String lang) {
		if (StringUtil.isEmpty(url))
			return "";
		else if (url.indexOf("%") == -1)
			return url.trim();
		else if (url.indexOf("%u") > -1)
			return unescape(url.trim());
		else {
			try {
				return URLDecoder.decode(url, detectEncoding(lang, url));
			} catch (Exception e) {
				try {
					return URLDecoder.decode(url, System.getProperty("file.encoding"));
				} catch (Exception e1) {
					return url;
				}
			}
		}
	}

	public static String decode(String str, Locale defaultLocale) {
		return decode(str, defaultLocale.toString());
	}

	public static String urlDecoder(Object obj, String lang) {
		return urlDecoder((String) obj, lang);
	}

	public static String urlDecoderLocale(Object attrId, Object obj) {
		if (obj instanceof Locale){
			return urlDecoder(attrId, ((Locale)obj).getLanguage()) ;
		} else if (obj instanceof String){
			return urlDecoder(attrId, (String)obj) ;
		} else {
			throw new IllegalArgumentException("argument is not valid..") ;
		}
	}
	
	public static String urlDecoder(String str, String lang) {
		String url = "";
		String param = "";
		if (StringUtil.isEmpty(str))
			return "";
		else if (str.indexOf("?") > -1) {
			url = StringUtil.substringBefore(str, "?");
			param = StringUtil.substringAfter(str, "?");
			return decode(url, lang) + "?" + getParam(param, lang);
		} else {
			return getParam(str, lang);
		}
	}

	private static String getParam(String param, String lang) {
		String rtn = "";
		String[] params = param.split("&");
		for (String s : params) {
			int idx = s.indexOf("=") + 1;
			rtn += "&" + s.substring(0, idx) + decode(s.substring(idx), lang);
		}
		return rtn.substring(1);
	}

	public static String getParamValue(String url, String key) {
		String queryString = StringUtil.substringAfter(url, "?");
		if (StringUtil.isNotEmpty(queryString) && StringUtil.isNotEmpty(key)) {
			String[] params = queryString.split("&");
			for (String s : params) {
				if (s.indexOf(key + "=") == 0) {
					return StringUtil.substringAfter(s, "=");
				}
			}
		}
		return "";
	}

	private static String detectEncoding(String country, String URI) throws IOException {
		int lang = 0;
		if ("ja".equals(country) || "ja_JP".equals(country)) {
			lang = nsPSMDetector.JAPANESE;
		} else if ("zh".equals(country)) {
			lang = nsPSMDetector.CHINESE;
		} else if ("zh_CN".equals(country)) {
			lang = nsPSMDetector.SIMPLIFIED_CHINESE;
		} else if ("zh_TW".equals(country)) {
			lang = nsPSMDetector.TRADITIONAL_CHINESE;
		} else if ("ko".equals(country) || "ko_KR".equals(country)) {
			lang = nsPSMDetector.KOREAN;
		}

		byte[] buf = URI.getBytes();
		nsDetector det = new nsDetector(lang);
		det.Init(new nsICharsetDetectionObserver() {
			public void Notify(String charset) {
				HtmlCharsetDetector.found = true;
			}
		});
		det.DoIt(buf, buf.length, false);
		det.DataEnd();

		String[] probableCharsets = det.getProbableCharsets();

		// for (String charset : probableCharsets) {
		// System.out.println("CHARSET=" + charset);
		// }
		// System.out.println("URI:" + URI);
		// System.out.println("Re-encodedURI(UTF-8):" + URLEncoder.encode(URLDecoder.decode(URI, "UTF-8"), "UTF-8"));
		// System.out.println(URI.toUpperCase().equals(URLEncoder.encode(URLDecoder.decode(URI, "UTF-8"),
		// "UTF-8").toUpperCase()));

		URI = URI.replaceAll("~", "%7E");
		URI = URI.replaceAll("!", "%21");
		URI = URI.replaceAll("'", "%27");
		URI = URI.replaceAll("\\(", "%28");
		URI = URI.replaceAll("\\)", "%29");
		URI = URI.replaceAll("%20", "+");

		String charset;
		if (URI.toUpperCase().equals(URLEncoder.encode(URLDecoder.decode(URI, "UTF-8"), "UTF-8").toUpperCase())) {
			// UTF-8
			if (probableCharsets.length > 0) {
				charset = probableCharsets[0];
			} else {
				charset = "UTF-8";
			}
		} else { // NOT UTF-8
			if (probableCharsets.length > 0) {
				if ("UTF-8".equals(probableCharsets[0])) {
					if (probableCharsets.length > 1) {
						charset = probableCharsets[1];
					} else {
						charset = "ISO-8859-1";
					}
				} else {
					charset = probableCharsets[0];
				}
			} else {
				charset = "ISO-8859-1";
			}
		}
		return charset;
	}

	public static void main(String[] args) throws UnsupportedEncodingException {
		String txt = "~!@#$%^&*()-_=+[]{},.<>?/'\":;\\|";
		String entxt = URLEncoder.encode(txt, "utf-8");
		String detxt = URLDecoder.decode(entxt, "utf-8");
		System.out.println(entxt);
		System.out.println(detxt);
		System.out.println(txt.equals(detxt));
	}
}
