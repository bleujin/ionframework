package net.ion.framework.util;

import java.util.HashMap;

public class FullToHalfString {
	private static HashMap<String, String> charMap = new HashMap<String, String>();

	private static void makeCharMap(String fullChar, String halfChar) {
		charMap.put(fullChar, halfChar);
	}

	private static void makeMap() {
		makeCharMap("\uFF01", "!");
		makeCharMap("\uFF02", "\"");
		makeCharMap("\uFF03", "#");
		makeCharMap("\uFF04", "$");
		makeCharMap("\uFF05", "%");
		makeCharMap("\uFF06", "&");
		makeCharMap("\uFF07", "'");
		makeCharMap("\uFF08", "(");
		makeCharMap("\uFF09", ")");
		makeCharMap("\uFF0A", "*");
		makeCharMap("\uFF0B", "+");
		makeCharMap("\uFF0C", ",");
		makeCharMap("\uFF0D", "-");
		makeCharMap("\uFF0E", ".");
		makeCharMap("\uFF0F", "/");

		makeCharMap("\uFF10", "0");
		makeCharMap("\uFF11", "1");
		makeCharMap("\uFF12", "2");
		makeCharMap("\uFF13", "3");
		makeCharMap("\uFF14", "4");
		makeCharMap("\uFF15", "5");
		makeCharMap("\uFF16", "6");
		makeCharMap("\uFF17", "7");
		makeCharMap("\uFF18", "8");
		makeCharMap("\uFF19", "9");
		makeCharMap("\uFF1A", ":");
		makeCharMap("\uFF1B", ";");
		makeCharMap("\uFF1C", "<");
		makeCharMap("\uFF1D", "=");
		makeCharMap("\uFF1E", ">");
		makeCharMap("\uFF1F", "?");

		makeCharMap("\uFF20", "@");
		makeCharMap("\uFF21", "A");
		makeCharMap("\uFF22", "B");
		makeCharMap("\uFF23", "C");
		makeCharMap("\uFF24", "D");
		makeCharMap("\uFF25", "E");
		makeCharMap("\uFF26", "F");
		makeCharMap("\uFF27", "G");
		makeCharMap("\uFF28", "H");
		makeCharMap("\uFF29", "I");
		makeCharMap("\uFF2A", "J");
		makeCharMap("\uFF2B", "K");
		makeCharMap("\uFF2C", "L");
		makeCharMap("\uFF2D", "M");
		makeCharMap("\uFF2E", "N");
		makeCharMap("\uFF2F", "O");

		makeCharMap("\uFF30", "P");
		makeCharMap("\uFF31", "Q");
		makeCharMap("\uFF32", "R");
		makeCharMap("\uFF33", "S");
		makeCharMap("\uFF34", "T");
		makeCharMap("\uFF35", "U");
		makeCharMap("\uFF36", "V");
		makeCharMap("\uFF37", "W");
		makeCharMap("\uFF38", "X");
		makeCharMap("\uFF39", "Y");
		makeCharMap("\uFF3A", "Z");
		makeCharMap("\uFF3B", "[");
		makeCharMap("\uFF3C", "\\");
		makeCharMap("\uFF3D", "]");
		makeCharMap("\uFF3E", "^");
		makeCharMap("\uFF3F", "_");

		makeCharMap("\uFF40", "`");
		makeCharMap("\uFF41", "a");
		makeCharMap("\uFF42", "b");
		makeCharMap("\uFF43", "c");
		makeCharMap("\uFF44", "d");
		makeCharMap("\uFF45", "e");
		makeCharMap("\uFF46", "f");
		makeCharMap("\uFF47", "g");
		makeCharMap("\uFF48", "h");
		makeCharMap("\uFF49", "i");
		makeCharMap("\uFF4A", "j");
		makeCharMap("\uFF4B", "k");
		makeCharMap("\uFF4C", "l");
		makeCharMap("\uFF4D", "m");
		makeCharMap("\uFF4E", "n");
		makeCharMap("\uFF4F", "o");

		makeCharMap("\uFF50", "p");
		makeCharMap("\uFF51", "q");
		makeCharMap("\uFF52", "r");
		makeCharMap("\uFF53", "s");
		makeCharMap("\uFF54", "t");
		makeCharMap("\uFF55", "u");
		makeCharMap("\uFF56", "v");
		makeCharMap("\uFF57", "w");
		makeCharMap("\uFF58", "x");
		makeCharMap("\uFF59", "y");
		makeCharMap("\uFF5A", "z");
		makeCharMap("\uFF5B", "{");
		makeCharMap("\uFF5C", "|");
		makeCharMap("\uFF5D", "}");
		makeCharMap("\uFF5E", "~");
		makeCharMap("\uFF5F", "(");

		makeCharMap("\uFF60", ")");
		makeCharMap("\uFF61", "\u3002");
		makeCharMap("\uFF62", "\u300C");
		makeCharMap("\uFF63", "\u300D");
		makeCharMap("\uFF64", "\u3001");
		makeCharMap("\uFF65", "\u30FB");
		makeCharMap("\uFF66", "\u30F2");
		makeCharMap("\uFF67", "\u30A1");
		makeCharMap("\uFF68", "\u30A3");
		makeCharMap("\uFF69", "\u30A5");
		makeCharMap("\uFF6A", "\u30A7");
		makeCharMap("\uFF6B", "\u30A9");
		makeCharMap("\uFF6C", "\u30E3");
		makeCharMap("\uFF6D", "\u30E5");
		makeCharMap("\uFF6E", "\u30E7");
		makeCharMap("\uFF6F", "\u30C3");

		makeCharMap("\uFF70", "\u30FC");
		makeCharMap("\uFF71", "\u30A2");
		makeCharMap("\uFF72", "\u30A4");
		makeCharMap("\uFF73", "\u30A6");
		makeCharMap("\uFF74", "\u30A8");
		makeCharMap("\uFF75", "\u30AA");
		makeCharMap("\uFF76", "\u30AB");
		makeCharMap("\uFF77", "\u30AD");
		makeCharMap("\uFF78", "\u30AF");
		makeCharMap("\uFF79", "\u30B1");
		makeCharMap("\uFF7A", "\u30B3");
		makeCharMap("\uFF7B", "\u30B5");
		makeCharMap("\uFF7C", "\u30B7");
		makeCharMap("\uFF7D", "\u30B9");
		makeCharMap("\uFF7E", "\u30BB");
		makeCharMap("\uFF7F", "\u30BD");

		makeCharMap("\uFF80", "\u30BF");
		makeCharMap("\uFF81", "\u30C1");
		makeCharMap("\uFF82", "\u30C4");
		makeCharMap("\uFF83", "\u30C6");
		makeCharMap("\uFF84", "\u30C8");
		makeCharMap("\uFF85", "\u30CA");
		makeCharMap("\uFF86", "\u30CB");
		makeCharMap("\uFF87", "\u30CC");
		makeCharMap("\uFF88", "\u30CD");
		makeCharMap("\uFF89", "\u30CE");
		makeCharMap("\uFF8A", "\u30CF");
		makeCharMap("\uFF8B", "\u30D2");
		makeCharMap("\uFF8C", "\u30D5");
		makeCharMap("\uFF8D", "\u30D8");
		makeCharMap("\uFF8E", "\u30DB");
		makeCharMap("\uFF8F", "\u30DE");

		makeCharMap("\uFF90", "\u30DF");
		makeCharMap("\uFF91", "\u30E0");
		makeCharMap("\uFF92", "\u30E1");
		makeCharMap("\uFF93", "\u30E2");
		makeCharMap("\uFF94", "\u30E4");
		makeCharMap("\uFF95", "\u30E6");
		makeCharMap("\uFF96", "\u30E8");
		makeCharMap("\uFF97", "\u30E9");
		makeCharMap("\uFF98", "\u30EA");
		makeCharMap("\uFF99", "\u30EB");
		makeCharMap("\uFF9A", "\u30EC");
		makeCharMap("\uFF9B", "\u30ED");
		makeCharMap("\uFF9C", "\u30EF");
		makeCharMap("\uFF9D", "\u30F3");
		makeCharMap("\uFF9E", "\u309B");
		makeCharMap("\uFF9F", "\u309C");

		makeCharMap("\uFFA0", " ");

		makeCharMap("\uFFE0", "\u00A2");
		makeCharMap("\uFFE1", "\u00A3");
		makeCharMap("\uFFE2", "\u00AC");
		makeCharMap("\uFFE3", "\u00AF");
		makeCharMap("\uFFE4", "\u00A6");
		makeCharMap("\uFFE5", "\u00A5");
		makeCharMap("\uFFE6", "\u20A9");

		makeCharMap("\uFFE8", "|");
		makeCharMap("\uFFE9", "\u2190");
		makeCharMap("\uFFEA", "\u2191");
		makeCharMap("\uFFEB", "\u2192");
		makeCharMap("\uFFEC", "\u2193");
		makeCharMap("\uFFED", "\u25A0");
		makeCharMap("\uFFEE", "\u25CB");
	}

