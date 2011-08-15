package net.ion.framework.db.bean.handlers;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import net.ion.framework.db.Rows;
import net.ion.framework.db.bean.ResultSetHandler;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

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

		List<Object[]> rmap = (List<Object[]>) new ArrayListHandler().handleString(rs, attrColNames);

		JSONArray rows = JSONArray.fromObject(rmap);
		JSONObject body = new JSONObject();

		body.put("type", types);
		body.put("header", cols);
		body.put("rows", rows);

		JSONObject result = new JSONObject();
		result.put((props == null) ? "property" : "node", body);

		if (props != null && (props.getRowCount() != 0)) {
			JSONObject nextObj = (JSONObject) props.toHandle(new JSONNodeHandler(null, propAttrNames, propColNames, null, null));
			result.put("next", nextObj);
		}

		return result;
	}

	private boolean wantToView(ResultSetMetaData meta, int column) throws SQLException {
		return ArrayUtils.contains(attrColNames, meta.getColumnName(column).toLowerCase());
	}
}
