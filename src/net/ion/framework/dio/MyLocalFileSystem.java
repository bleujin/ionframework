package net.ion.framework.dio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.nio.ByteBuffer;

import net.ion.framework.configuration.Configuration;

public class MyLocalFileSystem extends IOSystem {

	private URI uri;
	private Path workingDir;

	public MyLocalFileSystem(URI uri, Configuration conf) {
		this.uri = uri;
		this.workingDir = Path.create(System.getProperty("user.dir")).makeQualified(this);
	}

	@Override
	public URI getUri() {
		return uri;
	}

	@Override
	public Path getWorkingDirectory() {
		return workingDir;
	}

	public String toString() {
		return "LocalFS";
	}

	public Path getHomeDirectory() {
		return Path.create(System.getProperty("user.home")).makeQualified(this);
	}

	public void setWorkingDirectory(Path newDir) {
		workingDir = newDir;
	}

	public File pathToFile(Path path) {
		if (!path.isAbsolute()) {
			path = Path.create(getWorkingDirectory(), path);
		}
		return new File(path.toUri());
	}

	public FSInputStream open(Path path) throws IOException {
		return new LocalFSFileInputStream(path);
	}

	public FSDataInputStream open(Path path, int bufferSize) throws IOException {
		if (!exists(path)) {
			throw new FileNotFoundException(path.toString());
		}
		return new FSDataInputStream(new BufferedFSInputStream(new LocalFSFileInputStream(path), bufferSize));
	}
	
	
	public boolean exists(Path path) {
		return pathToFile(path).exists();
	}

	public FSDataOutputStream create(Path path) throws IOException {
		return new FSDataOutputStream(new LocalFSFileOutputStream(path, false));
	}








	class LocalFSFileInputStream extends FSInputStream {
		FileInputStream fis;
		private long position;

		public LocalFSFileInputStream(Path path) throws IOException {
			this.fis = new TrackingFileInputStream(pathToFile(path));
		}

		public void seek(long pos) throws IOException {
			fis.getChannel().position(pos);
			this.position = pos;
		}

		public long getPos() throws IOException {
			return this.position;
		}

		public boolean seekToNewSource(long targetPos) throws IOException {
			return false;
		}

		/*
		 * Just forward to the fis
		 */
		public int available() throws IOException {
			return fis.available();
		}

		public void close() throws IOException {
			fis.close();
		}

		public boolean markSupport() {
			return false;
		}

		public int read() throws IOException {
			try {
				int value = fis.read();
				if (value >= 0) {
					this.position++;
				}
				return value;
			} catch (IOException e) { // unexpected exception
				throw new FSError(e); // assume native fs error
			}
		}

		public int read(byte[] b, int off, int len) throws IOException {
			try {
				int value = fis.read(b, off, len);
				if (value > 0) {
					this.position += value;
				}
				return value;
			} catch (IOException e) { // unexpected exception
				throw new FSError(e); // assume native fs error
			}
		}

		public int read(long position, byte[] b, int off, int len) throws IOException {
			ByteBuffer bb = ByteBuffer.wrap(b, off, len);
			try {
				return fis.getChannel().read(bb, position);
			} catch (IOException e) {
				throw new FSError(e);
			}
		}

		public long skip(long n) throws IOException {
			long value = fis.skip(n);
			if (value > 0) {
				this.position += value;
			}
			return value;
		}
	}

	class LocalFSFileOutputStream extends OutputStream implements Syncable {
		FileOutputStream fos;

		private LocalFSFileOutputStream(Path f, boolean append) throws IOException {
			this.fos = new FileOutputStream(pathToFile(f), append);
		}

		public void close() throws IOException {
			fos.close();
		}

		public void flush() throws IOException {
			fos.flush();
		}

		public void write(byte[] b, int off, int len) throws IOException {
			try {
				fos.write(b, off, len);
			} catch (IOException e) { // unexpected exception
				throw new FSError(e); // assume native fs error
			}
		}

		public void write(int b) throws IOException {
			try {
				fos.write(b);
			} catch (IOException e) { // unexpected exception
				throw new FSError(e); // assume native fs error
			}
		}

		/** {@inheritDoc} */
		public void sync() throws IOException {
			fos.getFD().sync();
		}
	}

	class TrackingFileInputStream extends FileInputStream {
		public TrackingFileInputStream(File f) throws IOException {
			super(f);
		}

		public int read() throws IOException {
			int result = super.read();
			if (result != -1) {
				// statistics.incrementBytesRead(1);
			}
			return result;
		}

		public int read(byte[] data) throws IOException {
			int result = super.read(data);
			if (result != -1) {
				// statistics.incrementBytesRead(result);
			}
			return result;
		}

		public int read(byte[] data, int offset, int length) throws IOException {
			int result = super.read(data, offset, length);
			if (result != -1) {
				// statistics.incrementBytesRead(result);
			}
			return result;
		}
	}

}
