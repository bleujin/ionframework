package net.ion.framework.util;

public class PathMaker {

	public static String getFilePath(String _prePath, String filePath) {
		String prePath = StringUtil.defaultIfEmpty(_prePath, "") ;
		String tempDir = prePath + "/" + filePath;
		tempDir = tempDir.replaceAll("\\\\", "/");

		String path[] = StringUtil.split(tempDir, "/");
		return (prePath.startsWith("/") ? "/" : "") + StringUtil.join(path, "/");
	}

}
