package net.ion.framework.db;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import java.math.BigDecimal;
import java.net.URL;
import java.nio.channels.UnsupportedAddressTypeException;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.NClob;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Map;

import javax.sql.RowSetListener;
import javax.sql.RowSetMetaData;

import net.ion.framework.db.bean.ResultSetHandler;
import net.ion.framework.db.procedure.IQueryable;
import net.ion.framework.parse.gson.JsonParser;
import net.ion.framework.util.CaseInsensitiveHashMap;
import net.ion.framework.util.StringUtil;

public class FakeRows implements Rows {

	private ArrayList<VColumn> columns = new ArrayList<VColumn>();
	private ArrayList<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
	private int currIndex = -1;

	public FakeRows() {

	}

	public void addColumn(String name, int type) {
		columns.add(new VColumn(name, name, type));
	}

	public void addColumn(String name, String alias, int type) {
		columns.add(new VColumn(name, alias, type));
	}


	public void addRow(Map newRow) {
		Map<String, Object> row = new CaseInsensitiveHashMap<Object>();

		for (VColumn col : columns) {
			Object value = newRow.get(col.getName());
			row.put(col.getAlias(), value);
		}

		rows.add(row);
	}

	public Row firstRow() {
		return getIndexedRow(0);
	}

	private Row getCurrentRow() {
		return getIndexedRow(currIndex);
	}

	private Row getIndexedRow(int idx) {
		String[] cols = new String[columns.size()];
		for (int i = 0; i < cols.length; i++) {
			cols[i] = columns.get(i).getAlias();
		}

		return new Row(rows.get(idx), cols);
	}

	public IQueryable getQueryable() {
		return null;
	}

	public String getDefaultString(String col1, String defaultString) throws SQLException {
		return StringUtil.defaultIfEmpty(getCurrentRow().getString(col1), defaultString);
	}

	public IXmlWriter getIXmlWriter() throws SQLException {
		return null;
	}

	public Rows getNextRows() {
		return null;
	}

	public int getRowCount() {
		return rows.size();
	}

	public boolean getShowDeleted() throws SQLException {
		return false;
	}

	public String getTableName() throws SQLException {
		return null;
	}

	public Rows nextPageRows() throws SQLException {
		return null;
	}

	public void populate(ResultSet rs) throws SQLException {
		
	}

	public void populate(ResultSet rs, int skip, int length) throws SQLException {
		
	}

	public Rows prePageRows() throws SQLException {
		return null;
	}

	public Rows refreshRows(boolean useCache) throws SQLException {
		return null;
	}

	public Rows setNextRows(Rows rows) {
		return null;
	}

	public void setTableName(String name) throws SQLException {
		
	}

	public void setXmlWriter(IXmlWriter writer) throws SQLException {
		
	}

	public Collection toCollection() throws SQLException {
		return null;
	}

	public Object toHandle(ResultSetHandler rsh) throws SQLException {
		this.beforeFirst() ;
		return rsh.handle(this);
	}

	public void writeXml(Writer writer, IXmlWriter xmlWriter) throws SQLException {
		
	}

	public void writeXml(Writer writer) throws SQLException, IOException {
		
	}

	public void addRowSetListener(RowSetListener arg0) {
		
	}

	public void clearParameters() throws SQLException {
		
	}
		

	public void execute() throws SQLException {
		
	}

	public String getCommand() {
		return null;
	}

	public String getDataSourceName() {
		return null;
	}

	public boolean getEscapeProcessing() throws SQLException {
		return false;
	}

	public int getMaxFieldSize() throws SQLException {
		return 0;
	}

	public int getMaxRows() throws SQLException {
		return 0;
	}

	public String getPassword() {
		return null;
	}

	public int getQueryTimeout() throws SQLException {
		return 0;
	}

	public int getTransactionIsolation() {
		return 0;
	}

	public Map<String, Class<?>> getTypeMap() throws SQLException {
		return null;
	}

	public String getUrl() throws SQLException {
		return null;
	}

	public String getUsername() {
		return null;
	}

