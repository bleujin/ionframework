package net.ion.framework.db.bean.handlers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StringArrayHandler extends ScalarHandler {
	public StringArrayHandler() {
	}

	public StringArrayHandler(String columnName) {
		super(columnName);
	}

	public StringArrayHandler(int columnIndex) {
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
		return store.toArray(new String[0]);
	}

}
