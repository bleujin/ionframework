package net.ion.framework.db.bean.handlers;

import java.sql.ResultSet;
import java.sql.SQLException;

import net.ion.framework.db.bean.ResultSetHandler;

import org.apache.ecs.xml.XML;

public class XMLHandler implements ResultSetHandler<XML> {

	private static final long serialVersionUID = 2188220058627112088L;
	private String[] attributes;
	private String[] datas;

	public XMLHandler(String[] attributes, String[] datas) {
		this.attributes = attributes;
		this.datas = datas;
	}

	public XML handle(ResultSet rs) throws SQLException {
		XML root = new XML("root");

		while (rs.next()) {
			XML node = new XML("node");
			for (String attr : attributes) {
				String attrName = attr.toLowerCase();
				node.addAttribute(attrName, rs.getString(attrName));
			}

			for (String data : datas) {
				String dataName = data.toLowerCase();
				node.addElement(dataName, rs.getString(dataName));
			}

			root.addElement(node);
		}

		return root;
	}

}
