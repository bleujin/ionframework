package net.ion.framework.db.bean.handlers;

import java.sql.ResultSet;
import java.sql.SQLException;

import net.ion.framework.db.bean.ResultSetHandler;

/**
 * @author bleujin
 * @version 1.0
 */
public class ShapingListHandler implements ResultSetHandler {
	private Class<?> parent;
	private Class<?> child;

	private BasicShapingRowProcessor convert = new BasicShapingRowProcessor();

	public ShapingListHandler(Class<?> parent, Class<?> child) {
		this.parent = parent;
		this.child = child;
	}

	public Object handle(ResultSet rs) throws SQLException {
		return this.convert.toShapeList(rs, this.parent, this.child);
	}
}
