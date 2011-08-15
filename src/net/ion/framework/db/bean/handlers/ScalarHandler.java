package net.ion.framework.db.bean.handlers;

import java.sql.ResultSet;
import java.sql.SQLException;

import net.ion.framework.db.bean.ResultSetHandler;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: I-ON Communications
 * </p>
 * 
 * @author bleujin
 * @version 1.0
 */

public class ScalarHandler implements ResultSetHandler {

	/**
	 * The column number to retrieve.
	 */
	private int columnIndex = 1;

	/**
	 * The column name to retrieve. Either columnName or columnIndex will be used but never both.
	 */
	private String columnName = null;

	/**
	 * Creates a new instance of ScalarHandler. The first column will be returned from <code>handle()</code>.
	 */
	public ScalarHandler() {
		super();
	}

	/**
	 * Creates a new instance of ScalarHandler.
	 * 
	 * @param columnIndex
	 *            The index of the column to retrieve from the <code>ResultSet</code>.
	 */
	public ScalarHandler(int columnIndex) {
		this.columnIndex = columnIndex;
	}

	protected String getColumnName() {
		return columnName;
	}

	protected int getColumnIndex() {
		return columnIndex;
	}

	/**
	 * Creates a new instance of ScalarHandler.
	 * 
	 * @param columnName
	 *            The name of the column to retrieve from the <code>ResultSet</code>.
	 */
	public ScalarHandler(String columnName) {
		this.columnName = columnName;
	}

	/**
	 * Returns one <code>ResultSet</code> column as an object via the <code>ResultSet.getObject()</code> method that performs type conversions.
	 * 
	 * @return The column or <code>null</code> if there are no rows in the <code>ResultSet</code>.
	 * 
	 * @throws SQLException
	 * 
	 * @see org.apache.commons.dbutils.ResultSetHandler#handle(java.sql.ResultSet)
	 */
	public Object handle(ResultSet rs) throws SQLException {

		if (rs.next()) {
			if (this.columnName == null) {
				return rs.getObject(this.columnIndex);
			} else {
				return rs.getObject(this.columnName);
			}

		} else {
			return null;
		}
	}
}
