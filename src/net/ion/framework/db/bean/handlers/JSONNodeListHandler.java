package net.ion.framework.db.bean.handlers;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.ion.framework.db.bean.ResultSetHandler;
import net.ion.framework.parse.gson.JsonObject;
import net.ion.framework.parse.gson.JsonParser;
import net.ion.framework.util.ArrayUtil;

public class JSONNodeListHandler extends AbstractListHandler implements ResultSetHandler<JsonObject> {

	private static final long serialVersionUID = -4499992880931321488L;

	public JSONNodeListHandler(String[] attrNames, String[] attrColNames) {
		super(attrNames, attrColNames);
	}

	public JsonObject handle(ResultSet rs) throws SQLException {
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

		List<Map<String, String>> list = new StringMapListHandler().handle(rs) ;
		for (Map<String, String> map : list) {
			for (Entry<String, String> entry : map.entrySet()) {
				if (! ArrayUtil.contains(getAttributeNames(), entry.getKey())){
					map.remove(entry.getKey()) ;
				}
			}
		}

		JsonObject body = new JsonObject();

		body.add("type", JsonParser.fromList(types));
		body.add("header", JsonParser.fromList(cols));
		body.add("rows", JsonParser.fromList(list));

		JsonObject result = new JsonObject();
		result.add("nodes", body);

		return result;
	}
}
