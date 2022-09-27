package net.ion.framework.db.rowset;

import java.io.InputStream;
import java.io.Reader;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.Ref;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import javax.naming.OperationNotSupportedException;
import javax.sql.RowSet;
import javax.sql.RowSetEvent;
import javax.sql.RowSetListener;

public abstract class BaseRowSet implements Serializable, Cloneable {

	public static final int UNICODE_STREAM_PARAM = 0;
	public static final int BINARY_STREAM_PARAM = 1;
	public static final int ASCII_STREAM_PARAM = 2;
	protected transient InputStream binaryStream;
	protected transient InputStream unicodeStream;
	protected transient InputStream asciiStream;
	protected transient Reader charStream;
	private String command;
	private String URL;
	private String dataSource;
	private transient String username;
	private transient String password;
	private int rowSetType;
	private boolean showDeleted;
	private int queryTimeout;
	private int maxRows;
	private int maxFieldSize;
	private int concurrency;
	private boolean readOnly;
	private boolean escapeProcessing;
	private int isolation;
	private int fetchDir;
	private int fetchSize;
	private Map map;
	private transient Vector<RowSetListener> listeners = new Vector<RowSetListener>();
	private transient Vector params;

	public BaseRowSet() {
		// listeners = new Vector<RowSetListener>();
	}

	public void addRowSetListener(RowSetListener rowsetlistener) {
		listeners.add(rowsetlistener);
	}

	private void checkParamIndex(int i) throws SQLException {
		if (i < 1)
			throw new SQLException("Invalid Parameter Index");
		else
			return;
	}

	public void clearParameters() throws SQLException {
		params.clear();
	}

	public String getCommand() {
		return command;
	}

	public int getConcurrency() throws SQLException {
		return concurrency;
	}

	public String getDataSourceName() {
		return dataSource;
	}

	public boolean getEscapeProcessing() throws SQLException {
		return escapeProcessing;
	}

	public int getFetchDirection() throws SQLException {
		return fetchDir;
	}

	public int getFetchSize() throws SQLException {
		return fetchSize;
	}

	public int getMaxFieldSize() throws SQLException {
		return maxFieldSize;
	}

	public int getMaxRows() throws SQLException {
		return maxRows;
	}

	public Object[] getParams() throws SQLException {
		return params.toArray();
	}

	public String getPassword() {
		return password;
	}

	public int getQueryTimeout() throws SQLException {
		return queryTimeout;
	}

	public boolean getShowDeleted() throws SQLException {
		return showDeleted;
	}

	public int getTransactionIsolation() {
		return isolation;
	}

	public int getType() throws SQLException {
		return rowSetType;
	}

	public Map getTypeMap() {
		return map;
	}

	public String getUrl() throws SQLException {
		return URL;
	}

	public String getUsername() {
		return username;
	}

	protected void initParams() {
		params = new Vector();
	}

	public boolean isReadOnly() {
		return readOnly;
	}

	protected void notifyCursorMoved() {
		if (existListener()) {
			RowSetEvent rowsetevent = new RowSetEvent((RowSet) this);
			for (Iterator<RowSetListener> iterator = listeners.iterator(); iterator.hasNext(); ){
				(iterator.next()).cursorMoved(rowsetevent) ;
			}
				;
		}
	}
	
	private boolean existListener(){
		return listeners != null && !listeners.isEmpty() ;
	}

	protected void notifyRowChanged() {
		if (existListener()) {
			RowSetEvent rowsetevent = new RowSetEvent((RowSet) this);
			for (Iterator<RowSetListener> iterator = listeners.iterator(); iterator.hasNext(); (iterator.next()).rowSetChanged(rowsetevent))
				;
		}
	}

	protected void notifyRowSetChanged() {
		if (existListener()) {
			RowSetEvent rowsetevent = new RowSetEvent((RowSet) this);
			for (Iterator<RowSetListener> iterator = listeners.iterator(); iterator.hasNext(); (iterator.next()).rowSetChanged(rowsetevent))
				;
		}
	}

	public void removeRowSetListener(RowSetListener rowsetlistener) {
		listeners.remove(rowsetlistener);
	}

	public void setArray(int i, Array array) throws SQLException {
		checkParamIndex(i);
		params.add(i - 1, array);
	}

