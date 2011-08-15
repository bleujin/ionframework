package net.ion.framework.db.manager;

import java.sql.SQLException;
import java.util.Properties;

import oracle.jdbc.pool.OracleConnectionCacheManager;

public class OracleCacheReleaseDBManager extends OracleCacheDBManager {

	private OracleCacheReleaseDBManager() {
		super("jdbc:oracle:thin:@ics4db.i-on.net:8080:DB9I", "odin", "odin");
	}

	public OracleCacheReleaseDBManager(String jdbcURL, String user, String passwd) {
		super(jdbcURL, user, passwd);
	}

	public OracleCacheReleaseDBManager(String jdbcURL, String user, String passwd, int connectionLimit) {
		super(jdbcURL, user, passwd, connectionLimit);
	}

	public OracleCacheReleaseDBManager(String jdbcURL, String user, String passwd, Integer connectionLimit) {
		super(jdbcURL, user, passwd, connectionLimit.intValue());
	}

	public synchronized void initPoolConnection() throws SQLException {
		initializeConnectionCacheDataSrc();
		connMgr = OracleConnectionCacheManager.getConnectionCacheManagerInstance();
		Properties properties = new Properties();
		properties.setProperty("MinLimit", "0");
		properties.setProperty("MaxLimit", String.valueOf(this.InitConnectionCount <= 3 ? 3 : this.InitConnectionCount));
		properties.setProperty("InitialLimit", String.valueOf(this.InitConnectionCount / 4 <= 3 ? 3 : this.InitConnectionCount / 4));
		properties.setProperty("InactivityTimeout", "60");
		properties.setProperty("AbandonedConnectionTimeout", "600");
		properties.setProperty("ConnectionWaitTimeout", "120");
		properties.setProperty("ValidateConnection", "true");
		properties.setProperty("PropertyCheckInterval", "60");

		connMgr.createCache(CACHE_NAME, cpds, properties);
		System.setProperty("oracledatabasemetadata.get_lob_precision", "false");
	}

	public int getDBManagerType() {
		return DBType.OracleCacheReleaseDBManager;
	}
}
