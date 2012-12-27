package net.ion.framework.db.bean.handlers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import net.ion.framework.db.bean.ResultSetHandler;

public class StringArrayHandler extends ColNameHelper implements ResultSetHandler<String[]> {
	private static final long serialVersionUID = -3324971735090528896L;

	public StringArrayHandler() {
		super() ;
	}

	public StringArrayHandler(String columnName) {
		super(columnName);
	}

	public StringArrayHandler(int columnIndex) {
		super(columnIndex);
	}

	public String[] handle(ResultSet rs) throws SQLException {

		List<String> store = new ArrayList<String>();
		while (rs.next()) {
			if (columnName() == null) {
				store.add(rs.getString(columnIndex()));
			} else {
				store.add(rs.getString(columnName()));
			}
		}
		return store.toArray(new String[0]);
	}

}