	public boolean isReadOnly() {
		return false;
	}

	public void removeRowSetListener(RowSetListener arg0) {
		
	}

	public void setArray(int arg0, Array arg1) throws SQLException {
		
	}

	public void setAsciiStream(int arg0, InputStream arg1, int arg2) throws SQLException {
		
	}

	public void setBigDecimal(int arg0, BigDecimal arg1) throws SQLException {
			}

	public void setBinaryStream(int arg0, InputStream arg1, int arg2) throws SQLException {
		
	}

	public void setBlob(int arg0, Blob arg1) throws SQLException {
		
	}

	public void setBoolean(int arg0, boolean arg1) throws SQLException {
		
	}

	public void setByte(int arg0, byte arg1) throws SQLException {
		
	}

	public void setBytes(int arg0, byte[] arg1) throws SQLException {
		
	}

	public void setCharacterStream(int arg0, Reader arg1, int arg2) throws SQLException {
		
	}

	public void setClob(int arg0, Clob arg1) throws SQLException {
		
	}

	public void setCommand(String arg0) throws SQLException {
		
	}

	public void setConcurrency(int arg0) throws SQLException {
		
	}

	public void setDataSourceName(String arg0) throws SQLException {
		
	}

	public void setDate(int arg0, Date arg1) throws SQLException {
		
	}

	public void setDate(int arg0, Date arg1, Calendar arg2) throws SQLException {
		
	}

	public void setDouble(int arg0, double arg1) throws SQLException {
		
	}

	public void setEscapeProcessing(boolean arg0) throws SQLException {
		
	}

	public void setFloat(int arg0, float arg1) throws SQLException {
		
	}

	public void setInt(int arg0, int arg1) throws SQLException {
		
	}

	public void setLong(int arg0, long arg1) throws SQLException {
		
	}

	public void setMaxFieldSize(int arg0) throws SQLException {
		
	}

	public void setMaxRows(int arg0) throws SQLException {
		
	}

	public void setNull(int arg0, int arg1) throws SQLException {
		
	}

	public void setNull(int arg0, int arg1, String arg2) throws SQLException {
		
	}

	public void setObject(int arg0, Object arg1) throws SQLException {
		
	}

	public void setObject(int arg0, Object arg1, int arg2) throws SQLException {
		
	}

	public void setObject(int arg0, Object arg1, int arg2, int arg3) throws SQLException {
		
	}

	public void setPassword(String arg0) throws SQLException {
		
	}

	public void setQueryTimeout(int arg0) throws SQLException {
		
	}

	public void setReadOnly(boolean arg0) throws SQLException {
		
	}

	public void setRef(int arg0, Ref arg1) throws SQLException {
		
	}

	public void setShort(int arg0, short arg1) throws SQLException {
		
	}

	public void setString(int arg0, String arg1) throws SQLException {
		
	}

	public void setTime(int arg0, Time arg1) throws SQLException {
		
	}

	public void setTime(int arg0, Time arg1, Calendar arg2) throws SQLException {
		
	}

	public void setTimestamp(int arg0, Timestamp arg1) throws SQLException {
		
	}

	public void setTimestamp(int arg0, Timestamp arg1, Calendar arg2) throws SQLException {
		
	}

	public void setTransactionIsolation(int arg0) throws SQLException {
		
	}

	public void setType(int arg0) throws SQLException {
		
	}

	// public void setTypeMap(Map<String, Class<?>> arg0) throws SQLException {
	// 
	//		
	// }

	public void setUrl(String arg0) throws SQLException {
		
	}

	public void setUsername(String arg0) throws SQLException {
		
	}

	public boolean absolute(int arg0) throws SQLException {
		return false;
	}

	public void afterLast() throws SQLException {
		
	}

	public void beforeFirst() throws SQLException {
		
	}

	public void cancelRowUpdates() throws SQLException {
		
	}

	public void clearWarnings() throws SQLException {
		
	}

	public void close() throws SQLException {
		
	}

	public void deleteRow() throws SQLException {
		
	}

