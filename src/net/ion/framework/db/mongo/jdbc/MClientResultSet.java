//MongoResultSet.java

/**
 * Copyright (C) 2008 10gen Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.ion.framework.db.mongo.jdbc;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;

import net.ion.framework.util.Debug;

import com.mongodb.DBCursor;

public class MClientResultSet implements ResultSet {


	// members
	// private final DBCursor cursor;
	private Map current;
	private int pos = -1;
	private boolean closed = false;
	private final ClientResult result ;

	private MClientResultSet(DBCursor cursor) {
		// this.cursor = cursor;
		result = ClientResult.create(cursor) ;
	}
	
	final static MClientResultSet create(DBCursor cursor){
		return new MClientResultSet(cursor) ;
	}

	public void clearWarnings() {
		// NO-OP
	}

	public void close() {
		closed = true;
	}

	public boolean isClosed() {
		return closed;
	}

	// meta data

	public int getConcurrency() {
		return CONCUR_READ_ONLY;
	}

	public int getType() {
		return TYPE_FORWARD_ONLY;
	}

	public void setFetchDirection(int direction) {
		if (direction == getFetchDirection())
			return;
		throw new UnsupportedOperationException();
	}

	public int getFetchDirection() {
		return 1;
	}

	public String getCursorName() {
		return "MongoResultSet: " + result.toString();
	}

	public ResultSetMetaData getMetaData() {
		return new MongoResultSetMetaData(result);
	}

	public SQLWarning getWarnings() {
		throw new UnsupportedOperationException();
	}

	public void setFetchSize(int rows) {
		throw new UnsupportedOperationException();
	}

	public int getFetchSize() {
		throw new UnsupportedOperationException();
	}

	public Statement getStatement() {
		throw new UnsupportedOperationException();
	}

	public int getHoldability() {
		return ResultSet.HOLD_CURSORS_OVER_COMMIT;
	}

	// cursor moving methods

	public boolean absolute(int row) {
		throw new UnsupportedOperationException();
	}

	public void afterLast() {
		throw new UnsupportedOperationException();
	}

	public void beforeFirst() {
		this.pos = -1 ;
		this.current = null ;
	}

	public boolean first() {
		if (result.getRowCount() < 1) return false ;
		this.pos = -1 ;
		return next() ;
	}

	public int getRow() {
		return pos;
	}

	public boolean isAfterLast() {
		throw new UnsupportedOperationException();
	}

	public boolean isBeforeFirst() {
		throw new UnsupportedOperationException();
	}

	public boolean isFirst() {
		throw new UnsupportedOperationException();
	}

	public boolean isLast() {
		throw new UnsupportedOperationException();
	}

	public boolean last() {
		throw new UnsupportedOperationException();
	}

	public void moveToCurrentRow() {
		throw new UnsupportedOperationException();
	}

	public void moveToInsertRow() {
		throw new UnsupportedOperationException();
	}

	public boolean previous() {
		throw new UnsupportedOperationException();
	}

	public void refreshRow() {
		throw new UnsupportedOperationException();
	}

	public boolean relative(int rows) {
		throw new UnsupportedOperationException();
	}

	public boolean rowDeleted() {
		throw new UnsupportedOperationException();
	}

	public boolean rowInserted() {
		throw new UnsupportedOperationException();
	}

	public boolean rowUpdated() {
		throw new UnsupportedOperationException();
	}

	// modifications

	public void insertRow() {
		throw new UnsupportedOperationException();
	}

	public void cancelRowUpdates() {
		throw new UnsupportedOperationException();
	}

	public void deleteRow() {
		throw new UnsupportedOperationException();
	}

	public void updateRow() {
		throw new UnsupportedOperationException();
	}

	// field updates

	public void updateArray(int columnIndex, Array x) {
		throw new UnsupportedOperationException();
	}

	public void updateArray(String columnName, Array x) {
		throw new UnsupportedOperationException();
	}

	public void updateAsciiStream(int columnIndex, InputStream x, int length) {
		throw new UnsupportedOperationException();
	}

	public void updateAsciiStream(String columnName, InputStream x, int length) {
		throw new UnsupportedOperationException();
	}

	public void updateAsciiStream(int columnIndex, InputStream x, long length) {
		throw new UnsupportedOperationException();
	}

	public void updateAsciiStream(String columnName, InputStream x, long length) {
		throw new UnsupportedOperationException();
	}

	public void updateAsciiStream(int columnIndex, InputStream x) {
		throw new UnsupportedOperationException();
	}

	public void updateAsciiStream(String columnName, InputStream x) {
		throw new UnsupportedOperationException();
	}

	public void updateBigDecimal(int columnIndex, BigDecimal x) {
		throw new UnsupportedOperationException();
	}

	public void updateBigDecimal(String columnName, BigDecimal x) {
		throw new UnsupportedOperationException();
	}

	public void updateBinaryStream(int columnIndex, InputStream x, int length) {
		throw new UnsupportedOperationException();
	}

	public void updateBinaryStream(String columnName, InputStream x, int length) {
		throw new UnsupportedOperationException();
	}

	public void updateBinaryStream(int columnIndex, InputStream x, long length) {
		throw new UnsupportedOperationException();
	}

	public void updateBinaryStream(String columnName, InputStream x, long length) {
		throw new UnsupportedOperationException();
	}

	public void updateBinaryStream(int columnIndex, InputStream x) {
		throw new UnsupportedOperationException();
	}

	public void updateBinaryStream(String columnName, InputStream x) {
		throw new UnsupportedOperationException();
	}

	public void updateBlob(int columnIndex, Blob x) {
		throw new UnsupportedOperationException();
	}

	public void updateBlob(String columnName, Blob x) {
		throw new UnsupportedOperationException();
	}

	public void updateBlob(int columnIndex, InputStream x) {
		throw new UnsupportedOperationException();
	}

	public void updateBlob(String columnName, InputStream x) {
		throw new UnsupportedOperationException();
	}

	public void updateBlob(int columnIndex, InputStream x, long l) {
		throw new UnsupportedOperationException();
	}

	public void updateBlob(String columnName, InputStream x, long l) {
		throw new UnsupportedOperationException();
	}

	public void updateBoolean(int columnIndex, boolean x) {
		throw new UnsupportedOperationException();
	}

	public void updateBoolean(String columnName, boolean x) {
		throw new UnsupportedOperationException();
	}

	public void updateByte(int columnIndex, byte x) {
		throw new UnsupportedOperationException();
	}

	public void updateByte(String columnName, byte x) {
		throw new UnsupportedOperationException();
	}

	public void updateBytes(int columnIndex, byte[] x) {
		throw new UnsupportedOperationException();
	}

	public void updateBytes(String columnName, byte[] x) {
		throw new UnsupportedOperationException();
	}

	public void updateCharacterStream(int columnIndex, Reader x, int length) {
		throw new UnsupportedOperationException();
	}

	public void updateCharacterStream(String columnName, Reader reader, int length) {
		throw new UnsupportedOperationException();
	}

	public void updateCharacterStream(int columnIndex, Reader x, long length) {
		throw new UnsupportedOperationException();
	}

	public void updateCharacterStream(String columnName, Reader reader, long length) {
		throw new UnsupportedOperationException();
	}

	public void updateCharacterStream(int columnIndex, Reader x) {
		throw new UnsupportedOperationException();
	}

	public void updateCharacterStream(String columnName, Reader reader) {
		throw new UnsupportedOperationException();
	}

	public void updateClob(int columnIndex, Clob x) {
		throw new UnsupportedOperationException();
	}

	public void updateClob(String columnName, Clob x) {
		throw new UnsupportedOperationException();
	}

	public void updateClob(int columnIndex, Reader x) {
		throw new UnsupportedOperationException();
	}

	public void updateClob(String columnName, Reader x) {
		throw new UnsupportedOperationException();
	}

	public void updateClob(int columnIndex, Reader x, long l) {
		throw new UnsupportedOperationException();
	}

	public void updateClob(String columnName, Reader x, long l) {
		throw new UnsupportedOperationException();
	}

	public void updateDate(int columnIndex, Date x) {
		throw new UnsupportedOperationException();
	}

	public void updateDate(String columnName, Date x) {
		throw new UnsupportedOperationException();
	}

	public void updateDouble(int columnIndex, double x) {
		throw new UnsupportedOperationException();
	}

	public void updateDouble(String columnName, double x) {
		throw new UnsupportedOperationException();
	}

	public void updateFloat(int columnIndex, float x) {
		throw new UnsupportedOperationException();
	}

	public void updateFloat(String columnName, float x) {
		throw new UnsupportedOperationException();
	}

	public void updateInt(int columnIndex, int x) {
		throw new UnsupportedOperationException();
	}

	public void updateInt(String columnName, int x) {
		throw new UnsupportedOperationException();
	}

	public void updateLong(int columnIndex, long x) {
		throw new UnsupportedOperationException();
	}

	public void updateLong(String columnName, long x) {
		throw new UnsupportedOperationException();
	}

	public void updateNull(int columnIndex) {
		throw new UnsupportedOperationException();
	}

	public void updateNull(String columnName) {
		throw new UnsupportedOperationException();
	}

	public void updateObject(int columnIndex, Object x) {
		throw new UnsupportedOperationException();
	}

	public void updateObject(int columnIndex, Object x, int scale) {
		throw new UnsupportedOperationException();
	}

	public void updateObject(String columnName, Object x) {
		throw new UnsupportedOperationException();
	}

	public void updateObject(String columnName, Object x, int scale) {
		throw new UnsupportedOperationException();
	}

	public void updateRef(int columnIndex, Ref x) {
		throw new UnsupportedOperationException();
	}

	public void updateRef(String columnName, Ref x) {
		throw new UnsupportedOperationException();
	}

	public void updateShort(int columnIndex, short x) {
		throw new UnsupportedOperationException();
	}

	public void updateShort(String columnName, short x) {
		throw new UnsupportedOperationException();
	}

	public void updateString(int columnIndex, String x) {
		throw new UnsupportedOperationException();
	}

	public void updateString(String columnName, String x) {
		throw new UnsupportedOperationException();
	}

	public void updateTime(int columnIndex, Time x) {
		throw new UnsupportedOperationException();
	}

	public void updateTime(String columnName, Time x) {
		throw new UnsupportedOperationException();
	}

	public void updateTimestamp(int columnIndex, Timestamp x) {
		throw new UnsupportedOperationException();
	}

	public void updateTimestamp(String columnName, Timestamp x) {
		throw new UnsupportedOperationException();
	}

	// accessors
	public Array getArray(int i) {
		return getArray(findColumn(i));
	}

	public Array getArray(String colName) {
		throw new UnsupportedOperationException();
	}

	public InputStream getAsciiStream(int columnIndex) {
		return getAsciiStream(findColumn(columnIndex));
	}

	public InputStream getAsciiStream(String columnName) {
		throw new UnsupportedOperationException();
	}

	public BigDecimal getBigDecimal(int columnIndex) {
		return getBigDecimal(findColumn(columnIndex));
	}

	public BigDecimal getBigDecimal(int columnIndex, int scale) {
		return getBigDecimal(findColumn(columnIndex), scale);
	}

	public BigDecimal getBigDecimal(String columnName) {
		throw new UnsupportedOperationException();
	}

	public BigDecimal getBigDecimal(String columnName, int scale) {
		throw new UnsupportedOperationException();
	}

	public InputStream getBinaryStream(int columnIndex) {
		return getBinaryStream(findColumn(columnIndex));
	}

	public InputStream getBinaryStream(String columnName) {
		throw new UnsupportedOperationException();
	}

	public Blob getBlob(int i) {
		return getBlob(findColumn(i));
	}

	public Blob getBlob(String colName) {
		throw new UnsupportedOperationException();
	}

	public boolean getBoolean(int columnIndex) {
		return getBoolean(findColumn(columnIndex));
	}

	public boolean getBoolean(String columnName) {
		Object x = current.get(columnName);
		if (x == null)
			return false;
		return (Boolean) x;
	}

	public byte getByte(int columnIndex) {
		return getByte(findColumn(columnIndex));
	}

	public byte getByte(String columnName) {
		throw new UnsupportedOperationException();
	}

	public byte[] getBytes(int columnIndex) {
		return getBytes(findColumn(columnIndex));
	}

	public byte[] getBytes(String columnName) {
		return (byte[]) current.get(columnName);
	}

	public Reader getCharacterStream(int columnIndex) {
		return getCharacterStream(findColumn(columnIndex));
	}

	public Reader getCharacterStream(String columnName) {
		throw new UnsupportedOperationException();
	}

	public Clob getClob(int i) {
		return getClob(findColumn(i));
	}

	public Clob getClob(String colName) {
		throw new UnsupportedOperationException();
	}

	public Date getDate(int columnIndex) {
		return getDate(findColumn(columnIndex));
	}

	public Date getDate(int columnIndex, Calendar cal) {
		return getDate(findColumn(columnIndex), cal);
	}

	public Date getDate(String columnName) {
		return (Date) current.get(columnName);
	}

	public Date getDate(String columnName, Calendar cal) {
		throw new UnsupportedOperationException();
	}

	public double getDouble(int columnIndex) {
		return getDouble(findColumn(columnIndex));
	}

	public double getDouble(String columnName) {
		return _getNumber(columnName).doubleValue();
	}

	public float getFloat(int columnIndex) {
		return getFloat(findColumn(columnIndex));
	}

	public float getFloat(String columnName) {
		return _getNumber(columnName).floatValue();
	}

	public int getInt(int columnIndex) {
		return getInt(findColumn(columnIndex));
	}

	public int getInt(String columnName) {
		return _getNumber(columnName).intValue();
	}

	public long getLong(int columnIndex) {
		return getLong(findColumn(columnIndex));
	}

	public long getLong(String columnName) {
		return _getNumber(columnName).longValue();
	}

	public short getShort(int columnIndex) {
		return getShort(findColumn(columnIndex));
	}

	public short getShort(String columnName) {
		return _getNumber(columnName).shortValue();
	}

	Number _getNumber(String n) {
		Number x = (Number) (current.get(n));
		if (x == null)
			return 0;
		return x;
	}

	public Object getObject(int columnIndex) {
		if (columnIndex == 0)
			return current;
		return getObject(findColumn(columnIndex));
	}

	public Object getObject(int i, Map map) {
		if (i == 0)
			return current;
		return getObject(findColumn(i), map);
	}

	public Object getObject(String columnName) {
		return current.get(columnName);
	}

	public Object getObject(String colName, Map map) {
		throw new UnsupportedOperationException();
	}

	public Ref getRef(int i) {
		return getRef(findColumn(i));
	}

	public Ref getRef(String colName) {
		throw new UnsupportedOperationException();
	}

	public String getString(int columnIndex) {
		Debug.line(findColumn(columnIndex)) ;
		return getString(findColumn(columnIndex));
	}

	public String getString(String columnName) {
		Object x = current.get(columnName);
		if (x == null)
			return null;
		return x.toString();
	}

	public Time getTime(int columnIndex) {
		return getTime(findColumn(columnIndex));
	}

	public Time getTime(int columnIndex, Calendar cal) {
		return getTime(findColumn(columnIndex), cal);
	}

	public Time getTime(String columnName) {
		throw new UnsupportedOperationException();
	}

	public Time getTime(String columnName, Calendar cal) {
		throw new UnsupportedOperationException();
	}

	public Timestamp getTimestamp(int columnIndex) {
		return getTimestamp(findColumn(columnIndex));
	}

	public Timestamp getTimestamp(int columnIndex, Calendar cal) {
		return getTimestamp(findColumn(columnIndex), cal);
	}

	public Timestamp getTimestamp(String columnName) {
		throw new UnsupportedOperationException();
	}

	public Timestamp getTimestamp(String columnName, Calendar cal) {
		throw new UnsupportedOperationException();
	}

	public InputStream getUnicodeStream(int columnIndex) {
		return getUnicodeStream(findColumn(columnIndex));
	}

	public InputStream getUnicodeStream(String columnName) {
		throw new UnsupportedOperationException();
	}

	public URL getURL(int columnIndex) throws SQLException {
		return getURL(findColumn(columnIndex));
	}

	public URL getURL(String columnName) throws SQLException {
		try {
			return new URL(getString(columnName));
		} catch (java.net.MalformedURLException m) {
			throw new SQLException("bad url [" + getString(columnName) + "]");
		}
	}

	// N stuff

	public String getNString(int columnIndex) {
		return getNString(findColumn(columnIndex));
	}

	public String getNString(String columnName) {
		throw new UnsupportedOperationException();
	}

	public Reader getNCharacterStream(int columnIndex) {
		return getNCharacterStream(findColumn(columnIndex));
	}

	public Reader getNCharacterStream(String columnName) {
		throw new UnsupportedOperationException();
	}

	public void updateNCharacterStream(int columnIndex, Reader x) {
		throw new UnsupportedOperationException();
	}

	public void updateNCharacterStream(int columnIndex, Reader x, long length) {
		throw new UnsupportedOperationException();
	}

	public void updateNCharacterStream(String columnLabel, Reader reader) {
		throw new UnsupportedOperationException();
	}

	public void updateNCharacterStream(String columnLabel, Reader reader, long length) {
		throw new UnsupportedOperationException();
	}

	public void updateNClob(int columnIndex, Reader reader) {
		throw new UnsupportedOperationException();
	}

	public void updateNClob(int columnIndex, Reader reader, long length) {
		throw new UnsupportedOperationException();
	}

	public void updateNClob(String columnLabel, Reader reader) {
		throw new UnsupportedOperationException();
	}

	public void updateNClob(String columnLabel, Reader reader, long length) {
		throw new UnsupportedOperationException();
	}

	public void updateNString(int columnIndex, String nString) {
		throw new UnsupportedOperationException();
	}

	public void updateNString(String columnLabel, String nString) {
		throw new UnsupportedOperationException();
	}

	public boolean wasNull() {
		throw new UnsupportedOperationException();
	}

	// column <-> int mapping

	public int findColumn(String columnName) {
		throw new UnsupportedOperationException();
	}

	public String findColumn(int i) {
		return result.getColName(i) ;
	}

	// random stuff

	public <T> T unwrap(Class<T> iface) throws SQLException {
		throw new UnsupportedOperationException();
	}

	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		throw new UnsupportedOperationException();
	}

	// moving throgh cursor

	public boolean next() {
		boolean hasNext = result.getRowCount() != 0 && result.getRowCount() > (pos+1) ;
		if (hasNext){
			pos++ ;
			current = result.getRow(pos) ;
		}
		return hasNext ;
		
//		if (!cursor.hasNext()) {
//			return false;
//		}
//		current = cursor.next();
//		return true;
	}

}
