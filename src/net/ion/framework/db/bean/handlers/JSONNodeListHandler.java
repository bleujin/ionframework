package net.ion.framework.db.bean.handlers;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.ion.framework.db.bean.ResultSetHandler;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class JSONNodeListHandler extends AbstractListHandler implements ResultSetHandler {

	public JSONNodeListHandler(String[] attrNames, String[] attrColNames) {
		super(attrNames, attrColNames);
	}

	public Object handle(ResultSet rs) throws SQLException {
		ResultSetMetaData meta = rs.getMetaData();

		List<String> types = new ArrayList<String>();
		List<String> cols = new ArrayList<String>();

		for (int i = 0; i < meta.getColumnCount(); i++) {
			int column = i + 1;
			// if(! wantToView(meta, column)) continue ;
			for (int k = 0; k < getAttributeNames().length; k++) {
				if (getColumnName(k).equalsIgnoreCase(meta.getColumnName(column))) {
					types.add(meta.getColumnTypeName(column));
					cols.add(getAttributeName(k));
				}
			}
		}

		List<Map> rmap = (List<Map>) new MapListHandler().handleString(rs, getAttributeNames(), getColumnNames());

		JSONArray rows = JSONArray.fromObject(rmap);
		JSONObject body = new JSONObject();

		body.put("type", types);
		body.put("header", cols);
		body.put("rows", rows);

		JSONObject result = new JSONObject();
		result.put("nodes", body);

		return result;
	}
}
