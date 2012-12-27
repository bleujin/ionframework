package net.ion.framework.db.bean.handlers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.batik.util.gui.xmleditor.XMLDocument;

import net.ion.framework.db.Rows;
import net.ion.framework.db.bean.ResultSetHandler;
import net.ion.framework.xml.XmlDocument;

public class SimpleXMLDocumentHandler implements ResultSetHandler<XmlDocument> {
	
	private static final long serialVersionUID = -1549471698910095504L;
	private SimpleXMLStringBufferHandler inner ;
	public SimpleXMLDocumentHandler() {
		this.inner = new SimpleXMLStringBufferHandler(Rows.DEFAULT_ROOT_NAME, Rows.DEFAULT_ROW_NAME);
	}

	public SimpleXMLDocumentHandler(String rootElementName, String rowElementName) {
		this.inner = new SimpleXMLStringBufferHandler(rootElementName, rowElementName);
	}

	public XmlDocument handle(ResultSet resultSet) throws SQLException {
		StringBuffer buffer = inner.handle(resultSet);
		return new XmlDocument(buffer);
	}
}
