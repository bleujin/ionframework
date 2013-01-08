package net.ion.framework.db;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Map;

import net.ion.framework.db.bean.ResultSetHandler;
import net.ion.framework.db.procedure.IQueryable;
import net.ion.framework.db.procedure.Queryable;
import net.ion.framework.db.rowset.WebRowSet;
import net.ion.framework.db.rowset.XmlWriter;
import net.ion.framework.parse.gson.JsonParser;
import net.ion.framework.util.IOUtil;
import net.ion.framework.util.MapUtil;
import net.ion.framework.util.StringUtil;

/**
 * CLOB precessing and Adapter Pattern
 * <p>
 * Title: Database framework
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: I-ON Communications
 * </p>
 * 
 * @author bleujin
 * @version 1.0
 */

public class RowsImpl extends WebRowSet implements Rows {

	private static final long serialVersionUID = -4186725660224959905L;
	private Rows nextRows = null;
	private transient Queryable query;
	private long modifyCount;
	private ScreenInfo screenInfo = ScreenInfo.UNKNOWN;

	protected RowsImpl(Queryable query) throws SQLException {
		super();
		this.query = query;
		// this.modifyCount = query.getCurrentModifyCount() ;
		setXmlWriter(new DefaultXmlWriter());
	}

	public String getString(int i) throws SQLException {
		ResultSetMetaData meta = this.getMetaData();
		if (meta.getColumnType(i) == Types.CLOB) {
			if (this.getClob(i) == null)
				return StringUtil.EMPTY;
			else {
				return this.getClobString(i);
			}
			// else return ClobToString2( ((OracleResultSet)this.getOriginalRow()).getCLOB(i) ) ;
		} else {
			return super.getString(i);
		}
	}

	protected String getClobString(int i) throws SQLException {
		return RowsUtils.clobToString(this.getClob(i));
	}

	public String getString(String column) throws SQLException {
		return getString(getColumnIndex(column));
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
		throw new SQLException("exception.framework.db.not_valid_column_name[" + column + "]");
	}

	// private Map nameToIndexMap = null;
	protected String getClobString(String column) throws SQLException {
		int i = getColumnIndex(column);

		return this.getClobString(i);
	}

	public int getRowCount() {
		return super.size();
	}

	public Rows setNextRows(Rows rows) {
		this.nextRows = rows;
		return rows;
	}

	public Rows getNextRows() {
		return nextRows;
	}

	public void setXmlWriter(IXmlWriter writer) throws SQLException {
		super.setXmlWriter(writer);
	}

	public void writeXml(Writer writer, IXmlWriter xmlWriter) throws SQLException {
		if (xmlWriter != null) {
			setXmlWriter(xmlWriter);
		}
		writeXml(writer);
	}

	public IXmlWriter getIXmlWriter() throws SQLException {
		return (IXmlWriter) super.getXmlWriter();
	}

	public String toString() {
		StringWriter writer = new StringWriter();
		XmlWriter swriter = null;
		try {
			swriter = getXmlWriter();
			if (swriter == null) {
				writer.append(RowsImpl.class.getCanonicalName() + "@" + hashCode());
			} else {
				swriter.writeXML(this, writer);
				// this.writeXml(writer);
			}
		} catch (Exception ignore) {
			ignore.printStackTrace();
		} finally {
			writer.flush();
			IOUtil.closeQuietly(writer);
		}
		return writer.toString();
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

	public <T> T toHandle(ResultSetHandler<T> rsh) throws SQLException {
		return rsh.handle(this);
	}

	// @TODO ... Cache 
	public synchronized Rows refreshRows(boolean useCache) throws SQLException {
		if (useCache && query.getCurrentModifyCount() >= 0 && (query.getCurrentModifyCount() == this.modifyCount)) {
			return this;
		}
		return query.execQuery();
	}

	public IQueryable getQueryable() {
		if (query == null)
			throw new IllegalStateException("maybe cached rows. this rows cached only result rows..");
		return query;
	}

	// not impl..
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

	public URL getURL(String columnName) throws SQLException {
		return getURL(getColumnIndex(columnName));
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

	public void clearQuery() {
		this.query = null;
	}

	public ScreenInfo getScreenInfo() {
		return this.screenInfo;
	}

	public void setScreenInfo(ScreenInfo screenInfo) {
		this.screenInfo = screenInfo;
	}

	public Rows toClone() throws IOException {
		try {
			
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			ObjectOutputStream output = new ObjectOutputStream(out);
			output.writeObject(this);
			output.flush() ;
			output.close() ;
			return (Rows) new ObjectInputStream(new ByteArrayInputStream(out.toByteArray())).readObject();
		} catch (ClassNotFoundException e) {
			throw new IOException(e.getMessage());
		}
	}

	public void debugPrint() throws SQLException{
		DebugHandler.handle(this) ;
	}
	
	public static ResultSetHandler<Void> DebugHandler = new ResultSetHandler<Void>(){
		private static final long serialVersionUID = 9075443775350544529L;

		public Void handle(ResultSet rs) throws SQLException {
			ResultSetMetaData meta = rs.getMetaData();
			while(rs.next()){
				Map<String, Object> rowMap = MapUtil.newMap() ; 
				for (int i = 1 ; i <= meta.getColumnCount() ; i++) {
					rowMap.put(meta.getColumnLabel(i), rs.getObject(i)) ;
				} 
				System.out.println(JsonParser.fromMap(rowMap).toString()) ;
			}
			return null;
		}		
	} ;
}
