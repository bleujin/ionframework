package net.ion.framework.db.bean.handlers;

import java.sql.ResultSet;
import java.sql.SQLException;

import net.ion.framework.db.bean.ResultSetHandler;

import org.apache.ecs.xml.XML;

public class XMLNodeListHandler extends AbstractListHandler implements ResultSetHandler {

	private String valueColName;

	public XMLNodeListHandler(String[] attrNames, String[] attrColNames, String valueColName) {
		super(attrNames, attrColNames);
		this.valueColName = valueColName;
	}

	public Object handle(ResultSet rs) throws SQLException {
		XML nodes = new XML("nodes");

		while (rs.next()) {
			XML node = new XML("node");
			// "catId", "catNm", "upperCatId", "charsetCd"
			for (int i = 0, last = getAttribueSize(); i < last; i++) {
				node.addAttribute(getAttributeName(i), rs.getString(getColumnName(i)));
			}
			// "<![CDATA[" + rs.getString(valueColName)
			node.addElement(getCDATASection(rs.getString(valueColName)));
			nodes.addElement(node);
		}

		return nodes;
	}
}
