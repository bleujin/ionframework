package net.ion.framework.file;

import java.util.StringTokenizer;

public class HexUtil {

	public static byte[] toByteArray(String hexString) {
		byte[] number = new byte[hexString.length() / 2];
		int i;
		for (i = 0; i < hexString.length(); i += 2) {
			int j = Integer.parseInt(hexString.substring(i, i + 2), 16);
			number[i / 2] = (byte) (j & 0x000000ff);
		}
		return number;
	}

	public static byte[] toByteArray(String hexString, String separators) {
		byte[] buf = new byte[hexString.length() / 3];

		int i = 0;
		StringTokenizer values = new StringTokenizer(hexString, separators);
		while (values.hasMoreTokens()) {
			String s = values.nextToken();
			int hex = Integer.parseInt(s, 16);
			buf[i++] = (byte) (hex & 0x000000ff);
		}
		return buf;
	}

	public static String toHex(int i) {
		byte b[] = new byte[4];
		b[0] = (byte) ((i & 0xff000000) >>> 24);
		b[1] = (byte) ((i & 0x00ff0000) >>> 16);
		b[2] = (byte) ((i & 0x0000ff00) >>> 8);
		b[3] = (byte) ((i & 0x000000ff));

		return toHex(b[0]) + toHex(b[1]) + toHex(b[2]) + toHex(b[3]);
	}

	public static String toHex(short i) {
		byte b[] = new byte[2];
		b[0] = (byte) ((i & 0xff00) >>> 8);
		b[1] = (byte) ((i & 0x00ff));

		return toHex(b[0]) + toHex(b[1]);
	}

	public static String toHex(byte b) {
		Integer iI = new Integer((b << 24) >>> 24);
		int i = iI.intValue();

		if (i < (byte) 16) {
			return "0" + Integer.toString(i, 16);
		} else {
			return Integer.toString(i, 16);
		}
	}

	public static String toHex(byte[] b) {
		return toHex(b, 0, b.length);
	}

	public static String[] toHexSplit(byte[] b, int piece ){
		if (b == null &&  b.length <= piece) return new String[]{toHex(b)} ;
		
		String[] result = new String[piece] ;
		int start = 0 ;
		for (int i = 0; i < piece; i++) {
			int end = b.length * (i+1) / piece;
			result[i] = toHex(b, start, end) ;
			start = end ;
		}
		
		return result ;
	}
	
	
	public static String toHex(short[] b) {
		return toHex(b, 0, b.length);
	}

	public static String toHex(int[] b) {
		return toHex(b, 0, b.length);
	}

	public static String toHex(int[] b, int off, int len) {
		if (b == null) {
			return "";
		}
		StringBuffer s = new StringBuffer("");
		int i;
		for (i = off; i < len; i++) {
			s.append(toHex(b[i]));
		}
		return s.toString();
	}

	public static String toHex(short[] b, int off, int len) {
		if (b == null) {
			return "";
		}
		StringBuffer s = new StringBuffer("");
		int i;
		for (i = off; i < len; i++) {
			s.append(toHex(b[i]));
		}
		return s.toString();
	}

	public static String toHex(byte[] b, int off, int len) {
		if (b == null) {
			return "";
		}
		StringBuffer s = new StringBuffer("");
		int i;
		for (i = off; i < len; i++) {
			s.append(toHex(b[i]));
		}
		return s.toString();
	}

	public static String toHexFormat(int[] b) {
		return toHexFormat(b, 0, b.length);
	}

	public static String toHexFormat(short[] b) {
		return toHexFormat(b, 0, b.length);
	}

	public static String toHexFormat(byte[] b) {
		return toHexFormat(b, 0, b.length);
	}

	public static String toHexFormat(byte[] b, int off, int len) {
		StringBuffer s = new StringBuffer("");
		StringBuffer c = new StringBuffer("");
		int i;

		if (b == null) {
			return "<null>";
		}

		for (i = off; i < len; i++) {
			if (i % 16 == 0) {
				s.append(toHex(i) + " ");
			}

			s.append(" " + toHex(b[i]));
			if (b[i] >= 0x20 && b[i] < 0x7f) {
				c.append((char) b[i]);
			} else {
				c.append('.');
			}
			if (i % 16 == 15) {
				s.append(c).append("\n");
				c.setLength(0);
			}

		}

		if (i % 16 != 0) {
			s.append(" \"").append(c).append("\"\n");
		}

		return s.toString();
	}

	public static String toHexFormat(short[] b, int off, int len) {
		StringBuffer s = new StringBuffer("");
		int i;

		if (b == null) {
			return "<null>";
		}

		for (i = off; i < len; i++) {
			s.append(" " + toHex(b[i]));
			if (i % 16 == 7) {
				s.append("\n");
			} else {
				if (i % 4 == 3) {
					s.append(" ");
				}
			}
		}
		if (i % 8 != 0) {
			s.append("\n");
		}

		return s.toString();
	}

	public static String toHexFormat(int[] b, int off, int len) {
		StringBuffer s = new StringBuffer("");
		int i;

		if (b == null) {
			return "<null>";
		}

		for (i = off; i < len; i++) {
			s.append(" " + toHex(b[i]));
			if (i % 4 == 3) {
				s.append("\n");
			}
		}
		if (i % 4 != 0) {
			s.append("\n");
		}

		return s.toString();
	}

}
