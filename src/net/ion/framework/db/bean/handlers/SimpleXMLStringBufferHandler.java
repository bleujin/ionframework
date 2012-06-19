package net.ion.framework.db.bean.handlers;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Stack;

import net.ion.framework.db.Rows;
import net.ion.framework.db.bean.ResultSetHandler;
import net.ion.framework.util.HttpUtils;

import org.apache.commons.io.IOUtils;

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

public class SimpleXMLStringBufferHandler implements ResultSetHandler {
	StringBuffer sbuffer = new StringBuffer();
	String rootElementName;
	String rowElementName;

	Stack<String> stack = new Stack<String>();
	protected boolean replace = false; // only use CDATA Section

	String[] columnLabel;

	public SimpleXMLStringBufferHandler() {
		this(Rows.DEFAULT_ROOT_NAME, Rows.DEFAULT_ROW_NAME);
	}

	public SimpleXMLStringBufferHandler(String rootElementName, String rowElementName) {
		this.rootElementName = rootElementName;
		this.rowElementName = rowElementName;
	}

	public Object handle(ResultSet resultSet) throws SQLException {
		appendHeader();
		beginSection(rootElementName);
		initMeta(resultSet);
		appendData(resultSet, rowElementName);
		endSection(rootElementName);
		return sbuffer;
	}

	protected void beginSection(String s) {
		setTag(s);
		appendIndent(stack.size());
		sbuffer.append("<" + s + ">\n");
	}

	protected void beginSection(String s, int index) {
		setTag(s);
		appendIndent(stack.size());
		sbuffer.append("<" + s + " index=\"" + index + "\">\n");
	}

	protected void beginTag(String s) {
		setTag(s);
		appendIndent(stack.size());
		sbuffer.append("<" + s + ">");
	}

	protected void emptyTag(String s) {
		sbuffer.append("<" + s + "/>");
	}

	protected void endSection() {
		appendIndent(stack.size());
		String s = getTag();
		sbuffer.append("</" + s + ">\n");
	}

	protected void endSection(String s) {
		appendIndent(stack.size());
		String s1 = getTag();
		if (s.equals(s1))
			sbuffer.append("</" + s1 + ">\n");
	}

	protected void endTag(String s) {
		String s1 = getTag();
		if (s.equals(s1))
			sbuffer.append("</" + s1 + ">\n");
	}

	protected String getTag() {
		return (String) stack.pop();
	}

	protected void propBoolean(String s, boolean flag) {
		beginTag(s);
		appendBoolean(flag);
		endTag(s);
	}

	protected void propInteger(String s, int i) {
		beginTag(s);
		appendInteger(i);
		endTag(s);
	}

	protected void propString(String s, String s1) {
		beginTag(s);
		appendString(s1);
		endTag(s);
	}

	protected void setTag(String s) {
		stack.push(s);
	}

	protected void appendBigDecimal(BigDecimal bigdecimal) {
		if (bigdecimal != null)
			sbuffer.append(bigdecimal.toString());
		else
			appendNull();
	}

	protected void appendBoolean(boolean flag) {
		sbuffer.append((new Boolean(flag)).toString());
	}

	protected void appendClob(String data) {
		if (data != null) {
			if (replace) {
				data = data.replaceAll("--]]>", "--]] >");
				data = data.replace((char) 0xb, '\n');
			}
			startCDATATag();
			sbuffer.append(data);
			endCDATATag();
		} else
			appendNull();
	}

	protected void startCDATATag() {
		sbuffer.append("<![CDATA[");
	}

	protected void endCDATATag() {
		sbuffer.append("]]>");
	}

	protected void appendData(ResultSet resultSet, String rowElementName) throws SQLException {
		ResultSetMetaData resultsetmetadata = resultSet.getMetaData();
		int i = resultsetmetadata.getColumnCount();
		for (int k = 1; resultSet.next(); endSection(), k++) {
			if (resultSet.rowDeleted() && resultSet.rowInserted())
				beginSection("insdel", k);
			else if (resultSet.rowDeleted())
				beginSection("del", k);
			else if (resultSet.rowInserted())
				beginSection("ins", k);
			else
				beginSection(rowElementName, k);
			for (int j = 1; j <= i; j++) {
				beginTag(columnLabel[j].toUpperCase());
				appendValue(j, resultSet);
				endTag(columnLabel[j].toUpperCase());
			}
		}
	}

	protected void appendDouble(double d) {
		sbuffer.append(Double.toString(d));
	}

	protected void appendFloat(float f) {
		sbuffer.append(Float.toString(f));
	}

	protected void appendHeader() {
		sbuffer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		sbuffer.append("<!-- DOCTYPE RowSet SYSTEM '" + "User Defined" + "' -->\n\n");
	}

