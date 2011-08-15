package net.ion.framework.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;

/**
 * stream ���� ����, byte �迭�� ����, byte �迭�� stream���� ��ȯ ���� �۾��� �Ѵ�.
 * 
 * @author Kim, Sanghoon (wizest@i-on.net)
 * @version 1.0
 */

public class StreamUtils {
	private StreamUtils() {
	}

	/**
	 * ���� �ϰ� ���� stream�� �ݴ´�. (in/out ���)
	 * 
	 * @param is
	 *            InputStream
	 * @param os
	 *            OutputStream
	 * @throws IOException
	 */
	public static void copy(InputStream is, OutputStream os) throws IOException {
		copy(is, os, true);
		// NIOUtils.copyCompletely(fis, os);
	}

	/**
	 * is,os�� �����Ѵ�.
	 * 
	 * @param is
	 *            InputStream
	 * @param os
	 *            OutputStream
	 * @param closeStreams
	 *            boolean �������� ��Ʈ���� ������ ����
	 * @throws IOException
	 */
	public static void copy(InputStream is, OutputStream os, final boolean closeStreams) throws IOException {
		IOUtils.copy(is, os);
		if (closeStreams) {
			is.close();
			os.close();
		}

		// if (is instanceof FileInputStream ) {
		// if(os instanceof FileOutputStream) {
		// FileInputStream fis=(FileInputStream)is;
		// FileOutputStream fos=(FileOutputStream)os;
		// FileChannel fc=fis.getChannel();
		// FileChannel fc2=fos.getChannel();
		//
		// ByteBuffer buff=ByteBuffer.allocateDirect(4096);
		// while(fc.read(buff) >= 0) {
		// buff.flip();
		// fc2.write(buff);
		// buff.clear();
		// }
		//
		// fos.flush();
		// if (closeStreams){
		// fos.close();
		// fis.close();
		// }
		// }
		// else {
		// ByteBuffer buff=ByteBuffer.allocate(4096);
		// FileChannel fc=((FileInputStream)is).getChannel();
		//
		// while(true) {
		// int ret=fc.read(buff);
		// if(ret == -1) {
		// break;
		// }
		// buff.flip();
		// os.write(buff.array(),0,ret);
		// buff.clear();
		// }
		// os.flush();
		// if (closeStreams){
		// os.close();
		// is.close();
		// }
		// }
		// }
		// else {
		// BufferedInputStream in = new BufferedInputStream(is);
		// BufferedOutputStream out = new BufferedOutputStream(os);
		//
		// byte[] buff = new byte[4096];
		// for (; ; ) {
		// int read = in.read(buff);
		// if (read < 0)
		// break;
		// out.write(buff, 0, read);
		// }
		// out.flush();
		// if (closeStreams){
		// out.close();
		// in.close();
		// }
		// }
	}

	/**
	 * stream�� byte �迭��
	 * 
	 * @param is
	 *            InputStream
	 * @throws IOException
	 * @return byte[]
	 */
	public static byte[] toByteArray(InputStream is) throws IOException {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		copy(is, os);
		return os.toByteArray();
	}

	/**
	 * byte�迭�� stream����
	 * 
	 * @param bytes
	 *            byte[]
	 * @return InputStream
	 */
	public static InputStream toInputStream(byte[] bytes) {
		ByteArrayInputStream is = new ByteArrayInputStream(bytes);
		return is;
	}

	public static String getStringFromFile(File f, String charset) throws IOException {
		FileInputStream fis = new FileInputStream(f);
		return new String(toByteArray(fis), charset);
	}

}
