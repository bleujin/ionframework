// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   XmlReaderDocHandler.java

package net.ion.framework.db.rowset;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import javax.sql.RowSet;
import javax.sql.RowSetMetaData;

import org.xml.sax.AttributeList;
import org.xml.sax.HandlerBase;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

// Referenced classes of package sun.jdbc.rowset:
//            BaseRowSet, CachedRowSet, Row, RowSetMetaDataImpl, 
//            WebRowSet

public class XmlReaderDocHandler extends HandlerBase {

	XmlReaderDocHandler(RowSet rowset) {
		rs = (WebRowSet) rowset;
		initMaps();
		updates = new Vector();
		columnValue = new String("");
		propertyValue = new String("");
		metaDataValue = new String("");
		nullVal = false;
		idx = 0;
	}

	private void applyUpdates() throws SAXException {
		if (updates.size() > 0) {
			try {
				Object aobj[];
				for (Iterator iterator = updates.iterator(); iterator.hasNext(); insertValue((String) aobj[1])) {
					aobj = (Object[]) iterator.next();
					idx = ((Integer) aobj[0]).intValue();
				}

				rs.updateRow();
			} catch (SQLException sqlexception) {
				throw new SAXException("Error updating row: " + sqlexception.getMessage());
			}
			updates.removeAllElements();
		}
	}

	public void characters(char ac[], int i, int j) throws SAXException {
		try {
			switch (getState()) {
			case 1: // '\001'
				propertyValue = new String(ac, i, j);
				break;

			case 2: // '\002'
				metaDataValue = new String(ac, i, j);
				break;

			case 3: // '\003'
				setDataValue(ac, i, j);
				break;
			}
		} catch (SQLException sqlexception) {
			throw new SAXException("characters: " + sqlexception.getMessage());
		}
	}

	public void endElement(String s) throws SAXException {
		label0: switch (getState()) {
		default:
			break;

		case 1: // '\001'
			if (s.equals("properties")) {
				state = 0;
				break;
			}
			try {
				int i = ((Integer) propMap.get(s)).intValue();
				switch (i) {
				case 7: // '\007'
					if (keyCols != null) {
						int ai[] = new int[keyCols.size()];
						for (int k = 0; k < ai.length; k++)
							ai[k] = Integer.parseInt((String) keyCols.elementAt(k));

						rs.setKeyColumns(ai);
					}
					break;
				}
				if (getNullValue()) {
					setPropertyValue(null);
					setNullValue(false);
				} else {
					setPropertyValue(propertyValue);
				}
			} catch (SQLException sqlexception) {
				throw new SAXException(sqlexception.getMessage());
			}
			propertyValue = new String("");
			setTag(-1);
			break;

		case 2: // '\002'
			if (s.equals("metadata")) {
				try {
					rs.setMetaData(md);
					state = 0;
				} catch (SQLException sqlexception1) {
					throw new SAXException("Error setting Metadata: " + sqlexception1.getMessage());
				}
			} else {
				try {
					if (getNullValue()) {
						setMetaDataValue(null);
						setNullValue(false);
					} else {
						setMetaDataValue(metaDataValue);
					}
				} catch (SQLException sqlexception2) {
					throw new SAXException("Error setting Metadata: " + sqlexception2.getMessage());
				}
				metaDataValue = new String("");
			}
			setTag(-1);
			break;

		case 3: // '\003'
			if (s.equals("data")) {
				state = 0;
				return;
			}
			int j = ((Integer) dataMap.get(s)).intValue();
			switch (j) {
			default:
				break label0;

			case 1: // '\001'
				try {
					idx++;
					if (getNullValue()) {
						insertValue(null);
						setNullValue(false);
					} else {
						insertValue(columnValue);
					}
					columnValue = new String("");
				} catch (SQLException sqlexception3) {
					throw new SAXException("Error inserting values: " + sqlexception3.getMessage());
				}
				break label0;

			case 0: // '\0'
				try {
					rs.insertRow();
					rs.moveToCurrentRow();
					rs.next();
					Row row = (Row) rs.getCurrentRow();
					row.clearInserted();
					applyUpdates();
				} catch (SQLException sqlexception4) {
					throw new SAXException("Error constructing row: " + sqlexception4.getMessage());
				}
				break label0;

			case 3: // '\003'
				try {
					rs.insertRow();
					rs.moveToCurrentRow();
					rs.next();
					Row row1 = (Row) rs.getCurrentRow();
					row1.clearInserted();
					row1.setDeleted();
					applyUpdates();
				} catch (SQLException sqlexception5) {
					throw new SAXException("Error constructing deleted row: " + sqlexception5.getMessage());
				}
				break label0;

			case 2: // '\002'
				try {
					rs.insertRow();
					rs.moveToCurrentRow();
					rs.next();
					applyUpdates();
				} catch (SQLException sqlexception6) {
					throw new SAXException("Error constructing inserted row: " + sqlexception6.getMessage());
				}
				break label0;

			case 4: // '\004'
				break;
			}
			try {
				rs.insertRow();
				rs.moveToCurrentRow();
				rs.next();
				Row row2 = (Row) rs.getCurrentRow();
				row2.setDeleted();
				applyUpdates();
			} catch (SQLException sqlexception7) {
				throw new SAXException("Error constructing insdel row: " + sqlexception7.getMessage());
			}
			break;
		}
	}

