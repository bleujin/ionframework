package net.ion.framework.db.manager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.commons.dbcp.ConnectionFactory;
import org.apache.commons.dbcp.DriverManagerConnectionFactory;
import org.apache.commons.dbcp.PoolableConnectionFactory;
import org.apache.commons.dbcp.PoolingDriver;
import org.apache.commons.pool.impl.GenericObjectPool;

/**
 * <p>
 * Title:
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

public class MSSQLCacheDBManager extends MSSQLDBManager {
	final String DriverName = "cmsformssql";

	GenericObjectPool connectPool;
	private int maxLimit = 20;

	protected MSSQLCacheDBManager() {
		this("com.microsoft.jdbc.sqlserver.SQLServerDriver", "jdbc:microsoft:sqlserver://bleujin:1433;DatabaseName=IMBC[;SelectMethod=cursor]", "ipub40",
				"ipub40");
	}

	public MSSQLCacheDBManager(String driverName, String url, String user, String passwd) {
		super(driverName, url, user, passwd);
	}

	public MSSQLCacheDBManager(String driverName, String url, String user, String passwd, int maxLimit) {
		super(driverName, url, user, passwd);
		this.maxLimit = maxLimit;
	}

	public void myInitPool() throws SQLException {
		try {
			Class.forName(getDriverName());
		} catch (ClassNotFoundException ex) {
			throw new SQLException("Driver Class Not Found", ex.toString());
		}
		connectPool = new GenericObjectPool(null);
		connectPool.setMaxActive(maxLimit);
		connectPool.setMaxIdle(maxLimit / 2);

		ConnectionFactory connectionFactory = new DriverManagerConnectionFactory(getJdbcURL(), getUserId(), getUserPwd());
		PoolableConnectionFactory poolableConnectionFactory = new PoolableConnectionFactory(connectionFactory, connectPool, null, null, true, true);
		poolableConnectionFactory.setDefaultTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);

		PoolingDriver driver = new PoolingDriver();
		driver.registerPool(DriverName, connectPool);
	}

	public Connection getConnection() throws SQLException {
		Connection conn = DriverManager.getConnection("jdbc:apache:commons:dhcp:" + DriverName);
		return conn;
	}

	public void freeConnection(Connection conn) throws SQLException {
		if (conn != null) {
			conn.close();
		}

	}

	protected void myDestroyPool() throws Exception {
		connectPool.clear();
		connectPool.close();
	}

	public int getDBManagerType() {
		return DBType.MSSQLCacheDBManager;
	}

}
