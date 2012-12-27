package net.ion.framework.db.manager;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.PooledConnection;

import oracle.jdbc.pool.OracleConnectionPoolDataSource;

/**
 * <p>
 * Title: Oracle Connection Pool�� ����ϴ� DBManager
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
public class OraclePoolDBManager extends OracleDBManager {
	OracleConnectionPoolDataSource ocpds = null;
	PooledConnection pc = null;

	public OraclePoolDBManager(String jdbcURL, String user, String passwd) {
		super(jdbcURL, user, passwd);
	}
	public OraclePoolDBManager(String jdbcURL, String user, String passwd, int ignore) {
		super(jdbcURL, user, passwd);
	}


	public void myInitPool() throws SQLException {
		ocpds = new OracleConnectionPoolDataSource();
		ocpds.setURL(getJdbcURL());
		ocpds.setUser(getUserId());
		ocpds.setPassword(getUserPwd());
		
		pc = ocpds.getPooledConnection();
		// to resolve a rowset bug; http://developer.java.sun.com/developer/bugParade/bugs/4625851.html NumberFormatException
		System.setProperty("oracledatabasemetadata.get_lob_precision", "false");
	}

	public Connection getConnection() throws SQLException {
		return pc.getConnection();
	}

	public void myDestroyPool() throws SQLException {
		try {
			if (pc != null) {
				pc.close();
				System.out.println(" -- destroyPoolConnection() --");
			}
			pc = null;
		} catch (SQLException ex) {
			System.out.println(ex);
			throw ex;
		}
	}

	public int getDBManagerType() {
		return DBType.OraclePoolDBManager;
	}
}
