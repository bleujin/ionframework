package net.ion.framework.db.bean.handlers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CSVStoreHandler extends CustomResultSetHandler {
	protected File file;
	protected String[] columns;
	protected String encoding;

	public CSVStoreHandler(File file, String[] columns, String encoding) {
		this.file = file;
		this.columns = columns;
		this.encoding = encoding;
	}

	public Object handle(ResultSet rs) throws SQLException {
		CSVWriter writer = null;
		try {
			writer = getWriter(file, encoding);
			writer.writeHeader(columns);

			int[] types = getColumnType(rs, columns);

			while (rs.next()) {
				String[] row = new String[columns.length];
				for (int i = 0, last = columns.length; i < last; i++) {
					row[i] = getDefaultString(rs, types, i, columns[i]);
				}
				writer.writeData(row);
			}
			writer.close();
			return createReader(file, encoding);
		} catch (IOException ex) {
			throw new SQLException(ex.getMessage());
		} finally {
			try {
				if (writer != null) {
					writer.close();
				}
			} catch (IOException ex1) {
			}
		}
	}

	public static CSVWriter getWriter(File file, String encoding) throws IOException {
		return new CSVWriter(file, encoding);
	}

	public static CSVReader createReader(File file, String encoding) throws IOException {
		return new CSVReader(new InputStreamReader(new FileInputStream(file), encoding));
	}

}
