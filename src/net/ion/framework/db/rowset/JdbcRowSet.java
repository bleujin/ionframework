// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   JdbcRowSet.java

package net.ion.framework.db.rowset;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
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

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import javax.sql.RowSet;

// Referenced classes of package sun.jdbc.rowset:
//            BaseRowSet

public class JdbcRowSet extends BaseRowSet implements RowSet {

	public JdbcRowSet() throws SQLException {
		conn = null;
		ps = null;
		rs = null;
		initParams();
		setShowDeleted(false);
		setQueryTimeout(0);
		setMaxRows(0);
		setMaxFieldSize(0);
		setType(1004);
		setConcurrency(1007);
		setReadOnly(true);
		setTransactionIsolation(2);
		setEscapeProcessing(true);
		setTypeMap(null);
	}

	public boolean absolute(int i) throws SQLException {
		checkState();
		boolean flag = rs.absolute(i);
		notifyCursorMoved();
		return flag;
	}

	public void afterLast() throws SQLException {
		checkState();
		rs.afterLast();
		notifyCursorMoved();
	}

	public void beforeFirst() throws SQLException {
		checkState();
		rs.beforeFirst();
		notifyCursorMoved();
	}

	public void cancelRowUpdates() throws SQLException {
		checkState();
		rs.cancelRowUpdates();
		notifyRowChanged();
	}

	void checkState() throws SQLException {
		if (conn == null || ps == null || rs == null)
			throw new SQLException("Invalid State");
		else
			return;
	}

	public void clearWarnings() throws SQLException {
		checkState();
		rs.clearWarnings();
	}

	public void close() throws SQLException {
		if (rs != null)
			rs.close();
		if (ps != null)
			ps.close();
		if (conn != null)
			conn.close();
	}

	private Connection connect() throws SQLException {
		if (getDataSourceName() != null)
			try {
				InitialContext initialcontext = new InitialContext();
				DataSource datasource = (DataSource) initialcontext.lookup(getDataSourceName());
				return datasource.getConnection(getUsername(), getPassword());
			} catch (NamingException _ex) {
				throw new SQLException("(JNDI) Unable to connect");
			}
		if (getUrl() != null)
			return DriverManager.getConnection(getUrl(), getUsername(), getPassword());
		else
			return null;
	}

	private void decodeParams(Object aobj[], PreparedStatement preparedstatement) throws SQLException {
		Object aobj1[] = null;
		for (int i = 0; i < aobj.length; i++)
			if (aobj1[i] instanceof Object[]) {
				aobj1 = (Object[]) aobj[i];
				if (aobj1.length == 2) {
					if (aobj1[0] == null)
						preparedstatement.setNull(i + 1, ((Integer) aobj1[1]).intValue());
					else if ((aobj1[0] instanceof Date) || (aobj1[0] instanceof Time) || (aobj1[0] instanceof Timestamp)) {
						System.err.println("Detected a Date");
						if (aobj1[1] instanceof Calendar) {
							System.err.println("Detected a Calendar");
							preparedstatement.setDate(i + 1, (Date) aobj1[0], (Calendar) aobj1[1]);
						} else {
							throw new SQLException("Unable to deduce param type");
						}
					} else if (aobj1[0] instanceof Reader)
						preparedstatement.setCharacterStream(i + 1, (Reader) aobj1[0], ((Integer) aobj1[1]).intValue());
					else if (aobj1[1] instanceof Integer)
						preparedstatement.setObject(i + 1, aobj1[0], ((Integer) aobj1[1]).intValue());
				} else if (aobj1.length == 3) {
					if (aobj1[0] == null) {
						preparedstatement.setNull(i + 1, ((Integer) aobj1[1]).intValue(), (String) aobj1[2]);
					} else {
						if (aobj1[0] instanceof InputStream)
							switch (((Integer) aobj1[2]).intValue()) {
							case 0: // '\0'
								preparedstatement.setUnicodeStream(i + 1, (InputStream) aobj1[0], ((Integer) aobj1[1]).intValue());
								// fall through

							case 1: // '\001'
								preparedstatement.setBinaryStream(i + 1, (InputStream) aobj1[0], ((Integer) aobj1[1]).intValue());
								// fall through

							case 2: // '\002'
								preparedstatement.setAsciiStream(i + 1, (InputStream) aobj1[0], ((Integer) aobj1[1]).intValue());
								// fall through

							default:
								throw new SQLException("Unable to deduce parameter type");
							}
						if ((aobj1[1] instanceof Integer) && (aobj1[2] instanceof Integer))
							preparedstatement.setObject(i + 1, aobj1[0], ((Integer) aobj1[1]).intValue(), ((Integer) aobj1[2]).intValue());
						else
							throw new SQLException("Unable to deduce param type");
					}
				} else {
					preparedstatement.setObject(i + 1, aobj[i]);
				}
			}

	}

