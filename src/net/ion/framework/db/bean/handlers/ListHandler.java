package net.ion.framework.db.bean.handlers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import net.ion.framework.db.bean.ResultSetHandler;
import net.ion.framework.util.ListUtil;

public class ListHandler extends ColNameHelper implements ResultSetHandler<List<String>> {
	
	private static final long serialVersionUID = 9103095185581415511L;

	public ListHandler() {
	}

	public ListHandler(String columnName) {
		super(columnName);
	}

	public ListHandler(int columnIndex) {
		super(columnIndex);
	}

	public List<String> handle(ResultSet rs) throws SQLException {
		List<String> store = ListUtil.newList() ;
		while (rs.next()) {
			if (columnName() == null) {
				store.add(rs.getString(columnIndex()));
			} else {
				store.add(rs.getString(columnName()));
			}
		}
		return store;
	}

}
