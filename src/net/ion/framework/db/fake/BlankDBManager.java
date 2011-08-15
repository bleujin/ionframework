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
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void myInitPool() throws SQLException {
		// TODO Auto-generated method stub
		
	}

	protected void heartbeatQuery(IDBController dc) throws SQLException {
		;
	}
}
