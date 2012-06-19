package net.ion.framework.xml;

import java.io.IOException;
import java.io.Writer;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Stack;

import javax.sql.RowSet;

import net.ion.framework.db.IXmlWriter;
import net.ion.framework.db.Rows;
import net.ion.framework.db.rowset.WebRowSet;

/**
 * sun.jdbc.rowset.XmlWriter를 확장하여 XML Schema를 Write한다.
 * 
 * @author not attributable
 * @version 1.0
 */
public final class SimpleXSDWriter implements IXmlWriter {
	protected Writer writer;
	protected Stack<String> stack;
	final static int CUT_SIZE = 4000;

	String[] columnLabel;

	public SimpleXSDWriter() {
	}

	private void beginSchema() throws IOException {
		writer
				.write("<xs:schema targetNamespace=\"http://www.i-on.co.kr/ics4/rows.xsd\" xmlns:xs=\"http://www.w3.org/2001/XMLSchema\" xmlns=\"http://www.i-on.co.kr/ics4/rows.xsd\" elementFormDefault=\"qualified\">\n\n");
	}

	private void endSchema() throws IOException {
		writer.write("\n\n</xs:schema>");
		writer.flush();
	}

	private void beginElement(String name) throws IOException {
		writer.write("<xs:element name=\"" + name + "\">\n");
	}

	// private void beginElement(String name, int indent) throws IOException{
	// writeIndent(indent);
	// writer.write("<xs:element name=\"" + name+ "\">\n");
	// }
	private void beginCollectionElement(String name, int indent) throws IOException {
		writeIndent(indent);
		writer.write("<xs:element name=\"" + name + "\" minOccurs=\"0\" maxOccurs=\"unbounded\">\n");
	}

	private void endElement(int indent) throws IOException {
		writeIndent(indent);
		writer.write("</xs:element>\n");
	}

	private void endElement() throws IOException {
		writer.write("</xs:element>\n");
	}

	private void writeSimpleElement(String name, String type) throws IOException {
		writer.write("<xs:element name=\"" + name + "\" type=\"xs:" + type + "\" />\n");
	}

	// private void writeElementRef(String refName, int indent) throws IOException{
	// writeIndent(indent);
	// writer.write("<xs:element ref=\"" + refName + "\" />\n");
	// }

	private void beginSequence(int indent) throws IOException {
		writeIndent(indent);
		writer.write("<xs:sequence>\n");
	}

	private void endSequence(int indent) throws IOException {
		writeIndent(indent);
		writer.write("</xs:sequence>\n");
	}

	private void beginComplexType(int indent) throws IOException {
		writeIndent(indent);
		writer.write("<xs:complexType>\n");
	}

	private void endComplexType(int indent) throws IOException {
		writeIndent(indent);
		writer.write("</xs:complexType>\n");
	}

	// private void writeAttribute(String name, String type, int indent) throws IOException{
	// writeIndent(indent);
	// writer.write("<xs:attribute name=\"" + name + "\" type=\"" + type + "\" />\n");
	// }

	protected String getTag() {
		return (String) stack.pop();
	}

	// protected void setTag(String s)
	// {
	// stack.push(s);
	// }

	protected void writeBoolean(boolean flag) throws IOException {
		writer.write((new Boolean(flag)).toString());
	}

	protected void writeDouble(double d) throws IOException {
		writer.write(Double.toString(d));
	}

	protected void writeFloat(float f) throws IOException {
		writer.write(Float.toString(f));
	}

	protected void writeHeader() throws IOException {
		writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		writer.write("<!-- Simple Schema -->\n\n");
	}

	protected void writeIndent(int indent) throws IOException {
		for (int j = 0; j < indent; j++)
			writer.write("\t");
	}

	protected void writeRowsElement(RowSet webrowset, String rowSetElementName, String rowElementName) throws IOException, SQLException {

		// <xs:element name="Rowset">
		// <xs:complexType>
		// <xs:sequence>

		// </xs:sequence>
		// </xs:complexType>
		// </xs:element>

		beginElement(rowSetElementName);
		beginComplexType(0);
		beginSequence(1);

		writeDetailRowsElement(webrowset, rowElementName);

		endSequence(1);
		endComplexType(0);

		endElement();
	}

