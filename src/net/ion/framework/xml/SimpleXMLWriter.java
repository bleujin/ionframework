package net.ion.framework.xml;

import java.io.IOException;
import java.io.Writer;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Stack;

import javax.sql.RowSet;

import net.ion.framework.db.IONXmlWriter;
import net.ion.framework.db.IXmlWriter;
import net.ion.framework.db.Rows;
import net.ion.framework.db.rowset.WebRowSet;

/**
 * sun.jdbc.rowset.XmlWriter∏¶ »Æ¿Â.
 * 
 * @author not attributable
 * @version 1.0
 */
public class SimpleXMLWriter extends IONXmlWriter implements IXmlWriter {
	protected boolean replace = false;

	public SimpleXMLWriter() {
	}

	protected void beginSection(String s, int index) throws IOException {
		setTag(s);
		writeIndent(stack.size());
		writer.write("<" + s + " index=\"" + index + "\">\n");
	}

	protected void writeClob(String data) throws IOException {
		if (data != null) {
			if (replace) {
				data = data.replaceAll("--]]>", "--]] >");
				data = data.replace((char) 0xb, '\n');
			}
			startCDATATag();
			writer.write(data);
			endCDATATag();
		} else
			writeNull();
	}

	protected void writeData(RowSet webrowset, String rowElementName) throws IOException {
		try {
			ResultSetMetaData resultsetmetadata = webrowset.getMetaData();
			int i = resultsetmetadata.getColumnCount();
			webrowset.beforeFirst();
			for (int k = 1; webrowset.next(); endSection(), k++) {
				if (webrowset.rowDeleted() && webrowset.rowInserted())
					beginSection("insdel", k);
				else if (webrowset.rowDeleted())
					beginSection("del", k);
				else if (webrowset.rowInserted())
					beginSection("ins", k);
				else
					beginSection(rowElementName, k);
				for (int j = 1; j <= i; j++) {
					beginTag(columnLabel[j].toUpperCase());
					writeValue(j, webrowset);
					endTag(columnLabel[j].toUpperCase());
				}
			}

		} catch (SQLException sqlexception) {
			throw new IOException("SQLException: " + sqlexception.getMessage());
		}
	}

	protected void writeRowSet(RowSet webrowset, String rowSetElementName, String rowElementName) throws SQLException {
		try {
			writeHeader();
			beginSection(rowSetElementName);
			initMeta(webrowset);
			writeData(webrowset, rowElementName);
			endSection(rowSetElementName);
		} catch (IOException ioexception) {
			throw new SQLException("IOException: " + ioexception.getMessage());
		}
	}

	private void initMeta(RowSet webrowset) throws SQLException {
		ResultSetMetaData resultsetmetadata = webrowset.getMetaData();
		int i = resultsetmetadata.getColumnCount();
		columnLabel = new String[i + 1];

		for (int j = 1; j <= i; j++) {
			columnLabel[j] = resultsetmetadata.getColumnLabel(j);
		}
	}

	public void writeXML(Rows webrowset, Writer writer) throws SQLException {
		writeXMLwithTagName(webrowset, writer, Rows.DEFAULT_ROOT_NAME, Rows.DEFAULT_ROW_NAME);
	}

	public void writeXMLwithTagName(RowSet webrowset, Writer writer, String rowSetElementName, String rowElementName) throws SQLException {
		stack = new Stack<String>();
		this.writer = writer;
		writeRowSet(webrowset, rowSetElementName, rowElementName);
	}

	public void writeXMLbyReplace(RowSet webrowset, Writer writer) throws SQLException {
		this.replace = true;
		writeXMLbyReplace(webrowset, writer, Rows.DEFAULT_ROOT_NAME, Rows.DEFAULT_ROW_NAME);
	}

	public void writeXMLbyReplace(RowSet webrowset, Writer writer, String rowSetElementName, String rowElementName) throws SQLException {
		this.replace = true;
		this.writer = writer;
		writeRowSet(webrowset, rowSetElementName, rowElementName);
	}

	public void writeXML(WebRowSet webrowset, Writer writer) throws SQLException {
		writeXMLwithTagName(webrowset, writer, Rows.DEFAULT_ROOT_NAME, Rows.DEFAULT_ROW_NAME);
	}

}
