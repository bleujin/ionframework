package net.ion.framework.db.procedure;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import net.ion.framework.db.IDBController;
import net.ion.framework.db.RepositoryException;
import net.ion.framework.db.manager.DBManager;

import org.h2.tools.Server;
import org.xml.sax.SAXException;

public class H2EmbedDBManager extends DBManager {

	private Server server = null;

	public final RepositoryService service;
	private final static String DRIVER_NAME = "org.h2.Driver";

	private HSQLBean bean;

	public H2EmbedDBManager(HSQLBean bean) {
		super(DRIVER_NAME, bean.getAddress(), bean.getUserId(), bean.getUserPwd());
		service = new H2EmbedRepositoryService(bean);
		this.bean = bean;
	}
	
	public final static H2EmbedDBManager testMem(){
		HSQLBean bean = HSQLBean.create("jdbc:h2:mem:mytest", "sa", "") ;
		return new H2EmbedDBManager(bean);
	}
	

	public H2EmbedDBManager(String filePath) {
		try {
			HSQLParser parser = new HSQLParser(filePath);
			HSQLBean bean = parser.getHQLBean();
			service = new H2EmbedRepositoryService(bean);
			this.bean = bean;
			this.set(DRIVER_NAME, bean.getAddress(), bean.getUserId(), bean.getUserPwd());
		} catch (SAXException e) {
			throw RepositoryException.throwIt(e);
		} catch (IOException e) {
			throw RepositoryException.throwIt(e);
		}
	}

	protected void myInitPool() throws SQLException {
		String[] args = new String[] { "-ssl", "true" };
		server = Server.createTcpServer(args);
		server.start();
		try {
			Class.forName(getDriverName());
		} catch (ClassNotFoundException e) {
			throw new SQLException(e.getMessage());
		}
	}

	protected void myDestroyPool() throws SQLException {
		// server.shutdown() ;
		server.stop();
	}

	public Connection getConnection() throws SQLException {
		return DriverManager.getConnection(getJdbcURL(), getUserId(), getUserPwd());
	}

	public void freeConnection(Connection conn) throws SQLException {
		if (conn != null)
			conn.close();
	}

	public int getDBManagerType() {
		return 7;
	}

	public String getDBType() {
		return "H2";
	}

	public RepositoryService getRepositoryService() {
		return this.service;
	}

	public String getJdbcURL() {
		return bean.getAddress();
	}

	public String getDriverName() {
		return DRIVER_NAME;
	}

	public String getUserPwd() {
		return bean.getUserPwd();
	}

	public String getUserId() {
		return bean.getUserId();
	}

	protected void heartbeatQuery(IDBController dc) {
		;
	}
}
