package net.ion.framework.db.bean.handlers;

import java.sql.ResultSet;
import java.sql.SQLException;

import net.ion.framework.db.Rows;
import net.ion.framework.db.bean.ResultSetHandler;

import org.apache.ecs.xml.XML;

public class XMLNodeHandler extends AbstractXMLHandler implements ResultSetHandler<XML> {

	private static final long serialVersionUID = 9018503631840316691L;
	private Rows props;
	private String[] attrNames;
	private String[] attrColNames;
	private String[] propAttrNames;
	private String[] propColNames;
	private String valueColName;

	public XMLNodeHandler(Rows props, String[] attrNames, String[] attrColNames, String[] propAttrNames, String[] propColNames, String valueColName) {
		this.props = props;
		this.attrNames = attrNames;
		this.attrColNames = attrColNames;
		this.propAttrNames = propAttrNames;
		this.propColNames = propColNames;
		this.valueColName = valueColName;
	}

	public XML handle(ResultSet rs) throws SQLException {
		XML node = new XML("node");

		while (rs.next()) {
			// "catId", "catNm", "upperCatId", "charsetCd"
			for (int i = 0, last = attrNames.length; i < last; i++) {
				node.addAttribute(attrNames[i], rs.getString(attrColNames[i]));
			}

			if (props == null)
				continue;
			while (props.next()) {
				XML property = new XML("property");
				for (int i = 0, last = propAttrNames.length; i < last; i++) {
					property.addAttribute(propAttrNames[i], props.getString(propColNames[i]));
				}
				property.addElement(getCDATASection(props.getString(valueColName)));
				node.addElement(property);
			}
		}

		return node;
	}

}
