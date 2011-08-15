// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   RowSetReaderImpl.java

package net.ion.framework.db.rowset;

import java.io.InputStream;
import java.io.Reader;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import javax.sql.RowSet;
import javax.sql.RowSetInternal;
import javax.sql.RowSetReader;

// Referenced classes of package sun.jdbc.rowset:
//            BaseRowSet, CachedRowSet

public class RowSetReaderImpl implements RowSetReader, Serializable {

	public RowSetReaderImpl() {
		writerCalls = 0;
		userCon = false;
	}

	public Connection connect(RowSetInternal rowsetinternal) throws SQLException {
		if (rowsetinternal.getConnection() != null) {
			userCon = true;
			return rowsetinternal.getConnection();
		}
		if (((RowSet) rowsetinternal).getDataSourceName() != null)
			try {
				InitialContext initialcontext = new InitialContext();
				DataSource datasource = (DataSource) initialcontext.lookup(((RowSet) rowsetinternal).getDataSourceName());
				return datasource.getConnection(((RowSet) rowsetinternal).getUsername(), ((RowSet) rowsetinternal).getPassword());
			} catch (NamingException _ex) {
				throw new SQLException("(JNDI) Unable to connect");
			}
		if (((RowSet) rowsetinternal).getUrl() != null)
			return DriverManager.getConnection(((RowSet) rowsetinternal).getUrl(), ((RowSet) rowsetinternal).getUsername(), ((RowSet) rowsetinternal)
					.getPassword());
		else
			return null;
	}

	private void decodeParams(Object aobj[], PreparedStatement preparedstatement) throws SQLException {
		for (int j = 0; j < aobj.length; j++) {
			int i;
			try {
				i = Array.getLength(aobj[j]);
			} catch (IllegalArgumentException _ex) {
				preparedstatement.setObject(j + 1, aobj[j]);
				continue;
			}
			Object aobj1[] = (Object[]) aobj[j];
			if (i == 2) {
				if (aobj1[0] == null)
					preparedstatement.setNull(j + 1, ((Integer) aobj1[1]).intValue());
				else if ((aobj1[0] instanceof Date) || (aobj1[0] instanceof Time) || (aobj1[0] instanceof Timestamp)) {
					System.err.println("Detected a Date");
					if (aobj1[1] instanceof Calendar) {
						System.err.println("Detected a Calendar");
						preparedstatement.setDate(j + 1, (Date) aobj1[0], (Calendar) aobj1[1]);
					} else {
						throw new SQLException("Unable to deduce param type");
					}
				} else if (aobj1[0] instanceof Reader)
					preparedstatement.setCharacterStream(j + 1, (Reader) aobj1[0], ((Integer) aobj1[1]).intValue());
				else if (aobj1[1] instanceof Integer)
					preparedstatement.setObject(j + 1, aobj1[0], ((Integer) aobj1[1]).intValue());
			} else if (i == 3)
				if (aobj1[0] == null) {
					preparedstatement.setNull(j + 1, ((Integer) aobj1[1]).intValue(), (String) aobj1[2]);
				} else {
					if (aobj1[0] instanceof InputStream)
						switch (((Integer) aobj1[2]).intValue()) {
						case 0: // '\0'
							preparedstatement.setUnicodeStream(j + 1, (InputStream) aobj1[0], ((Integer) aobj1[1]).intValue());
							// fall through

						case 1: // '\001'
							preparedstatement.setBinaryStream(j + 1, (InputStream) aobj1[0], ((Integer) aobj1[1]).intValue());
							// fall through

						case 2: // '\002'
							preparedstatement.setAsciiStream(j + 1, (InputStream) aobj1[0], ((Integer) aobj1[1]).intValue());
							// fall through

						default:
							throw new SQLException("Unable to deduce parameter type");
						}
					if ((aobj1[1] instanceof Integer) && (aobj1[2] instanceof Integer))
						preparedstatement.setObject(j + 1, aobj1[0], ((Integer) aobj1[1]).intValue(), ((Integer) aobj1[2]).intValue());
					else
						throw new SQLException("Unable to deduce param type");
				}
		}

	}

	protected boolean getCloseConnection() {
		return !userCon;
	}

	public void readData(RowSetInternal rowsetinternal) throws SQLException {
		try {
			CachedRowSet cachedrowset = (CachedRowSet) rowsetinternal;
			cachedrowset.close();
			writerCalls = 0;
			userCon = false;
			Connection connection = connect(rowsetinternal);
			if (connection == null || cachedrowset.getCommand() == null)
				throw new SQLException();
			try {
				connection.setTransactionIsolation(cachedrowset.getTransactionIsolation());
			} catch (Exception _ex) {
			}
			PreparedStatement preparedstatement = connection.prepareStatement(cachedrowset.getCommand());
			decodeParams(rowsetinternal.getParams(), preparedstatement);
			try {
				preparedstatement.setMaxRows(cachedrowset.getMaxRows());
				preparedstatement.setMaxFieldSize(cachedrowset.getMaxFieldSize());
				preparedstatement.setEscapeProcessing(cachedrowset.getEscapeProcessing());
				preparedstatement.setQueryTimeout(cachedrowset.getQueryTimeout());
			} catch (Exception _ex) {
			}
			ResultSet resultset = preparedstatement.executeQuery();
			cachedrowset.populate(resultset);
			resultset.close();
			preparedstatement.close();
			try {
				connection.commit();
			} catch (SQLException _ex) {
			}
			if (getCloseConnection())
				connection.close();
		} catch (SQLException sqlexception) {
			throw sqlexception;
		}
	}

	public boolean reset() throws SQLException {
		writerCalls++;
		return writerCalls == 1;
	}

	private int writerCalls;
	private boolean userCon;
}
