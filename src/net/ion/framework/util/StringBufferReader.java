package net.ion.framework.util;

import java.io.IOException;
import java.io.Reader;

public class StringBufferReader extends Reader {

	// TODO (3.0): change to StringBuffer (including the name of the class)

	// The StringBuffer to read from.
	private StringBuffer sb;

	// The length of 'sb'.
	private int length;

	// The next position to read from the StringBuffer.
	private int next = 0;

	// The mark position. The default value 0 means the start of the text.
	private int mark = 0;

	public StringBufferReader(StringBuffer sb) {
		set(sb);
	}

	/** Check to make sure that the stream has not been closed. */
	private void ensureOpen() throws IOException {
		if (sb == null) {
			throw new IOException("Stream has already been closed");
		}
	}

	public void close() {
		synchronized (lock) {
			sb = null;
		}
	}

	public void mark(int readAheadLimit) throws IOException {
		if (readAheadLimit < 0) {
			throw new IllegalArgumentException("Read-ahead limit cannpt be negative: " + readAheadLimit);
		}
		synchronized (lock) {
			ensureOpen();
			mark = next;
		}
	}

	public boolean markSupported() {
		return true;
	}

	public int read() throws IOException {
		synchronized (lock) {
			ensureOpen();
			return next >= length ? -1 : sb.charAt(next++);
		}
	}

	public int read(char cbuf[], int off, int len) throws IOException {
		synchronized (lock) {
			ensureOpen();

			// Validate parameters
			if (off < 0 || off > cbuf.length || len < 0 || off + len > cbuf.length) {
				throw new IndexOutOfBoundsException("off=" + off + " len=" + len + " cbuf.length=" + cbuf.length);
			}

			if (len == 0) {
				return 0;
			}

			if (next >= length) {
				return -1;
			}

			int n = Math.min(length - next, len);
			sb.getChars(next, next + n, cbuf, off);
			next += n;
			return n;
		}
	}

	public boolean ready() throws IOException {
		synchronized (lock) {
			ensureOpen();
			return true;
		}
	}

	public void reset() throws IOException {
		synchronized (lock) {
			ensureOpen();
			next = mark;
			length = sb.length();
		}
	}

	public void set(StringBuffer sb) {
		synchronized (lock) {
			this.sb = sb;
			length = sb.length();
		}
	}

	public long skip(long ns) throws IOException {
		synchronized (lock) {
			ensureOpen();
			if (next >= length) {
				return 0;
			}

			// Bound skip by beginning and end of the source
			long n = Math.min(length - next, ns);
			n = Math.max(-next, n);
			next += n;
			return n;
		}
	}

}
