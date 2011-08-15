package net.ion.framework.db.bean.handlers;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.ion.framework.db.Rows;
import net.ion.framework.db.bean.ResultSetHandler;
import net.ion.framework.util.Debug;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class JSONDefaultNodeHandler extends AbstractXMLHandler implements ResultSetHandler {

	private Rows props;

	public JSONDefaultNodeHandler(Rows props) {
		this.props = props;
	}

	public Object handle(ResultSet rs) throws SQLException {
		ResultSetMetaData meta = rs.getMetaData();

		List<String> types = new ArrayList<String>();
		List<String> cols = new ArrayList<String>();
		int columnCount = meta.getColumnCount();

		for (int i = 0; i < columnCount; i++) {
			int column = i + 1;
			// if(! wantToView(meta, column)) continue ;
			types.add(meta.getColumnTypeName(column));
			cols.add(meta.getColumnName(column).toLowerCase());
		}

		List<Map> rmap = (List<Map>) new MapListHandler().handleString(rs, cols.toArray(new String[0]), cols.toArray(new String[0]));

		JSONArray rows = JSONArray.fromObject(rmap);
		JSONObject body = new JSONObject();

		Debug.debug("=+=");

		body.put("type", types);
		body.put("header", cols);
		body.put("rows", rows);

		JSONObject result = new JSONObject();
		result.put((props == null) ? "property" : "node", body);

		if (props != null && (props.getRowCount() != 0)) {
			JSONObject nextObj = (JSONObject) props.toHandle(new JSONDefaultNodeHandler(null));
			result.put("next", nextObj);
		}

		return result;
	}

}