	public void error(SAXParseException saxparseexception) throws SAXParseException {
		throw saxparseexception;
	}

	private BigDecimal getBigDecimalValue(String s) {
		return new BigDecimal(s);
	}

	private byte[] getBinaryValue(String s) {
		return s.getBytes();
	}

	private boolean getBooleanValue(String s) {
		return (new Boolean(s)).booleanValue();
	}

	private byte getByteValue(String s) {
		return Byte.parseByte(s);
	}

	private Date getDateValue(String s) {
		return new Date(getLongValue(s));
	}

	private double getDoubleValue(String s) {
		return Double.parseDouble(s);
	}

	private float getFloatValue(String s) {
		return Float.parseFloat(s);
	}

	private int getIntegerValue(String s) {
		return Integer.parseInt(s);
	}

	private long getLongValue(String s) {
		return Long.parseLong(s);
	}

	private boolean getNullValue() {
		return nullVal;
	}

	private short getShortValue(String s) {
		return Short.parseShort(s);
	}

	private int getState() {
		return state;
	}

	private String getStringValue(String s) {
		return s;
	}

	private int getTag() {
		return tag;
	}

	private Time getTimeValue(String s) {
		return new Time(getLongValue(s));
	}

	private Timestamp getTimestampValue(String s) {
		return new Timestamp(getLongValue(s));
	}

	private void initMaps() {
		propMap = new HashMap();
		int i = properties.length;
		for (int j = 0; j < i; j++)
			propMap.put(properties[j], new Integer(j));

		colDefMap = new HashMap();
		i = colDef.length;
		for (int k = 0; k < i; k++)
			colDefMap.put(colDef[k], new Integer(k));

		dataMap = new HashMap();
		i = data.length;
		for (int l = 0; l < i; l++)
			dataMap.put(data[l], new Integer(l));

	}

	private void insertValue(String s) throws SQLException {
		if (getNullValue()) {
			rs.updateNull(idx);
			return;
		}
		int i = rs.getMetaData().getColumnType(idx);
		switch (i) {
		case -7:
			rs.updateByte(idx, getByteValue(s));
			break;

		case 5: // '\005'
			rs.updateShort(idx, getShortValue(s));
			break;

		case 4: // '\004'
			rs.updateInt(idx, getIntegerValue(s));
			break;

		case -5:
			rs.updateLong(idx, getLongValue(s));
			break;

		case 6: // '\006'
		case 7: // '\007'
			rs.updateFloat(idx, getFloatValue(s));
			break;

		case 8: // '\b'
			rs.updateDouble(idx, getDoubleValue(s));
			break;

		case 2: // '\002'
		case 3: // '\003'
			rs.updateObject(idx, getBigDecimalValue(s));
			break;

		case -4:
		case -3:
		case -2:
			rs.updateBytes(idx, getBinaryValue(s));
			break;

		case 91: // '['
			rs.updateDate(idx, getDateValue(s));
			break;

		case 92: // '\\'
			rs.updateTime(idx, getTimeValue(s));
			break;

		case 93: // ']'
			rs.updateTimestamp(idx, getTimestampValue(s));
			break;

		case -1:
		case 1: // '\001'
		case 12: // '\f'
			rs.updateString(idx, getStringValue(s));
			break;

		default:
			System.out.println("Why here? Why now?");
			break;
		}
	}