	public void deleteRow() throws SQLException {
		checkState();
		rs.deleteRow();
		notifyRowChanged();
	}

	public void execute() throws SQLException {
		conn = connect();
		try {
			conn.setTransactionIsolation(getTransactionIsolation());
		} catch (SQLException sqlexception) {
			System.err.println("JdbcRowSet (setTransactionIsolation): " + sqlexception.getMessage());
		}
		try {
			conn.setTypeMap(getTypeMap());
		} catch (Throwable throwable) {
			System.err.println("JdbcRowSet (setTypeMap): " + throwable.getMessage());
		}
		try {
			ps = conn.prepareStatement(getCommand());
		} catch (SQLException sqlexception1) {
			System.err.println("JdbcRowSet (execute): " + sqlexception1.getMessage());
			if (ps != null)
				ps.close();
			if (conn != null)
				conn.close();
			throw new SQLException(sqlexception1.getMessage());
		}
		setProperties(ps);
		decodeParams(getParams(), ps);
		rs = ps.executeQuery();
		notifyRowSetChanged();
	}

	public int findColumn(String s) throws SQLException {
		checkState();
		return rs.findColumn(s);
	}

	public boolean first() throws SQLException {
		checkState();
		boolean flag = rs.first();
		notifyCursorMoved();
		return flag;
	}

	public Array getArray(int i) throws SQLException {
		checkState();
		return rs.getArray(i);
	}

	public Array getArray(String s) throws SQLException {
		return getArray(findColumn(s));
	}

	public InputStream getAsciiStream(int i) throws SQLException {
		checkState();
		return rs.getAsciiStream(i);
	}

	public InputStream getAsciiStream(String s) throws SQLException {
		return getAsciiStream(findColumn(s));
	}

	public BigDecimal getBigDecimal(int i) throws SQLException {
		checkState();
		return rs.getBigDecimal(i);
	}

	/**
	 * @deprecated Method getBigDecimal is deprecated
	 */

	public BigDecimal getBigDecimal(int i, int j) throws SQLException {
		checkState();
		return rs.getBigDecimal(i, j);
	}

	public BigDecimal getBigDecimal(String s) throws SQLException {
		return getBigDecimal(findColumn(s));
	}

	/**
	 * @deprecated Method getBigDecimal is deprecated
	 */

	public BigDecimal getBigDecimal(String s, int i) throws SQLException {
		return getBigDecimal(findColumn(s), i);
	}

	public InputStream getBinaryStream(int i) throws SQLException {
		checkState();
		return rs.getBinaryStream(i);
	}

	public InputStream getBinaryStream(String s) throws SQLException {
		return getBinaryStream(findColumn(s));
	}

	public Blob getBlob(int i) throws SQLException {
		checkState();
		return rs.getBlob(i);
	}

	public Blob getBlob(String s) throws SQLException {
		return getBlob(findColumn(s));
	}

	public boolean getBoolean(int i) throws SQLException {
		checkState();
		return rs.getBoolean(i);
	}

	public boolean getBoolean(String s) throws SQLException {
		return getBoolean(findColumn(s));
	}

	public byte getByte(int i) throws SQLException {
		checkState();
		return rs.getByte(i);
	}

	public byte getByte(String s) throws SQLException {
		return getByte(findColumn(s));
	}

	public byte[] getBytes(int i) throws SQLException {
		checkState();
		return rs.getBytes(i);
	}

	public byte[] getBytes(String s) throws SQLException {
		return getBytes(findColumn(s));
	}

	public Reader getCharacterStream(int i) throws SQLException {
		checkState();
		return rs.getCharacterStream(i);
	}

