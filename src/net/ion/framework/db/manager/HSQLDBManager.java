package net.ion.framework.db.manager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import net.ion.framework.db.procedure.RepositoryService;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2005
 * </p>
 * <p>
 * Company: i-on
 * </p>
 * 
 * @author bleujin
 * @version 1.0
 */

public class HSQLDBManager extends DBManager {

	private final static String DRIVER_NAME = "org.hsqldb.jdbcDriver";

	static HSQLDBManager TEST_MANAGER = new HSQLDBManager("org.hsqldb.jdbcDriver", "jdbc:hsqldb:E:/dev/odin/WEB-INF/data/mem", "sa", "");
	public static HSQLDBManager TEST_MANAGER2 = new HSQLDBManager("org.hsqldb.jdbcDriver", "jdbc:hsqldb:hsql://61.250.201.190/xdb", "sa", "");
	private final static RepositoryService service = RepositoryService.HSQL;

	public HSQLDBManager(String driverName, String jdbcURL, String userId, String userPwd) {
		super(driverName, jdbcURL, userId, userPwd);
	}

	public HSQLDBManager(String jdbcURL, String userId, String userPwd) {
		super(DRIVER_NAME, jdbcURL, userId, userPwd);
	}

	protected void myDestroyPool() throws SQLException {
	}

	public Connection getConnection() throws SQLException {
		try {
			return DriverManager.getConnection(getJdbcURL(), getUserId(), getUserPwd());
		} catch (SQLException ex1) {
			throw ex1;
		}
	}

	public int getDBManagerType() {
		return DBType.HSQLDBManager;
	}

	public String getDBType() {
		return DBType.HSQLDBName;
	}

	public RepositoryService getRepositoryService() {
		return service;
	}

	protected void myInitPool() throws SQLException {
		try {
			Class.forName(getDriverName());
		} catch (ClassNotFoundException ex) {
			throw new SQLException(ex.getMessage());
		}
	}

}
