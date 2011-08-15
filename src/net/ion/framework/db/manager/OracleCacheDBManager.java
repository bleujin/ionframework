package net.ion.framework.db.manager;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Properties;
import java.util.Random;

import net.ion.framework.util.Debug;

import oracle.jdbc.pool.OracleConnectionCacheManager;
import oracle.jdbc.pool.OracleDataSource;

public class OracleCacheDBManager extends OracleDBManager {
	protected final String CACHE_NAME = OracleCacheDBManager.class.getCanonicalName() + (new Random()).nextInt()
			+ String.valueOf(Calendar.getInstance().getTimeInMillis());
	protected OracleConnectionCacheManager connMgr = null;
	protected OracleDataSource cpds = null;
	final int InitConnectionCount;

	// final private int MAX_TRY = 5;

	protected OracleCacheDBManager() {
		this("jdbc:oracle:thin:@221.148.247.24:1521:cassora", "odin", "odin", 20);
	}

	public OracleCacheDBManager(String jdbcURL, String user, String passwd) {
		this(jdbcURL, user, passwd, 20);
	}

	public OracleCacheDBManager(String jdbcURL, String user, String passwd, int connectionLimit) {
		super(jdbcURL, user, passwd);
		this.InitConnectionCount = connectionLimit;
	}

	public OracleCacheDBManager(String jdbcURL, String user, String passwd, Integer connectionLimit) {
		this(jdbcURL, user, passwd, connectionLimit.intValue());
	}

	public synchronized void myInitPool() throws SQLException {
		this.initializeConnectionCacheDataSrc();
		connMgr = OracleConnectionCacheManager.getConnectionCacheManagerInstance();
		Properties properties = new Properties();
		properties.setProperty("MinLimit", "0");
		properties.setProperty("MaxLimit", String.valueOf(this.InitConnectionCount <= 3 ? 3 : this.InitConnectionCount));
		properties.setProperty("InitialLimit", String.valueOf(this.InitConnectionCount / 4 <= 3 ? 3 : this.InitConnectionCount / 4));
		properties.setProperty("InactivityTimeout", "900");
		properties.setProperty("AbandonedConnectionTimeout", "600");
		properties.setProperty("ConnectionWaitTimeout", "120");
		properties.setProperty("ValidateConnection", "true");
		properties.setProperty("PropertyCheckInterval", "600");

		connMgr.createCache(CACHE_NAME, cpds, properties);
		System.setProperty("oracledatabasemetadata.get_lob_precision", "false");
	}

	protected void initializeConnectionCacheDataSrc() throws SQLException {
		try {

			/* Initialize the Datasource */
			cpds = new OracleDataSource();

			/*
			 * Configure the Datasource with proper values of Host Name, Sid, Port, Driver type, User Name and Password
			 */
			this.configDSConnection();

			/* Enable cahcing */
			cpds.setConnectionCachingEnabled(true);

			/* Set the cache name */
			cpds.setConnectionCacheName(CACHE_NAME);

		} catch (SQLException sqlEx) { /* Catch SQL Errors */
			sqlEx.printStackTrace();
			throw new SQLException("SQL Errors = " + sqlEx.toString());
		} catch (Exception ex) { /* Catch Generic Errors */
			ex.printStackTrace();
			throw new SQLException("Generic Errors = " + ex.toString());
		}
	}

	private void configDSConnection() throws SQLException {
		cpds.setDriverType("thin");
		cpds.setNetworkProtocol("tcp");
		cpds.setURL(getJdbcURL());

		cpds.setUser(getUserId());
		cpds.setPassword(getUserPwd());
		Properties prop = new Properties() ;
		prop.put("sec_case_sensitive_logon", "false") ;
		cpds.setConnectionProperties(prop) ;
		
	}

	public int getActiveSize() throws Exception {
		try {
			return connMgr.getNumberOfActiveConnections(CACHE_NAME);
		} catch (SQLException sqlEx) {
			sqlEx.printStackTrace();
			throw new Exception("SQL Error while getting the none of active " + " connections");
		}
	}

	public int getCacheSize() throws Exception {
		return connMgr.getNumberOfActiveConnections(CACHE_NAME) + connMgr.getNumberOfAvailableConnections(CACHE_NAME);
	}

	public synchronized void myDestroyPool() throws SQLException {
		if (cpds != null) {
			cpds.close();
		}
	}

	public Connection getConnection() throws SQLException {
		return cpds.getConnection();
	}

	// public final static DBManager TEST = new Oracle9iCacheDBManager("jdbc:oracle:thin:@ion-dev-ora9i:1521:rv", "ics", "dkdldhs" ,5) ;
	//
	// OracleConnectionCacheImpl ocpds = null;
	// final int InitConnectionCount ;
	//
	// // Only For Test
	// protected OracleCacheDBManager() {
	// this("jdbc:oracle:thin:@221.148.247.24:1521:cassora", "odin", "odin" ,20) ;
	// }
	//
	// public OracleCacheDBManager( String jdbcURL, String user, String passwd ) {
	// this(jdbcURL, user, passwd, 20) ;
	// }
	//
	// public OracleCacheDBManager( String jdbcURL, String user, String passwd, int connectionLimit ) {
	// super(jdbcURL, user, passwd);
	// this.InitConnectionCount = connectionLimit ;
	// }
	//
	// public void initPoolConnection() throws SQLException {
	// ocpds = new OracleConnectionCacheImpl();
	// ocpds.setURL( getJdbcURL() );
	// ocpds.setUser( getUserId() );
	// ocpds.setPassword( getUserPwd() );
	// ocpds.setMaxLimit( this.InitConnectionCount );
	// setCreated(true);
	// // to resolve a rowset bug; http://developer.java.sun.com/developer/bugParade/bugs/4625851.html
	// // NumberFormatException
	// System.setProperty( "oracledatabasemetadata.get_lob_precision", "false" ) ;
	//
	// }
	//
	// public Connection getConnection() throws SQLException {
	// return ocpds.getConnection();
	// }
	//
	// public void destroyPoolConnection() throws SQLException {
	// if ( ocpds != null )
	// ocpds.close();
	// ocpds = null;
	// setCreated(false);
	// }

	public int getDBManagerType() {
		return DBType.OracleCacheDBManager;
	}
}
