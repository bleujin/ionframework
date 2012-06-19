package net.ion.framework.db;

import java.io.IOException;
import java.io.Writer;
import java.math.BigDecimal;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;

import javax.sql.RowSet;

import net.ion.framework.db.rowset.WebRowSet;

public class ExtXmlWriter implements IXmlWriter {
	protected Writer writer;
	protected Stack<String> stack;
	final static int CUT_SIZE = 4000;

	public ExtXmlWriter() {
	}

	protected void beginSection(String s) throws IOException {
		setTag(s);
		writeIndent(stack.size());
		writer.write("<" + s + ">\n");
	}

	protected void beginTag(String s) throws IOException {
		setTag(s);
		writeIndent(stack.size());
		writer.write("<" + s + ">");
	}

	protected void emptyTag(String s) throws IOException {
		writer.write("<" + s + "/>");
	}

	protected void endSection() throws IOException {
		writeIndent(stack.size());
		String s = getTag();
		writer.write("</" + s + ">\n");
		writer.flush();
	}

	protected void endSection(String s) throws IOException {
		writeIndent(stack.size());
		String s1 = getTag();
		if (s.equals(s1))
			writer.write("</" + s1 + ">\n");
		writer.flush();
	}

	protected void endTag(String s) throws IOException {
		String s1 = getTag();
		if (s.equals(s1))
			writer.write("</" + s1 + ">\n");
		writer.flush();
	}

	protected String getTag() {
		return (String) stack.pop();
	}

	protected void propBoolean(String s, boolean flag) throws IOException {
		beginTag(s);
		writeBoolean(flag);
		endTag(s);
	}

	protected void propInteger(String s, int i) throws IOException {
		beginTag(s);
		writeInteger(i);
		endTag(s);
	}

	protected void propString(String s, String s1) throws IOException {
		beginTag(s);
		writeString(s1);
		endTag(s);
	}

	protected void setTag(String s) {
		stack.push(s);
	}

	protected void writeBigDecimal(BigDecimal bigdecimal) throws IOException {
		if (bigdecimal != null)
			writer.write(bigdecimal.toString());
		else
			writeNull();
	}

	protected void writeBoolean(boolean flag) throws IOException {
		writer.write((new Boolean(flag)).toString());
	}

	protected void writeClob(String data) throws IOException {
		if (data != null) {
			startCDATATag();
			writer.write(data);
			endCDATATag();
		} else
			writeNull();
	}

	protected void startCDATATag() throws IOException {
		writer.write("<![CDATA[");
	}

	protected void endCDATATag() throws IOException {
		writer.write("]]>");
	}

	protected void writeData(RowSet webrowset) throws IOException {
		try {
			ResultSetMetaData resultsetmetadata = webrowset.getMetaData();
			int i = resultsetmetadata.getColumnCount();
			beginSection("data");
			webrowset.beforeFirst();
			// webrowset.setShowDeleted(true);
			for (; webrowset.next(); endSection()) {
				if (webrowset.rowDeleted() && webrowset.rowInserted())
					beginSection("insdel");
				else if (webrowset.rowDeleted())
					beginSection("del");
				else if (webrowset.rowInserted())
					beginSection("ins");
				else
					beginSection("row");
				for (int j = 1; j <= i; j++) {
					// if(webrowset.columnUpdated(j)) {
					// ResultSet resultset=webrowset.getOriginalRow();
					// resultset.next();
					// beginTag("col");
					// writeValue(j,(RowSet)resultset);
					// endTag("col");
					// beginTag("upd");
					// writeValue(j,webrowset);
					// endTag("upd");
					// }
					// else {
					beginTag("col");
					writeValue(j, webrowset);
					endTag("col");
					// }
				}

			}

			endSection("data");
		} catch (SQLException sqlexception) {
			throw new IOException("SQLException: " + sqlexception.getMessage());
		}
	}

	protected void writeDouble(double d) throws IOException {
		writer.write(Double.toString(d));
	}

