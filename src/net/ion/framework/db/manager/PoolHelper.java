package net.ion.framework.db.manager;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.dbcp.ConnectionFactory;
import org.apache.commons.dbcp.DriverManagerConnectionFactory;
import org.apache.commons.dbcp.PoolableConnectionFactory;
import org.apache.commons.dbcp.PoolingDataSource;
import org.apache.commons.pool.ObjectPool;
import org.apache.commons.pool.impl.GenericObjectPool;

public class PoolHelper {
	private PoolingDataSource dataSource = null;

	private ObjectPool connectionPool = null;

	private int maxLimit;

	private String jdbcUrl;
	private String userId;
	private String userPwd;

	public PoolHelper(String jdbcUrl, String userId, String userPwd, int maxLimit) {
		this.jdbcUrl = jdbcUrl;
		this.userId = userId;
		this.userPwd = userPwd;
		this.maxLimit = maxLimit;
	}

	public void initPoolConnection() throws SQLException {

		GenericObjectPool gPool = new GenericObjectPool(null);
		gPool.setMaxActive(maxLimit);

		connectionPool = gPool;

		ConnectionFactory connectionFactory = new DriverManagerConnectionFactory(jdbcUrl, userId, userPwd);
		PoolableConnectionFactory poolableConnectionFactory = new PoolableConnectionFactory(connectionFactory, connectionPool, null, "select 1", false, true);
		poolableConnectionFactory.setDefaultTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);

		dataSource = new PoolingDataSource(connectionPool);
	}

	public Connection getConnection() throws SQLException {
		return dataSource.getConnection();
	}

	public void freeConnection(Connection conn) throws SQLException {
		if (conn != null) {
			try {
				conn.close();
			} catch (Exception ex) {
				throw new SQLException(ex.toString());
			}
		}
	}

	public void destroyPoolConnection() throws SQLException {
		try {
			connectionPool.clear();
		} catch (Exception ex) {
			throw new SQLException(ex.toString());
		}
	}

}
