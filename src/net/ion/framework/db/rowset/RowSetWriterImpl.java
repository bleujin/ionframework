// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   RowSetWriterImpl.java

package net.ion.framework.db.rowset;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Vector;

import javax.sql.RowSetInternal;
import javax.sql.RowSetWriter;

// Referenced classes of package sun.jdbc.rowset:
//            BaseRowSet, CachedRowSet, RowSetReaderImpl

public class RowSetWriterImpl implements RowSetWriter, Serializable {

	public RowSetWriterImpl() {
	}

	private void buildKeyDesc(CachedRowSet cachedrowset) throws SQLException {
		keyCols = cachedrowset.getKeyColumns();
		if (keyCols == null || keyCols.length == 0) {
			keyCols = new int[callerColumnCount];
			for (int i = 0; i < keyCols.length;)
				keyCols[i++] = i;

		}
		params = new Object[keyCols.length];
	}

	private String buildTableName(DatabaseMetaData databasemetadata, String s, String s1) throws SQLException {
		String s2 = new String();
		if (databasemetadata.isCatalogAtStart()) {
			if (s.length() > 0)
				s2 = s2 + s + databasemetadata.getCatalogSeparator();
			s2 = s2 + s1;
		} else {
			s2 = s2 + s1;
			if (s.length() > 0)
				s2 = s2 + databasemetadata.getCatalogSeparator() + s;
		}
		s2 = s2 + " ";
		return s2;
	}

	private String buildWhereClause(String s, ResultSet resultset) throws SQLException {
		s = "WHERE ";
		for (int i = 0; i < keyCols.length; i++) {
			if (i > 0)
				s = s + "AND ";
			s = s + callerMd.getColumnName(keyCols[i]);
			params[i] = resultset.getObject(keyCols[i]);
			if (resultset.wasNull())
				s = s + " IS NULL ";
			else
				s = s + " = ? ";
		}

		return s;
	}

	private boolean deleteOriginalRow(CachedRowSet cachedrowset) throws SQLException {
		int k = 0;
		ResultSet resultset = cachedrowset.getOriginalRow();
		resultset.next();
		deleteWhere = buildWhereClause(deleteWhere, resultset);
		PreparedStatement preparedstatement = con.prepareStatement(selectCmd + deleteWhere);
		for (int i = 0; i < keyCols.length; i++)
			if (params[i] != null)
				preparedstatement.setObject(++k, params[i]);

		try {
			preparedstatement.setMaxRows(cachedrowset.getMaxRows());
			preparedstatement.setMaxFieldSize(cachedrowset.getMaxFieldSize());
			preparedstatement.setEscapeProcessing(cachedrowset.getEscapeProcessing());
			preparedstatement.setQueryTimeout(cachedrowset.getQueryTimeout());
		} catch (Exception _ex) {
		}
		ResultSet resultset1 = preparedstatement.executeQuery();
		if (resultset1.next()) {
			if (resultset1.next())
				return true;
			resultset1.close();
			preparedstatement.close();
			String s = deleteCmd + deleteWhere;
			preparedstatement = con.prepareStatement(s);
			int l = 0;
			for (int j = 0; j < keyCols.length; j++)
				if (params[j] != null)
					preparedstatement.setObject(++l, params[j]);

			if (preparedstatement.executeUpdate() != 1)
				return true;
			preparedstatement.close();
		} else {
			return true;
		}
		return false;
	}

	public RowSetReaderImpl getReader() throws SQLException {
		return reader;
	}

	private void initSQLStatements(CachedRowSet cachedrowset) throws SQLException {
		callerMd = cachedrowset.getMetaData();
		callerColumnCount = callerMd.getColumnCount();
		if (callerColumnCount < 1)
			return;
		String s = cachedrowset.getTableName();
		if (s == null) {
			s = callerMd.getSchemaName(1);
			if (s != null && s.length() > 0)
				s = s + ".";
			String s1 = callerMd.getTableName(1);
			if (s == null || s.length() == 0)
				throw new SQLException("writeData cannot determine the table name.");
			s = s + s1;
		}
		String s2 = callerMd.getCatalogName(1);
		DatabaseMetaData databasemetadata = con.getMetaData();
		selectCmd = "SELECT ";
		for (int i = 1; i <= callerColumnCount; i++) {
			selectCmd += callerMd.getColumnName(i);
			if (i < callerMd.getColumnCount())
				selectCmd += ", ";
			else
				selectCmd += " ";
		}

		selectCmd += "FROM " + buildTableName(databasemetadata, s2, s);
		updateCmd = "UPDATE " + buildTableName(databasemetadata, s2, s);
		updateCmd += "SET ";
		insertCmd = "INSERT INTO " + buildTableName(databasemetadata, s2, s);
		insertCmd += "(";
		for (int j = 1; j <= callerColumnCount; j++) {
			insertCmd += callerMd.getColumnName(j);
			if (j < callerMd.getColumnCount())
				insertCmd += ", ";
			else
				insertCmd += ") VALUES (";
		}

		for (int k = 1; k <= callerColumnCount; k++) {
			insertCmd += "?";
			if (k < callerColumnCount)
				insertCmd += ", ";
			else
				insertCmd += ")";
		}

		deleteCmd = "DELETE FROM " + buildTableName(databasemetadata, s2, s);
		buildKeyDesc(cachedrowset);
	}

