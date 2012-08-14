package net.ion.framework.util;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.Date;

import org.apache.commons.io.IOUtils;

public class IOUtil extends IOUtils {

	public static void close(Closeable... cls) {
		for (Closeable cl : cls) {
			closeQuietly(cl);
		}
	}

	public static void closeQuietly(Closeable cl) {
		try {
			if (cl != null) cl.close();
		} catch (IOException ignore) {
			ignore.printStackTrace() ;
//			throw new IllegalStateException(ignore) ;
		}
	}

	public static void copyNClose(InputStream input, OutputStream output) throws IOException {
		try {
			IOUtil.copyLarge(input, output) ;
		} finally {
			IOUtil.close(input, output) ;
		}
	}

	public static void copyNClose(Reader reader, Writer writer) throws IOException {
		try {
			IOUtil.copyLarge(reader, writer) ;
		} finally {
			IOUtil.close(reader, writer) ;
		}
		
	}
	
	private static File tempDir = null;
	public static void setTempDir(File tempDir){
		IOUtil.tempDir = tempDir;
		Shell.forceMkdir(tempDir);
	}
	public static File createTempFile(String prefix) throws IOException {
		if (tempDir == null) {
			File tmpfile = File.createTempFile(prefix + "_" + (new Long(new Date().getTime())).toString(), ".tmp");
			tmpfile.deleteOnExit();
			return tmpfile;
		} else {
			File tmpfile = File.createTempFile(prefix + "_" + (new Long(new Date().getTime())).toString(), ".tmp", tempDir);
			tmpfile.deleteOnExit();
			return tmpfile;
		}
	}
	public static File getTempDir() {
		if (tempDir == null) {
			return new File(System.getProperty("java.io.tmpdir"));
		} else {
			return tempDir;
		}
	}

	public static void copyNClose(Reader reader, OutputStream output, String charset) throws UnsupportedEncodingException, IOException {
		copyNClose(reader, new OutputStreamWriter(output, charset)) ;
	}
}
