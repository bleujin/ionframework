package net.ion.framework.file;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.UnsupportedCharsetException;

import net.ion.framework.parse.gson.JsonObject;
import net.ion.framework.parse.gson.JsonParser;
import net.ion.framework.util.IOUtil;
import net.ion.framework.util.StringUtil;

public class PropFile {

	private final String[] pathHex;
	private final File targetFile;
	private JsonObject meta = null;

	private PropFile(String[] pathHex, File targetFile) {
		this.pathHex = pathHex;
		this.targetFile = targetFile;

	}

	public static PropFile create(String[] pathHex, File newFile) {
		return new PropFile(pathHex, newFile);
	}

	public boolean exist() {
		return targetFile.exists();
	}

	public String filePath() throws UnsupportedEncodingException {
		return new String(HexUtil.toByteArray(StringUtil.join(pathHex)), PropFileManager.UTF8);
	}

	public synchronized InputStream inputStream() throws IOException {
		DataInputStream dinput = new DataInputStream(new FileInputStream(targetFile));
		int metaLength = dinput.readInt();
		dinput.read(new byte[metaLength]);
		return dinput;
	}

	public synchronized String propJson() throws IOException {
		if (!exist())
			return null;
		if (meta == null) {
			DataInputStream dinput = null;
			try {
				dinput = new DataInputStream(new FileInputStream(targetFile));
				int metaLength = dinput.readInt();
				byte[] bytes = new byte[metaLength];
				dinput.read(bytes);

				this.meta = JsonObject.fromString(new String(bytes, PropFileManager.UTF8));
			} catch(UnsupportedCharsetException ex) {
				throw new IOException(ex.getMessage()) ;
			} finally {
				IOUtil.closeQuietly(dinput) ;
			}
		}

		return meta.toString();
	}

}
