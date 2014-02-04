package net.ion.framework.util;

import org.apache.commons.lang.math.NumberUtils;

public class NumberUtil extends NumberUtils {

	public final static int toIntWithMark(String s) {
		if (s.startsWith("+"))
			return toIntWithMark(s.substring(1));
		else
			return toInt(s);
	}

	public final static int toIntWithMark(Object obj, int dft) {
		String str = ObjectUtil.toString(obj, String.valueOf(dft)) ;
		return toInt(str) ;
	}

	public final static boolean isModify(long l, int mod){
		return l % mod == 0 ;
	}
}
