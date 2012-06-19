package net.ion.framework.db.procedure;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

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
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author bleujin
 * @version 1.0
 */

// Bridge Pattern..

public class TimeOutQuery extends AbstractQueryable {

	private final Queryable query;
	private final int timeOutSec;

	public TimeOutQuery(Queryable query, int timeOutSec) {
		super(query.getDBController(), query.getProcSQL(), query.getQueryType());
		this.query = query;
		this.timeOutSec = timeOutSec;
	}

	@Override
	public Rows myQuery(Connection conn) throws SQLException {
		new TimeOutMonitor(query, timeOutSec).start();
		return query.myQuery(conn);
	}

	@Override
	public int myUpdate(Connection conn) throws SQLException {
		new TimeOutMonitor(query, timeOutSec).start();
		return query.myUpdate(conn);
	}

	@Override
	public Object myHandlerQuery(Connection conn, ResultSetHandler handler) throws SQLException {
		throw new UnsupportedOperationException();
	}

	public Object execHandlerQuery(ResultSetHandler handler) throws SQLException {
		throw new UnsupportedOperationException();
	}

	public Statement getStatement() throws SQLException {
		throw new SQLException("can't excecute");
	}

	public String toString() {
		return query.toString();
	}

	public void cancel() throws SQLException, InterruptedException {
		query.cancel();
	}

	public String getProcFullSQL() {
		return query.getProcFullSQL();
	}

	public String getProcSQL() {
		return query.getProcSQL();
	}
}

class TimeOutMonitor extends Thread {

	private final IQueryable query;
	private final int timeOutSec;

	public TimeOutMonitor(IQueryable query, int timeOutSec) {
		this.query = query;
		this.timeOutSec = timeOutSec;
	}

	public void run() {
		try {
			Thread.sleep(timeOutSec * 1000);
			query.cancel();
		} catch (Exception ignore) {
		}
	}
}
