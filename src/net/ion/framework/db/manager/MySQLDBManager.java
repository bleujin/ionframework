package net.ion.framework.db.manager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import net.ion.framework.db.IDBController;
import net.ion.framework.db.procedure.MySqlRepositoryService;
import net.ion.framework.db.procedure.RepositoryService;

public class MySQLDBManager extends DBManager implements DBType {

	protected final static String DRIVER_NAME = "com.mysql.jdbc.Driver";

	public MySQLDBManager(String jdbcURL, String user, String passwd) {
		super(DRIVER_NAME, jdbcURL, user, passwd);
	}

	protected void myDestroyPool() throws SQLException {
		
	}

	public Connection getConnection() throws SQLException {
		return DriverManager.getConnection(getJdbcURL(), getUserId(), getUserPwd());
	}

	public int getDBManagerType() {
		return DBType.MySQLDBManager;
	}

	public String getDBType() {
		return MySqlDBName;
	}

	private final static MySqlRepositoryService service = new MySqlRepositoryService();

	public RepositoryService getRepositoryService() {
		return service;
	}

	protected void myInitPool() throws SQLException {
		try {
			Class.forName(getDriverName());
		} catch (ClassNotFoundException e) {
			throw new SQLException(e.getMessage());
		}
	}

	protected void heartbeatQuery(IDBController dc) throws SQLException {
		; // no action
	}
}