	private void writeDetailRowsElement(RowSet webrowset, String rowElementName) throws IOException, SQLException {
		// <xs:element name="row">
		// <xs:complexType>
		// <xs:sequence>
		// <xs:element name="TAGID" type="xs:string" />
		// <xs:element name="TAGNM" type="xs:string" />
		// <xs:element name="TAGKINDCD" type="xs:string" />
		// <xs:element name="TAGGRPCD" type="xs:string" />
		// <xs:element name="CREDATE" type="xs:long" />
		// </xs:sequence>
		// </xs:complexType>
		// </xs:element>

		beginCollectionElement(rowElementName, 1);
		beginComplexType(2);
		beginSequence(2);

		ResultSetMetaData resultsetmetadata = webrowset.getMetaData();

		for (int i = 1, last = resultsetmetadata.getColumnCount(); i <= last; i++) {
			String columnLabel = resultsetmetadata.getColumnLabel(i).toUpperCase();
			String columnTypeName = getTypeName(resultsetmetadata.getColumnType(i));
			writeIndent(3);
			writeSimpleElement(columnLabel, columnTypeName);
		}

		endSequence(2);
		endComplexType(2);
		endElement(1);
	}

	protected void writeRowSet(RowSet webrowset, String rowSetElementName, String rowElementName) throws SQLException {
		try {
			writeHeader();
			beginSchema();
			initMeta(webrowset);
			writeRowsElement(webrowset, rowSetElementName, rowElementName);
			endSchema();
		} catch (IOException ex) {
			throw new SQLException("IOException: " + ex.getMessage());
		}
	}

	protected void writeRowSet(RowSet webrowset) throws SQLException {
		writeRowSet(webrowset, "RowSet", "row");
	}

	private void initMeta(RowSet webrowset) throws SQLException {
		ResultSetMetaData resultsetmetadata = webrowset.getMetaData();
		int i = resultsetmetadata.getColumnCount();
		columnLabel = new String[i + 1];

		for (int j = 1; j <= i; j++) {
			columnLabel[j] = resultsetmetadata.getColumnLabel(j);
		}
	}

	protected void writeShort(short word0) throws IOException {
		writer.write(Short.toString(word0));
	}

	private String getTypeName(int columnType) throws IOException {
		switch (columnType) {
		case Types.BIT:
			return "byte";
		case Types.LONGVARBINARY:
		case Types.VARBINARY:
		case Types.BINARY:
			return "binary";

		case Types.SMALLINT:
			return "short";
		case Types.INTEGER:
			return "int";
		case Types.BIGINT:
			return "long";
		case Types.FLOAT:
			return "float";

		case Types.REAL:
		case Types.DOUBLE:
		case Types.NUMERIC:
		case Types.DECIMAL:
			return "double";

		case Types.DATE:
			return "date";
		case Types.TIME:
			return "time";
		case Types.TIMESTAMP:
			return "date";

		case Types.LONGVARCHAR:
			return "string";

		case Types.CHAR:
		case Types.VARCHAR:
			return "string";

		default:
			throw new IOException("Unknown ColumnType : Failed to writeType");
		}
	}

	// private String removeTag(String s)
	// {
	// return HttpUtils.filterHTML(s);
	// }

	public void writeXML(Rows webrowset, Writer writer) throws SQLException {
		stack = new Stack<String>();
		this.writer = writer;
		writeRowSet(webrowset);
	}

	public void writeXMLwithTagName(Rows webrowset, Writer writer, String rowSetElementName, String rowElementName) throws SQLException {
		stack = new Stack<String>();
		this.writer = writer;
		writeRowSet(webrowset, rowSetElementName, rowElementName);
	}

	public void writeXML(WebRowSet webrowset, Writer writer) throws SQLException {
		this.stack = new Stack<String>();
		this.writer = writer;
		writeRowSet(webrowset);
	}

}
