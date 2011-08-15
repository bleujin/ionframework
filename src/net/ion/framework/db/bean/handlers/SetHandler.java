package net.ion.framework.db.bean.handlers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class SetHandler extends ScalarHandler {
	public SetHandler() {
	}

	public SetHandler(String columnName) {
		super(columnName);
	}

	public SetHandler(int columnIndex) {
		super(columnIndex);
	}

	public Object handle(ResultSet rs) throws SQLException {

		Set<String> store = new HashSet<String>();
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
