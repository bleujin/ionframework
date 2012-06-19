package net.ion.framework.db.procedure;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import net.ion.framework.db.DBController;
import net.ion.framework.db.IDBController;
import net.ion.framework.db.RepositoryException;
import net.ion.framework.db.Rows;
import net.ion.framework.db.bean.ResultSetHandler;
import net.ion.framework.db.manager.DBManager;
import net.ion.framework.db.manager.DBType;
import net.ion.framework.util.StringUtil;

public class HSQLChainUserProcedure extends UserProcedure {

	private String procName;
	private String chainedSQL;
	private IUserProcedures chain;
	private ArrayList<IQueryable> querys = new ArrayList<IQueryable>();

	protected HSQLChainUserProcedure(IDBController dc, String procName, String stmtSQL) {
		super(dc, procName);
		this.procName = procName;
		this.chainedSQL = stmtSQL;
		this.chain = new DBController(dc.getDBManager()).createUserProcedures(stmtSQL);
		addProcedure(stmtSQL, chain);

	}

	private void addProcedure(String stmtSQL, IUserProcedures chain) {
		String[] queryStr = StringUtil.split(stmtSQL, ";");
		for (int i = 0; i < queryStr.length; i++) {
			HSQLUserProcedure cmd = new HSQLUserProcedure(getDBController(), procName + "_" + i, queryStr[i]);
			cmd.addNamedParam(this.getNamedParam());

			chain.add(cmd);
		}
	}

	@Override
	public Rows myQuery(Connection conn) throws SQLException {
		return chain.myQuery(conn);
	}

	@Override
	public Object myHandlerQuery(Connection conn, ResultSetHandler handler) throws SQLException {
		return chain.myHandlerQuery(conn, handler);
	}

	final String ParamPattern = "\\@\\{(\\w+)\\}";

	protected String getStmtSQL() {
		return transNamedSQL(this.chainedSQL);
	}

	public int myUpdate(Connection conn) throws SQLException {
		return chain.myUpdate(conn);
	}

	public Queryable getQuery(int i) {
		return (Queryable) querys.get(i);
	}

	public Statement getStatement() {
		throw RepositoryException.throwIt(new UnsupportedOperationException());
	}

}

class InnerHSQLDBManager extends DBManager {

	private DBManager dbm;
	private Connection conn;

	InnerHSQLDBManager(DBManager dbm, Connection conn) {
		this.dbm = dbm;
		this.conn = conn;
	}

	public int getDBManagerType() {
		return DBType.UNKNOWN;
	}

	public String getDBType() {
		return DBType.UnknownDBName;
	}

	public RepositoryService getRepositoryService() {
		try {
			return RepositoryService.makeService(this.conn);
		} catch (SQLException ex) {
			throw new IllegalArgumentException(ex.getMessage());
		}
	}

	public Connection getConnection() {
		return this.conn;
	}

	public void freeConnection(Connection conn) throws SQLException {
	}

	protected void myInitPool() {
	}

	protected void myDestroyPool() throws SQLException {
	}

}