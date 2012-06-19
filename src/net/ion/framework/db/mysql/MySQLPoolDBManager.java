package net.ion.framework.db.mysql;

import java.sql.Connection;
import java.sql.SQLException;

import net.ion.framework.db.manager.DBType;
import net.ion.framework.db.manager.PoolHelper;

public class MySQLPoolDBManager extends MySQLDBManager {

	private PoolHelper pHelper;

	final static MySQLPoolDBManager TEST = new MySQLPoolDBManager("jdbc:mysql://localhost/test", "bleu", "redf");

	public MySQLPoolDBManager(String jdbcURL, String user, String passwd) {
		super(jdbcURL, user, passwd);
		this.pHelper = new PoolHelper(getJdbcURL(), getUserId(), getUserPwd(), 15);
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
		return DBType.MySQLPoolDBManager;
	}

}
