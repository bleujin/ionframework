package net.ion.framework.db.bean.handlers;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import net.ion.framework.db.RepositoryException;
import net.ion.framework.db.Row;
import net.ion.framework.db.RowsUtils;
import net.ion.framework.db.bean.ResultSetHandler;

public class FirstRowHandler implements ResultSetHandler<Row>{

	private static final long serialVersionUID = -6786579511226655817L;
	public final static FirstRowHandler SELF = new FirstRowHandler() ;
	
	public Row handle(ResultSet rs) {
		try {
			if (!rs.first()) {
				throw RepositoryException.throwIt("No Data Found\n");
			}
			
			return Row.create(RowsUtils.currentRowToMap(rs), getColumnsNames(rs));
		} catch (SQLException ex) {
			throw RepositoryException.throwIt(ex);
		}
	}
	

	private static String[] getColumnsNames(ResultSet rs) throws SQLException {
		ResultSetMetaData meta = rs.getMetaData();
		String[] names = new String[meta.getColumnCount()];

		for (int i = 0; i < names.length; ++i) {
			names[i] = meta.getColumnName(i + 1).toUpperCase();
		}

		return names;
	}

}
