package net.ion.framework.db.fake;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import net.ion.framework.db.Rows;
import net.ion.framework.db.procedure.MSSQLParamUtils;
import net.ion.framework.db.procedure.UserCommand;

public class FakeUserCommand extends UserCommand implements Executable {

	/*
	 * 이 클래스의 존재 이유는.. 가르켜 줄건 connection 밖에 없지만... 즉 이건 기존 레거시 사용방법을 크게 흐트리지 않으면서 해당 Framework의 기능 - 트랜잭션, 클라이언트 커서 등등- 은 모두 사용하고 싶을때이다.
	 * 
	 * connection을 닫을 책임은 기존 connection을 생성시키는 레거시 코드에 있으며 (connection이 풀로 관리되는지 알수 없기 때문이다.) Fake는 Connection을 닫지 않는다.
	 */

	public FakeUserCommand(Connection conn, String stmtSQL) {
		super(new FakeDBController(conn), stmtSQL);
	}

	@Override
	public int myUpdate(Connection conn) throws SQLException {
		int updateCount;
		try {
			pstmt = conn.prepareStatement(getProcSQL());
			MSSQLParamUtils.setCommandParam(pstmt, getParams(), getTypes());
			updateCount = pstmt.executeUpdate();
		} finally {
			closeSilence(pstmt);
		}
		return updateCount;
	}

}

interface Executable {
	public int execUpdate() throws SQLException;

	public Rows execQuery() throws SQLException;
}

class StoreContentZip {

	protected static final int ZIP_BUFFER = 2048;

	private long contentLength = 0;

	private long initialContentLength = -1;

	private OutputStream theOS = null;

	/**
	 * Constructor for StoreContentZip.
	 */
	public StoreContentZip() {
		super();
		contentLength = 0;
	}

	/**
	 * This method compress the input stream and returns the outputstream
	 * 
	 * @param InputStream
	 *            inIPS
	 * @exception IOException
	 *                ,ZipException
	 * @return the compressed OutputStream
	 */

	public void Zip(InputStream inIPS) throws IOException, ZipException {
		int byteCount = 0;
		contentLength = 0;
		initialContentLength = 0;
		byte data[] = new byte[ZIP_BUFFER];
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ZipOutputStream zoutp = new ZipOutputStream(baos);
		zoutp.putNextEntry(new ZipEntry("zippedfile"));
		while ((byteCount = inIPS.read(data, 0, ZIP_BUFFER)) != -1) {
			zoutp.write(data, 0, byteCount);
			initialContentLength += byteCount;
		}
		zoutp.finish();
		zoutp.flush();
		zoutp.close();
		baos.flush();
		baos.close();
		contentLength = (long) baos.size();
		theOS = baos;
	}

	/**
	 * This method decompress the input stream and returns the outputstream
	 * 
	 * @param InputStream
	 *            inIPS
	 * @exception IOException
	 *                ,ZipException
	 * @return the decompressed OutputStream
	 */
	public void UnZip(InputStream inIPS) throws IOException, ZipException {
		int byteCount = 0;
		contentLength = 0;
		byte indata[] = new byte[ZIP_BUFFER];
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ZipInputStream zinp = new ZipInputStream(inIPS);
		while (zinp.getNextEntry() != null) {
			while ((byteCount = zinp.read(indata, 0, ZIP_BUFFER)) != -1) {
				baos.write(indata, 0, byteCount);
			}
		}
		contentLength = (long) baos.size();
		baos.flush();
		baos.close();
		zinp.close();
		theOS = baos;
	}

	/**
	 * This method returns the compressed/decompressed stream as InputStream
	 * 
	 * @param void
	 * @exception IOException
	 *                ,ZipException
	 * @return the processed InputStream
	 */
	public InputStream getInputStream() throws IOException, ZipException {
		return new ByteArrayInputStream(((ByteArrayOutputStream) theOS).toByteArray());
	}

	/**
	 * This method returns the compressed/decompressed stream as O/PStream
	 * 
	 * @param void
	 * @exception IOException
	 *                ,ZipException
	 * @return the processed InputStream
	 */
	public OutputStream getOutputStream() throws IOException, ZipException {
		return theOS;
	}

	/**
	 * Gets the length.
	 * 
	 * @return return the length of the un/compressed Stream
	 */
	public long getContentLength() {
		return contentLength;
	}

	public long getInitialContentLength() {
		return initialContentLength;
	}

}

class JDBCAwareInputStream2 extends FilterInputStream {

	// ---------------------------------------------------------- Instance Data

	/**
	 * The JDBC statement that will be closed along with its result set when the input stream is told to close itself.
	 */
	private Connection conn = null;

	// ----------------------------------------------------------- Constructors

	/**
	 * Creates an input stream that closes a statmenet, a resultset and a connection when the stream itself is closed.
	 * 
	 */
	public JDBCAwareInputStream2(InputStream in, Connection connection) {
		super(in);
		this.conn = connection;
	}

	// --------------------------------------------- InputStream Implementation

	/**
	 * Overridden to close the associated JDBC statement, result set and connection together with the input stream.
	 */
	public void close() throws IOException {
		try {
			if (conn != null) {
				try {
					conn.commit();
				} catch (SQLException e) {
					throw new IOException(e.getMessage());
				} finally {
					try {
						conn.close();
					} catch (SQLException e) {
						throw new IOException(e.getMessage());
					}
				}
			}
		} finally {
			super.close();
		}
	}
}

class JDBCAwareInputStream extends FilterInputStream {

	// ---------------------------------------------------------- Instance Data

	/**
	 * The JDBC statement that will be closed along with its result set when the input stream is told to close itself.
	 */
	private Statement stmt = null;

	private ResultSet rs = null;

	private Connection connection = null;

	// ----------------------------------------------------------- Constructors

	/**
	 * Creates an input stream that closes a statmenet, a resultset and a connection when the stream itself is closed.
	 * 
	 */
	public JDBCAwareInputStream(InputStream in, Statement stmt, ResultSet rs, Connection connection) {
		super(in);

		this.stmt = stmt;
		this.rs = rs;
		this.connection = connection;
	}

	// --------------------------------------------- InputStream Implementation

	/**
	 * Overridden to close the associated JDBC statement, result set and connection together with the input stream.
	 */
	public void close() throws IOException {
		try {
			if (rs != null) {
				rs.close();
			}
		} catch (SQLException e) {
			throw new IOException(e.getMessage());
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (SQLException e) {
				throw new IOException(e.getMessage());
			} finally {
				try {
					if (connection != null) {
						try {
							connection.commit();
						} catch (SQLException e) {
							throw new IOException(e.getMessage());
						} finally {
							try {
								connection.close();
							} catch (SQLException e) {
								throw new IOException(e.getMessage());
							}
						}
					}
				} finally {
					super.close();
				}
			}
		}
	}
}