package net.ion.framework.db.manager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import net.ion.framework.db.procedure.RepositoryService;

/**
 * <p>
 * Title: MS의 MSSQL2000을 대상으로 하는 기본 DBManager
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: I-ON Communications
 * </p>
 * 
 * @author bleujin
 * @version 1.0
 */
public class MSSQLDBManager extends DBManager {
	final static String D_DRIVER_NAME = "com.microsoft.jdbc.sqlserver.SQLServerDriver";
	private final static RepositoryService service = RepositoryService.MSSQL;
	public final static MSSQLDBManager TEST_MANAGER = new MSSQLDBManager();

	// Only For Test
	protected MSSQLDBManager() {
		this(D_DRIVER_NAME, "jdbc:microsoft:sqlserver://novision:1433;DatabaseName=test", "bleu", "redf");
	}

	public MSSQLDBManager(String jdbcURL, String user, String passwd) {
		this(D_DRIVER_NAME, jdbcURL, user, passwd);
	}

	public MSSQLDBManager(String driverName, String jdbcURL, String userId, String userPwd) {
		super(driverName, jdbcURL, userId, userPwd);
	}

	public MSSQLDBManager(String driverName, String jdbcURL, String user, String passwd, int limitConnection) {
		this(driverName, jdbcURL, user, passwd);
	}

	public Connection getConnection() throws SQLException {
		Connection conn = null;
		conn = DriverManager.getConnection(getJdbcURL(), getUserId(), getUserPwd());
		return conn;
	}

	public int getDBManagerType() {
		return DBType.MSSQLDBManager;
	}

	protected void myDestroyPool() throws Exception {
	}

	protected void myInitPool() throws SQLException {
		try {
			Class.forName(getDriverName());
		} catch (ClassNotFoundException ex) {
			throw new SQLException("SQL Exception thrown in getConnection() method of MSSQLDBManager Class\n" + ex.toString());
		}
	}

	public String getDBType() {
		return DBType.MSSQLDBName;
	}

	public RepositoryService getRepositoryService() {
		return service;
	}

}
