package net.ion.framework.db.manager;

import java.sql.Connection;
import java.sql.SQLException;

public class PostSqlPoolDBManager extends PostSqlDBManager {

	private PoolHelper pHelper;

	public PostSqlPoolDBManager(String jdbcURL, String user, String passwd) {
		this(jdbcURL, user, passwd, 15);
	}

	public PostSqlPoolDBManager(String jdbcURL, String user, String passwd, int psize) {
		super(jdbcURL, user, passwd);
		this.pHelper = new PoolHelper(getJdbcURL(), getUserId(), getUserPwd(), psize);
	}

	
	protected void myDestroyPool() throws SQLException {
		pHelper.destroyPoolConnection();
	}

	public Connection getConnection() throws SQLException {
		return pHelper.getConnection();
	}

	protected void myInitPool() throws SQLException {
		try {
			Class.forName(getDriverName());
			pHelper.initPoolConnection();
		} catch (ClassNotFoundException e) {
			throw new SQLException(e.getMessage());
		}
	}

	public int getDBManagerType() {
		return DBType.PostgreSQLPoolDBManager;
	}

	public static DBManager test() {
		return new PostSqlPoolDBManager("jdbc:postgresql://127.0.0.1:5432/postgres", "postgres", "redf");
	}
	
}
