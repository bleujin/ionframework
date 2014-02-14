package net.ion.framework.db.manager;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Logger;

import net.ion.framework.db.IDBController;
import net.ion.framework.db.procedure.RepositoryService;
import net.ion.framework.logging.LogBroker;
import net.ion.framework.util.Debug;

/**
 * Used Pattern : Templet Method Pattern
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
public abstract class DBManager {
	final static int DEFAULT_FETCHSIZE = 10;
	private boolean isCreate = false;

	private IDBController owner;
	private String driverName = null;
	private String jdbcURL = null;
	private String userId = null;
	private String userPwd = null;
	private Logger log = LogBroker.getLogger(DBManager.class) ;
	
	public DBManager() {
		this("", "", "", "");
	}

	public DBManager(String driverName, String jdbcURL, String userId, String userPwd) {
		this.driverName = driverName;
		this.jdbcURL = jdbcURL;
		this.userId = userId;
		this.userPwd = userPwd;
	}

	private void setCreated(boolean isCreate) {
		this.isCreate = isCreate;
	}

	private boolean isCreated() {
		return isCreate;
	}

	public abstract int getDBManagerType();

	public abstract String getDBType();

	public abstract RepositoryService getRepositoryService();

	public final synchronized void initPool(IDBController dc) throws SQLException {
		if (isCreated())
			return;
		if (dc.getDBManager() == this && owner == null) {
			log.info("set Owner : " +  this);
			owner = dc; // set Owner
		}

		myInitPool();
		log.info("Init Pool : " + getClass().getName() +  ", owner:" + owner.getName() + ", created");
		setCreated(true);
		heartbeatQuery(dc);
	}

	protected abstract void myInitPool() throws SQLException;

	public final synchronized void destroyPool(IDBController dc) throws SQLException {
		try {
			if (owner != dc)
				return;
			if (!isCreate)
				return;

			myDestroyPool();
			log.info("Destroy Pool, owner:" + owner.getName());
			setCreated(false);
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}
	}

	protected abstract void myDestroyPool() throws Exception;

	public abstract Connection getConnection() throws SQLException;

	public void freeConnection(Connection conn) throws SQLException {
		if (conn != null) {
			conn.setAutoCommit(true); // default.. auto commit
			conn.close();
			// System.out.println( "-- freeConnnection() --" );
		}
	}

	public String getJdbcURL() {
		return jdbcURL;
	}

	public String getDriverName() {
		return driverName;
	}

	public String getUserPwd() {
		return userPwd;
	}

	public String getUserId() {
		return userId;
	}

	protected void set(String driverName, String jdbcURL, String userId, String userPwd) {
		this.driverName = driverName;
		this.jdbcURL = jdbcURL;
		this.userId = userId;
		this.userPwd = userPwd;
	}

	protected void heartbeatQuery(IDBController dc) throws SQLException {
		getRepositoryService().createUserCommand(dc, "select /* hearbeat query */ 1").execQuery();
	}

	public IDBController getOwner() {
		return owner;
	}

}
