package net.ion.framework.db.manager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import net.ion.framework.db.IDBController;
import net.ion.framework.db.procedure.PostgreSqlRepositoryService;
import net.ion.framework.db.procedure.RepositoryService;

public class PostSqlDBManager extends DBManager implements DBType {

	protected final static String DRIVER_NAME = "org.postgresql.Driver";

	public PostSqlDBManager(String jdbcURL, String user, String passwd) {
		super(DRIVER_NAME, jdbcURL, user, passwd);
	}

	protected void myDestroyPool() throws SQLException {
		
	}

	public Connection getConnection() throws SQLException {
		return DriverManager.getConnection(getJdbcURL(), getUserId(), getUserPwd());
	}

	public int getDBManagerType() {
		return PostgreSQLDBManager;
	}

	public String getDBType() {
		return PostgreSqlDBName;
	}

	private final static PostgreSqlRepositoryService service = new PostgreSqlRepositoryService();

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

	public static DBManager test() {
		return new PostSqlDBManager("jdbc:postgresql://127.0.0.1:5432/ics6", "postgres", "redf");
	}
}

