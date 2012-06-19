package net.ion.framework.util.nio;

import java.io.IOException;
import java.io.OutputStream;
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

public class BufferOutputStream extends OutputStream {
	private ByteBuffer buffer;

	public void setBuffer(ByteBuffer buffer) {
		this.buffer = buffer;
	}

	public ByteBuffer getBuffer() {
		return buffer;
	}

	public BufferOutputStream() {
	}

	public BufferOutputStream(ByteBuffer buffer) {
		this.buffer = buffer;
	}

	/**
	 * Writes one byte of data
	 * 
	 * @param b
	 *            int
	 */
	public void write(int b) {
		buffer.put((byte) b);
	}

	public void write(byte[] b, int off, int len) throws IOException {
		buffer.put(b, off, len);
	}

}
