package net.ion.framework.db.bean.handlers;

import java.sql.ResultSet;
import java.sql.SQLException;

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

public class ArrayHandler implements ResultSetHandler {
	private RowProcessor convert = BasicRowProcessor.instance();

	/**
	 * Creates a new instance of ArrayHandler using a <code>BasicRowProcessor</code> for conversion.
	 */
	public ArrayHandler() {
		super();
	}

	/**
	 * Creates a new instance of ArrayHandler.
	 * 
	 * @param convert
	 *            The <code>RowProcessor</code> implementation to use when converting rows into arrays.
	 */
	public ArrayHandler(RowProcessor convert) {
		super();
		this.convert = convert;
	}

	/**
	 * Places the column values from the first row in an <code>Object[]</code>.
	 * 
	 * @return An Object[] or <code>null</code> if there are no rows in the <code>ResultSet</code>.
	 * 
	 * @throws SQLException
	 * @see org.apache.commons.dbutils.ResultSetHandler#handle(java.sql.ResultSet)
	 */
	public Object handle(ResultSet rs) throws SQLException {
		return rs.next() ? this.convert.toArray(rs) : null;
	}

}
