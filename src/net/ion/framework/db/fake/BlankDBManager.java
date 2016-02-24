package net.ion.framework.db.fake;

import java.sql.Connection;
import java.sql.SQLException;

import net.ion.framework.db.IDBController;
import net.ion.framework.db.manager.DBManager;
import net.ion.framework.db.procedure.RepositoryService;

public class BlankDBManager extends DBManager{

	@Override
	public Connection getConnection() throws SQLException {
		return BlankConnection.Blank  ;
	}

	@Override
	public int getDBManagerType() {

		return 0;
	}

	@Override
	public String getDBType() {
		return "blank";
	}

	@Override
	public RepositoryService getRepositoryService() {
		return RepositoryService.ORACLE;
	}

	@Override
	protected void myDestroyPool() throws Exception {

		
	}

	@Override
	protected void myInitPool() throws SQLException {

		
	}

	protected void heartbeatQuery(IDBController dc) throws SQLException {
		;
	}
}
