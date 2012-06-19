package net.ion.framework.db;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.ion.framework.util.Debug;

public class FakeResultSetMeta implements ResultSetMetaData {

	private static HashMap<Integer, String> FAKE_TYPENAME = new HashMap<Integer, String>();
	static {
		FAKE_TYPENAME.put(12, "VARCHAR") ;
		FAKE_TYPENAME.put(4, "INTEGER") ;
		FAKE_TYPENAME.put(91, "DATE") ;
	}
	
	private List<VColumn> columns ;
	public FakeResultSetMeta(List<VColumn> columns) {
		this.columns = columns ;
	}

	public String getCatalogName(int idx) throws SQLException {
		return null;
	}

	public String getColumnClassName(int idx) throws SQLException {
		return null;
	}

	public int getColumnCount() throws SQLException {
		return columns.size();
	}

	public int getColumnDisplaySize(int idx) throws SQLException {
		return 0;
	}

	public String getColumnLabel(int idx) throws SQLException {
		return columns.get(idx-1).getName();
	}

	public String getColumnName(int idx) throws SQLException {
		return columns.get(idx-1).getName();
	}

	public int getColumnType(int idx) throws SQLException {
		return columns.get(idx-1).getType();
	}

	public String getColumnTypeName(int idx) throws SQLException {
		return FAKE_TYPENAME.get(getColumnType(idx));
	}

	public int getPrecision(int arg0) throws SQLException {
		return 0;
	}

	public int getScale(int arg0) throws SQLException {
		return 0;
	}

	public String getSchemaName(int arg0) throws SQLException {
		return null;
	}

	public String getTableName(int arg0) throws SQLException {
		return null;
	}

	public boolean isAutoIncrement(int arg0) throws SQLException {
		return false;
	}

	public boolean isCaseSensitive(int arg0) throws SQLException {
		return false;
	}

	public boolean isCurrency(int arg0) throws SQLException {
		return false;
	}

	public boolean isDefinitelyWritable(int arg0) throws SQLException {
		return false;
	}

	public int isNullable(int arg0) throws SQLException {
		return 0;
	}

	public boolean isReadOnly(int arg0) throws SQLException {
		return false;
	}

	public boolean isSearchable(int arg0) throws SQLException {
		return false;
	}

	public boolean isSigned(int arg0) throws SQLException {
		return false;
	}

	public boolean isWritable(int arg0) throws SQLException {
		return false;
	}

}
