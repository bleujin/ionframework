package net.ion.framework.db.bean.handlers;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import net.ion.framework.db.Rows;
import net.ion.framework.db.bean.ResultSetHandler;
import net.ion.framework.util.CaseInsensitiveSet;
import net.ion.framework.util.StringUtil;

import org.apache.ecs.xml.XML;

public class XMLDefaultNodeHandler extends AbstractXMLHandler implements ResultSetHandler {

	private Rows props;
	private String rootName;
	private CaseInsensitiveSet<String> dataSections;
	private CaseInsensitiveSet<String> attrDataSessions;

	public XMLDefaultNodeHandler(Rows props, String rootName, String[] dataSections, String[] attrDataSessions) {
		this.props = props;
		this.rootName = rootName;
		this.dataSections = new CaseInsensitiveSet<String>(dataSections);
		this.attrDataSessions = new CaseInsensitiveSet<String>(attrDataSessions);
	}

	public Object handle(ResultSet rs) throws SQLException {
		XML node = new XML(rootName);

		ResultSetMetaData meta = rs.getMetaData();
		int columnCount = meta.getColumnCount();
		while (rs.next()) {
			for (int i = 0; i < columnCount; i++) {
				int column = i + 1;
				String columnName = meta.getColumnName(column).toLowerCase();
				String columnValue = StringUtil.defaultIfEmpty(rs.getString(column), "");
				if (dataSections.contains(columnName)) {
					XML element = new XML(columnName);
					element.addElement(getCDATASection(columnValue));
					node.addElement(element);
				} else {

					node.addAttribute(columnName, columnValue);
				}
			}

			if (props == null || (props.getRowCount() == 0))
				continue;
			XML property = (XML) props.toHandle(new XMLDefaultNodeHandler(null, "property", (String[]) attrDataSessions.toArray(new String[0]), null));
			node.addElement(property);
		}

		return node;
	}

}