	protected void appendIndent(int i) {
		for (int j = 1; j < i; j++)
			sbuffer.append("\t");

	}

	protected void appendInteger(int i) {
		sbuffer.append(Integer.toString(i));
	}

	protected void appendLong(long l) {
		sbuffer.append(Long.toString(l));
	}

	protected void appendNull() {
		emptyTag("null");
	}

	private void initMeta(ResultSet resultSet) throws SQLException {
		ResultSetMetaData resultsetmetadata = resultSet.getMetaData();
		int i = resultsetmetadata.getColumnCount();
		columnLabel = new String[i + 1];

		for (int j = 1; j <= i; j++) {
			columnLabel[j] = resultsetmetadata.getColumnLabel(j);
		}
	}

	protected void appendShort(short word0) {
		sbuffer.append(Short.toString(word0));
	}

	protected void appendString(String s) {
		if (s != null) {
			if (!s.equals("")) {
				sbuffer.append(s);
			} else {
				appendNull();
			}
		} else
			appendNull();
	}

	protected void appendValue(int i, ResultSet resultSet) throws SQLException {
		int j = resultSet.getMetaData().getColumnType(i);
		switch (j) {
		case Types.BIT:
			boolean flag = resultSet.getBoolean(i);
			if (resultSet.wasNull())
				appendNull();
			else
				appendBoolean(flag);
			break;

		case Types.LONGVARBINARY:
		case Types.VARBINARY:
		case Types.BINARY:
			break;

		case Types.SMALLINT: // '\005'
			short word0 = resultSet.getShort(i);
			if (resultSet.wasNull())
				appendNull();
			else
				appendShort(word0);
			break;

		case Types.INTEGER: // '\004'
			int k = resultSet.getInt(i);
			if (resultSet.wasNull())
				appendNull();
			else
				appendInteger(k);
			break;

		case Types.BIGINT:
			long l = resultSet.getLong(i);
			if (resultSet.wasNull())
				appendNull();
			else
				appendLong(l);
			break;

		case Types.FLOAT: // '\006'
		case Types.REAL: // '\007'
			float f = resultSet.getFloat(i);
			if (resultSet.wasNull())
				appendNull();
			else
				appendFloat(f);
			break;

		case Types.DOUBLE: // '\b'
			double d = resultSet.getDouble(i);
			if (resultSet.wasNull())
				appendNull();
			else
				appendDouble(d);
			break;

		case Types.NUMERIC: // '\002'
		case Types.DECIMAL: // '\003'

			// writeBigDecimal( rowset.getBigDecimal( i ) ); -_- zzim zzim..
			appendString(resultSet.getString(i));
			break;

		case Types.DATE: // '['
		// java.sql.Date date=resultSet.getDate(i);
		// if(resultSet.wasNull())
		// appendNull();
		// else
		// appendLong(date.getTime());
			appendString(resultSet.getString(i));
			break;

		case Types.TIME: // '\\'
			java.sql.Time time = resultSet.getTime(i);
			if (resultSet.wasNull())
				appendNull();
			else
				appendLong(time.getTime());
			break;

		case Types.TIMESTAMP: // ']'
			try {
				java.sql.Timestamp timestamp = resultSet.getTimestamp(i);
				if (resultSet.wasNull()) {
					appendNull();
				} else {
					appendLong(timestamp.getTime());
				}
			} catch (ClassCastException ex) {
				// writeXml 에 의해 만들어진 XML String으로 Rows 를 만든후 writeXml() 하면 ClassCastException 발생 그래서 -_- 이렇게 처리
				if (resultSet.wasNull())
					appendNull();
				else
					appendLong(Long.parseLong(resultSet.getString(i)));
			}

			break;

		case Types.LONGVARCHAR:
		case Types.CLOB:
			if (resultSet.getClob(i) == null) {
				appendNull();
			} else {
				appendClob(ClobToString(resultSet.getClob(i)));
			}
			break;

		case Types.CHAR: // '\001'
		case Types.VARCHAR: // '\f'
			appendString(removeTag(resultSet.getString(i)));
			break;

		default:
			System.out.println("Unknown ColumnType : " + j);
			break;
		}
	}

	private String ClobToString(Clob clob) throws SQLException {

		int len = (int) clob.length();
		StringBuffer result = new StringBuffer(len);

		try {
			result.append(IOUtils.toString(clob.getCharacterStream()));
		} catch (SQLException ex) {
			throw ex;
		} catch (IOException ex) {
			throw new SQLException("Not Known Clob Foramt : can't make clob string" + ex.toString());
		}
		return result.toString();

	}

	private String removeTag(String s) {
		return HttpUtils.filterHTML(s);
	}

}