	static {
		makeMap();
	}

	public static String fullToHalf(String fullWidhCahr) {
		char charVal[] = fullWidhCahr.toCharArray();
		int sizeChar = charVal.length;
		String strTemp = "";
		String strAdd = "";
		String strRet = "";
		for (int i = 0; i < sizeChar; i++) {
			strTemp = String.valueOf(charVal[i]);
			if ((strAdd = (String) charMap.get(strTemp)) != null)
				strRet = strRet + strAdd;
			else
				strRet = strRet + strTemp;
		}

		return strRet;
	}

	// public static void main(String[] args) {
	//
	// print();
	// String arg =
	// "\uFF66\uFF67\uFF68\uFF69\uFF6A\uFF6B\uFF6C\uFF6D\uFF6E\uFF6F\uFF70\uFF71\uFF72\uFF73\uFF74\uFF75\uFF76\uFF77\uFF78\uFF79\uFF7A\uFF7B\uFF7C\uFF7D\uFF7E\uFF7F\uFF80\uFF81\uFF82\uFF83\uFF84\uFF85\uFF86\uFF87\uFF88\uFF89\uFF8A\uFF8B\uFF8C\uFF8D\uFF8E\uFF8F\uFF90\uFF91\uFF92\uFF93\uFF94\uFF95\uFF96\uFF97\uFF98\uFF99\uFF9A\uFF9B\uFF9C\uFF9D";
	// System.out.println("£¤£¤£¤£¤£¤£¤£¤£¤£¤£¤£¤£¤£¤£¤£¤ " + arg);
	// try {
	// String x = FullToHalfString.FullToHalf(arg);
	// System.out.println("£¤£¤£¤£¤£¤£¤£¤£¤£¤£¤£¤£¤£¤£¤£¤ " + x);
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }
	//
	// public static void print() {
	// Set set = charMap.keySet();
	// Iterator itr = null;
	// if (!set.isEmpty())
	// itr = set.iterator();
	//
	// while (itr.hasNext()) {
	// String key = (String) itr.next();
	//
	// System.out.println("setmeration.key : " + key);
	// System.out.println("setmeration.value : " + charMap.get(key));
	// }
	// }

}