	public int findColumn(String arg0) throws SQLException {
		return 0;
	}

	public boolean first() throws SQLException {
		if (this.rows.size() > 0){
			this.currIndex = 0 ;
			return true ;
		}
		return false;
	}

	public Array getArray(int arg0) throws SQLException {
		return null;
	}

	public Array getArray(String arg0) throws SQLException {
		return null;
	}

	public InputStream getAsciiStream(int arg0) throws SQLException {
		return null;
	}

	public InputStream getAsciiStream(String arg0) throws SQLException {
		return null;
	}

	public BigDecimal getBigDecimal(int arg0) throws SQLException {
		return null;
	}

	public BigDecimal getBigDecimal(String arg0) throws SQLException {
		return null;
	}

	public BigDecimal getBigDecimal(int arg0, int arg1) throws SQLException {
		return null;
	}

	public BigDecimal getBigDecimal(String arg0, int arg1) throws SQLException {
		return null;
	}

	public InputStream getBinaryStream(int arg0) throws SQLException {
		return null;
	}

	public InputStream getBinaryStream(String arg0) throws SQLException {
		return null;
	}

	public Blob getBlob(int arg0) throws SQLException {
		return null;
	}

	public Blob getBlob(String arg0) throws SQLException {
		return null;
	}

	public boolean getBoolean(int arg0) throws SQLException {
		return false;
	}

	public boolean getBoolean(String arg0) throws SQLException {
		return false;
	}

	public byte getByte(int arg0) throws SQLException {
		return 0;
	}

	public byte getByte(String arg0) throws SQLException {
		return 0;
	}

	public byte[] getBytes(int arg0) throws SQLException {
		return null;
	}

	public byte[] getBytes(String arg0) throws SQLException {
		return null;
	}

	public Reader getCharacterStream(int arg0) throws SQLException {
		return null;
	}

	public Reader getCharacterStream(String arg0) throws SQLException {
		return null;
	}

	public Clob getClob(int arg0) throws SQLException {
		return null;
	}

	public Clob getClob(String arg0) throws SQLException {
		return null;
	}

	public int getConcurrency() throws SQLException {
		return 0;
	}

	public String getCursorName() throws SQLException {
		return null;
	}

	public Date getDate(int arg0) throws SQLException {
		return null;
	}

	public Date getDate(String arg0) throws SQLException {
		return null;
	}

	public Date getDate(int arg0, Calendar arg1) throws SQLException {
		return null;
	}

	public Date getDate(String arg0, Calendar arg1) throws SQLException {
		return null;
	}

	public double getDouble(int arg0) throws SQLException {
		return 0;
	}

	public double getDouble(String arg0) throws SQLException {
		return 0;
	}

	public int getFetchDirection() throws SQLException {
		return 0;
	}

	public int getFetchSize() throws SQLException {
		return 0;
	}

	public float getFloat(int arg0) throws SQLException {
		return 0;
	}

	public float getFloat(String arg0) throws SQLException {
		return 0;
	}

	public int getInt(int arg0) throws SQLException {
		return Integer.valueOf(getString(arg0));
	}

	public int getInt(String arg0) throws SQLException {
		return Integer.valueOf(getString(arg0));
	}

	public long getLong(int arg0) throws SQLException {
		return Long.valueOf(getString(arg0));
	}

	public long getLong(String arg0) throws SQLException {
		return Long.valueOf(getString(arg0));
	}

	public ResultSetMetaData getMetaData() throws SQLException {
		return new FakeResultSetMeta(this.columns);
	}

	public Object getObject(int cindex) throws SQLException {
		return getCurrentRow().getObject(cindex);
	}

	public Object getObject(String cname) throws SQLException {
		return getCurrentRow().getObject(cname);
	}

	public Object getObject(int arg0, Map<String, Class<?>> arg1) throws SQLException {
		return null;
	}

	public Object getObject(String arg0, Map<String, Class<?>> arg1) throws SQLException {
		return null;
	}

	public Ref getRef(int arg0) throws SQLException {
		return null;
	}

	public Ref getRef(String arg0) throws SQLException {
		return null;
	}

