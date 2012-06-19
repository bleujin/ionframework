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

public class BeanListHandler implements ResultSetHandler {
	/**
	 * The Class of beans produced by this handler.
	 */
	private Class<?> type = null;

	/**
	 * The RowProcessor implementation to use when converting rows into beans.
	 */
	private RowProcessor convert = BasicRowProcessor.instance();

	/**
	 * Creates a new instance of BeanListHandler.
	 * 
	 * @param type
	 *            The Class that objects returned from <code>handle()</code> are created from.
	 */
	public BeanListHandler(Class<?> type) {
		this.type = type;
	}

	/**
	 * Creates a new instance of BeanListHandler.
	 * 
	 * @param type
	 *            The Class that objects returned from <code>handle()</code> are created from.
	 * @param convert
	 *            The <code>RowProcessor</code> implementation to use when converting rows into beans.
	 */
	public BeanListHandler(Class<?> type, RowProcessor convert) {
		this.type = type;
		this.convert = convert;
	}

	/**
	 * Convert the <code>ResultSet</code> rows into a <code>List</code> of beans with the <code>Class</code> given in the constructor.
	 * 
	 * @return A <code>List</code> of beans (one for each row), never <code>null</code>.
	 * 
	 * @throws SQLException
	 * @see org.apache.commons.dbutils.ResultSetHandler#handle(java.sql.ResultSet)
	 */
	public Object handle(ResultSet rs) throws SQLException {
		return this.convert.toBeanList(rs, type);
	}

}