	public void setAsciiStream(int i, InputStream inputstream, int j) throws SQLException {
		checkParamIndex(i);
		Object aobj[] = new Object[3];
		aobj[0] = inputstream;
		aobj[1] = new Integer(j);
		aobj[2] = new Integer(2);
		params.add(i - 1, ((Object) (aobj)));
	}

	public void setBigDecimal(int i, BigDecimal bigdecimal) throws SQLException {
		checkParamIndex(i);
		params.add(i - 1, bigdecimal);
	}

	public void setBinaryStream(int i, InputStream inputstream, int j) throws SQLException {
		checkParamIndex(i);
		Object aobj[] = new Object[3];
		aobj[0] = inputstream;
		aobj[1] = new Integer(j);
		aobj[2] = new Integer(1);
		params.add(i - 1, ((Object) (aobj)));
	}

	public void setBlob(int i, Blob blob) throws SQLException {
		checkParamIndex(i);
		params.add(i - 1, blob);
	}

	public void setBoolean(int i, boolean flag) throws SQLException {
		checkParamIndex(i);
		params.add(i - 1, new Boolean(flag));
	}

	public void setByte(int i, byte byte0) throws SQLException {
		checkParamIndex(i);
		params.add(i - 1, new Byte(byte0));
	}

	public void setBytes(int i, byte abyte0[]) throws SQLException {
		checkParamIndex(i);
		params.add(i - 1, abyte0);
	}

	public void setCharacterStream(int i, Reader reader, int j) throws SQLException {
		checkParamIndex(i);
		Object aobj[] = new Object[2];
		aobj[0] = reader;
		aobj[1] = new Integer(j);
		params.add(i - 1, ((Object) (aobj)));
	}

	public void setClob(int i, Clob clob) throws SQLException {
		checkParamIndex(i);
		params.add(i - 1, clob);
	}

	public void setCommand(String s) throws SQLException {
		command = new String(s);
		params.clear();
	}

	public void setConcurrency(int i) {
		concurrency = i;
	}

	public void setDataSourceName(String s) {
		if (s == null)
			dataSource = null;
		else
			dataSource = new String(s);
		URL = null;
	}

	public void setDate(int i, Date date) throws SQLException {
		checkParamIndex(i);
		params.add(i - 1, date);
	}

	public void setDate(int i, Date date, Calendar calendar) throws SQLException {
		checkParamIndex(i);
		Object aobj[] = new Object[2];
		aobj[0] = date;
		aobj[1] = calendar;
		params.add(i - 1, ((Object) (aobj)));
	}

	public void setDouble(int i, double d) throws SQLException {
		checkParamIndex(i);
		params.add(i - 1, new Double(d));
	}

	public void setEscapeProcessing(boolean flag) throws SQLException {
		escapeProcessing = flag;
	}

	public void setFetchDirection(int i) throws SQLException {
		if (getType() == 1003 && i != 1000) {
			throw new SQLException("Invalid Fetch Direction");
		} else {
			fetchDir = i;
			return;
		}
	}

	public void setFetchSize(int i) throws SQLException {
		fetchSize = i;
	}

	public void setFloat(int i, float f) throws SQLException {
		checkParamIndex(i);
		params.add(i - 1, new Float(f));
	}

	public void setInt(int i, int j) throws SQLException {
		checkParamIndex(i);
		params.add(i - 1, new Integer(j));
	}

	public void setLong(int i, long l) throws SQLException {
		checkParamIndex(i);
		params.add(i - 1, new Long(l));
	}

	public void setMaxFieldSize(int i) throws SQLException {
		maxFieldSize = i;
	}

	public void setMaxRows(int i) throws SQLException {
		maxRows = i;
	}

	public void setNull(int i, int j) throws SQLException {
		checkParamIndex(i);
		Object aobj[] = new Object[2];
		aobj[0] = null;
		aobj[1] = new Integer(j);
		params.add(i - 1, ((Object) (aobj)));
	}

	public void setNull(int i, int j, String s) throws SQLException {
		checkParamIndex(i);
		Object aobj[] = new Object[3];
		aobj[0] = null;
		aobj[1] = new Integer(j);
		aobj[2] = new String(s);
		params.add(i - 1, ((Object) (aobj)));
	}

