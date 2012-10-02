package net.ion.framework.file;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.List;

import javax.swing.plaf.ListUI;

import org.apache.commons.lang.SystemUtils;
import org.apache.tools.ant.input.PropertyFileInputHandler;

import net.ion.framework.parse.gson.JsonObject;
import net.ion.framework.util.Debug;
import net.ion.framework.util.FileUtil;
import net.ion.framework.util.IOUtil;
import net.ion.framework.util.ListUtil;
import net.ion.framework.util.StringUtil;

public class PropFileManager {

	private final static int PIECE_LENGTH = 3;
	private File baseDir;
	static String UTF8 = "UTF-8";

	private PropFileManager(File baseDir) {
		this.baseDir = baseDir;
	}

	public static PropFileManager create(String baseDirName) {
		File baseDir = new File(baseDirName);
		if (!baseDir.exists())
			baseDir.mkdirs();

		return new PropFileManager(baseDir);
	}

	public PropFile save(String uniquePath, JsonObject meta, InputStream input) throws IOException {
		String[] pathHex = toHexSplit(uniquePath.getBytes(UTF8), PIECE_LENGTH);

		String metaString = (meta == null) ? "{}" : meta.toString();
		byte[] metaBytes = metaString.getBytes(UTF8);
		int length = metaBytes.length;

		File newFile = new File(baseDir, StringUtil.join(pathHex, "/"));
		if (!newFile.getParentFile().exists())
			newFile.getParentFile().mkdirs();

		DataOutputStream dout = new DataOutputStream(new FileOutputStream(newFile));
		dout.writeInt(length);
		dout.write(metaBytes);
		IOUtil.copyNClose(input, dout);

		return PropFile.create(pathHex, newFile);
	}

	public PropFile read(String uniquePath) throws UnsupportedEncodingException {
		String[] pathHex = toHexSplit(uniquePath.getBytes(UTF8), PIECE_LENGTH);
		File newFile = new File(baseDir, StringUtil.join(pathHex, "/"));

		return PropFile.create(pathHex, newFile);
	}

	public List<PropFile> listFile(String startPath) throws IOException {

		final String hexString = HexUtil.toHex(startPath.getBytes(UTF8));
		final String prefixHexString = HexUtil.toHex(StringUtil.replace(startPath, SystemUtils.FILE_SEPARATOR, "").getBytes(UTF8));

		File[] findFiles = findFiles(baseDir, new FileFilter() {
			public boolean accept(File childFile) {
				try {
					String relativePath = PropFileManager.this.relativePath(childFile) ;
					if (childFile.isFile() && relativePath.startsWith(hexString))
						return true;
				} catch (IOException ex) {
					return false;
				}
				return false;
			}
		}, prefixHexString, true);
		List<PropFile> result = ListUtil.newList();
		for (File findFile : findFiles) {
			String relativePath = StringUtil.substringAfter(findFile.getCanonicalPath(), baseDir.getCanonicalPath() + SystemUtils.FILE_SEPARATOR);
			String hexPathName = StringUtil.join(StringUtil.split(relativePath, SystemUtils.FILE_SEPARATOR));
			String[] pathHex = toHexSplit(HexUtil.toByteArray(hexPathName), PIECE_LENGTH);

			result.add(PropFile.create(pathHex, findFile));
		}

		return result;
	}
	private String relativePath(File childFile) throws IOException{
		return StringUtil.replace(StringUtil.substringAfter(childFile.getCanonicalPath(), baseDir.getCanonicalPath() + SystemUtils.FILE_SEPARATOR), SystemUtils.FILE_SEPARATOR, "") ;
	}
	
	private File[] findFiles(File parentDir, FileFilter filter, String prefix, boolean recursive) throws IOException {
		List<File> result = ListUtil.newList() ;
		if (filter.accept(parentDir)) {
			result.add(parentDir) ;
		}

		if (parentDir.isDirectory() && recursive){
			for (File child : parentDir.listFiles()) {
				String relativePath = relativePath(child);
				if (! (prefix.startsWith(relativePath) || relativePath.startsWith(prefix))) continue ;
				for(File find : findFiles(child, filter, prefix, recursive)){
					result.add(find) ;
				} ;
			}
		} 
		return result.toArray(new File[0]);
	}
	
	static String[] toHexSplit(byte[] b, int pieceLength ){
		if (b == null &&  b.length <= pieceLength) return new String[]{HexUtil.toHex(b)} ;
		
		String hexString = HexUtil.toHex(b) ;
		String[] result = new String[3] ;
		for (int i = 0; i < result.length; i++) {
			result[i] = StringUtil.substring(hexString, i*pieceLength, ((i == 2) ? hexString.length() : (i+1)*pieceLength)) ;
		}
		
		return result ;
//		String[] result = new String[pieceLength] ;
//		int start = 0 ;
//		for (int i = 0; i < pieceLength; i++) {
//			int end = b.length * (i+1) / pieceLength;
//			result[i] = HexUtil.toHex(b, start, end) ;
//			start = end ;
//		}
//		
//		return result ;
	}
	
}
