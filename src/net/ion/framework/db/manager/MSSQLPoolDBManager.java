package net.ion.framework.db.manager;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * <p>
 * Title: MSSQL Connection Pool을 사용하는 DBManager
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

public class MSSQLPoolDBManager extends MSSQLDBManager {
	public final static DBManager TEST = new MSSQLPoolDBManager("com.microsoft.jdbc.sqlserver.SQLServerDriver",
			"jdbc:microsoft:sqlserver://bleujin:1433;DatabaseName=ics5", "ics5", "ics5");

	private int maxLimit = 40;
	private PoolHelper pHelper;

	protected MSSQLPoolDBManager() {
		this("com.microsoft.jdbc.sqlserver.SQLServerDriver", "jdbc:microsoft:sqlserver://bleujin:1433;DatabaseName=ics5", "ics5", "ics5");
	}

	public MSSQLPoolDBManager(String jdbcURL, String user, String passwd) {
		super(jdbcURL, user, passwd);
	}

	public MSSQLPoolDBManager(String driverName, String jdbcURL, String user, String passwd) {
		super(driverName, jdbcURL, user, passwd);
	}

	public MSSQLPoolDBManager(String driverName, String jdbcURL, String user, String passwd, int maxLimit) {
		super(driverName, jdbcURL, user, passwd);
		this.maxLimit = maxLimit;
	}

	protected void myInitPool() throws SQLException {
		try {
			Class.forName(getDriverName());
		} catch (ClassNotFoundException ex) {
			throw new SQLException("Driver Class Not Found", ex.toString());
		}
		pHelper = new PoolHelper(getJdbcURL(), getUserId(), getUserPwd(), maxLimit);
		pHelper.initPoolConnection();
	}

	public Connection getConnection() throws SQLException {
		return pHelper.getConnection();
	}

	public void freeConnection(Connection conn) throws SQLException {
		pHelper.freeConnection(conn);
	}

	protected void myDestroyPool() throws SQLException {
		pHelper.destroyPoolConnection();
	}

	public int getDBManagerType() {
		return DBType.MSSQLPoolDBManager;
	}

}