	public void setObject(int i, Object obj) throws SQLException {
		checkParamIndex(i);
		params.add(i - 1, obj);
	}

	public void setObject(int i, Object obj, int j) throws SQLException {
		checkParamIndex(i);
		Object aobj[] = new Object[2];
		aobj[0] = obj;
		aobj[1] = new Integer(j);
		params.add(i - 1, ((Object) (aobj)));
	}

	public void setObject(int i, Object obj, int j, int k) throws SQLException {
		checkParamIndex(i);
		Object aobj[] = new Object[3];
		aobj[0] = obj;
		aobj[1] = new Integer(j);
		aobj[2] = new Integer(k);
		params.add(i - 1, ((Object) (aobj)));
	}

	public void setPassword(String s) {
		password = new String(s);
	}

	public void setQueryTimeout(int i) throws SQLException {
		queryTimeout = i;
	}

	public void setReadOnly(boolean flag) {
		readOnly = flag;
	}

	public void setRef(int i, Ref ref) throws SQLException {
		checkParamIndex(i);
		params.add(i - 1, ref);
	}

	public void setShort(int i, short word0) throws SQLException {
		checkParamIndex(i);
		params.add(i - 1, new Short(word0));
	}

	public void setShowDeleted(boolean flag) throws SQLException {
		showDeleted = flag;
	}

	public void setString(int i, String s) throws SQLException {
		checkParamIndex(i);
		params.add(i - 1, s);
	}

	public void setTime(int i, Time time) throws SQLException {
		checkParamIndex(i);
		params.add(i - 1, time);
	}

	public void setTime(int i, Time time, Calendar calendar) throws SQLException {
		checkParamIndex(i);
		Object aobj[] = new Object[2];
		aobj[0] = time;
		aobj[1] = calendar;
		params.add(i - 1, ((Object) (aobj)));
	}

	public void setTimestamp(int i, Timestamp timestamp) throws SQLException {
		checkParamIndex(i);
		params.add(i - 1, timestamp);
	}

	public void setTimestamp(int i, Timestamp timestamp, Calendar calendar) throws SQLException {
		checkParamIndex(i);
		Object aobj[] = new Object[2];
		aobj[0] = timestamp;
		aobj[1] = calendar;
		params.add(i - 1, ((Object) (aobj)));
	}

	public void setTransactionIsolation(int i) {
		isolation = i;
	}

	public void setType(int i) {
		rowSetType = i;
	}

	public void setTypeMap(Map map1) {
		map = map1;
	}

	/**
	 * @deprecated Method setUnicodeStream is deprecated
	 */

	public void setUnicodeStream(int i, InputStream inputstream, int j) throws SQLException {
		checkParamIndex(i);
		Object aobj[] = new Object[3];
		aobj[0] = inputstream;
		aobj[1] = new Integer(j);
		aobj[2] = new Integer(0);
		params.add(i - 1, ((Object) (aobj)));
	}

	public void setUrl(String s) throws SQLException {
		if (s == null)
			URL = null;
		else
			URL = new String(s);
		dataSource = null;
	}

	public void setUsername(String s) {
		username = new String(s);
	}
	

	
	// jdk 1.5 higher..

	public java.net.URL getURL(int columnIndex) throws SQLException {
		throw new IllegalArgumentException("not supported-set operation") ;
	}

	public java.net.URL getURL(String columnName) throws SQLException {
		throw new IllegalArgumentException("not supported-set operation") ;
	}

	public void updateArray(int columnIndex, Array x) throws SQLException {
		throw new IllegalArgumentException("not supported-set operation") ;
	}

	public void updateArray(String columnName, Array x) throws SQLException {
		throw new IllegalArgumentException("not supported-set operation") ;
	}

	public void updateBlob(int columnIndex, Blob x) throws SQLException {
		throw new IllegalArgumentException("not supported-set operation") ;
	}

	public void updateBlob(String columnName, Blob x) throws SQLException {
		throw new IllegalArgumentException("not supported-set operation") ;
	}

	public void updateClob(int columnIndex, Clob x) throws SQLException {
		throw new IllegalArgumentException("not supported-set operation") ;
	}

	public void updateClob(String columnName, Clob x) throws SQLException {
		throw new IllegalArgumentException("not supported-set operation") ;
	}

	public void updateRef(int columnIndex, Ref x) throws SQLException {
		throw new IllegalArgumentException("not supported-set operation") ;
	}

