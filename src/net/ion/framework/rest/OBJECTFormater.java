package net.ion.framework.rest;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import net.ion.framework.db.bean.ResultSetHandler;
import net.ion.framework.db.bean.handlers.MapListHandler;

import org.restlet.data.MediaType;
import org.restlet.representation.ObjectRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.ResourceException;

public class OBJECTFormater implements ResultSetHandler, IMapListRepresentationHandler, IRowsRepresentationHandler{

	public Representation toRepresentation(IRequest req, List<Map<String, ? extends Object>> datas, IResponse res) throws ResourceException  {
		StdObject sto = StdObject.create(req, datas, res) ;
		return new ObjectRepresentation<StdObject>(sto, MediaType.APPLICATION_JAVA_OBJECT);
	}

	public Representation toRepresentation(IRequest req, ResultSet rows, IResponse res) throws ResourceException {
		try {
			List<Map<String, ?>> datas = (List<Map<String, ?>>) new MapListHandler().handle(rows) ;
			return toRepresentation(req, datas, res) ;
		} catch (SQLException e) {
			throw new ResourceException(e) ;
		}
	}

	public Object handle(ResultSet rs) throws SQLException {
		return new MapListHandler().handle(rs);
	}

}
