package net.ion.framework.db.bean.handlers;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.ion.framework.db.bean.ResultSetHandler;
import net.ion.framework.parse.gson.JsonObject;
import net.ion.framework.parse.gson.JsonParser;

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

		List<Map> list = (List<Map>) new MapListHandler().handleString(rs, getAttributeNames(), getColumnNames());

		JsonObject body = new JsonObject();

		body.add("type", JsonParser.fromList(types));
		body.add("header", JsonParser.fromList(cols));
		body.add("rows", JsonParser.fromList(list));

		JsonObject result = new JsonObject();
		result.add("nodes", body);

		return result;
	}
}
