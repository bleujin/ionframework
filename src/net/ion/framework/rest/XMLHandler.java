package net.ion.framework.rest;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ecs.xml.XML;

public interface XMLHandler {
	public XML toXML(ResultSet rows) throws SQLException;
}