	public Reader getCharacterStream(String s) throws SQLException {
		return getCharacterStream(findColumn(s));
	}

	public Clob getClob(int i) throws SQLException {
		checkState();
		return rs.getClob(i);
	}

	public Clob getClob(String s) throws SQLException {
		return getClob(findColumn(s));
	}

	public int getConcurrency() throws SQLException {
		checkState();
		return rs.getConcurrency();
	}

	public String getCursorName() throws SQLException {
		checkState();
		return rs.getCursorName();
	}

	public Date getDate(int i) throws SQLException {
		checkState();
		return rs.getDate(i);
	}

	public Date getDate(int i, Calendar calendar) throws SQLException {
		checkState();
		return rs.getDate(i, calendar);
	}

	public Date getDate(String s) throws SQLException {
		return getDate(findColumn(s));
	}

	public Date getDate(String s, Calendar calendar) throws SQLException {
		return getDate(findColumn(s), calendar);
	}

	public double getDouble(int i) throws SQLException {
		checkState();
		return rs.getDouble(i);
	}

	public double getDouble(String s) throws SQLException {
		return getDouble(findColumn(s));
	}

	public int getFetchDirection() throws SQLException {
		checkState();
		return rs.getFetchDirection();
	}

	public float getFloat(int i) throws SQLException {
		checkState();
		return rs.getFloat(i);
	}

	public float getFloat(String s) throws SQLException {
		return getFloat(findColumn(s));
	}

	public int getInt(int i) throws SQLException {
		checkState();
		return rs.getInt(i);
	}

	public int getInt(String s) throws SQLException {
		return getInt(findColumn(s));
	}

	public long getLong(int i) throws SQLException {
		checkState();
		return rs.getLong(i);
	}

	public long getLong(String s) throws SQLException {
		return getLong(findColumn(s));
	}

	public ResultSetMetaData getMetaData() throws SQLException {
		checkState();
		return rs.getMetaData();
	}

	public Object getObject(int i) throws SQLException {
		checkState();
		return rs.getObject(i);
	}

	public Object getObject(int i, Map map) throws SQLException {
		checkState();
		return rs.getObject(i, map);
	}

	public Object getObject(String s) throws SQLException {
		return getObject(findColumn(s));
	}

	public Object getObject(String s, Map map) throws SQLException {
		return getObject(findColumn(s), map);
	}

	public Ref getRef(int i) throws SQLException {
		checkState();
		return rs.getRef(i);
	}

	public Ref getRef(String s) throws SQLException {
		return getRef(findColumn(s));
	}

	public int getRow() throws SQLException {
		checkState();
		return rs.getRow();
	}

	public short getShort(int i) throws SQLException {
		checkState();
		return rs.getShort(i);
	}

	public short getShort(String s) throws SQLException {
		return getShort(findColumn(s));
	}

	public Statement getStatement() throws SQLException {
		throw new SQLException("Unsupported Operation");
	}

	public String getString(int i) throws SQLException {
		checkState();
		return rs.getString(i);
	}

	public String getString(String s) throws SQLException {
		return getString(findColumn(s));
	}

	public Time getTime(int i) throws SQLException {
		checkState();
		return rs.getTime(i);
	}

	public Time getTime(int i, Calendar calendar) throws SQLException {
		checkState();
		return rs.getTime(i, calendar);
	}

	public Time getTime(String s) throws SQLException {
		return getTime(findColumn(s));
	}

	public Time getTime(String s, Calendar calendar) throws SQLException {
		return getTime(findColumn(s), calendar);
	}

	public Timestamp getTimestamp(int i) throws SQLException {
		checkState();
		return rs.getTimestamp(i);
	}

	public Timestamp getTimestamp(int i, Calendar calendar) throws SQLException {
		checkState();
		return rs.getTimestamp(i, calendar);
	}

	public Timestamp getTimestamp(String s) throws SQLException {
		return getTimestamp(findColumn(s));
	}

	public Timestamp getTimestamp(String s, Calendar calendar) throws SQLException {
		return getTimestamp(findColumn(s), calendar);
	}

	public int getType() throws SQLException {
		checkState();
		return rs.getType();
	}

	/**
	 * @deprecated Method getUnicodeStream is deprecated
	 */

