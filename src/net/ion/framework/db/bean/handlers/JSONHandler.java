package net.ion.framework.db.bean.handlers;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.ion.framework.db.Rows;
import net.ion.framework.db.bean.JSONRowProcessor;
import net.ion.framework.db.bean.ResultSetHandler;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class JSONHandler implements ResultSetHandler {

	public Object handle(ResultSet rs) throws SQLException {

		ResultSetMetaData meta = rs.getMetaData();

		List<String> types = new ArrayList<String>();
		List<String> cols = new ArrayList<String>();
		for (int i = 0; i < meta.getColumnCount(); i++) {
			types.add(meta.getColumnTypeName(i + 1));
			cols.add(meta.getColumnName(i + 1));
		}

		List<Map> rmap = (List<Map>) new MapListHandler(new JSONRowProcessor()).handle(rs);
		JSONArray rows = JSONArray.fromObject(rmap);
		JSONObject body = new JSONObject();

		body.put("TYPE", types);
		body.put("HEADER", cols);
		body.put("ROWS", rows);

		JSONObject result = new JSONObject();
		result.put("RESULT", body);

		if ((rs instanceof Rows) && (((Rows) rs).getNextRows() != null)) {
			Rows nextRows = ((Rows) rs).getNextRows();
			JSONObject nextObj = (JSONObject) nextRows.toHandle(new JSONHandler());
			result.put("NEXT", nextObj);
		}

		return result;
	}

}
