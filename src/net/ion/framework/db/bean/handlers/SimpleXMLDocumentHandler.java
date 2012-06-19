package net.ion.framework.db.bean.handlers;

import java.sql.ResultSet;
import java.sql.SQLException;

import net.ion.framework.db.Rows;
import net.ion.framework.xml.XmlDocument;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: I-ON Communications
 * </p>
 * 
 * @author not attributable
 * @version 1.0
 */

public class SimpleXMLDocumentHandler extends SimpleXMLStringBufferHandler {
	public SimpleXMLDocumentHandler() {
		super(Rows.DEFAULT_ROOT_NAME, Rows.DEFAULT_ROW_NAME);
	}

	public SimpleXMLDocumentHandler(String rootElementName, String rowElementName) {
		super(rootElementName, rowElementName);
	}

	public Object handle(ResultSet resultSet) throws SQLException {
		StringBuffer buffer = (StringBuffer) super.handle(resultSet);
		return new XmlDocument(buffer);
	}
}