	private void setDataValue(char ac[], int i, int j) throws SQLException {
		switch (getTag()) {
		case 1: // '\001'
			columnValue = new String(ac, i, j);
			break;

		case 5: // '\005'
			Object aobj[] = new Object[2];
			aobj[1] = new String(ac, i, j);
			aobj[0] = new Integer(idx);
			updates.add(((Object) (aobj)));
			break;
		}
	}

	private void setMetaDataValue(String s) throws SQLException {
		boolean flag = getNullValue();
		switch (getTag()) {
		case 1: // '\001'
		default:
			break;

		case 0: // '\0'
			md = new RowSetMetaDataImpl();
			idx = 0;
			if (flag)
				throw new SQLException("Bad value; non-nullable metadata");
			md.setColumnCount(getIntegerValue(s));
			break;

		case 2: // '\002'
			idx++;
			break;

		case 3: // '\003'
			if (flag)
				throw new SQLException("Bad value; non-nullable metadata");
			md.setAutoIncrement(idx, getBooleanValue(s));
			break;

		case 4: // '\004'
			if (flag)
				throw new SQLException("Bad value; non-nullable metadata");
			md.setCaseSensitive(idx, getBooleanValue(s));
			break;

		case 5: // '\005'
			if (flag)
				throw new SQLException("Bad value; non-nullable metadata");
			md.setCurrency(idx, getBooleanValue(s));
			break;

		case 6: // '\006'
			if (flag)
				throw new SQLException("Bad value; non-nullable metadata");
			md.setNullable(idx, getIntegerValue(s));
			break;

		case 7: // '\007'
			if (flag)
				throw new SQLException("Bad value; non-nullable metadata");
			md.setSigned(idx, getBooleanValue(s));
			break;

		case 8: // '\b'
			if (flag)
				throw new SQLException("Bad value; non-nullable metadata");
			md.setSearchable(idx, getBooleanValue(s));
			break;

		case 9: // '\t'
			if (flag)
				throw new SQLException("Bad value; non-nullable metadata");
			md.setColumnDisplaySize(idx, getIntegerValue(s));
			break;

		case 10: // '\n'
			if (flag)
				md.setColumnLabel(idx, null);
			else
				md.setColumnLabel(idx, s);
			break;

		case 11: // '\013'
			if (flag)
				md.setColumnName(idx, null);
			else
				md.setColumnName(idx, s);
			break;

		case 12: // '\f'
			if (flag)
				md.setSchemaName(idx, null);
			else
				md.setSchemaName(idx, s);
			break;

		case 13: // '\r'
			if (flag)
				throw new SQLException("Bad value; non-nullable metadata");
			md.setPrecision(idx, getIntegerValue(s));
			break;

		case 14: // '\016'
			if (flag)
				throw new SQLException("Bad value; non-nullable metadata");
			md.setScale(idx, getIntegerValue(s));
			break;

		case 15: // '\017'
			if (flag)
				md.setTableName(idx, null);
			else
				md.setTableName(idx, s);
			break;

		case 16: // '\020'
			if (flag)
				md.setCatalogName(idx, null);
			else
				md.setCatalogName(idx, s);
			break;

		case 17: // '\021'
			if (flag)
				throw new SQLException("Bad value; non-nullable metadata");
			md.setColumnType(idx, getIntegerValue(s));
			break;

		case 18: // '\022'
			if (flag)
				md.setColumnTypeName(idx, null);
			else
				md.setColumnTypeName(idx, s);
			break;
		}
	}

