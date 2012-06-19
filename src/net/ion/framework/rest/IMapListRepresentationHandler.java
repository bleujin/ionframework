package net.ion.framework.rest;

import java.util.List;
import java.util.Map;

import org.restlet.representation.Representation;
import org.restlet.resource.ResourceException;

public interface IMapListRepresentationHandler {

	public Representation toRepresentation(IRequest req, List<Map<String, ? extends Object>> datas, IResponse res) throws ResourceException ;

}
