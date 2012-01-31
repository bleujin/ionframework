package net.ion.framework.rest;

import java.io.StringWriter;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.ion.framework.db.bean.ResultSetHandler;
import net.ion.framework.db.bean.handlers.MapListHandler;
import net.ion.framework.parse.gson.Gson;
import net.ion.framework.parse.gson.JsonArray;
import net.ion.framework.parse.gson.JsonObject;
import net.ion.framework.parse.gson.JsonParser;
import net.ion.framework.util.StringUtil;

import org.restlet.data.MediaType;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ResourceException;

public class JSONFormater implements ResultSetHandler, IRowsRepresentationHandler, IMapListRepresentationHandler, MyConstant {

	private static final long serialVersionUID = 6113762745155031544L;
//	private static JsonConfig JCONFIG = new JsonConfig() ;
//	static {
//		JCONFIG.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT) ;
//	}
	
	public Object handle(ResultSet rs) throws SQLException {
		JsonObject result = new JsonObject();

		return toJSON(rs, result);
	}

	public Representation toRepresentation(IRequest req, ResultSet rs, IResponse res) throws ResourceException {
		try {
			JsonObject result = new JsonObject();
			result.addProperty("request", StringUtil.toString(req.toJSON()));
			result.addProperty("response", StringUtil.toString(res.toJSON()));
			toJSON(rs, result);

			JsonObject root = new JsonObject();
			root.add("result", result);

			StringWriter rw = new StringWriter();
			new Gson().toJson(root, rw) ;

			return new StringRepresentation(rw.getBuffer(), MediaType.APPLICATION_JSON);
		} catch (SQLException e) {
			throw new ResourceException(e);
		}
	}

	public Representation toRepresentation(IRequest req, List<Map<String, ? extends Object>> datas, IResponse res) throws ResourceException  {

		JsonObject result = new JsonObject();
		result.addProperty(REQUEST, StringUtil.toString(req.toJSON()));
		result.addProperty(RESPONSE, StringUtil.toString(res.toJSON()));
		result.add(NODES, JsonParser.fromList(datas));

		JsonObject root = new JsonObject();
		root.add(RESULT, result);

		StringWriter rw = new StringWriter();
		new Gson().toJson(root, rw) ;
//		long start = System.currentTimeMillis() ;
//		RopeWriter rw = new RopeWriter();
//		root.write(rw);
//		Debug.line(System.currentTimeMillis() - start) ;
//		return new RopeRepresentation(sw.get, MediaType.APPLICATION_JSON, Language.ALL, CharacterSet.UTF_8);

//		long start = System.currentTimeMillis() ;
//		StringWriter sw = new StringWriter(); 
//		root.write(sw) ;
//		Debug.line(System.currentTimeMillis() - start) ;
		
		return new StringRepresentation(rw.getBuffer(), MediaType.APPLICATION_JSON);
		// return new RopeRepresentation(sw.get, MediaType.APPLICATION_JSON, Language.ALL, CharacterSet.UTF_8);
	}

	private JsonObject toJSON(ResultSet rs, JsonObject parent) throws SQLException {
		ResultSetMetaData meta = rs.getMetaData();

		List<String> types = new ArrayList<String>();
		List<String> cols = new ArrayList<String>();

		for (int i = 0; i < meta.getColumnCount(); i++) {
			int column = i + 1;
			types.add(meta.getColumnTypeName(column));
			cols.add(meta.getColumnName(column));
		}

		List<Map> list = (List<Map>) new MapListHandler().handle(rs);
		
		parent.add(TYPE, JsonParser.fromList(types));
		parent.add("header", JsonParser.fromList(cols));
		parent.add(NODES, JsonParser.fromList(list));

		return parent;
	}

}