	private void setNullValue(boolean flag) {
		nullVal = flag;
	}

	private void setPropertyValue(String s) throws SQLException {
		boolean flag = getNullValue();
		switch (getTag()) {
		case 7: // '\007'
		case 8: // '\b'
		case 17: // '\021'
		default:
			break;

		case 0: // '\0'
			if (flag)
				rs.setCommand(null);
			else
				rs.setCommand(s);
			break;

		case 1: // '\001'
			if (flag)
				throw new SQLException("Bad value; non-nullable property");
			rs.setConcurrency(getIntegerValue(s));
			break;

		case 2: // '\002'
			if (flag)
				rs.setDataSourceName(null);
			else
				rs.setDataSourceName(s);
			break;

		case 3: // '\003'
			if (flag)
				throw new SQLException("Bad value; non-nullable property");
			rs.setEscapeProcessing(getBooleanValue(s));
			break;

		case 4: // '\004'
			if (flag)
				throw new SQLException("Bad value; non-nullable property");
			rs.setFetchDirection(getIntegerValue(s));
			break;

		case 5: // '\005'
			if (flag)
				throw new SQLException("Bad value; non-nullable property");
			rs.setFetchSize(getIntegerValue(s));
			break;

		case 6: // '\006'
			if (flag)
				throw new SQLException("Bad value; non-nullable property");
			rs.setTransactionIsolation(getIntegerValue(s));
			break;

		case 18: // '\022'
			if (keyCols == null)
				keyCols = new Vector();
			keyCols.add(s);
			break;

		case 9: // '\t'
			if (flag)
				throw new SQLException("Bad value; non-nullable property");
			rs.setMaxFieldSize(getIntegerValue(s));
			break;

		case 10: // '\n'
			if (flag)
				throw new SQLException("Bad value; non-nullable property");
			rs.setMaxRows(getIntegerValue(s));
			break;

		case 11: // '\013'
			if (flag)
				throw new SQLException("Bad value; non-nullable property");
			rs.setQueryTimeout(getIntegerValue(s));
			break;

		case 12: // '\f'
			if (flag)
				throw new SQLException("Bad value; non-nullable property");
			rs.setReadOnly(getBooleanValue(s));
			break;

		case 13: // '\r'
			if (flag)
				throw new SQLException("Bad value; non-nullable property");
			rs.setType(getIntegerValue(s));
			break;

		case 14: // '\016'
			if (flag)
				throw new SQLException("Bad value; non-nullable property");
			rs.setShowDeleted(getBooleanValue(s));
			break;

		case 15: // '\017'
			if (flag)
				rs.setTableName(null);
			else
				rs.setTableName(s);
			break;

		case 16: // '\020'
			if (flag)
				rs.setUrl(null);
			else
				rs.setUrl(s);
			break;
		}
	}

	private void setState(String s) throws SAXException {
		if (s.equals("RowSet"))
			state = 0;
		else if (s.equals("properties")) {
			if (state != 1)
				state = 1;
			else
				state = 0;
		} else if (s.equals("metadata")) {
			if (state != 2)
				state = 2;
			else
				state = 0;
		} else if (s.equals("data"))
			if (state != 3)
				state = 3;
			else
				state = 0;
	}

	private void setTag(int i) {
		tag = i;
	}

	public void startElement(String s, AttributeList attributelist) throws SAXException {
		switch (getState()) {
		case 1: // '\001'
			int i = ((Integer) propMap.get(s)).intValue();
			if (i == 17)
				setNullValue(true);
			else
				setTag(i);
			break;

		case 2: // '\002'
			int j = ((Integer) colDefMap.get(s)).intValue();
			if (j == 19)
				setNullValue(true);
			else
				setTag(j);
			break;

		case 3: // '\003'
			int k = ((Integer) dataMap.get(s)).intValue();
			if (k == 6) {
				setNullValue(true);
				break;
			}
			setTag(k);
			if (k != 0 && k != 3 && k != 2)
				break;
			idx = 0;
			try {
				rs.moveToInsertRow();
			} catch (SQLException _ex) {
			}
			break;

		default:
			setState(s);
			break;
		}
	}

