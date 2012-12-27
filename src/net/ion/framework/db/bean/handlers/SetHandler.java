package net.ion.framework.db.bean.handlers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import net.ion.framework.db.bean.ResultSetHandler;
import net.ion.framework.util.SetUtil;

public class SetHandler implements ResultSetHandler<Set<String>> {

	private static final long serialVersionUID = -8425306067396806533L;
	private int columnIndex = 1;
	private String columnName = null;

	public SetHandler() {
	}

	public SetHandler(String columnName) {
		this.columnName = columnName;
	}

	public SetHandler(int columnIndex) {
		this.columnIndex = columnIndex;
	}
	
	protected String getColumnName() {
		return columnName;
	}

	protected int getColumnIndex() {
		return columnIndex;
	}

	public Set<String> handle(ResultSet rs) throws SQLException {

		Set<String> store = SetUtil.newSet() ;
		while (rs.next()) {
			if (getColumnName() == null) {
				store.add(rs.getString(getColumnIndex()));
			} else {
				store.add(rs.getString(getColumnName()));
			}
		}
		return store;
	}

}