	public int getRow() throws SQLException {
		return currIndex;
	}

	public short getShort(int arg0) throws SQLException {
		return 0;
	}

	public short getShort(String arg0) throws SQLException {
		return 0;
	}

	public Statement getStatement() throws SQLException {
		return null;
	}

	public String getString(int idx) throws SQLException {
		Row currentRow = getCurrentRow();
		return currentRow.getString(idx);
	}

	public String getString(String arg0) throws SQLException {
		return getCurrentRow().getString(arg0);
	}

	public Time getTime(int arg0) throws SQLException {
		return null;
	}

	public Time getTime(String arg0) throws SQLException {
		return null;
	}

	public Time getTime(int arg0, Calendar arg1) throws SQLException {
		return null;
	}

	public Time getTime(String arg0, Calendar arg1) throws SQLException {
		return null;
	}

	public Timestamp getTimestamp(int arg0) throws SQLException {
		return null;
	}

	public Timestamp getTimestamp(String arg0) throws SQLException {
		return null;
	}

	public Timestamp getTimestamp(int arg0, Calendar arg1) throws SQLException {
		return null;
	}

	public Timestamp getTimestamp(String arg0, Calendar arg1) throws SQLException {
		return null;
	}

	public int getType() throws SQLException {
		return 0;
	}

	public URL getURL(int arg0) throws SQLException {
		return null;
	}

	public URL getURL(String arg0) throws SQLException {
		return null;
	}

	public InputStream getUnicodeStream(int arg0) throws SQLException {
		return null;
	}

	public InputStream getUnicodeStream(String arg0) throws SQLException {
		return null;
	}

	public SQLWarning getWarnings() throws SQLException {
		return null;
	}

	public void insertRow() throws SQLException {
		
	}

	public boolean isAfterLast() throws SQLException {
		return false;
	}

	public boolean isBeforeFirst() throws SQLException {
		return false;
	}

	public boolean isFirst() throws SQLException {
		return false;
	}

	public boolean isLast() throws SQLException {
		return false;
	}

	public boolean last() throws SQLException {
		return false;
	}

	public void moveToCurrentRow() throws SQLException {
		
	}

	public void moveToInsertRow() throws SQLException {
		
	}

	public boolean next() throws SQLException {
		if (rows.size() - 1 > currIndex) {
			currIndex++;
			return true;
		} else {
			return false;
		}
	}

	public boolean previous() throws SQLException {
		if (currIndex < 0) {
			return false;
		} else {
			currIndex--;
			return true;
		}
	}

	public void refreshRow() throws SQLException {
		
	}

	public boolean relative(int arg0) throws SQLException {
		return false;
	}

	public boolean rowDeleted() throws SQLException {
		return false;
	}

	public boolean rowInserted() throws SQLException {
		return false;
	}

	public boolean rowUpdated() throws SQLException {
		return false;
	}

	public void setFetchDirection(int arg0) throws SQLException {
		
	}

	public void setFetchSize(int arg0) throws SQLException {
		
	}

	public void updateArray(int arg0, Array arg1) throws SQLException {
		
	}

	public void updateArray(String arg0, Array arg1) throws SQLException {
		
	}

	public void updateAsciiStream(int arg0, InputStream arg1, int arg2) throws SQLException {
		
	}

	public void updateAsciiStream(String arg0, InputStream arg1, int arg2) throws SQLException {
		
	}

	public void updateBigDecimal(int arg0, BigDecimal arg1) throws SQLException {
		
	}

	public void updateBigDecimal(String arg0, BigDecimal arg1) throws SQLException {
		
	}

	public void updateBinaryStream(int arg0, InputStream arg1, int arg2) throws SQLException {
		
	}

	public void updateBinaryStream(String arg0, InputStream arg1, int arg2) throws SQLException {
		
	}

	public void updateBlob(int arg0, Blob arg1) throws SQLException {
		
	}

	public void updateBlob(String arg0, Blob arg1) throws SQLException {
		
	}

