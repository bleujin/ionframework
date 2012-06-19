package net.ion.framework.db.mongo.jdbc;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;

public class MongoResultSetMetaData implements ResultSetMetaData{

	private ClientResult result ;
	public MongoResultSetMetaData(ClientResult result) {
		this.result = result ;
	}

	public String getCatalogName(int i) throws SQLException {
		return result.getColName(i) ;
	}

	public String getColumnClassName(int i) throws SQLException {
		return getCatalogName(i);
	}

	public int getColumnCount() throws SQLException {
		return result.getColSize();
	}

	public int getColumnDisplaySize(int i) throws SQLException {
		return 80;
	}

	public String getColumnLabel(int i) throws SQLException {
		return getCatalogName(i);
	}

	public String getColumnName(int i) throws SQLException {
		return getCatalogName(i);
	}

	public int getColumnType(int i) throws SQLException {
		return Types.OTHER;
	}

	public String getColumnTypeName(int i) throws SQLException {
		return "other";
	}

	public int getPrecision(int i) throws SQLException {
		return 0;
	}

	public int getScale(int i) throws SQLException {
		return 0;
	}

	public String getSchemaName(int i) throws SQLException {
		return null;
	}

	public String getTableName(int i) throws SQLException {
		return null;
	}

	public boolean isAutoIncrement(int i) throws SQLException {
		return false;
	}

	public boolean isCaseSensitive(int i) throws SQLException {
		return true;
	}

	public boolean isCurrency(int i) throws SQLException {
		return false;
	}

	public boolean isDefinitelyWritable(int i) throws SQLException {
		return false;
	}

	public int isNullable(int i) throws SQLException {
		return ResultSetMetaData.columnNullableUnknown;
	}

	public boolean isReadOnly(int i) throws SQLException {
		return true;
	}

	public boolean isSearchable(int i) throws SQLException {
		return false;
	}

	public boolean isSigned(int i) throws SQLException {
		return false;
	}

	public boolean isWritable(int i) throws SQLException {
		return false;
	}


}
