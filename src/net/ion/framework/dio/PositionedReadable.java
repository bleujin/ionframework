package net.ion.framework.dio;

import java.io.IOException;

public interface PositionedReadable {
	public int read(long position, byte[] buffer, int offset, int length) throws IOException;
	public void readFully(long position, byte[] buffer, int offset, int length) throws IOException;
	public void readFully(long position, byte[] buffer) throws IOException;
}