	public void updateBoolean(int arg0, boolean arg1) throws SQLException {
		
	}

	public void updateBoolean(String arg0, boolean arg1) throws SQLException {
		
	}

	public void updateByte(int arg0, byte arg1) throws SQLException {
		
	}

	public void updateByte(String arg0, byte arg1) throws SQLException {
		
	}

	public void updateBytes(int arg0, byte[] arg1) throws SQLException {
		
	}

	public void updateBytes(String arg0, byte[] arg1) throws SQLException {
		
	}

	public void updateCharacterStream(int arg0, Reader arg1, int arg2) throws SQLException {
		
	}

	public void updateCharacterStream(String arg0, Reader arg1, int arg2) throws SQLException {
		
	}

	public void updateClob(int arg0, Clob arg1) throws SQLException {
		
	}

	public void updateClob(String arg0, Clob arg1) throws SQLException {
		
	}

	public void updateDate(int arg0, Date arg1) throws SQLException {
		
	}

	public void updateDate(String arg0, Date arg1) throws SQLException {
		
	}

	public void updateDouble(int arg0, double arg1) throws SQLException {
		
	}

	public void updateDouble(String arg0, double arg1) throws SQLException {
		
	}

	public void updateFloat(int arg0, float arg1) throws SQLException {
		
	}

	public void updateFloat(String arg0, float arg1) throws SQLException {
		
	}

	public void updateInt(int arg0, int arg1) throws SQLException {
		
	}

	public void updateInt(String arg0, int arg1) throws SQLException {
		
	}

	public void updateLong(int arg0, long arg1) throws SQLException {
		
	}

	public void updateLong(String arg0, long arg1) throws SQLException {
		
	}

	public void updateNull(int arg0) throws SQLException {
		
	}

	public void updateNull(String arg0) throws SQLException {
		
	}

	public void updateObject(int arg0, Object arg1) throws SQLException {
		
	}

	public void updateObject(String arg0, Object arg1) throws SQLException {
		
	}

	public void updateObject(int arg0, Object arg1, int arg2) throws SQLException {
		
	}

	public void updateObject(String arg0, Object arg1, int arg2) throws SQLException {
		
	}

	public void updateRef(int arg0, Ref arg1) throws SQLException {
		
	}

	public void updateRef(String arg0, Ref arg1) throws SQLException {
		
	}

	public void updateRow() throws SQLException {
		
	}

	public void updateShort(int arg0, short arg1) throws SQLException {
		
	}

	public void updateShort(String arg0, short arg1) throws SQLException {
		
	}

	public void updateString(int arg0, String arg1) throws SQLException {
		
	}

	public void updateString(String arg0, String arg1) throws SQLException {
		
	}

	public void updateTime(int arg0, Time arg1) throws SQLException {
		
	}

	public void updateTime(String arg0, Time arg1) throws SQLException {
		
	}

	public void updateTimestamp(int arg0, Timestamp arg1) throws SQLException {
		
	}

	public void updateTimestamp(String arg0, Timestamp arg1) throws SQLException {
		
	}

	public boolean wasNull() throws SQLException {
		return false;
	}

	public Connection getConnection() throws SQLException {
		return null;
	}

	public ResultSet getOriginal() throws SQLException {
		return null;
	}

	public ResultSet getOriginalRow() throws SQLException {
		return null;
	}

	public Object[] getParams() throws SQLException {
		return null;
	}

	public void setMetaData(RowSetMetaData arg0) throws SQLException {
		
	}

	public void clearQuery() {

	}

	public ScreenInfo getScreenInfo() {
		return ScreenInfo.UNKNOWN;
	}

	public void setTypeMap(Map arg0) throws SQLException {
		
	}

	public Rows toClone() throws IOException {
		throw new UnsupportedOperationException() ;
	}

	public void debugPrint() throws SQLException {
		RowsImpl.DebugHandler.handle(this) ;
	}

	
	
	
	// jdk7
	public RowId getRowId(int columnIndex) throws SQLException {
		return null;
	}

	public RowId getRowId(String columnLabel) throws SQLException {
		return null;
	}

