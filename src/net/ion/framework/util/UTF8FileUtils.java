package net.ion.framework.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PushbackInputStream;
import java.io.Reader;
import java.io.StringReader;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

public class UTF8FileUtils extends FileUtils {

	public static String readFileToString(File file) throws IOException {
		return UTF8FileUtils.readFileToString(file, "UTF-8");
	}

	public static String readFileToString(File file, String encoding) throws IOException { // if encoding NULL to use systemdefault
		FileInputStream fis = new FileInputStream(file);
		return UTF8FileUtils.readInputStreamToString(fis, encoding);
	}

	public static String readInputStreamToString(InputStream fis, String encoding) throws IOException { // if encoding NULL to use systemdefault
		UnicodeInputStream uin = new UnicodeInputStream(fis, encoding);
		String enc = uin.getEncoding(); // check for BOM mark and skip bytes
		InputStreamReader in = null;
		try {
			if (enc == null)
				in = new InputStreamReader(uin);
			else
				in = new InputStreamReader(uin, enc);

			return IOUtils.toString(in);
		} finally {
			IOUtil.closeQuietly(in);
		}
	}

	public static Reader readFileToReader(File file) throws IOException {
		return new StringReader(UTF8FileUtils.readFileToString(file));
	}

	public static Reader readFileToReader(File file, String encoding) throws IOException {
		return new StringReader(UTF8FileUtils.readFileToString(file, encoding));
	}

}

/**
 * This inputstream will recognize unicode BOM marks and will skip bytes if getEncoding() method is called before any of the read(...) methods.
 * 
 * Usage pattern: String enc = "ISO-8859-1"; // or NULL to use systemdefault FileInputStream fis = new FileInputStream(file); UnicodeInputStream uin = new UnicodeInputStream(fis, enc); enc = uin.getEncoding(); // check for BOM mark and skip bytes InputStreamReader in; if (enc == null) in = new InputStreamReader(uin); else in = new
 * InputStreamReader(uin, enc);
 */
class UnicodeInputStream extends InputStream {
	PushbackInputStream internalIn;
	boolean isInited = false;
	String defaultEnc;
	String encoding;

	private static final int BOM_SIZE = 4;

	UnicodeInputStream(InputStream in, String defaultEnc) {
		internalIn = new PushbackInputStream(in, BOM_SIZE);
		this.defaultEnc = defaultEnc;
	}

	public String getDefaultEncoding() {
		return defaultEnc;
	}

	public String getEncoding() {
		if (!isInited) {
			try {
				init();
			} catch (IOException ex) {
				throw new IllegalStateException("Init method failed.");
				// (Throwable)ex);
			}
		}
		return encoding;
	}

	/**
	 * Read-ahead four bytes and check for BOM marks. Extra bytes are unread back to the stream, only BOM bytes are skipped.
	 */
	protected void init() throws IOException {
		if (isInited)
			return;

		byte bom[] = new byte[BOM_SIZE];
		int n, unread;
		n = internalIn.read(bom, 0, bom.length);

		if ((bom[0] == (byte) 0xEF) && (bom[1] == (byte) 0xBB) && (bom[2] == (byte) 0xBF)) {
			encoding = "UTF-8";
			unread = n - 3;
		} else if ((bom[0] == (byte) 0xFE) && (bom[1] == (byte) 0xFF)) {
			encoding = "UTF-16BE";
			unread = n - 2;
		} else if ((bom[0] == (byte) 0xFF) && (bom[1] == (byte) 0xFE)) {
			encoding = "UTF-16LE";
			unread = n - 2;
		} else if ((bom[0] == (byte) 0x00) && (bom[1] == (byte) 0x00) && (bom[2] == (byte) 0xFE) && (bom[3] == (byte) 0xFF)) {
			encoding = "UTF-32BE";
			unread = n - 4;
		} else if ((bom[0] == (byte) 0xFF) && (bom[1] == (byte) 0xFE) && (bom[2] == (byte) 0x00) && (bom[3] == (byte) 0x00)) {
			encoding = "UTF-32LE";
			unread = n - 4;
		} else {
			// Unicode BOM mark not found, unread all bytes
			encoding = defaultEnc;
			unread = n;
		}
		// System.out.println("read=" + n + ", unread=" + unread);

		if (unread > 0)
			internalIn.unread(bom, (n - unread), unread);

		isInited = true;
	}

	public void close() throws IOException {
		// init();
		isInited = true;
		internalIn.close();
	}

	public int read() throws IOException {
		init();
		isInited = true;
		return internalIn.read();
	}

	public static void main(String[] args) throws IOException {
		String path = "C:\\Temp\\.__ids\\users\\admin\\test.txt";

		File fils = new File(path);
		String sss = UTF8FileUtils.readFileToString(fils, "euc-kr");
		System.out.println(sss);

	}

}