	public void warning(SAXParseException saxparseexception) throws SAXParseException {
		System.out.println("** Warning, line " + saxparseexception.getLineNumber() + ", uri " + saxparseexception.getSystemId());
		System.out.println("   " + saxparseexception.getMessage());
	}

	private HashMap propMap;
	private HashMap colDefMap;
	private HashMap dataMap;
	private Vector updates;
	private Vector keyCols;
	private String columnValue;
	private String propertyValue;
	private String metaDataValue;
	private int tag;
	private int state;
	private WebRowSet rs;
	private boolean nullVal;
	private RowSetMetaData md;
	private int idx;
	private String properties[] = { "command", "concurrency", "datasource", "escape-processing", "fetch-direction", "fetch-size", "isolation-level",
			"key-columns", "map", "max-field-size", "max-rows", "query-timeout", "read-only", "rowset-type", "show-deleted", "table-name", "url", "null",
			"column", "type", "class" };
	private static final int CommandTag = 0;
	private static final int ConcurrencyTag = 1;
	private static final int DatasourceTag = 2;
	private static final int EscapeProcessingTag = 3;
	private static final int FetchDirectionTag = 4;
	private static final int FetchSizeTag = 5;
	private static final int IsolationLevelTag = 6;
	private static final int KeycolsTag = 7;
	private static final int MapTag = 8;
	private static final int MaxFieldSizeTag = 9;
	private static final int MaxRowsTag = 10;
	private static final int QueryTimeoutTag = 11;
	private static final int ReadOnlyTag = 12;
	private static final int RowsetTypeTag = 13;
	private static final int ShowDeletedTag = 14;
	private static final int TableNameTag = 15;
	private static final int UrlTag = 16;
	private static final int PropNullTag = 17;
	private static final int PropColumnTag = 18;
	private static final int PropTypeTag = 19;
	private static final int PropClassTag = 20;
	private String colDef[] = { "column-count", "column-definition", "column-index", "auto-increment", "case-sensitive", "currency", "nullable", "signed",
			"searchable", "column-display-size", "column-label", "column-name", "schema-name", "column-precision", "column-scale", "table-name",
			"catalog-name", "column-type", "column-type-name", "null" };
	private static final int ColumnCountTag = 0;
	private static final int ColumnDefinitionTag = 1;
	private static final int ColumnIndexTag = 2;
	private static final int AutoIncrementTag = 3;
	private static final int CaseSensitiveTag = 4;
	private static final int CurrencyTag = 5;
	private static final int NullableTag = 6;
	private static final int SignedTag = 7;
	private static final int SearchableTag = 8;
	private static final int ColumnDisplaySizeTag = 9;
	private static final int ColumnLabelTag = 10;
	private static final int ColumnNameTag = 11;
	private static final int SchemaNameTag = 12;
	private static final int ColumnPrecisionTag = 13;
	private static final int ColumnScaleTag = 14;
	private static final int MetaTableNameTag = 15;
	private static final int CatalogNameTag = 16;
	private static final int ColumnTypeTag = 17;
	private static final int ColumnTypeNameTag = 18;
	private static final int MetaNullTag = 19;
	private String data[] = { "row", "col", "ins", "del", "insdel", "upd", "null" };
	private static final int RowTag = 0;
	private static final int ColTag = 1;
	private static final int InsTag = 2;
	private static final int DelTag = 3;
	private static final int InsDelTag = 4;
	private static final int UpdTag = 5;
	private static final int NullTag = 6;
	private static final int INITIAL = 0;
	private static final int PROPERTIES = 1;
	private static final int METADATA = 2;
	private static final int DATA = 3;
}
