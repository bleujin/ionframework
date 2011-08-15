package net.ion.framework.db.procedure;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import net.ion.framework.db.IDBController;
import net.ion.framework.db.ObjectWrapper;
import net.ion.framework.db.Rows;
import net.ion.framework.db.bean.ResultSetHandler;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2005
 * </p>
 * <p>
 * Company: i-on
 * </p>
 * 
 * @author bleujin
 * @version 1.0
 */

public class EmptyQueryable extends AbstractQueryable {

	private String name;

	public EmptyQueryable(IDBController dc, String name) {
		super(dc, name, QueryType.EMPTY);
	}

	protected ObjectWrapper execHandlerQuery(ResultSetHandler rsh, int limitRow) {
		throw new UnsupportedOperationException("EmptyQuery cannot execQuery");
	}

	public Rows myQuery(Connection conn) {
		throw new UnsupportedOperationException("EmptyQuery cannot execQuery");
	}

	public int myUpdate(Connection conn) {
		return 1; // none action...
	}

	public Statement getStatement() {
		return null;
	}

	@Override
	public Object myHandlerQuery(Connection conn, ResultSetHandler handler) throws SQLException {
		return null;
	}
}
