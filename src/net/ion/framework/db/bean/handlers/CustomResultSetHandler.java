package net.ion.framework.db.bean.handlers;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;

import net.ion.framework.db.RowsUtils;
import net.ion.framework.db.bean.ResultSetHandler;
import net.ion.framework.util.StringUtil;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: I-ON Communications
 * </p>
 * 
 * @author bleujin
 * @version 1.0
 */

public abstract class CustomResultSetHandler implements ResultSetHandler {

	protected String getDefaultString(ResultSet rs, String columnName, int type) throws SQLException {
		return StringUtil.escapeControlChar(StringUtil.defaultString(getString(rs, columnName, type), ""));
	}

	protected String getString(ResultSet rs, String columnName, int type) throws SQLException {
		if (type == Types.CLOB) {
			return RowsUtils.clobToString(rs.getClob(columnName));
		} else {
			return rs.getString(columnName);
		}
	}

	protected String getDefaultString(ResultSet rs, int[] types, int colIndex, String columnName) throws SQLException {
		return StringUtil.defaultString(StringUtil.escapeControlChar(getString(rs, types, colIndex, columnName)), "");
	}

	protected String getString(ResultSet rs, int[] types, int colIndex, String columnName) throws SQLException {
		if (types[colIndex] == Types.CLOB) {
			return RowsUtils.clobToString(rs.getClob(columnName));
		} else {
			return rs.getString(columnName);
		}
	}

	protected int getColumnType(ResultSet rs, String column) throws SQLException {
		ResultSetMetaData meta = rs.getMetaData();
		int type = Types.OTHER; // unknown

		for (int k = 1, klast = meta.getColumnCount(); k <= klast; k++) {
			if (column.equalsIgnoreCase(meta.getColumnName(k))) {
				type = meta.getColumnType(k);
				break;
			}
		}
		return type;
	}

	protected int[] getColumnType(ResultSet rs, String[] columns) throws SQLException {
		ResultSetMetaData meta = rs.getMetaData();
		int types[] = new int[columns.length];

		for (int i = 0, last = columns.length; i < last; i++) {
			for (int k = 1, klast = meta.getColumnCount(); k <= klast; k++) {
				if (columns[i].equalsIgnoreCase(meta.getColumnName(k))) {
					types[i] = meta.getColumnType(k);
					break;
				}
			}
		}

		return types;
	}

}
