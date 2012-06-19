package net.ion.framework.util.nio;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;

import net.ion.framework.util.Shell;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: I-ON Communications
 * </p>
 * 
 * @author bleujin
 * @version 1.0
 */

/*
 * Takes care of "endian" and unsigned mess + a couple of helper functions
 */
public class NIOUtils {
	// ------------------------------------------------------------------------------
	// Read from a stream
	// ------------------------------------------------------------------------------

	public static final byte[] readBytes(InputStream in, int nCount) throws IOException {
		byte[] buf = new byte[nCount];
		readFully(in, buf);

		return buf;
	}

	public static final short readUInt8(InputStream in) throws IOException {
		int ch = in.read();
		if (ch < 0) {
			throw new EOFException();
		}
		return (short) (ch & 0xff);
	}

	public static final int readUInt16(InputStream in) throws IOException {
		return (readUInt8(in) + readUInt8(in) * 256) & 0xffff;
	}

	public static final int readInt32(InputStream in) throws IOException {
		return readUInt8(in) + readUInt8(in) * 256 + readUInt8(in) * 256 * 256 + readUInt8(in) * 256 * 256 * 256;
	}

	public static final void readFully(InputStream in, byte[] buf) throws IOException {
		readFully(in, buf, 0, buf.length);
	}

	public static final void readFully(InputStream in, byte[] buf, int off, int len) throws IOException {
		if (len < 0) {
			throw new IndexOutOfBoundsException();
		}
		int n = 0;
		while (n < len) {
			int count = in.read(buf, off + n, len - n);
			if (count < 0) {
				throw new EOFException();
			}
			n += count;
		}
	}

	// ------------------------------------------------------------------------------
	// Read from a buffer
	// ------------------------------------------------------------------------------

	public static final short readUInt8(ByteBuffer in) throws IOException {
		int ch = in.get();
		if (ch < 0) {
			throw new EOFException();
		}
		return (short) (ch & 0xff);
	}

	public static final int readUInt16(ByteBuffer in) throws IOException {
		return (readUInt8(in) + readUInt8(in) * 256) & 0xffff;
	}

	public static final int readInt32(ByteBuffer in) throws IOException {
		return readUInt8(in) + readUInt8(in) * 256 + readUInt8(in) * 256 * 256 + readUInt8(in) * 256 * 256 * 256;
	}

	// ------------------------------------------------------------------------------
	// Write to a stream
	// ------------------------------------------------------------------------------

	// To be checked (seems to work fine ;))
	public static final void writeInt32(OutputStream out, int n) throws IOException {
		out.write((n >>> 0) & 0xFF);
		out.write((n >>> 8) & 0xFF);
		out.write((n >>> 16) & 0xFF);
		out.write((n >>> 24) & 0xFF);
	}

	// Yes this is also needed :)
	public static final void writeUInt24(OutputStream out, int n) throws IOException {
		out.write((n >>> 0) & 0xFF);
		out.write((n >>> 8) & 0xFF);
		out.write((n >>> 16) & 0xFF);
	}

	public static final void writeUInt16(OutputStream out, int n) throws IOException {
		out.write((n >>> 0) & 0xFF);
		out.write((n >>> 8) & 0xFF);
	}

	// This is fine, (byte)((int) 128) returns -128
	public static final void writeUInt8(OutputStream out, int n) throws IOException {
		out.write(n);
	}

	public static final void writeBytes(OutputStream out, String s) throws IOException {
		int len = s.length();
		for (int i = 0; i < len; i++) {
			out.write((byte) s.charAt(i));
		}
	}

	public static final void write(OutputStream out, byte[] buf) throws IOException {
		out.write(buf);
	}

	public static final void write(OutputStream out, byte[] buf, int off, int len) throws IOException {
		out.write(buf, off, len);
	}

	public static final void write(OutputStream out, EmSerializable obj) throws IOException {
		obj.writeSelf(out);
	}

	// ------------------------------------------------------------------------------
	// Write to a buffer
	// ------------------------------------------------------------------------------

