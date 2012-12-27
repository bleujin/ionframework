package net.ion.framework.db.bean;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface RowProcessor {
	/**
	 * Create an <code>Object[]</code> from the column values in one <code>ResultSet</code> row. The <code>ResultSet</code> should be positioned on a valid row before passing it to this method. Implementations of this method must not alter the row
	 * position of the <code>ResultSet</code>.
	 */
	public Object[] toArray(ResultSet rs) throws SQLException;

	/**
	 * Create a JavaBean from the column values in one <code>ResultSet</code> row. The <code>ResultSet</code> should be positioned on a valid row before passing it to this method. Implementations of this method must not alter the row position of the
	 * <code>ResultSet</code>.
	 */
	public <T> T toBean(ResultSet rs, Class<T> type) throws SQLException;

	/**
	 * Create a <code>List</code> of JavaBeans from the column values in all <code>ResultSet</code> rows. <code>ResultSet.next()</code> should <strong>not</strong> be called before passing it to this method.
	 * 
	 * @return A <code>List</code> of beans with the given type in the order they were returned by the <code>ResultSet</code>.
	 */
	public <T> List<T> toBeanList(ResultSet rs, Class<T> type) throws SQLException;

	/**
	 * Create a <code>Map</code> from the column values in one <code>ResultSet</code> row. The <code>ResultSet</code> should be positioned on a valid row before passing it to this method. Implementations of this method must not alter the row position
	 * of the <code>ResultSet</code>.
	 */
	public Map<String, Object> toMap(ResultSet rs) throws SQLException;

	public Object[] toArray(ResultSet rs, String[] colNames) throws SQLException;

	public Map<String, Object> toStringMap(ResultSet rs, String[] attributeNames, String[] columnNames) throws SQLException;

}
