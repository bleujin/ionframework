package net.ion.framework.util.nio;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

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

public class BufferInputStream extends InputStream {
	private ByteBuffer buffer;

	public void setBuffer(ByteBuffer buffer) {
		this.buffer = buffer;
	}

	public ByteBuffer getBuffer() {
		return buffer;
	}

	public BufferInputStream() {
	}

	public BufferInputStream(ByteBuffer buffer) {
		this.buffer = buffer;
	}

	public int read() {
		return buffer.get();
	}

	public int read(byte[] dst, int off, int len) throws IOException {
		buffer.get(dst, off, len);
		// buffer underflow would be thrown if less then len bytes are read anyway,
		// so assume everything went ok
		return len;
	}

}