	// To be checked (seems to work fine ;))
	public static final void writeInt32(ByteBuffer out, int n) throws IOException {
		out.put((byte) ((n >>> 0) & 0xFF));
		out.put((byte) ((n >>> 8) & 0xFF));
		out.put((byte) ((n >>> 16) & 0xFF));
		out.put((byte) ((n >>> 24) & 0xFF));
	}

	// Yes this is also needed :)
	public static final void writeUInt24(ByteBuffer out, int n) throws IOException {
		out.put((byte) ((n >>> 0) & 0xFF));
		out.put((byte) ((n >>> 8) & 0xFF));
		out.put((byte) ((n >>> 16) & 0xFF));
	}

	public static final void writeUInt16(ByteBuffer out, int n) throws IOException {
		out.put((byte) ((n >>> 0) & 0xFF));
		out.put((byte) ((n >>> 8) & 0xFF));
	}

	// This is fine, (byte)((int) 128) returns -128
	public static final void writeUInt8(ByteBuffer out, int n) throws IOException {
		out.put((byte) n);
	}

	public static final void writeBytes(ByteBuffer out, String s) throws IOException {
		int len = s.length();
		for (int i = 0; i < len; i++) {
			out.put((byte) s.charAt(i));
		}
	}

	public static final void write(ByteBuffer out, byte[] buf) throws IOException {
		out.put(buf);
	}

	public static final void write(ByteBuffer out, byte[] buf, int off, int len) throws IOException {
		out.put(buf, off, len);
	}

	// ------------------------------------------------------------------------------
	// IOStream wrappers around buffers
	// ------------------------------------------------------------------------------

	public static final InputStream getInputStream(ByteBuffer buf) {
		return new BufferInputStream(buf);
	}

	public static final OutputStream getOutputStream(ByteBuffer buf) {
		return new BufferOutputStream(buf);
	}

	// ------------------------------------------------------------------------------
	// Channels
	// ------------------------------------------------------------------------------

	public static void writeFully(WritableByteChannel channel, ByteBuffer buffer) throws IOException {
		int n = 0;
		int len = buffer.remaining();
		while (n < len) {
			n += channel.write(buffer);
		}
	}

	public static void copyCompletely(InputStream input, OutputStream output) throws IOException {
		// if both are file streams, use channel IO
		if ((output instanceof FileOutputStream) && (input instanceof FileInputStream)) {
			try {
				FileChannel target = ((FileOutputStream) output).getChannel();
				FileChannel source = ((FileInputStream) input).getChannel();

				source.transferTo(0, Integer.MAX_VALUE, target);

				source.close();
				target.close();

				return;
			} catch (Exception e) { /* failover to byte stream version */
			}
		}

		byte[] buf = new byte[8192];
		while (true) {
			int length = input.read(buf);
			if (length < 0)
				break;
			output.write(buf, 0, length);
		}

		try {
			input.close();
		} catch (IOException ignore) {
		}
		try {
			output.close();
		} catch (IOException ignore) {
		}
	}

	public static void copyCompletely(Reader input, Writer output) throws IOException {
		char[] buf = new char[8192];
		while (true) {
			int length = input.read(buf);
			if (length < 0)
				break;
			output.write(buf, 0, length);
		}

		try {
			input.close();
		} catch (IOException ignore) {
		}
		try {
			output.close();
		} catch (IOException ignore) {
		}
	}

	public static void copyCompletely(URI input, URI output) throws IOException {
		try {
			InputStream in = null;
			try {
				File f = new File(input);
				if (f.exists())
					in = new FileInputStream(f);
			} catch (Exception notAFile) {
			}

			File out = new File(output);
			Shell.forceMkdir(out.getParentFile());

			if (in == null)
				in = input.toURL().openStream();

			NIOUtils.copyCompletely(in, new FileOutputStream(out));
		} catch (IllegalArgumentException e) {
			throw new IOException("Cannot copy to " + output);
		}
	}

}
