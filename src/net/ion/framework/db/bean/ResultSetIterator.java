package net.ion.framework.db.bean;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;

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

public class ResultSetIterator implements Iterator<Object> {
	/**
	 * The wrapped <code>ResultSet</code>.
	 */
	private ResultSet rs = null;

	/**
	 * The processor to use when converting a row into an Object[].
	 */
	private RowProcessor convert = BasicRowProcessor.instance();

	/**
	 * Constructor for ResultSetIterator.
	 * 
	 * @param rs
	 *            Wrap this <code>ResultSet</code> in an <code>Iterator</code>.
	 */
	public ResultSetIterator(ResultSet rs) {
		this.rs = rs;
	}

	/**
	 * Constructor for ResultSetIterator.
	 * 
	 * @param rs
	 *            Wrap this <code>ResultSet</code> in an <code>Iterator</code>.
	 * @param convert
	 *            The processor to use when converting a row into an <code>Object[]</code>. Defaults to a <code>BasicRowProcessor</code>.
	 */
	public ResultSetIterator(ResultSet rs, RowProcessor convert) {
		this.rs = rs;
		this.convert = convert;
	}

	public boolean hasNext() {
		try {
			return !rs.isLast();

		} catch (SQLException e) {
			// TODO Logging?
			// e.printStackTrace();
			return false;
		}
	}

	/**
	 * Returns the next row as an <code>Object[]</code>.
	 * 
	 * @return An <code>Object[]</code> with the same number of elements as columns in the <code>ResultSet</code>.
	 * @see java.util.Iterator#next()
	 */
	public Object next() {
		try {
			rs.next();
			return this.convert.toArray(rs);

		} catch (SQLException e) {
			// TODO Logging?
			// e.printStackTrace();
			return null;
		}
	}

	/**
	 * Deletes the current row from the <code>ResultSet</code>.
	 * 
	 * @see java.util.Iterator#remove()
	 */
	public void remove() {
		try {
			this.rs.deleteRow();

		} catch (SQLException e) {
			// TODO Logging?
			// e.printStackTrace();
		}
	}

}