	private boolean insertNewRow(CachedRowSet cachedrowset, PreparedStatement preparedstatement) throws SQLException {
		boolean flag = false;
		try {
			for (int i = 1; i <= cachedrowset.getMetaData().getColumnCount(); i++) {
				Object obj = cachedrowset.getObject(i);
				if (obj != null) {
					if (cachedrowset.getMetaData().getColumnType(i) == 5)
						preparedstatement.setShort(i, ((Short) obj).shortValue());
					else
						preparedstatement.setObject(i, obj);
				} else {
					preparedstatement.setNull(i, cachedrowset.getMetaData().getColumnType(i));
				}
			}

			int j = preparedstatement.executeUpdate();
			if (j != 1) {
				System.err.println("excuteUpdate ret: " + j);
				return true;
			} else {
				return false;
			}
		} catch (SQLException sqlexception) {
			System.out.println(sqlexception.getMessage());
		}
		return true;
	}

	public void setReader(RowSetReaderImpl rowsetreaderimpl) throws SQLException {
		reader = rowsetreaderimpl;
	}

	private boolean updateOriginalRow(CachedRowSet cachedrowset) throws SQLException {
		boolean flag = false;
		int l = 0;
		ResultSet resultset = cachedrowset.getOriginalRow();
		resultset.next();
		try {
			updateWhere = buildWhereClause(updateWhere, resultset);
			PreparedStatement preparedstatement = con.prepareStatement(selectCmd + updateWhere);
			for (int i = 0; i < keyCols.length; i++)
				if (params[i] != null)
					preparedstatement.setObject(++l, params[i]);

			try {
				preparedstatement.setMaxRows(cachedrowset.getMaxRows());
				preparedstatement.setMaxFieldSize(cachedrowset.getMaxFieldSize());
				preparedstatement.setEscapeProcessing(cachedrowset.getEscapeProcessing());
				preparedstatement.setQueryTimeout(cachedrowset.getQueryTimeout());
			} catch (Exception _ex) {
			}
			ResultSet resultset1 = preparedstatement.executeQuery();
			if (resultset1.next()) {
				if (resultset1.next()) {
					System.out.println("Cannot uniquely id. row");
					return true;
				}
				resultset1.close();
				preparedstatement.close();
				Vector vector = new Vector();
				String s = new String(updateCmd);
				boolean flag1 = true;
				for (int j = 1; j <= callerColumnCount; j++) {
					Object obj = resultset.getObject(j);
					Object obj1 = cachedrowset.getObject(j);
					if ((obj != null || obj1 != null) && (obj == null || obj1 == null || !obj.equals(obj1))) {
						if (!flag1)
							s = s + ", ";
						s = s + cachedrowset.getMetaData().getColumnName(j);
						vector.add(new Integer(j));
						s = s + " = ? ";
						flag1 = false;
					}
				}

				if (!flag1 && vector.size() == 0)
					return false;
				s = s + updateWhere;
				preparedstatement = con.prepareStatement(s);
				int k;
				for (k = 0; k < vector.size(); k++) {
					Object obj2 = cachedrowset.getObject(((Integer) vector.get(k)).intValue());
					if (obj2 != null)
						preparedstatement.setObject(k + 1, obj2);
					else
						preparedstatement.setNull(k + 1, cachedrowset.getMetaData().getColumnType(k + 1));
				}

				int i1 = k;
				for (k = 0; k < keyCols.length; k++)
					if (params[k] != null)
						preparedstatement.setObject(++i1, params[k]);

				k = preparedstatement.executeUpdate();
				if (k != 1) {
					System.err.println("excuteUpdate ret: " + k);
					return true;
				} else {
					return false;
				}
			} else {
				return true;
			}
		} catch (SQLException sqlexception) {
			System.out.println(sqlexception.getMessage());
		}
		return true;
	}

	public boolean writeData(RowSetInternal rowsetinternal) throws SQLException {
		boolean flag = false;
		boolean flag1 = false;
		PreparedStatement preparedstatement = null;
		CachedRowSet cachedrowset = (CachedRowSet) rowsetinternal;
		con = ((RowSetReaderImpl) cachedrowset.getReader()).connect(rowsetinternal);
		if (con == null)
			throw new SQLException("Unable to get Connection");
		if (con.getAutoCommit())
			con.setAutoCommit(false);
		con.setTransactionIsolation(cachedrowset.getTransactionIsolation());
		initSQLStatements(cachedrowset);
		preparedstatement = con.prepareStatement(insertCmd);
		if (callerColumnCount < 1) {
			if (((RowSetReaderImpl) cachedrowset.getReader()).getCloseConnection())
				con.close();
			return true;
		}
		flag1 = cachedrowset.getShowDeleted();
		cachedrowset.setShowDeleted(true);
		cachedrowset.beforeFirst();
		while (cachedrowset.next())
			if ((!cachedrowset.rowDeleted() || !cachedrowset.rowInserted())
					&& (cachedrowset.rowDeleted() ? (flag = deleteOriginalRow(cachedrowset)) : cachedrowset.rowInserted() ? (flag = insertNewRow(cachedrowset,
							preparedstatement)) : cachedrowset.rowUpdated() && (flag = updateOriginalRow(cachedrowset))))
				break;
		preparedstatement.close();
		cachedrowset.setShowDeleted(flag1);
		if (flag) {
			con.rollback();
			if (((RowSetReaderImpl) cachedrowset.getReader()).getCloseConnection())
				con.close();
			return false;
		}
		con.commit();
		if (((RowSetReaderImpl) cachedrowset.getReader()).getCloseConnection())
			con.close();
		return true;
	}

	private Connection con;
	private String selectCmd;
	private String updateCmd;
	private String updateWhere;
	private String deleteCmd;
	private String deleteWhere;
	private String insertCmd;
	private int keyCols[];
	private Object params[];
	private RowSetReaderImpl reader;
	private ResultSetMetaData callerMd;
	private int callerColumnCount;
}
