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

public class OBJECTFormater implements ResultSetHandler<List<Map<String, ? extends Object>>>, IMapListRepresentationHandler, IRowsRepresentationHandler{

	private static final long serialVersionUID = -2097100061558318458L;

	public Representation toRepresentation(IRequest req, List<Map<String, ? extends Object>> datas, IResponse res) throws ResourceException  {
		StdObject sto = StdObject.create(req, datas, res) ;
		return new ObjectRepresentation<StdObject>(sto, MediaType.APPLICATION_JAVA_OBJECT);
	}

	public Representation toRepresentation(IRequest req, ResultSet rows, IResponse res) throws ResourceException {
		List<Map<String, ? extends Object>> datas = new MapListHandler().handle(rows);
		return toRepresentation(req, datas, res) ;
	}

	public List<Map<String, ? extends Object>> handle(ResultSet rs) throws SQLException {
		return new MapListHandler().handle(rs);
	}

}
