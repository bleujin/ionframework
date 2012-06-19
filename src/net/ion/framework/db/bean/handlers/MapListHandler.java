package net.ion.framework.db.bean.handlers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.ion.framework.db.bean.BasicRowProcessor;
import net.ion.framework.db.bean.ResultSetHandler;
import net.ion.framework.db.bean.RowProcessor;

public class MapListHandler implements ResultSetHandler {

	/**
	 * The RowProcessor implementation to use when converting rows into Maps.
	 */
	private RowProcessor convert = BasicRowProcessor.instance();

	/**
	 * Creates a new instance of MapListHandler using a <code>BasicRowProcessor</code> for conversion.
	 */
	public MapListHandler() {
		super();
	}

	/**
	 * Creates a new instance of MapListHandler.
	 * 
	 * @param convert
	 *            The <code>RowProcessor</code> implementation to use when converting rows into Maps.
	 */
	public MapListHandler(RowProcessor convert) {
		super();
		this.convert = convert;
	}

	/**
	 * Converts the <code>ResultSet</code> rows into a <code>List</code> of <code>Map</code> objects.
	 * 
	 * @return A <code>List</code> of <code>Map</code>s, never null.
	 * 
	 * @throws SQLException
	 * 
	 * @see org.apache.commons.dbutils.ResultSetHandler#handle(java.sql.ResultSet)
	 */
	public Object handle(ResultSet rs) throws SQLException {

		List<Map<?, ?>> results = new ArrayList<Map<?, ?>>();

		while (rs.next()) {
			results.add(this.convert.toMap(rs));
		}

		return results;
	}

	public Object handleString(ResultSet rs, String[] attributeNames, String[] columnNames) throws SQLException {
		List<Map<?, ?>> results = new ArrayList<Map<?, ?>>();

		while (rs.next()) {
			results.add(this.convert.toStringMap(rs, attributeNames, columnNames));
		}

		return results;
	}

}
