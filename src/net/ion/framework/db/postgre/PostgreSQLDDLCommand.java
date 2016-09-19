package net.ion.framework.db.postgre;

import java.sql.Connection;
import java.sql.SQLException;

import net.ion.framework.db.IDBController;
import net.ion.framework.db.manager.DBManager;
import net.ion.framework.db.procedure.IQueryable;

public class PostgreSQLDDLCommand extends PostgreSQLUserCommand{

	public PostgreSQLDDLCommand(IDBController dc, String sql) {
		super(dc, sql);
	}

	private DBManager getDBManager() throws SQLException {
		return getDBController().getDBManager() ;
	}
	
	public int execUpdate() throws SQLException {
		Connection conn = null;
		int result = 0;

		long start = System.nanoTime();
		long end = 0;
		try {
			conn = getDBManager().getConnection();
			conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
//			conn.setAutoCommit(true);

			result = myUpdate(conn);
//			conn.commit();

		} catch (NullPointerException ex) {
			if (conn == null) {
				throw new SQLException("Now first, DBController.initPoolConnection();");
			}
			cleanThis();
//			conn.rollback();
			throw new SQLException("Null Pointer Exception at execUpdate - " + getExceptionMessage(ex, this));
		} catch (SQLException ex) {
			cleanThis();
//			conn.rollback();
			throw new SQLException(getExceptionMessage(ex, this), ex.getSQLState(), ex.getErrorCode());
		} finally {
			cleanThis();
			end = System.nanoTime();
			try {getDBController().handleServant(start, end, this, IQueryable.UPDATE_COMMAND);} catch(Exception ex){ex.printStackTrace();};
			if (conn != null) {
				conn.setAutoCommit(true);
			}
			getDBManager().freeConnection(conn);
		}

		return result;
	}
}
