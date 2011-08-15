package net.ion.framework.db;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import javax.sql.RowSet;

import net.ion.framework.db.procedure.Queryable;
import net.ion.framework.util.IOUtil;

import org.apache.commons.io.IOUtils;

public class RowsUtils {

	public static RowsImpl create(Queryable query) throws SQLException {
		return new RowsImpl(query);
	}

	public static String clobToString(Clob clob) throws SQLException {
		if (clob == null || clob.length() == 0L)
			return "";
		try {
			return IOUtils.toString(clob.getCharacterStream());
		} catch (SQLException ex) {
			throw ex;
		} catch (IOException ex) {
			throw new SQLException("Not Known Clob Foramt : can't make clob string" + ex.getMessage());
		}
	}

	public static StringBuffer clobToStringBuffer(Clob clob) throws SQLException {
		return new StringBuffer(clobToString(clob));
	}

	public static String blobToFileName(Blob blob) throws SQLException {
		InputStream is = null;
		File file = null;
		FileOutputStream fos = null;

		try {
			file = IOUtil.createTempFile("rows_");
			fos = new FileOutputStream(file);
			is = blob.getBinaryStream();

			IOUtils.copy(is, fos);

			fos.flush();
			fos.close();
			is.close();
			file.delete();
		} catch (IOException ex) {
			throw new SQLException("Blob: " + ex.getMessage());
		} finally {
			try {
				if (fos != null) {
					fos.close();
				}
				if (is != null) {
					is.close();
				}
			} catch (IOException ex) {
			}
		}

		return file.getAbsolutePath();
	}

	public static List<Object> getCurrentTuple(RowSet rs) throws SQLException {
		return getTuple(rs);
	}

	private static ArrayList<Object> getTuple(RowSet rs) throws SQLException {
		ArrayList<Object> tuple = new ArrayList<Object>();
		ResultSetMetaData meta = rs.getMetaData();

		for (int i = 1, last = meta.getColumnCount(); i <= last; i++) {
			// clob Ã³¸®
			if (meta.getColumnType(i) == Types.CLOB) {
				if (rs.getClob(i) == null) {
					tuple.add(i - 1, null);
				} else {
					tuple.add(i - 1, RowsUtils.clobToString(rs.getClob(i)));
				}
			} else if (meta.getColumnType(i) == Types.NULL) {
				tuple.add(i - 1, null);
			} else {
				tuple.add(i - 1, rs.getObject(i));
			}
		}
		return tuple;
	}

}
