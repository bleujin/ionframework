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

	public static void closeSilent(Closeable... cls) {
		for (Closeable cl : cls) {
			try {
				if (cl != null) cl.close();
			} catch (IOException ignore) {
			}
		}
	}
	
	public static void copyNCloseSilent(InputStream input, OutputStream output) throws IOException {
		try {
			IOUtil.copyLarge(input, output) ;
		} finally {
			IOUtil.closeSilent(input, output) ;
		}
	}

	public static void copyNCloseSilent(Reader reader, Writer writer) throws IOException {
		try {
			IOUtil.copyLarge(reader, writer) ;
		} finally {
			IOUtil.closeSilent(reader, writer) ;
		}
	}
	private static File tempDir = null;
	public static void setTempDir(File tempDir){
		IOUtil.tempDir = tempDir;
		Shell.forceMkdir(tempDir);
	}
	public static File createTempFile(String prefix) throws IOException {
		if (tempDir == null) {
			File tmpfile = File.createTempFile(prefix + "_" + ObjectId.get().toString(), ".tmp");
			tmpfile.deleteOnExit();
			return tmpfile;
		} else {
			File tmpfile = File.createTempFile(prefix + "_" + ObjectId.get().toString(), ".tmp", tempDir);
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
