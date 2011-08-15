package net.ion.framework.rest;

import static net.ion.framework.rest.MyConstant.*;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.ion.framework.db.bean.ResultSetHandler;
import net.ion.framework.db.bean.handlers.MapListHandler;
import net.ion.framework.rope.RopeWriter;
import net.ion.framework.util.StringUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.json.JSONException;
import org.restlet.data.CharacterSet;
import org.restlet.data.Language;
import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.resource.ResourceException;
public class JSONFormater implements ResultSetHandler, IRowsRepresentationHandler, IMapListRepresentationHandler {

	public Object handle(ResultSet rs) throws SQLException {
		JSONObject result = new JSONObject();

		return toJSON(rs, result);
	}

	public Representation toRepresentation(IRequest req, ResultSet rs, IResponse res) throws ResourceException, JSONException {
		try {
			JSONObject result = new JSONObject();
			result.put("request", StringUtil.toString(req.toJSON()));
			result.put("response", StringUtil.toString(res.toJSON()));
			toJSON(rs, result);

			JSONObject root = new JSONObject();
			root.put("result", result);

			RopeWriter rw = new RopeWriter();
			root.write(rw);

			return new RopeRepresentation(rw.getRope(), MediaType.APPLICATION_JSON, Language.ALL, CharacterSet.UTF_8);
		} catch (SQLException e) {
			throw new ResourceException(e);
		}
	}

	public Representation toRepresentation(IRequest req, List<Map<String, ? extends Object>> datas, IResponse res) throws ResourceException, JSONException {

		JSONObject result = new JSONObject();
		result.put(REQUEST, StringUtil.toString(req.toJSON()));
		result.put(RESPONSE, StringUtil.toString(res.toJSON()));
		result.put(NODES, JSONArray.fromObject(datas));

		JSONObject root = new JSONObject();
		root.put(RESULT, result);

		RopeWriter rw = new RopeWriter();
		root.write(rw);

		return new RopeRepresentation(rw.getRope(), MediaType.APPLICATION_JSON, Language.ALL, CharacterSet.UTF_8);
	}

	private JSONObject toJSON(ResultSet rs, JSONObject parent) throws SQLException {
		ResultSetMetaData meta = rs.getMetaData();

		List<String> types = new ArrayList<String>();
		List<String> cols = new ArrayList<String>();

		for (int i = 0; i < meta.getColumnCount(); i++) {
			int column = i + 1;
			types.add(meta.getColumnTypeName(column));
			cols.add(meta.getColumnName(column));
		}

		List<Map> rmap = (List<Map>) new MapListHandler().handle(rs);

		JSONArray rows = JSONArray.fromObject(rmap);
		parent.put(TYPE, types);
		parent.put("header", cols);
		parent.put(NODES, rows);

		return parent;
	}

}
