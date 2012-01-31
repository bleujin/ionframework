package net.ion.framework.db.bean.handlers;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import net.ion.framework.db.Rows;
import net.ion.framework.db.bean.ResultSetHandler;
import net.ion.framework.parse.gson.JsonObject;
import net.ion.framework.parse.gson.JsonParser;

import org.apache.commons.lang.ArrayUtils;

public class JSONNodeHandler implements ResultSetHandler {

	private Rows props;
	private String[] attrNames;
	private String[] attrColNames;
	private String[] propAttrNames;
	private String[] propColNames;

	public JSONNodeHandler(Rows props, String[] attrNames, String[] attrColNames, String[] propAttrNames, String[] propColNames) {
		this.props = props;
		this.attrNames = attrNames;
		this.attrColNames = attrColNames;
		this.propAttrNames = propAttrNames;
		this.propColNames = propColNames;
	}

	public Object handle(ResultSet rs) throws SQLException {
		ResultSetMetaData meta = rs.getMetaData();

		List<String> types = new ArrayList<String>();
		List<String> cols = new ArrayList<String>();

		for (int i = 0, last = meta.getColumnCount(); i < last; i++) {
			int column = i + 1;
			// if(! wantToView(meta, column)) continue ;
			for (int k = 0; k < attrNames.length; k++) {
				if (attrColNames[k].equalsIgnoreCase(meta.getColumnName(column))) {
					types.add(meta.getColumnTypeName(column));
					cols.add(attrNames[k]);
				}
			}
		}

		List<Object[]> list = (List<Object[]>) new ArrayListHandler().handleString(rs, attrColNames);

		JsonObject body = new JsonObject();

		body.add("type", JsonParser.fromList(types));
		body.add("header", JsonParser.fromList(cols));
		body.add("rows", JsonParser.fromList(list));

		JsonObject result = new JsonObject();
		result.add((props == null) ? "property" : "node", body);

		if (props != null && (props.getRowCount() != 0)) {
			JsonObject nextObj = (JsonObject) props.toHandle(new JSONNodeHandler(null, propAttrNames, propColNames, null, null));
			result.add("next", nextObj);
		}

		return result;
	}

	private boolean wantToView(ResultSetMetaData meta, int column) throws SQLException {
		return ArrayUtils.contains(attrColNames, meta.getColumnName(column).toLowerCase());
	}
}