	public InputStream getUnicodeStream(int i) throws SQLException {
		checkState();
		return rs.getUnicodeStream(i);
	}

	/**
	 * @deprecated Method getUnicodeStream is deprecated
	 */

	public InputStream getUnicodeStream(String s) throws SQLException {
		return getUnicodeStream(findColumn(s));
	}

	public SQLWarning getWarnings() throws SQLException {
		checkState();
		return rs.getWarnings();
	}

	public void insertRow() throws SQLException {
		checkState();
		rs.insertRow();
		notifyRowChanged();
	}

	public boolean isAfterLast() throws SQLException {
		checkState();
		return rs.isAfterLast();
	}

	public boolean isBeforeFirst() throws SQLException {
		checkState();
		return rs.isBeforeFirst();
	}

	public boolean isFirst() throws SQLException {
		checkState();
		return rs.isFirst();
	}

	public boolean isLast() throws SQLException {
		checkState();
		return rs.isLast();
	}

	public boolean last() throws SQLException {
		checkState();
		boolean flag = rs.last();
		notifyCursorMoved();
		return flag;
	}

	public void moveToCurrentRow() throws SQLException {
		checkState();
		rs.moveToCurrentRow();
	}

	public void moveToInsertRow() throws SQLException {
		checkState();
		rs.moveToInsertRow();
	}

	public boolean next() throws SQLException {
		checkState();
		boolean flag = rs.next();
		notifyCursorMoved();
		return flag;
	}

	public boolean previous() throws SQLException {
		checkState();
		boolean flag = rs.previous();
		notifyCursorMoved();
		return flag;
	}

	public void refreshRow() throws SQLException {
		checkState();
		rs.refreshRow();
	}

	public boolean relative(int i) throws SQLException {
		checkState();
		boolean flag = rs.relative(i);
		notifyCursorMoved();
		return flag;
	}

	public boolean rowDeleted() throws SQLException {
		checkState();
		return rs.rowDeleted();
	}

	public boolean rowInserted() throws SQLException {
		checkState();
		return rs.rowInserted();
	}

	public boolean rowUpdated() throws SQLException {
		checkState();
		return rs.rowUpdated();
	}

	public void setFetchDirection(int i) throws SQLException {
		checkState();
		rs.setFetchDirection(i);
	}

	public void setFetchSize(int i) throws SQLException {
		checkState();
		rs.setFetchSize(i);
	}

	private void setProperties(PreparedStatement preparedstatement) throws SQLException {
		try {
			preparedstatement.setEscapeProcessing(getEscapeProcessing());
		} catch (SQLException sqlexception) {
			System.err.println("JdbcRowSet (setEscapeProcessing): " + sqlexception.getMessage());
		}
		try {
			preparedstatement.setMaxFieldSize(getMaxFieldSize());
		} catch (SQLException sqlexception1) {
			System.err.println("JdbcRowSet (setMaxFieldSize): " + sqlexception1.getMessage());
		}
		try {
			preparedstatement.setMaxRows(getMaxRows());
		} catch (SQLException sqlexception2) {
			System.err.println("JdbcRowSet (setMaxRows): " + sqlexception2.getMessage());
		}
		try {
			preparedstatement.setQueryTimeout(getQueryTimeout());
		} catch (SQLException sqlexception3) {
			System.err.println("JdbcRowSet (setQueryTimeout): " + sqlexception3.getMessage());
		}
	}

	public void updateAsciiStream(int i, InputStream inputstream, int j) throws SQLException {
		checkState();
		rs.updateAsciiStream(i, inputstream, j);
	}

	public void updateAsciiStream(String s, InputStream inputstream, int i) throws SQLException {
		updateAsciiStream(findColumn(s), inputstream, i);
	}

	public void updateBigDecimal(int i, BigDecimal bigdecimal) throws SQLException {
		checkState();
		rs.updateBigDecimal(i, bigdecimal);
	}

	public void updateBigDecimal(String s, BigDecimal bigdecimal) throws SQLException {
		updateBigDecimal(findColumn(s), bigdecimal);
	}

	public void updateBinaryStream(int i, InputStream inputstream, int j) throws SQLException {
		checkState();
		rs.updateBinaryStream(i, inputstream, j);
	}

