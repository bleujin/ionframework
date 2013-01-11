package net.ion.framework.db.procedure;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Ref;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;

import net.ion.framework.db.DefaultXmlWriter;
import net.ion.framework.db.IXmlWriter;
import net.ion.framework.db.Page;
import net.ion.framework.db.RepositoryException;
import net.ion.framework.db.Row;
import net.ion.framework.db.Rows;
import net.ion.framework.db.RowsImpl;
import net.ion.framework.db.RowsUtils;
import net.ion.framework.db.ScreenInfo;
import net.ion.framework.db.Transformer;
import net.ion.framework.db.bean.ResultSetHandler;
import net.ion.framework.db.rowset.WebRowSet;
import net.ion.framework.util.StringUtil;

public class H2Rows extends WebRowSet implements Rows {

	private static final long serialVersionUID = 646563401686178061L;

	private Rows nextRows = null;
	private IXmlWriter xmlWriter = null;
	private IQueryable query;

	H2Rows(IQueryable query) throws SQLException {
		super();
		this.query = query;
		setXmlWriter(new DefaultXmlWriter());
	}

	public String getString(int i) throws SQLException {
		return super.getString(i);
		// return getClobString(i);
	}

	public InputStream getBinaryStream(int i) throws SQLException {
		Object obj = super.getBinaryStream(i);
		if (obj == null) {
			return null;
		} else {
			// System.out.println("obj................ : " + obj.getClass().getName()) ;
			return (InputStream) obj;
			// binaryStream = new ByteArrayInputStream((byte[]) (byte[]) obj);
			// return binaryStream;
		}
	}

	private String getClobString(int i) throws SQLException {
		ResultSetMetaData meta = this.getMetaData();
		if (meta.getColumnType(i) == Types.CLOB) {
			if (this.getClob(i) == null)
				return StringUtil.EMPTY;
			else {
				return RowsUtils.clobToString(this.getClob(i));
			}
		} else {
			return super.getString(i);
		}
	}

	public String getString(String column) throws SQLException {
		return super.getString(column);
		// return getClobString(column);
	}

	public Object getObject(int i) throws SQLException {
		return getClobObject(i);
	}

	private Object getClobObject(int i) throws SQLException {
		ResultSetMetaData meta = this.getMetaData();
		if (meta.getColumnType(i) == Types.CLOB) {
			if (this.getClob(i) == null)
				return StringUtil.EMPTY;
			else
				return RowsUtils.clobToString(this.getClob(i));
		} else {
			return super.getObject(i);
		}
	}

	public Object getObject(String column) throws SQLException {
		return getClobObject(getColumnIndex(column));
	}

	public boolean getBoolean(int i) throws SQLException {
		ResultSetMetaData meta = this.getMetaData();

		if (meta.getColumnType(i) == Types.VARCHAR || meta.getColumnType(i) == Types.CHAR) {
			if (super.getString(i) == null)
				return false;
			return (StringUtil.T.equals(super.getString(i).toUpperCase()) || StringUtil.TRUE.equals(super.getString(i).toUpperCase())) ? true : false;
		} else {
			return super.getBoolean(i);
		}
	}

	public boolean getBoolean(String column) throws SQLException {
		return getBoolean(getColumnIndex(column));
	}

	private int getColumnIndex(String column) throws SQLException {
		ResultSetMetaData meta = this.getMetaData();
		for (int i = 1, last = meta.getColumnCount(); i <= last; i++) {
			if (meta.getColumnName(i).toUpperCase().equals(column.toUpperCase())) {
				return i;
			}
		}
		throw new SQLException("Not Valid Column Name. " + column + " Not Exists");
	}

	// private Map nameToIndexMap = null;
	private String getClobString(String column) throws SQLException {
		ResultSetMetaData meta = this.getMetaData();
		int i = getColumnIndex(column);

		if (meta.getColumnType(i) == Types.CLOB) {
			return this.getClobString(i);
		} else {
			return super.getString(i);
		}
	}

	public int getRowCount() {
		try {
			int result = 0;
			int currLoc = super.getRow();
			if (!last()) {
				return 0;
			}
			result = super.getRow();

			if (currLoc > 0) {
				absolute(currLoc);
			} else {
				beforeFirst();
			}
			return result;
		} catch (SQLException ex) {
			throw RepositoryException.throwIt(ex);
		}
	}

	public Rows setNextRows(Rows rows) {
		this.nextRows = rows;
		return rows;
	}

	public Rows getNextRows() {
		return nextRows;
	}

	public void setXmlWriter(IXmlWriter writer) throws SQLException {
		this.xmlWriter = writer;
	}

	public void writeXml(Writer writer, IXmlWriter xmlWriter) throws SQLException {
		if (xmlWriter != null) {
			setXmlWriter(xmlWriter);
		}
		writeXml(writer);
	}

	public IXmlWriter getIXmlWriter() throws SQLException {
		return this.xmlWriter;
	}

	public String toString() {
		Writer writer = new StringWriter();
		try {
			this.writeXml(writer);
			writer.flush();
			writer.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (writer != null)
					writer.close();
			} catch (IOException ignore) {
			}
		}
		return writer.toString();
	}

	public URL getURL(int columnIndex) {
		throw new UnsupportedOperationException();
	}

	public void updateArray(int columnIndex, Array x) {
		throw new UnsupportedOperationException();
	}

	public void updateBlob(int columnIndex, Blob x) {
		throw new UnsupportedOperationException();
	}

	public void updateClob(int columnIndex, Clob x) {
		throw new UnsupportedOperationException();
	}

	public void updateRef(int columnIndex, Ref x) {
		throw new UnsupportedOperationException();
	}

	public URL getURL(String columnName) {
		throw new UnsupportedOperationException();
	}

	public void updateArray(String columnName, Array x) {
		throw new UnsupportedOperationException();
	}

	public void updateBlob(String columnName, Blob x) {
		throw new UnsupportedOperationException();
	}

	public void updateClob(String columnName, Clob x) {
		throw new UnsupportedOperationException();
	}

	public void updateRef(String columnName, Ref x) {
		throw new UnsupportedOperationException();
	}

	public String getDefaultString(String col1, String defaultString) throws SQLException {
		String str = this.getString(col1);
		return (str == null || "".equals(str)) ? defaultString : col1;
	}

	public Row firstRow() {
		return Transformer.fetchFirstToRow(this);
	}

	public Rows nextPageRows() throws SQLException {
		Page nextPage = query.getPage().getNextPage();
		query.setPage(nextPage);
		return query.execQuery();
	}

	public Rows prePageRows() throws SQLException {
		query.setPage(query.getPage().getPrePage());
		return query.execQuery();

	}

	public Object toHandle(ResultSetHandler rsh) throws SQLException {
		return rsh.handle(this);
	}

	public Rows refreshRows(boolean useCache) throws SQLException {
		throw new UnsupportedOperationException();
	}

	public IQueryable getQueryable() {
		return this.query;
	}

	public void clearQuery() {
		this.query = null;
	}

	public ScreenInfo getScreenInfo() {
		return ScreenInfo.UNKNOWN;
	}

	public Rows toClone() throws IOException {
		throw new UnsupportedOperationException() ;
	}

	public void debugPrint() throws SQLException {
		RowsImpl.DebugHandler.handle(this) ;
	}

}