	public void updateRef(String columnName, Ref x) throws SQLException {
		throw new IllegalArgumentException("not supported-set operation") ;
	}

	public void setNull(String parameterName, int sqlType) throws SQLException {
		throw new IllegalArgumentException("not supported-set operation") ;
	}

	public void setNull(String parameterName, int sqlType, String typeName) throws SQLException {
		throw new IllegalArgumentException("not supported-set operation") ;
	}

	public void setBoolean(String parameterName, boolean x) throws SQLException {
		throw new IllegalArgumentException("not supported-set operation") ;
	}

	public void setByte(String parameterName, byte x) throws SQLException {
		throw new IllegalArgumentException("not supported-set operation") ;
	}

	public void setShort(String parameterName, short x) throws SQLException {
		throw new IllegalArgumentException("not supported-set operation") ;
	}

	public void setInt(String parameterName, int x) throws SQLException {
		throw new IllegalArgumentException("not supported-set operation") ;
	}

	public void setLong(String parameterName, long x) throws SQLException {
		throw new IllegalArgumentException("not supported-set operation") ;
	}

	public void setFloat(String parameterName, float x) throws SQLException {
		throw new IllegalArgumentException("not supported-set operation") ;
	}

	public void setDouble(String parameterName, double x) throws SQLException {
		throw new IllegalArgumentException("not supported-set operation") ;
	}

	public void setBigDecimal(String parameterName, BigDecimal x) throws SQLException {
		throw new IllegalArgumentException("not supported-set operation") ;
	}

	public void setString(String parameterName, String x) throws SQLException {
		throw new IllegalArgumentException("not supported-set operation") ;
	}

	public void setBytes(String parameterName, byte[] x) throws SQLException {
		throw new IllegalArgumentException("not supported-set operation") ;
	}

	public void setTimestamp(String parameterName, Timestamp x) throws SQLException {
		throw new IllegalArgumentException("not supported-set operation") ;
	}

	public void setAsciiStream(String parameterName, InputStream x, int length) throws SQLException {
		throw new IllegalArgumentException("not supported-set operation") ;
	}

	public void setBinaryStream(String parameterName, InputStream x, int length) throws SQLException {
		throw new IllegalArgumentException("not supported-set operation") ;
	}

	public void setCharacterStream(String parameterName, Reader reader, int length) throws SQLException {
		throw new IllegalArgumentException("not supported-set operation") ;
	}

	public void setAsciiStream(int parameterIndex, InputStream x) throws SQLException {
		throw new IllegalArgumentException("not supported-set operation") ;
	}

	public void setAsciiStream(String parameterName, InputStream x) throws SQLException {
		throw new IllegalArgumentException("not supported-set operation") ;
	}

	public void setBinaryStream(int parameterIndex, InputStream x) throws SQLException {
		throw new IllegalArgumentException("not supported-set operation") ;
	}

	public void setBinaryStream(String parameterName, InputStream x) throws SQLException {
		throw new IllegalArgumentException("not supported-set operation") ;
	}

	public void setCharacterStream(int parameterIndex, Reader reader) throws SQLException {
		throw new IllegalArgumentException("not supported-set operation") ;
	}

	public void setCharacterStream(String parameterName, Reader reader) throws SQLException {
		throw new IllegalArgumentException("not supported-set operation") ;
	}

	public void setNCharacterStream(int parameterIndex, Reader value) throws SQLException {
		throw new IllegalArgumentException("not supported-set operation") ;
	}

	public void setObject(String parameterName, Object x, int targetSqlType, int scale) throws SQLException {
		throw new IllegalArgumentException("not supported-set operation") ;
	}

	public void setObject(String parameterName, Object x, int targetSqlType) throws SQLException {
		throw new IllegalArgumentException("not supported-set operation") ;
	}

	public void setObject(String parameterName, Object x) throws SQLException {
		throw new IllegalArgumentException("not supported-set operation") ;
	}

	public void setBlob(int parameterIndex, InputStream inputStream, long length) throws SQLException {
		throw new IllegalArgumentException("not supported-set operation") ;
	}

	public void setBlob(int parameterIndex, InputStream inputStream) throws SQLException {
		throw new IllegalArgumentException("not supported-set operation") ;
	}

