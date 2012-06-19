package net.ion.framework.db.fake;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import net.ion.framework.db.IDBController;
import net.ion.framework.db.RepositoryException;
import net.ion.framework.db.Rows;
import net.ion.framework.db.bean.ResultSetHandler;
import net.ion.framework.db.manager.DBManager;
import net.ion.framework.db.procedure.IBatchQueryable;
import net.ion.framework.db.procedure.ICombinedUserProcedures;
import net.ion.framework.db.procedure.IParameterQueryable;
import net.ion.framework.db.procedure.IQueryable;
import net.ion.framework.db.procedure.IUserCommand;
import net.ion.framework.db.procedure.IUserCommandBatch;
import net.ion.framework.db.procedure.IUserProcedure;
import net.ion.framework.db.procedure.IUserProcedureBatch;
import net.ion.framework.db.procedure.IUserProcedures;
import net.ion.framework.db.procedure.RepositoryService;

public class FakeDBController implements IDBController {

	private String name;
	private Connection conn;
	private DBManager dbm;

	public FakeDBController(Connection conn) {
		this.conn = conn;
		this.name = "Fake Controller";
		this.dbm = new FakeDBManager(conn);
	}

	public ICombinedUserProcedures createCombinedUserProcedures(String name) {
		return getRepositoryService().createCombinedUserProcedures(this, name);
	}

	public IParameterQueryable createParameterQuery(String proc) {
		return getRepositoryService().createParameterQuery(this, proc);
	}

	public IUserCommand createUserCommand(String proc) {
		return getRepositoryService().createUserCommand(this, proc);
	}

	public IUserCommandBatch createUserCommandBatch(String procSQL) {
		return getRepositoryService().createUserCommandBatch(this, procSQL);
	}

	public IBatchQueryable createBatchParameterQuery(IDBController dc, String strSQL) {
		return getRepositoryService().createBatchParameterQuery(this, strSQL);
	}

	public IUserProcedure createUserProcedure(String proc) {
		return getRepositoryService().createUserProcedure(this, proc);
	}

	public IUserProcedureBatch createUserProcedureBatch(String procSQL) {
		return getRepositoryService().createUserProcedureBatch(this, procSQL);
	}

	public IUserProcedures createUserProcedures(String name) {
		return getRepositoryService().createUserProcedures(this, name);
	}

	public void destroySelf() throws SQLException {
		// no action
	}

	public DBManager getDBManager() {
		return this.dbm;
	}

	public String getName() {
		return name;
	}

	public void handleServant(long start, long end, IQueryable queryable, int queryCommand) {
		// no action
	}

	public long getModifyCount() {
		return Long.MIN_VALUE;
	}

	public void initSelf() throws SQLException {
		// no action
	}

	private RepositoryService getRepositoryService() {
		return dbm.getRepositoryService();
	}

	public Object execHandlerQuery(IQueryable query, ResultSetHandler handler) {
		try {
			return query.execHandlerQuery(handler);
		} catch (SQLException ex) {
			throw RepositoryException.throwIt(ex, query);
		}
	}

	public int execUpdate(IQueryable query) {
		try {
			return query.execUpdate();
		} catch (SQLException ex) {
			throw RepositoryException.throwIt(ex, query);
		}
	}

	public DatabaseMetaData getDatabaseMetaData() {
		Connection conn = null;
		try {
			conn = getDBManager().getConnection();
			return conn.getMetaData();
		} catch (SQLException ignore) {
			ignore.printStackTrace();
		} finally {
			try {
				getDBManager().freeConnection(conn);
			} catch (Exception ignore) {
				ignore.printStackTrace();
			}
		}
		return null;
	}

	public Rows getRows(IQueryable query) {
		try {
			return query.execQuery();
		} catch (SQLException e) {
			throw RepositoryException.throwIt(e, query);
		}
	}

	public Rows getRows(String query) {
		return getRows(createUserCommand(query));
	}

}
