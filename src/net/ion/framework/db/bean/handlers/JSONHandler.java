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
import net.ion.framework.parse.gson.JsonObject;
import net.ion.framework.parse.gson.JsonParser;

public class JSONHandler implements ResultSetHandler {

	public Object handle(ResultSet rs) throws SQLException {

		ResultSetMetaData meta = rs.getMetaData();

		List<String> types = new ArrayList<String>();
		List<String> cols = new ArrayList<String>();
		for (int i = 0; i < meta.getColumnCount(); i++) {
			types.add(meta.getColumnTypeName(i + 1));
			cols.add(meta.getColumnName(i + 1));
		}

		List<Map> list = (List<Map>) new MapListHandler(new JSONRowProcessor()).handle(rs);
		JsonObject body = new JsonObject();

		body.add("TYPE", JsonParser.fromList(types));
		body.add("HEADER", JsonParser.fromList(cols));
		body.add("ROWS", JsonParser.fromList(list));

		JsonObject result = new JsonObject();
		result.add("RESULT", body);

		if ((rs instanceof Rows) && (((Rows) rs).getNextRows() != null)) {
			Rows nextRows = ((Rows) rs).getNextRows();
			JsonObject nextObj = (JsonObject) nextRows.toHandle(new JSONHandler());
			result.add("NEXT", nextObj);
		}

		return result;
	}

}
