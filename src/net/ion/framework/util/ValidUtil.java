package net.ion.framework.util;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;

public class ValidUtil {

	// public static boolean isAlphaNum(String str){
	// return str.matches("^[a-zA-Z0-9]+$");
	// }
	//
	// public static boolean isSmallAlphaNum(String str){
	// return str.matches("^[a-z0-9]+$");
	// }
	//
	public static boolean isEmail(String str) {
		if (StringUtil.isEmpty(str))
			return true;

		return str.matches("^((\\w|[\\-\\.])+)@((\\w|[\\-\\.])+)\\.([A-Za-z]+)$");
	}

	public static boolean isPhone(String str) {
		if (StringUtil.isEmpty(str))
			return true;

		return str.matches("^[0-9\\-#\\(\\)\\+\\.]+$");
	}

	// public static boolean isIpAddress(String str){
	// if(StringUtil.isEmpty(str)) return true;
	//    	
	// return str.matches("^((0|1[0-9]{0,2}|2[0-9]{0,1}|2[0-4][0-9]|25[0-5]|[3-9][0-9]{0,1})\\.){3}(0|1[0-9]{0,2}|2[0-9]{0,1}|2[0-4][0-9]|25[0-5]|[3-9][0-9]{0,1})$");
	// }

	public static boolean isAlphaNumUnderBar(String str) {
		if (StringUtil.isEmpty(str))
			return true;

		return str.matches("^[a-zA-Z0-9_]+$");
	}

	public static boolean isAlphaNumUnderBarHyphenDot(String str) {
		if (StringUtil.isEmpty(str))
			return true;

		return str.matches("^[a-zA-Z0-9_\\-\\.\\(\\)]+$");
	}

	public static boolean isSmallAlphaNumUnderBar(String str) {
		if (StringUtil.isEmpty(str))
			return true;

		return str.matches("^[a-z0-9_]+$");
	}

	public static boolean isUpperAlphaNumUnderBar(String str) {
		if (StringUtil.isEmpty(str))
			return true;

		return str.matches("^[A-Z0-9_]+$");
	}

	public static boolean isSmallAlphaNumUnderBarHyphen(String str) {
		if (StringUtil.isEmpty(str))
			return true;

		return str.matches("^[a-z0-9_\\-]+$");
	}

	public static boolean isUrlOrSlash(String str) {
		if (StringUtil.isEmpty(str))
			return true;

		return "/".equals(str) || (str.matches("^https?:\\/\\/([\\S+]+)+$") && str.matches("^[a-zA-Z0-9\\:\\#\\$\\%\\&\\=\\-\\~\\+\\/\\?\\_\\.\\,]+$"));
	}

	public static boolean isDirName(String str) {
		if (StringUtil.isEmpty(str))
			return true;

		return str.matches("^[a-zA-Z0-9_\\~`!@#\\$%\\^&\\(\\)\\+\\.,;\\{\\}\\-]+$");
	}

	// public static boolean hasNotAllowedSpecialCharForKeyword(String arg) {
	// if(arg == null ) {
	// return false;
	// }
	// ArrayList specialChars = CodeValidator.getNotAllowedChars() ;
	// for(int i = 0, last = specialChars.size() ; i < last ; i++) {
	// String key = specialChars.get(i).toString();
	// if(!",".equals(key)) {
	// if(arg.indexOf(specialChars.get(i).toString()) > 0) return true ;
	// }
	// }
	// return false ;
	// }

	public static boolean isNotMandatory(String arg) {
		if (arg == null) {
			return true;
		}
		String str = StringUtil.replace(arg, "\u3000", "");
		return str.trim().equals("");
	}

	public static boolean isLongerThan(String str, int constraintLength) {
		if (StringUtil.isEmpty(str))
			return false;

		return (str.length() > constraintLength);
	}

	public static boolean isLongerThanByByte(String str, int constraintLength) {
		if (StringUtil.isEmpty(str))
			return false;
		try {
			return (str.getBytes("UTF-8").length > constraintLength);
		} catch (UnsupportedEncodingException e) {
			throw new IllegalArgumentException(e);
		}
	}

	public static boolean isBetweenLength(String value, int minByteLen, int maxByteLen) {
		int valueLen = value.length();
		return (valueLen >= minByteLen && valueLen <= maxByteLen);
	}

	public static boolean isValidFileName(String arg) {
		String fileName = getFileNameFromPath(arg);
		String fileExt = getExtFromPath(arg);

		// if file does not have extension, consider it valid
		if (arg.endsWith(".") || arg.startsWith(".")) {
			return false;
		}

		if (isFileNameBlank(arg, fileName, fileExt))
			return true;

		if (isLongerThanByByte(fileName, 700)) {
			return false;
		}

		if (isFileNameOnly(fileName, fileExt)) {
			return isAlphaNumUnderBar(fileName);
		} else {
			return isAlphaNumUnderBarHyphenDot(fileName) && (isAlphaNumUnderBarHyphenDot(fileExt));
		}

	}

	private static boolean isFileNameOnly(String fileName, String fileExt) {
		return !fileName.equals("") && fileExt.equals("");
	}

	private static boolean isFileNameBlank(String arg, String fileName, String fileExt) {
		return fileName.equals("") && fileExt.equals("") && arg.length() == 0;
	}

	public static boolean isDate(String date) {
		if (StringUtil.isBlank(date)) {
			return true;
		}

		if (!date.matches("^[0-9\\-]+$")) {
			return false;
		}

		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd-HHmmss");
			sdf.setLenient(false);
			sdf.parse(date);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public static boolean isInvalidCatId(String catId) {
		return isNotMandatory(catId) || isLongerThan(catId, 12) || !isSmallAlphaNumUnderBar(catId);
	}

	public static boolean isInvalidUserId(String regUserId) {
		return isNotMandatory(regUserId) || isLongerThan(regUserId, 12) || !isSmallAlphaNumUnderBarHyphen(regUserId);
	}

	public static boolean isNumber(String str) {
		if (StringUtil.isBlank(str)) {
			return true;
		}

		try {
			Integer.parseInt(str);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

	public static boolean isNamoUploadFileNames(String str) {
		if (StringUtil.isBlank(str)) {
			return true;
		}

		for (int i = 0; i < str.length(); i++) {
			char cdAt = str.charAt(i);
			if (cdAt > 128) {
				return false;
			}
		}

		if (!str.matches("^[a-zA-Z0-9_()\\-. \\[\\]\\^\\@\\{\\}\\~\\!\\$\\`\\'\\;\\=]+$")) {
			return false;
		}
		return true;
	}

	/* AIRKJH */
	private static String getFileNameFromPath(String arg) {
		String realFileName = getRealFileNameFromPath(arg);
		return (realFileName.indexOf(".") != -1) ? StringUtil.substringBeforeLast(realFileName, ".") : realFileName;
	}

	/* AIRKJH */
	private static String getExtFromPath(String arg) {
		String realFileName = getRealFileNameFromPath(arg);
		// when filename is ".jpg", consider it does not have file name
		return (realFileName.indexOf(".") <= 0) ? "" : StringUtil.substringAfterLast(realFileName, ".");
	}

	private static String getRealFileNameFromPath(String path) {
		if (path.indexOf("/") >= 0)
			return StringUtil.substringAfterLast(path, "/");
		else
			return path;
	}
}
