package net.ion.framework.db.procedure;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import net.ion.framework.db.IDBController;
import net.ion.framework.db.Page;
import net.ion.framework.db.Rows;
import net.ion.framework.db.bean.ResultSetHandler;
import net.ion.framework.db.bean.handlers.PageRowsHandler;
import net.ion.framework.db.manager.DBManager;

public abstract class AbstractQueryable implements Queryable {

	private static final long serialVersionUID = -8986472247268128909L;
	private final transient IDBController dc;
	private final String procSQL;
	private final int queryType;
	private Page page;

	private volatile boolean requestCancle = false;

	protected AbstractQueryable(IDBController dc, String procSQL, int queryType) {
		this(dc, procSQL, queryType, Page.create(100000, 1));
	}

	protected AbstractQueryable(IDBController dc, String procSQL, int queryType, Page page) {
		this.dc = dc;
		this.procSQL = procSQL;
		this.queryType = queryType;
		this.page = page;
	}

	public final int getQueryType() {
		return queryType;
	}

	public final Page getPage() {
		return page;
	}

	public final IQueryable setPage(Page page) {
		this.page = page;
		return this ;
	}

	public final String getDBType() {
		return dc.getDBManager().getDBType();
	}

	public synchronized void cancel() throws SQLException, InterruptedException {
		if (requestCancle)
			return; // balk

		requestCancle = true;
		try {
			Statement stmt = getStatement();
			stmt.cancel();
		} catch (NullPointerException ex) {
			throw new InterruptedException("can't canceled because of Query is not running, Maybe query is not initialized or is already completed");
		} catch (SQLException ex) {
			throw ex;
		} finally {
			requestCancle = false;
		}
	}

	// /....
	public abstract Statement getStatement() throws SQLException;

	public abstract Rows myQuery(Connection conn) throws SQLException;

	public abstract <T> T myHandlerQuery(Connection conn, ResultSetHandler<T> handler) throws SQLException;

	public abstract int myUpdate(Connection conn) throws SQLException;

	public <T> T execHandlerQuery(ResultSetHandler<T> handler) throws SQLException {
		Connection conn = null;
		long start = 0, end = 0;
		try {
			conn = getConnection();
			start = System.nanoTime();
			conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
			// setPage(Page.create(1000000, 1)) ; -_-??
			T result = myHandlerQuery(conn, handler);

			return result;
		} catch (UnsupportedOperationException ex) {
			throw new SQLException(ex.getMessage());
		} finally {
			end = System.nanoTime();
			getDBController().handleServant(start, end, this, IQueryable.QUERY_COMMAND);
			if (conn != null) {
				getDBManager().freeConnection(conn);
			}
		}
	}

	public final Rows execPageQuery() throws SQLException {
		return execHandlerQuery(new PageRowsHandler(this));
	}

	public final Rows execQuery() throws SQLException {
		//
		Connection conn = null;
		long start = 0, end = 0;
		try {
			conn = getConnection();
			start = System.nanoTime();
			conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
			Rows rows = myQuery(conn);
			return rows;
		} catch (UnsupportedOperationException ex) {
			throw new SQLException(ex.getMessage(), "UnsupportedOperation:");
		} finally {
			end = System.nanoTime();
			getDBController().handleServant(start, end, this, IQueryable.QUERY_COMMAND);
			if (conn != null) {
				getDBManager().freeConnection(conn);
			}
		}

	}

	public final int execUpdate() throws SQLException {
		Connection conn = null;
		int result = 0;

		long start = System.nanoTime();
		long end = 0;
		try {
			conn = getConnection();
			conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
			conn.setAutoCommit(false);

			result = myUpdate(conn);
			conn.commit();

		} catch (NullPointerException ex) {
			if (conn == null) {
				throw new SQLException("Now first, DBController.initPoolConnection();");
			}
			cleanThis();
			conn.rollback();
			throw new SQLException("Null Pointer Exception at execUpdate - " + getExceptionMessage(ex, this));
		} catch (SQLException ex) {
			cleanThis();
			conn.rollback();
			throw new SQLException(getExceptionMessage(ex, this), ex.getSQLState(), ex.getErrorCode());
		} finally {
			cleanThis();
			end = System.nanoTime();
			getDBController().handleServant(start, end, this, IQueryable.UPDATE_COMMAND);
			if (conn != null) {
				conn.setAutoCommit(true);
			}
			getDBManager().freeConnection(conn);
		}

		return result;
	}

	protected void cleanThis() {
		; // no action ;
	}

	protected boolean isArrivedRequestCancel() {
		return requestCancle;
	}

	public final IDBController getDBController() {
		if (dc == null)
			throw new NullPointerException("DBController not setted");
		return dc;
	}

	private DBManager getDBManager() {
		return getDBController().getDBManager();
	}

	private Connection getConnection() throws SQLException {
		return getDBManager().getConnection();
	}

	protected final void closeSilence(ResultSet rs, Statement stmt, Connection conn) throws SQLException {
		try {
			if (rs != null)
				rs.close();
		} catch (SQLException ignore) {
		}
		try {
			if (stmt != null)
				stmt.close();
		} catch (SQLException ignore) {
		}
		// getDBManager().freeConnection(conn);
	}

	protected final void closeSilence(Statement stmt) throws SQLException {
		try {
			if (stmt != null)
				stmt.close();
		} catch (SQLException ignore) {
			ignore.printStackTrace() ;
		}
	}

	protected void setMaxRows(Statement stmt, int maxRows) throws SQLException {
		if (getMaxRows() > 0)
			stmt.setMaxRows(getMaxRows());
	}

	public final long getCurrentModifyCount() {
		return getDBController().getModifyCount();
	}

	public String getProcSQL() {
		return procSQL;
	}

	protected String toProcedureSQL() {
		return this.getProcSQL();
	}

	public String getProcFullSQL() {
		return getProcSQL();
	}

	protected final int getMaxRows() {
		return page.getMaxCount();
	}

	protected final int getFetchSize() {
		return page.getListNum() > 100 ? DEFAULT_FETCHSIZE : page.getListNum();
		// return limitedRow == 0 ? DEFAULT_FETCHSIZE : limitedRow;
	}

	protected final String getExceptionMessage(Exception ex, Queryable queryable) {
		return ex.getMessage() + "\n\t: " + queryable.toString() + "";
	}

	// this string's format can modify at future
	public String toString() {
		if (this instanceof IParameterQueryable)
			return ((IParameterQueryable) this).getProcFullSQL();
		return getProcSQL();
	}

}
