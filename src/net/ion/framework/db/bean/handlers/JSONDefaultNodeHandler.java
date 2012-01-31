package net.ion.framework.db.bean.handlers;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.ion.framework.db.Rows;
import net.ion.framework.db.bean.ResultSetHandler;
import net.ion.framework.parse.gson.JsonObject;
import net.ion.framework.parse.gson.JsonParser;
import net.ion.framework.util.Debug;

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

		List<Map> list = (List<Map>) new MapListHandler().handleString(rs, cols.toArray(new String[0]), cols.toArray(new String[0]));

		JsonObject body = new JsonObject();

		Debug.debug("=+=");

		body.add("type", JsonParser.fromList(types));
		body.add("header", JsonParser.fromList(cols));
		body.add("rows", JsonParser.fromList(list));

		JsonObject result = new JsonObject();
		result.add((props == null) ? "property" : "node", body);

		if (props != null && (props.getRowCount() != 0)) {
			JsonObject nextObj = (JsonObject) props.toHandle(new JSONDefaultNodeHandler(null));
			result.add("next", nextObj);
		}

		return result;
	}

}