	public void updateRowId(int columnIndex, RowId x) throws SQLException {
	}

	public void updateRowId(String columnLabel, RowId x) throws SQLException {
	}

	public int getHoldability() throws SQLException {
		return 0;
	}

	public boolean isClosed() throws SQLException {
		return false;
	}

	public void updateNString(int columnIndex, String nString) throws SQLException {
	}

	public void updateNString(String columnLabel, String nString) throws SQLException {
	}

	public void updateNClob(int columnIndex, NClob nClob) throws SQLException {
	}

	public void updateNClob(String columnLabel, NClob nClob) throws SQLException {
	}

	public NClob getNClob(int columnIndex) throws SQLException {
		return null;
	}

	public NClob getNClob(String columnLabel) throws SQLException {
		return null;
	}

	public SQLXML getSQLXML(int columnIndex) throws SQLException {
		return null;
	}

	public SQLXML getSQLXML(String columnLabel) throws SQLException {
		return null;
	}

	public void updateSQLXML(int columnIndex, SQLXML xmlObject) throws SQLException {
	}

	public void updateSQLXML(String columnLabel, SQLXML xmlObject) throws SQLException {
	}

	public String getNString(int columnIndex) throws SQLException {
		return null;
	}

	public String getNString(String columnLabel) throws SQLException {
		return null;
	}

	public Reader getNCharacterStream(int columnIndex) throws SQLException {
		return null;
	}

	public Reader getNCharacterStream(String columnLabel) throws SQLException {
		return null;
	}

	public void updateNCharacterStream(int columnIndex, Reader x, long length) throws SQLException {
	}

	public void updateNCharacterStream(String columnLabel, Reader reader, long length) throws SQLException {
	}

	public void updateAsciiStream(int columnIndex, InputStream x, long length) throws SQLException {
	}

	public void updateBinaryStream(int columnIndex, InputStream x, long length) throws SQLException {
	}

	public void updateCharacterStream(int columnIndex, Reader x, long length) throws SQLException {
	}

	public void updateAsciiStream(String columnLabel, InputStream x, long length) throws SQLException {
	}

	public void updateBinaryStream(String columnLabel, InputStream x, long length) throws SQLException {
	}

	public void updateCharacterStream(String columnLabel, Reader reader, long length) throws SQLException {
	}

	public void updateBlob(int columnIndex, InputStream inputStream, long length) throws SQLException {
	}

	public void updateBlob(String columnLabel, InputStream inputStream, long length) throws SQLException {
	}

	public void updateClob(int columnIndex, Reader reader, long length) throws SQLException {
	}

	public void updateClob(String columnLabel, Reader reader, long length) throws SQLException {
	}

	public void updateNClob(int columnIndex, Reader reader, long length) throws SQLException {
	}

	public void updateNClob(String columnLabel, Reader reader, long length) throws SQLException {
	}

	public void updateNCharacterStream(int columnIndex, Reader x) throws SQLException {
	}

	public void updateNCharacterStream(String columnLabel, Reader reader) throws SQLException {
	}

	public void updateAsciiStream(int columnIndex, InputStream x) throws SQLException {
	}

	public void updateBinaryStream(int columnIndex, InputStream x) throws SQLException {
	}

	public void updateCharacterStream(int columnIndex, Reader x) throws SQLException {
	}

	public void updateAsciiStream(String columnLabel, InputStream x) throws SQLException {
	}

	public void updateBinaryStream(String columnLabel, InputStream x) throws SQLException {
	}

	public void updateCharacterStream(String columnLabel, Reader reader) throws SQLException {
	}

	public void updateBlob(int columnIndex, InputStream inputStream) throws SQLException {
	}

	public void updateBlob(String columnLabel, InputStream inputStream) throws SQLException {
	}

	public void updateClob(int columnIndex, Reader reader) throws SQLException {
	}

	public void updateClob(String columnLabel, Reader reader) throws SQLException {
	}

	public void updateNClob(int columnIndex, Reader reader) throws SQLException {
	}

