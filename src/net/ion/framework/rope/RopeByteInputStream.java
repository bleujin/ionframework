package net.ion.framework.rope;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

public class RopeByteInputStream extends InputStream {

	private ByteArrayInputStream real;

	public RopeByteInputStream(Rope rope, String encoding) throws UnsupportedEncodingException {
		real = new ByteArrayInputStream(rope.toString().getBytes(encoding));
	}

	public int read() throws IOException {
		return real.read();
	}

}
