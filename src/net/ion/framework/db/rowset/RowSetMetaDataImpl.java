// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   RowSetMetaDataImpl.java

package net.ion.framework.db.rowset;

import java.io.Serializable;
import java.sql.SQLException;

import javax.sql.RowSetMetaData;

// Referenced classes of package sun.jdbc.rowset:
//            ColInfo

public class RowSetMetaDataImpl implements RowSetMetaData, Serializable {

	private int colCount;
	private ColInfo colInfo[];

	public RowSetMetaDataImpl() {
	}

	private void checkColRange(int i) throws SQLException {
		if (i <= 0 || i > colCount)
			throw new SQLException("Invalid column index");
		else
			return;
	}

	public String getCatalogName(int i) throws SQLException {
		checkColRange(i);
		return colInfo[i].catName;
	}

	public String getColumnClassName(int i) throws SQLException {
		return null;
	}

	public int getColumnCount() throws SQLException {
		return colCount;
	}

	public int getColumnDisplaySize(int i) throws SQLException {
		checkColRange(i);
		return colInfo[i].columnDisplaySize;
	}

	public String getColumnLabel(int i) throws SQLException {
		checkColRange(i);
		return colInfo[i].columnLabel;
	}

	public String getColumnName(int i) throws SQLException {
		checkColRange(i);
		return colInfo[i].columnName;
	}

	public int getColumnType(int i) throws SQLException {
		checkColRange(i);
		return colInfo[i].colType;
	}

	public String getColumnTypeName(int i) throws SQLException {
		checkColRange(i);
		return colInfo[i].colTypeName;
	}

	public int getPrecision(int i) throws SQLException {
		checkColRange(i);
		return colInfo[i].colPrecision;
	}

	public int getScale(int i) throws SQLException {
		checkColRange(i);
		return colInfo[i].colScale;
	}

	public String getSchemaName(int i) throws SQLException {
		checkColRange(i);
		return colInfo[i].schemaName;
	}

	public String getTableName(int i) throws SQLException {
		checkColRange(i);
		return colInfo[i].tableName;
	}

	public boolean isAutoIncrement(int i) throws SQLException {
		checkColRange(i);
		return colInfo[i].autoIncrement;
	}

	public boolean isCaseSensitive(int i) throws SQLException {
		checkColRange(i);
		return colInfo[i].caseSensitive;
	}

	public boolean isCurrency(int i) throws SQLException {
		checkColRange(i);
		return colInfo[i].currency;
	}

	public boolean isDefinitelyWritable(int i) throws SQLException {
		return true;
	}

	public int isNullable(int i) throws SQLException {
		checkColRange(i);
		return colInfo[i].nullable;
	}

	public boolean isReadOnly(int i) throws SQLException {
		checkColRange(i);
		return true;
	}

	public boolean isSearchable(int i) throws SQLException {
		checkColRange(i);
		return colInfo[i].searchable;
	}

	public boolean isSigned(int i) throws SQLException {
		checkColRange(i);
		return colInfo[i].signed;
	}

	public boolean isWritable(int i) throws SQLException {
		checkColRange(i);
		return false;
	}

	public void setAutoIncrement(int i, boolean flag) throws SQLException {
		checkColRange(i);
		colInfo[i].autoIncrement = flag;
	}

	public void setCaseSensitive(int i, boolean flag) throws SQLException {
		checkColRange(i);
		colInfo[i].caseSensitive = flag;
	}

	public void setCatalogName(int i, String s) throws SQLException {
		checkColRange(i);
		colInfo[i].catName = new String(s);
	}

	public void setColumnCount(int i) throws SQLException {
		if (i <= 0)
			throw new SQLException("Invalid column count");
		colCount = i;
		colInfo = new ColInfo[colCount + 1];
		for (int j = 1; j <= colCount; j++)
			colInfo[j] = new ColInfo();

	}

	public void setColumnDisplaySize(int i, int j) throws SQLException {
		checkColRange(i);
		colInfo[i].columnDisplaySize = j;
	}

	public void setColumnLabel(int i, String s) throws SQLException {
		checkColRange(i);
		colInfo[i].columnLabel = new String(s);
	}

	public void setColumnName(int i, String s) throws SQLException {
		checkColRange(i);
		colInfo[i].columnName = new String(s);
	}

	public void setColumnType(int i, int j) throws SQLException {
		checkColRange(i);
		colInfo[i].colType = j;
	}

	public void setColumnTypeName(int i, String s) throws SQLException {
		checkColRange(i);
		colInfo[i].colTypeName = new String(s);
	}

	public void setCurrency(int i, boolean flag) throws SQLException {
		checkColRange(i);
		colInfo[i].currency = flag;
	}

	public void setNullable(int i, int j) throws SQLException {
		checkColRange(i);
		colInfo[i].nullable = j;
	}

	public void setPrecision(int i, int j) throws SQLException {
		checkColRange(i);
		colInfo[i].colPrecision = j;
	}

	public void setScale(int i, int j) throws SQLException {
		checkColRange(i);
		colInfo[i].colScale = j;
	}

	public void setSchemaName(int i, String s) throws SQLException {
		checkColRange(i);
		colInfo[i].schemaName = new String(s);
	}

	public void setSearchable(int i, boolean flag) throws SQLException {
		checkColRange(i);
		colInfo[i].searchable = flag;
	}

	public void setSigned(int i, boolean flag) throws SQLException {
		checkColRange(i);
		colInfo[i].signed = flag;
	}

	public void setTableName(int i, String s) throws SQLException {
		checkColRange(i);
		if (s != null)
			colInfo[i].tableName = new String(s);
		else
			colInfo[i].tableName = new String("");
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
}