	public void updateNClob(String columnLabel, Reader reader) throws SQLException {
	}

	public <T> T getObject(int columnIndex, Class<T> type) throws SQLException {
		return null;
	}

	public <T> T getObject(String columnLabel, Class<T> type) throws SQLException {
		return null;
	}

	public <T> T unwrap(Class<T> iface) throws SQLException {
		if (iface.isInstance(this)) {
            return iface.cast(this);
        }
        throw new SQLException("not supported operation");
	}

	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		 return iface.isInstance(this);
	}

	public void setNull(String parameterName, int sqlType) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void setNull(String parameterName, int sqlType, String typeName) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void setBoolean(String parameterName, boolean x) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void setByte(String parameterName, byte x) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void setShort(String parameterName, short x) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void setInt(String parameterName, int x) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void setLong(String parameterName, long x) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void setFloat(String parameterName, float x) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void setDouble(String parameterName, double x) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void setBigDecimal(String parameterName, BigDecimal x) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void setString(String parameterName, String x) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void setBytes(String parameterName, byte[] x) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void setTimestamp(String parameterName, Timestamp x) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void setAsciiStream(String parameterName, InputStream x, int length) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void setBinaryStream(String parameterName, InputStream x, int length) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void setCharacterStream(String parameterName, Reader reader, int length) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void setAsciiStream(int parameterIndex, InputStream x) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void setAsciiStream(String parameterName, InputStream x) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void setBinaryStream(int parameterIndex, InputStream x) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void setBinaryStream(String parameterName, InputStream x) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void setCharacterStream(int parameterIndex, Reader reader) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void setCharacterStream(String parameterName, Reader reader) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void setNCharacterStream(int parameterIndex, Reader value) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void setObject(String parameterName, Object x, int targetSqlType, int scale) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void setObject(String parameterName, Object x, int targetSqlType) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void setObject(String parameterName, Object x) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void setBlob(int parameterIndex, InputStream inputStream, long length) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void setBlob(int parameterIndex, InputStream inputStream) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void setBlob(String parameterName, InputStream inputStream, long length) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void setBlob(String parameterName, Blob x) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void setBlob(String parameterName, InputStream inputStream) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void setClob(int parameterIndex, Reader reader, long length) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void setClob(int parameterIndex, Reader reader) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void setClob(String parameterName, Reader reader, long length) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void setClob(String parameterName, Clob x) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void setClob(String parameterName, Reader reader) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void setDate(String parameterName, Date x) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void setDate(String parameterName, Date x, Calendar cal) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void setTime(String parameterName, Time x) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void setTime(String parameterName, Time x, Calendar cal) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void setTimestamp(String parameterName, Timestamp x, Calendar cal) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void setSQLXML(int parameterIndex, SQLXML xmlObject) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void setSQLXML(String parameterName, SQLXML xmlObject) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void setRowId(int parameterIndex, RowId x) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void setRowId(String parameterName, RowId x) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void setNString(int parameterIndex, String value) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void setNString(String parameterName, String value) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void setNCharacterStream(int parameterIndex, Reader value, long length) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void setNCharacterStream(String parameterName, Reader value, long length) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void setNCharacterStream(String parameterName, Reader value) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void setNClob(String parameterName, NClob value) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void setNClob(String parameterName, Reader reader, long length) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void setNClob(String parameterName, Reader reader) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void setNClob(int parameterIndex, Reader reader, long length) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void setNClob(int parameterIndex, NClob value) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void setNClob(int parameterIndex, Reader reader) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void setURL(int parameterIndex, URL x) throws SQLException {
		// TODO Auto-generated method stub
		
	}

}

class VColumn implements Serializable{
	private static final long serialVersionUID = 3316614188749359298L;
	private String name;
	private String alias;
	private int type;

	VColumn(String name, String alias, int type) {
		this.name = name;
		this.alias = alias ;
		this.type = type;
	}

	String getName() {
		return this.name.toLowerCase();
	}

	String getAlias() {
		return this.alias.toLowerCase();
	}

	int getType() {
		return this.type;
	}
}