	protected void writeFloat(float f) throws IOException {
		writer.write(Float.toString(f));
	}

	protected void writeHeader() throws IOException {
		writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		writer.write("<!DOCTYPE RowSet PUBLIC '" + WebRowSet.PUBLIC_DTD_ID + "' '" + WebRowSet.SYSTEM_ID + "'>\n\n");
	}

	protected void writeIndent(int i) throws IOException {
		for (int j = 1; j < i; j++)
			writer.write("  ");

	}

	protected void writeInteger(int i) throws IOException {
		writer.write(Integer.toString(i));
	}

	protected void writeLong(long l) throws IOException {
		writer.write(Long.toString(l));
	}

	protected void writeMetaData(RowSet webrowset) throws IOException {
		beginSection("metadata");
		try {
			ResultSetMetaData resultsetmetadata = webrowset.getMetaData();
			int i = resultsetmetadata.getColumnCount();
			propInteger("column-count", i);
			for (int j = 1; j <= i; j++) {
				beginSection("column-definition");
				propInteger("column-index", j);
				propBoolean("auto-increment", resultsetmetadata.isAutoIncrement(j));
				propBoolean("case-sensitive", resultsetmetadata.isCaseSensitive(j));
				propBoolean("currency", resultsetmetadata.isCurrency(j));
				propInteger("nullable", resultsetmetadata.isNullable(j));
				propBoolean("signed", resultsetmetadata.isSigned(j));
				propBoolean("searchable", resultsetmetadata.isSearchable(j));
				propInteger("column-display-size", resultsetmetadata.getColumnDisplaySize(j));
				propString("column-label", resultsetmetadata.getColumnLabel(j));
				propString("column-name", resultsetmetadata.getColumnName(j));
				propString("schema-name", resultsetmetadata.getSchemaName(j));
				propInteger("column-precision", resultsetmetadata.getPrecision(j));
				propInteger("column-scale", resultsetmetadata.getScale(j));
				propString("table-name", resultsetmetadata.getTableName(j));
				propString("catalog-name", resultsetmetadata.getCatalogName(j));
				if (resultsetmetadata.getColumnType(j) == 2005) {
					propInteger("column-type", 12);
					propString("column-type-name", "VARCHAR2");
				} else {
					propInteger("column-type", resultsetmetadata.getColumnType(j));
					propString("column-type-name", resultsetmetadata.getColumnTypeName(j));
				}
				endSection("column-definition");
			}

		} catch (SQLException sqlexception) {
			throw new IOException("SQLException: " + sqlexception.getMessage());
		}
		endSection("metadata");
	}

	protected void writeNull() throws IOException {
		emptyTag("null");
	}

	protected void writeProperties(RowSet webrowset) throws IOException {
		beginSection("properties");
		try {
			propString("command", webrowset.getCommand());
			propInteger("concurrency", webrowset.getConcurrency());
			propString("datasource", webrowset.getDataSourceName());
			propBoolean("escape-processing", webrowset.getEscapeProcessing());
			propInteger("fetch-direction", webrowset.getFetchDirection());
			propInteger("fetch-size", webrowset.getFetchSize());
			propInteger("isolation-level", webrowset.getTransactionIsolation());
			beginTag("map");
			Map<String, Class<?>> map = webrowset.getTypeMap();
			if (map != null) {
				Class<?> class1;
				for (Iterator<String> iterator = map.keySet().iterator(); iterator.hasNext(); propString("class", class1.getName())) {
					String s = iterator.next();
					class1 = map.get(s);
					propString("type", s);
				}

			}
			endTag("map");
			propInteger("max-field-size", webrowset.getMaxFieldSize());
			propInteger("max-rows", webrowset.getMaxRows());
			propInteger("query-timeout", webrowset.getQueryTimeout());
			propBoolean("read-only", webrowset.isReadOnly());
			propInteger("rowset-type", webrowset.getType());
			propString("url", webrowset.getUrl());
		} catch (SQLException sqlexception) {
			throw new IOException("SQLException: " + sqlexception.getMessage());
		}
		endSection("properties");
	}

