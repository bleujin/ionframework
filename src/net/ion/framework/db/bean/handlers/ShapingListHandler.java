package net.ion.framework.db.bean.handlers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import net.ion.framework.db.bean.ResultSetHandler;
import net.ion.framework.db.bean.shaping.IsParent;


public class ShapingListHandler implements ResultSetHandler<List<IsParent>> {

	private static final long serialVersionUID = -687833316336360722L;
	private Class<?> parent;
	private Class<?> child;

	private BasicShapingRowProcessor convert = new BasicShapingRowProcessor();

	public ShapingListHandler(Class<?> parent, Class<?> child) {
		this.parent = parent;
		this.child = child;
	}

	public List<IsParent> handle(ResultSet rs) throws SQLException {
		return this.convert.toShapeList(rs, this.parent, this.child);
	}
}
