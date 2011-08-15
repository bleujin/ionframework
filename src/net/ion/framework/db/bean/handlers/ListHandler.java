package net.ion.framework.db.bean.handlers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ListHandler extends ScalarHandler {
	public ListHandler() {
	}

	public ListHandler(String columnName) {
		super(columnName);
	}

	public ListHandler(int columnIndex) {
		super(columnIndex);
	}

	public Object handle(ResultSet rs) throws SQLException {

		List<String> store = new ArrayList<String>();
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