	protected void writeRowSet(RowSet webrowset) throws SQLException {
		try {
			writeHeader();
			beginSection("RowSet");
			writeProperties(webrowset);
			writeMetaData(webrowset);
			writeData(webrowset);
			endSection("RowSet");
		} catch (IOException ioexception) {
			throw new SQLException("IOException: " + ioexception.getMessage());
		}
	}

	protected void writeShort(short word0) throws IOException {
		writer.write(Short.toString(word0));
	}

	protected void writeString(String s) throws IOException {
		if (s != null) {
			if (!s.equals("")) {
				writer.write(s);
			} else {
				writeNull();
			}
		} else
			writeNull();
	}

	protected void writeValue(int i, RowSet rowset) throws IOException {
		try {
			int j = rowset.getMetaData().getColumnType(i);
			switch (j) {
			case -7:
				boolean flag = rowset.getBoolean(i);
				if (rowset.wasNull())
					writeNull();
				else
					writeBoolean(flag);
				break;

			case -4:
			case -3:
			case -2:
				break;

			case 5: // '\005'
				short word0 = rowset.getShort(i);
				if (rowset.wasNull())
					writeNull();
				else
					writeShort(word0);
				break;

			case 4: // '\004'
				int k = rowset.getInt(i);
				if (rowset.wasNull())
					writeNull();
				else
					writeInteger(k);
				break;

			case -5:
				long l = rowset.getLong(i);
				if (rowset.wasNull())
					writeNull();
				else
					writeLong(l);
				break;

			case 6: // '\006'
			case 7: // '\007'
				float f = rowset.getFloat(i);
				if (rowset.wasNull())
					writeNull();
				else
					writeFloat(f);
				break;

			case 8: // '\b'
				double d = rowset.getDouble(i);
				if (rowset.wasNull())
					writeNull();
				else
					writeDouble(d);
				break;

			case 2: // '\002'
			case 3: // '\003'
			// writeBigDecimal( rowset.getBigDecimal( i ) );
				// -_- zzim zzim..
				writeString(rowset.getString(i));
				break;

			case 91: // '['
			// java.sql.Date date=rowset.getDate(i);
			// if(rowset.wasNull())
			// writeNull();
			// else
			// writeLong(date.getTime());

				// 일단 임시로..
				writeString(rowset.getString(i));
				break;

			case 92: // '\\'
				java.sql.Time time = rowset.getTime(i);
				if (rowset.wasNull())
					writeNull();
				else
					writeLong(time.getTime());
				break;

			case 93: // ']'
				try {
					java.sql.Timestamp timestamp = rowset.getTimestamp(i);
					if (rowset.wasNull()) {
						writeNull();
					} else {
						writeLong(timestamp.getTime());
					}
				} catch (ClassCastException ex) {
					// writeXml 에 의해 만들어진 XML String으로 Rows 를 만든후 writeXml() 하면 ClassCastException 발생
					// 그래서 -_- 이렇게 처리
					if (rowset.wasNull())
						writeNull();
					else
						writeLong(Long.parseLong(rowset.getString(i)));
				}

				break;

			case -1:
			case 1: // '\001'
			case 12: // '\f'
				startCDATATag();
				writeString(rowset.getString(i));
				endCDATATag();
				break;

			case 2005:
				writeClob(((Rows) rowset).getString(i));
				break;
			default:
				System.out.println("ColumnType : " + j);
				System.out.println("Why here? Why now?");
				break;
			}
		} catch (SQLException sqlexception) {
			throw new IOException("Failed to writeValue: " + sqlexception.getMessage());
		}
	}

	public void writeXML(WebRowSet webrowset, Writer writer1) throws SQLException {
		stack = new Stack<String>();
		writer = writer1;
		writeRowSet(webrowset);
	}

	public void writeXML(Rows rowset, Writer writer1) throws SQLException {
		stack = new Stack<String>();
		writer = writer1;
		writeRowSet(rowset);
	}

}
