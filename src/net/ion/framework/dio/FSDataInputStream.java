package net.ion.framework.dio;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Utility that wraps a {@link FSInputStream} in a {@link DataInputStream} and buffers input through a {@link BufferedInputStream}.
 */
public class FSDataInputStream extends DataInputStream implements Seekable, PositionedReadable {

	public FSDataInputStream(InputStream in) throws IOException {
		super(in);
		if (!(in instanceof Seekable) || !(in instanceof PositionedReadable)) {
			throw new IllegalArgumentException("In is not an instance of Seekable or PositionedReadable");
		}
	}

	public synchronized void seek(long desired) throws IOException {
		((Seekable) in).seek(desired);
	}

	public long getPos() throws IOException {
		return ((Seekable) in).getPos();
	}

	public int read(long position, byte[] buffer, int offset, int length) throws IOException {
		return ((PositionedReadable) in).read(position, buffer, offset, length);
	}

	public void readFully(long position, byte[] buffer, int offset, int length) throws IOException {
		((PositionedReadable) in).readFully(position, buffer, offset, length);
	}

	public void readFully(long position, byte[] buffer) throws IOException {
		((PositionedReadable) in).readFully(position, buffer, 0, buffer.length);
	}

	public boolean seekToNewSource(long targetPos) throws IOException {
		return ((Seekable) in).seekToNewSource(targetPos);
	}
}
