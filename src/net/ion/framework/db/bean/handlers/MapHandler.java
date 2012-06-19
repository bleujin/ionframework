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

public class MapHandler implements ResultSetHandler {

	/**
	 * The RowProcessor implementation to use when converting rows into Maps.
	 */
	private RowProcessor convert = BasicRowProcessor.instance();

	/**
	 * Creates a new instance of MapHandler using a <code>BasicRowProcessor</code> for conversion.
	 */
	public MapHandler() {
		super();
	}

	/**
	 * Creates a new instance of MapHandler.
	 * 
	 * @param convert
	 *            The <code>RowProcessor</code> implementation to use when converting rows into Maps.
	 */
	public MapHandler(RowProcessor convert) {
		super();
		this.convert = convert;
	}

	/**
	 * Converts the first row in the <code>ResultSet</code> into a <code>Map</code>.
	 * 
	 * @return A <code>Map</code> with the values from the first row or <code>null</code> if there are no rows in the <code>ResultSet</code>.
	 * 
	 * @throws SQLException
	 * 
	 * @see org.apache.commons.dbutils.ResultSetHandler#handle(java.sql.ResultSet)
	 */
	public Object handle(ResultSet rs) throws SQLException {
		return rs.next() ? this.convert.toMap(rs) : null;
	}

}
