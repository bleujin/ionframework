package net.ion.framework.db;

import java.io.File;
import java.io.RandomAccessFile;
import java.sql.ResultSetMetaData;

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

public class Row2XMLWriter {
	public Row2XMLWriter() {
	}

	private String tagNull(String value) {
		if (value == null) {
			value = "<null/>";
		} else if (value.equalsIgnoreCase("null")) {
			value = "<null/>";
		} else if (value.equalsIgnoreCase("")) {
			value = "<null/>";
		}
		return value;
	}

	private String generateXmlProperties(Rows rows) {
		String result = "";

		try {
			result = "  <properties>\r\n";

			result += "    <command>" + tagNull(rows.getCommand()) + "</command>\r\n";
			result += "    <concurrency>" + rows.getConcurrency() + "</concurrency>\r\n";
			result += "    <datasource>" + tagNull(rows.getDataSourceName()) + "</datasource>\r\n";
			result += "    <escape-processing>" + rows.getEscapeProcessing() + "</escape-processing>\r\n";
			result += "    <fetch-direction>" + rows.getFetchDirection() + "</fetch-direction>\r\n";
			result += "    <fetch-size>" + rows.getFetchSize() + "</fetch-size>\r\n";
			result += "    <isolation-level>" + rows.getTransactionIsolation() + "</isolation-level>\r\n";

			// int[] keyColumnList=wrs.getKeyColumns();
			result += "    <key-columns>\r\n    </key-columns>\r\n";
			// Map map = wrs.getTypeMap();
			result += "    <map></map>\r\n";

			result += "    <max-field-size>" + rows.getMaxFieldSize() + "</max-field-size>\r\n";
			result += "    <max-rows>" + rows.getMaxRows() + "</max-rows>\r\n";
			result += "    <query-timeout>" + rows.getQueryTimeout() + "</query-timeout>\r\n";
			result += "    <read-only>" + rows.isReadOnly() + "</read-only>\r\n";
			result += "    <rowset-type>" + rows.getType() + "</rowset-type>\r\n";
			result += "    <table-name>" + tagNull(rows.getTableName()) + "</table-name>\r\n";
			result += "    <url>" + tagNull(rows.getUrl()) + "</url>\r\n";

			result += "  </properties>";
		} catch (Exception e) {
			e.printStackTrace();
			result = "";
		}

		return result;
	}

	private String generateXmlMetadata(Rows rows) {
		String result = "";

		try {
			ResultSetMetaData rsm = rows.getMetaData();
			int columnCount = rsm.getColumnCount();

			result = "  <metadata>\r\n";
			result += "    <column-count>" + columnCount + "</column-count>\r\n";

			for (int i = 1; i <= columnCount; i++) {
				result += "    <column-definition>\r\n";

				result += "      <column-index>" + i + "</column-index>\r\n";
				result += "      <auto-increment>" + rsm.isAutoIncrement(i) + "</auto-increment>\r\n";
				result += "      <case-sensitive>" + rsm.isCaseSensitive(i) + "</case-sensitive>\r\n";
				result += "      <currency>" + rsm.isCurrency(i) + "</currency>\r\n";
				result += "      <nullable>" + rsm.isNullable(i) + "</nullable>\r\n";
				result += "      <signed>" + rsm.isSigned(i) + "</signed>\r\n";
				result += "      <searchable>" + rsm.isSearchable(i) + "</searchable>\r\n";
				result += "      <column-display-size>" + rsm.getColumnDisplaySize(i) + "</column-display-size>\r\n";
				result += "      <column-label>" + tagNull(rsm.getColumnLabel(i)) + "</column-label>\r\n";
				result += "      <column-name>" + tagNull(rsm.getColumnName(i)) + "</column-name>\r\n";
				result += "      <schema-name>" + tagNull(rsm.getSchemaName(i)) + "</schema-name>\r\n";
				result += "      <column-precision>" + rsm.getPrecision(i) + "</column-precision>\r\n";
				result += "      <column-scale>" + rsm.getScale(i) + "</column-scale>\r\n";
				result += "      <table-name>" + tagNull(rsm.getTableName(i)) + "</table-name>\r\n";
				result += "      <catalog-name>" + tagNull(rsm.getCatalogName(i)) + "</catalog-name>\r\n";
				result += "      <column-type>" + rsm.getColumnType(i) + "</column-type>\r\n";
				result += "      <column-type-name>" + tagNull(rsm.getColumnTypeName(i)) + "</column-type-name>\r\n";

				result += "    </column-definition>\r\n";
			}

			result += "  </metadata>";
		} catch (Exception e) {
			e.printStackTrace();
			result = "";
		}

		return result;
	}

	private String generateRowData(Rows rows) {
		String result = "";

		try {
			result += "    <row>\r\n";

			ResultSetMetaData rsm = rows.getMetaData();

			for (int i = 1; i <= rsm.getColumnCount(); i++) {
				// if ( rsm.getColumnType(i) == 1 || rsm.getColumnType( i ) == 12 )
				if (rsm.getColumnTypeName(i).equalsIgnoreCase("CHAR") || rsm.getColumnTypeName(i).equalsIgnoreCase("VARCHAR")
						|| rsm.getColumnTypeName(i).equalsIgnoreCase("VARCHAR2") || rsm.getColumnTypeName(i).equalsIgnoreCase("CLOB")
						|| rsm.getColumnTypeName(i).equalsIgnoreCase("TEXT")) {
					result += "      <col><![CDATA[" + tagNull(rows.getString(i)) + "]]></col>\r\n";
				} else {
					result += "      <col>" + rows.getString(i) + "</col>\r\n";
				}
			}

			result += "    </row>\r\n";
		} catch (Exception e) {
			e.printStackTrace();
			result = "";
		}

		return result;
	}

	private boolean appendFileWithString(File xmlfile, String data) {
		boolean bRet = true;

		try {
			RandomAccessFile fObject = new RandomAccessFile(xmlfile, "rw");
			long lStartOffset = fObject.length();
			fObject.seek(lStartOffset);
			fObject.write(data.getBytes());
			fObject.close();
		} catch (Exception e) {
			e.printStackTrace();
			bRet = false;
		}

		return bRet;
	}

	private boolean makeXml(Rows rows, File xmlfile) {
		boolean bRet = true;

		try {
			String properties = this.generateXmlProperties(rows);
			String metadata = this.generateXmlMetadata(rows);

			String xmlData = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n";
			xmlData += "<!DOCTYPE RowSet PUBLIC '-//Sun Microsystems, Inc.//DTD RowSet//EN' 'http://java.sun.com/j2ee/dtds/RowSet.dtd'>\r\n";
			xmlData += "\r\n<RowSet>\r\n" + properties + "\r\n" + metadata + "\r\n";
			xmlData += "  <data>\r\n";

			appendFileWithString(xmlfile, xmlData);

			rows.beforeFirst();
			while (rows.next()) {
				String rowData = generateRowData(rows);
				appendFileWithString(xmlfile, rowData);
			}

			xmlData = "  </data>\r\n</RowSet>\r\n";
			appendFileWithString(xmlfile, xmlData);
		} catch (Exception e) {
			e.printStackTrace();
			bRet = false;
		}

		return bRet;
	}

	public String writeXml(Rows rows, File xmlfile) throws RowsToConvertException {
		try {
			makeXml(rows, xmlfile);
		} catch (Exception ex) {
			ex.printStackTrace();

			throw new RowsToConvertException(ex.getMessage(), ex);
		}

		return xmlfile.getAbsolutePath();
	}

}
