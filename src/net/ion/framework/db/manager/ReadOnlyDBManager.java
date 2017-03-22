package net.ion.framework.db.manager;

import java.sql.Connection;
import java.sql.SQLException;

import net.ion.framework.db.IDBController;
import net.ion.framework.db.procedure.IQueryable;
import net.ion.framework.db.procedure.IUserCommand;
import net.ion.framework.db.procedure.RepositoryService;

public class ReadOnlyDBManager extends DBManager{

	
	private final DBManager dbm;
	private Connection conn ;
	private IQueryable setUpQuery;
	
	public ReadOnlyDBManager(DBManager dbm){
		this.dbm = dbm ;
	}
	
	
	
	@Override
	public int getDBManagerType() {
		return dbm.getDBManagerType() ;
	}

	@Override
	public String getDBType() {
		return dbm.getDBType();
	}

	@Override
	public RepositoryService getRepositoryService() {
		return dbm.getRepositoryService();
	}

	@Override
	protected void myInitPool() throws SQLException {
		dbm.myInitPool();
		if (conn == null) {
			conn = dbm.getConnection() ; // single
			conn.setAutoCommit(false);
			if (setUpQuery != null) setUpQuery.execUpdate() ;
		}
	}

	@Override
	protected void myDestroyPool() throws Exception {
		conn.rollback(); 
		dbm.freeConnection(conn);
		dbm.myDestroyPool(); 
	}

	@Override
	public Connection getConnection() throws SQLException {
		return conn;
	}
	
	@Override
	public void freeConnection(Connection conn) throws SQLException {
	}
	
	@Override
	public void commit(Connection conn){
	}

	@Override
	public void rollback(Connection conn){
	}

	@Override
	protected void heartbeatQuery(IDBController dc) throws SQLException {
		dbm.heartbeatQuery(dc);
	}

	public ReadOnlyDBManager setUp(IQueryable query) {
		this.setUpQuery = query ;
		return this ;
	}

	


}
