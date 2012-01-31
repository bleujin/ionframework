package net.ion.framework.rest;

import java.sql.ResultSet;

import org.restlet.representation.Representation;
import org.restlet.resource.ResourceException;

public interface IRowsRepresentationHandler {

	public Representation toRepresentation(IRequest req, ResultSet rows, IResponse res) throws ResourceException ;
}
