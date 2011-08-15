package net.ion.framework.dio;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Utility that wraps a {@link OutputStream} in a {@link DataOutputStream}, buffers output through a {@link BufferedOutputStream} and creates a checksum file.
 */
public class FSDataOutputStream extends DataOutputStream implements Syncable {
	private OutputStream wrappedStream;

	private static class PositionCache extends FilterOutputStream {
		long position;

		public PositionCache(OutputStream out, long pos) throws IOException {
			super(out);
			position = pos;
		}

		public void write(int b) throws IOException {
			out.write(b);
			position++;
		}

		public void write(byte b[], int off, int len) throws IOException {
			out.write(b, off, len);
			position += len; // update position
		}

		public long getPos() throws IOException {
			return position; // return cached position
		}

		public void close() throws IOException {
			out.close();
		}
	}

	public FSDataOutputStream(OutputStream out) throws IOException {
		this(out, 0);
	}

	public FSDataOutputStream(OutputStream out, long startPosition) throws IOException {
		super(new PositionCache(out, startPosition));
		wrappedStream = out;
	}

	public long getPos() throws IOException {
		return ((PositionCache) out).getPos();
	}

	public void close() throws IOException {
		out.close(); // This invokes PositionCache.close()
	}

	// Returns the underlying output stream. This is used by unit tests.
	public OutputStream getWrappedStream() {
		return wrappedStream;
	}

	/** {@inheritDoc} */
	public void sync() throws IOException {
		if (wrappedStream instanceof Syncable) {
			((Syncable) wrappedStream).sync();
		}
	}
}