	public void setBlob(String parameterName, InputStream inputStream, long length) throws SQLException {
		throw new IllegalArgumentException("not supported-set operation") ;
	}

	public void setBlob(String parameterName, Blob x) throws SQLException {
		throw new IllegalArgumentException("not supported-set operation") ;
	}

	public void setBlob(String parameterName, InputStream inputStream) throws SQLException {
		throw new IllegalArgumentException("not supported-set operation") ;
	}

	public void setClob(int parameterIndex, Reader reader, long length) throws SQLException {
		throw new IllegalArgumentException("not supported-set operation") ;
	}

	public void setClob(int parameterIndex, Reader reader) throws SQLException {
		throw new IllegalArgumentException("not supported-set operation") ;
	}

	public void setClob(String parameterName, Reader reader, long length) throws SQLException {
		throw new IllegalArgumentException("not supported-set operation") ;
	}

	public void setClob(String parameterName, Clob x) throws SQLException {
		throw new IllegalArgumentException("not supported-set operation") ;
	}

	public void setClob(String parameterName, Reader reader) throws SQLException {
		throw new IllegalArgumentException("not supported-set operation") ;
	}

	public void setDate(String parameterName, Date x) throws SQLException {
		throw new IllegalArgumentException("not supported-set operation") ;
	}

	public void setDate(String parameterName, Date x, Calendar cal) throws SQLException {
		throw new IllegalArgumentException("not supported-set operation") ;
	}

	public void setTime(String parameterName, Time x) throws SQLException {
		throw new IllegalArgumentException("not supported-set operation") ;
	}

	public void setTime(String parameterName, Time x, Calendar cal) throws SQLException {
		throw new IllegalArgumentException("not supported-set operation") ;
	}

	public void setTimestamp(String parameterName, Timestamp x, Calendar cal) throws SQLException {
		throw new IllegalArgumentException("not supported-set operation") ;
	}

	public void setSQLXML(int parameterIndex, SQLXML xmlObject) throws SQLException {
		throw new IllegalArgumentException("not supported-set operation") ;
	}

	public void setSQLXML(String parameterName, SQLXML xmlObject) throws SQLException {
		throw new IllegalArgumentException("not supported-set operation") ;
	}

	public void setRowId(int parameterIndex, RowId x) throws SQLException {
		throw new IllegalArgumentException("not supported-set operation") ;
	}

	public void setRowId(String parameterName, RowId x) throws SQLException {
		throw new IllegalArgumentException("not supported-set operation") ;
	}

	public void setNString(int parameterIndex, String value) throws SQLException {
		throw new IllegalArgumentException("not supported-set operation") ;
	}

	public void setNString(String parameterName, String value) throws SQLException {
		throw new IllegalArgumentException("not supported-set operation") ;
	}

	public void setNCharacterStream(int parameterIndex, Reader value, long length) throws SQLException {
		throw new IllegalArgumentException("not supported-set operation") ;
	}

	public void setNCharacterStream(String parameterName, Reader value, long length) throws SQLException {
		throw new IllegalArgumentException("not supported-set operation") ;
	}

	public void setNCharacterStream(String parameterName, Reader value) throws SQLException {
		throw new IllegalArgumentException("not supported-set operation") ;
	}

	public void setNClob(String parameterName, NClob value) throws SQLException {
		throw new IllegalArgumentException("not supported-set operation") ;
	}

	public void setNClob(String parameterName, Reader reader, long length) throws SQLException {
		throw new IllegalArgumentException("not supported-set operation") ;
	}

	public void setNClob(String parameterName, Reader reader) throws SQLException {
		throw new IllegalArgumentException("not supported-set operation") ;
	}

	public void setNClob(int parameterIndex, Reader reader, long length) throws SQLException {
		throw new IllegalArgumentException("not supported-set operation") ;
	}

	public void setNClob(int parameterIndex, NClob value) throws SQLException {
		throw new IllegalArgumentException("not supported-set operation") ;
	}

	public void setNClob(int parameterIndex, Reader reader) throws SQLException {
		throw new IllegalArgumentException("not supported-set operation") ;
	}

	public void setURL(int parameterIndex, java.net.URL x) throws SQLException {
		throw new IllegalArgumentException("not supported-set operation") ;
	}
	
	
	
}