	public void updateBinaryStream(String s, InputStream inputstream, int i) throws SQLException {
		updateBinaryStream(findColumn(s), inputstream, i);
	}

	public void updateBoolean(int i, boolean flag) throws SQLException {
		checkState();
		rs.updateBoolean(i, flag);
	}

	public void updateBoolean(String s, boolean flag) throws SQLException {
		updateBoolean(findColumn(s), flag);
	}

	public void updateByte(int i, byte byte0) throws SQLException {
		checkState();
		rs.updateByte(i, byte0);
	}

	public void updateByte(String s, byte byte0) throws SQLException {
		updateByte(findColumn(s), byte0);
	}

	public void updateBytes(int i, byte abyte0[]) throws SQLException {
		checkState();
		rs.updateBytes(i, abyte0);
	}

	public void updateBytes(String s, byte abyte0[]) throws SQLException {
		updateBytes(findColumn(s), abyte0);
	}

	public void updateCharacterStream(int i, Reader reader, int j) throws SQLException {
		checkState();
		rs.updateCharacterStream(i, reader, j);
	}

	public void updateCharacterStream(String s, Reader reader, int i) throws SQLException {
		updateCharacterStream(findColumn(s), reader, i);
	}

	public void updateDate(int i, Date date) throws SQLException {
		checkState();
		rs.updateDate(i, date);
	}

	public void updateDate(String s, Date date) throws SQLException {
		updateDate(findColumn(s), date);
	}

	public void updateDouble(int i, double d) throws SQLException {
		checkState();
		rs.updateDouble(i, d);
	}

	public void updateDouble(String s, double d) throws SQLException {
		updateDouble(findColumn(s), d);
	}

	public void updateFloat(int i, float f) throws SQLException {
		checkState();
		rs.updateFloat(i, f);
	}

	public void updateFloat(String s, float f) throws SQLException {
		updateFloat(findColumn(s), f);
	}

	public void updateInt(int i, int j) throws SQLException {
		checkState();
		rs.updateInt(i, j);
	}

	public void updateInt(String s, int i) throws SQLException {
		updateInt(findColumn(s), i);
	}

	public void updateLong(int i, long l) throws SQLException {
		checkState();
		rs.updateLong(i, l);
	}

	public void updateLong(String s, long l) throws SQLException {
		updateLong(findColumn(s), l);
	}

	public void updateNull(int i) throws SQLException {
		checkState();
		rs.updateNull(i);
	}

	public void updateNull(String s) throws SQLException {
		updateNull(findColumn(s));
	}

	public void updateObject(int i, Object obj) throws SQLException {
		checkState();
		rs.updateObject(i, obj);
	}

	public void updateObject(int i, Object obj, int j) throws SQLException {
		checkState();
		rs.updateObject(i, obj, j);
	}

	public void updateObject(String s, Object obj) throws SQLException {
		updateObject(findColumn(s), obj);
	}

	public void updateObject(String s, Object obj, int i) throws SQLException {
		updateObject(findColumn(s), obj, i);
	}

	public void updateRow() throws SQLException {
		checkState();
		rs.updateRow();
		notifyRowChanged();
	}

	public void updateShort(int i, short word0) throws SQLException {
		checkState();
		rs.updateShort(i, word0);
	}

	public void updateShort(String s, short word0) throws SQLException {
		updateShort(findColumn(s), word0);
	}

	public void updateString(int i, String s) throws SQLException {
		checkState();
		rs.updateString(i, s);
	}

	public void updateString(String s, String s1) throws SQLException {
		updateString(findColumn(s), s1);
	}

	public void updateTime(int i, Time time) throws SQLException {
		checkState();
		rs.updateTime(i, time);
	}

	public void updateTime(String s, Time time) throws SQLException {
		updateTime(findColumn(s), time);
	}

	public void updateTimestamp(int i, Timestamp timestamp) throws SQLException {
		checkState();
		rs.updateTimestamp(i, timestamp);
	}

	public void updateTimestamp(String s, Timestamp timestamp) throws SQLException {
		updateTimestamp(findColumn(s), timestamp);
	}

	public boolean wasNull() throws SQLException {
		checkState();
		return rs.wasNull();
	}

	private Connection conn;
	private PreparedStatement ps;
	private ResultSet rs;

}
