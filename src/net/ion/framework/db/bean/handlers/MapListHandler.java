package net.ion.framework.db.bean.handlers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.ion.framework.db.bean.BasicRowProcessor;
import net.ion.framework.db.bean.ResultSetHandler;
import net.ion.framework.db.bean.RowProcessor;

public class MapListHandler extends AbListHandler<Map<String, ? extends Object>>{

	private static final long serialVersionUID = 6758822446587422316L;
	private RowProcessor convert ;
	public MapListHandler() {
		this(ArrayHandler.ROW_PROCESSOR);
	}

	public MapListHandler(RowProcessor convert) {
		super();
		this.convert = convert;
	}

	protected Map<String,Object> handleRow(ResultSet rs) throws SQLException {
		return this.convert.toMap(rs);
	}

	public List<Map<String, Object>> handleString(ResultSet rs, String[] attributeNames, String[] columnNames) throws SQLException {
		List<Map<String, Object>> results = new ArrayList<Map<String, Object>>();

		while (rs.next()) {
			results.add(this.convert.toStringMap(rs, attributeNames, columnNames));
		}

		return results;
	}

}
