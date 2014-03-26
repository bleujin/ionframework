package net.ion.framework.parse.gson;


import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;

import javax.sql.RowSetMetaData;

import net.ion.framework.util.ListUtil;

// Referenced classes of package sun.jdbc.rowset:
//            ColInfo

public class RowsMetaDataImpl implements RowSetMetaData, Serializable {

	private List<ColInfo> colInfo = ListUtil.newList();

	RowsMetaDataImpl() {
	}

	private void checkColRange(int i) throws SQLException {
		if (i <= 0 || i > colInfo.size())
			throw new SQLException("Invalid column index");
		else
			return;
	}

	public String getCatalogName(int i) throws SQLException {
		checkColRange(i);
		return columnInfo(i).catName();
	}

	public String getColumnClassName(int i) throws SQLException {
		return null;
	}

	public int getColumnCount() throws SQLException {
		return colInfo.size();
	}

	public int getColumnDisplaySize(int i) throws SQLException {
		return columnInfo(i).columnDisplaySize();
	}

	private ColInfo columnInfo(int i) throws SQLException {
		checkColRange(i);
		return colInfo.get(i-1);
	}

	public String getColumnLabel(int i) throws SQLException {
		return columnInfo(i).columnLabel();
	}

	public String getColumnName(int i) throws SQLException {
		return columnInfo(i).columnName();
	}

	public int getColumnType(int i) throws SQLException {
		return columnInfo(i).colType();
	}

	public String getColumnTypeName(int i) throws SQLException {
		return columnInfo(i).colTypeName();
	}

	public int getPrecision(int i) throws SQLException {
		return columnInfo(i).colPrecision();
	}

	public int getScale(int i) throws SQLException {
		return columnInfo(i).colScale();
	}

	public String getSchemaName(int i) throws SQLException {
		return columnInfo(i).schemaName();
	}

	public String getTableName(int i) throws SQLException {
		return columnInfo(i).tableName();
	}

	public boolean isAutoIncrement(int i) throws SQLException {
		return columnInfo(i).autoIncrement();
	}

	public boolean isCaseSensitive(int i) throws SQLException {
		return columnInfo(i).caseSensitive();
	}

	public boolean isCurrency(int i) throws SQLException {
		return columnInfo(i).currency();
	}

	public boolean isDefinitelyWritable(int i) throws SQLException {
		return true;
	}

	public int isNullable(int i) throws SQLException {
		return columnInfo(i).nullable();
	}

	public boolean isReadOnly(int i) throws SQLException {
		checkColRange(i);
		return true;
	}

	public boolean isSearchable(int i) throws SQLException {
		return columnInfo(i).searchable();
	}

	public boolean isSigned(int i) throws SQLException {
		return columnInfo(i).signed();
	}

	public boolean isWritable(int i) throws SQLException {
		checkColRange(i);
		return false;
	}

	public void setAutoIncrement(int i, boolean flag) throws SQLException {
		columnInfo(i).autoIncrement(flag);
	}

	public void setCaseSensitive(int i, boolean flag) throws SQLException {
		columnInfo(i).caseSensitive(flag);
	}

	public void setCatalogName(int i, String s) throws SQLException {
		columnInfo(i).catName(s);
	}

	public ColInfo newCol(String label){
		ColInfo result = new ColInfo(this, label);
		this.colInfo.add(result) ;
		return result ;
	}
	
	public void setColumnCount(int i) throws SQLException {
	}

	public void setColumnDisplaySize(int i, int j) throws SQLException {
		columnInfo(i).columnDisplaySize(j);
	}

	public void setColumnLabel(int i, String s) throws SQLException {
		columnInfo(i).columnLabel(s);
	}

	public void setColumnName(int i, String s) throws SQLException {
		columnInfo(i).columnName(s);
	}

	public void setColumnType(int i, int j) throws SQLException {
		columnInfo(i).colType(j);
	}

	public void setColumnTypeName(int i, String s) throws SQLException {
		columnInfo(i).colTypeName(s);
	}

	public void setCurrency(int i, boolean flag) throws SQLException {
		columnInfo(i).currency(flag);
	}

	public void setNullable(int i, int j) throws SQLException {
		columnInfo(i).nullable(j);
	}

	public void setPrecision(int i, int j) throws SQLException {
		columnInfo(i).colPrecision(j);
	}

	public void setScale(int i, int j) throws SQLException {
		columnInfo(i).colScale(j);
	}

	public void setSchemaName(int i, String s) throws SQLException {
		columnInfo(i).schemaName(s);
	}

	public void setSearchable(int i, boolean flag) throws SQLException {
		columnInfo(i).searchable(flag);
	}

	public void setSigned(int i, boolean flag) throws SQLException {
		columnInfo(i).signed(flag);
	}

	public void setTableName(int i, String s) throws SQLException {
		columnInfo(i).tableName(s);
	}


}
