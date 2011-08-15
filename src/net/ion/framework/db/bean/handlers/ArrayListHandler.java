package net.ion.framework.db.bean.handlers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import net.ion.framework.db.bean.BasicRowProcessor;
import net.ion.framework.db.bean.ResultSetHandler;
import net.ion.framework.db.bean.RowProcessor;

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

public class ArrayListHandler implements ResultSetHandler {

	/**
	 * The RowProcessor implementation to use when converting rows into Object[]s.
	 */
	private RowProcessor convert = BasicRowProcessor.instance();

	/**
	 * Creates a new instance of ArrayListHandler using a <code>BasicRowProcessor</code> for conversions.
	 */
	public ArrayListHandler() {
		super();
	}

	/**
	 * Creates a new instance of ArrayListHandler.
	 * 
	 * @param convert
	 *            The <code>RowProcessor</code> implementation to use when converting rows into Object[]s.
	 */
	public ArrayListHandler(RowProcessor convert) {
		super();
		this.convert = convert;
	}

	/**
	 * Convert each row's columns into an <code>Object[]</code> and store them in a <code>List</code> in the same order they are returned from the <code>ResultSet.next()</code> method.
	 * 
	 * @return A <code>List</code> of <code>Object[]</code>s, never <code>null</code>.
	 * 
	 * @throws SQLException
	 * @see org.apache.commons.dbutils.ResultSetHandler#handle(java.sql.ResultSet)
	 */
	public Object handle(ResultSet rs) throws SQLException {

		List<Object> result = new ArrayList<Object>();

		while (rs.next()) {
			result.add(this.convert.toArray(rs));
		}

		return result;
	}

	public Object handleString(ResultSet rs, String[] colNames) throws SQLException {

		List<Object> result = new ArrayList<Object>();

		while (rs.next()) {
			result.add(this.convert.toArray(rs, colNames));
		}

		return result;
	}

}
