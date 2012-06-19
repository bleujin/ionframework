package net.ion.framework.db.manager;

import java.sql.Connection;
import java.sql.SQLException;

import net.ion.framework.db.IDBController;
import net.ion.framework.db.procedure.InterceptorRepositoryService;
import net.ion.framework.db.procedure.RepositoryService;

public class InterceptorDBManger extends DBManager {

	private final DBManager dbm;
	private RepositoryService rservice;

	public InterceptorDBManger(DBManager dbm) {
		this.dbm = dbm;
		this.rservice = new InterceptorRepositoryService(this, dbm);
	}

	public InterceptorDBManger(DBManager dbm, RepositoryService rservice) {
		this.dbm = dbm;
		this.rservice = rservice;
	}

	public void setRepositoryService(RepositoryService rservice) {
		this.rservice = rservice;
	}

	@Override
	public Connection getConnection() throws SQLException {
		return dbm.getConnection();
	}

	@Override
	public int getDBManagerType() {
		return dbm.getDBManagerType();
	}

	@Override
	public String getDBType() {
		return dbm.getDBType();
	}

	@Override
	public RepositoryService getRepositoryService() {
		return rservice;
	}

	@Override
	protected void myDestroyPool() throws Exception {
		dbm.myDestroyPool();
	}

	@Override
	protected void myInitPool() throws SQLException {
		dbm.myInitPool();
	}

	public boolean find(String proc) {
		return true;
	}

	protected void heartbeatQuery(IDBController dc) throws SQLException {
		dbm.heartbeatQuery(dc);
	}
}
