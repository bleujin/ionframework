package net.ion.framework.db.mongo;

import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.SQLException;

import net.ion.framework.db.IDBController;
import net.ion.framework.db.manager.DBManager;
import net.ion.framework.db.mongo.jdbc.MongoConnection;
import net.ion.framework.db.procedure.MongoRepositoryService;
import net.ion.framework.db.procedure.RepositoryService;

import com.mongodb.DB;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

public class MongoDBManager extends DBManager {

	private final static int MONGOTYPE = 8;
	private final static String MONGO = "mongo";

	private String host;
	private int port;
	private String dbName;

	private Mongo mongo;
	private DB db;
	private final static RepositoryService REPOSITORY_SERVICE = new MongoRepositoryService();

	private MongoDBManager(String host, int port, String dbName) {
		this.host = host;
		this.port = port;
		this.dbName = dbName;
	}

	public final static MongoDBManager create(String host, int port, String dbName) {
		return new MongoDBManager(host, port, dbName);
	}
	
	@Override
	public Connection getConnection() throws SQLException {
		return new MongoConnection(db);
	}

	@Override
	public int getDBManagerType() {
		return MONGOTYPE;
	}

	@Override
	public String getDBType() {
		return MONGO;
	}

	@Override
	public RepositoryService getRepositoryService() {
		return REPOSITORY_SERVICE;
	}

	@Override
	protected void myDestroyPool() throws Exception {
		mongo.close();
	}

	@Override
	protected void myInitPool() throws SQLException {
		try {
			this.mongo = new Mongo(host, port);
			this.db = mongo.getDB(dbName);
			Class.forName("net.ion.framework.db.mongo.jdbc.MongoDriver");
		} catch (UnknownHostException e) {
			throw new SQLException(e.getMessage());
		} catch (MongoException e) {
			throw new SQLException(e.getMessage());
		} catch (ClassNotFoundException e) {
			throw new SQLException(e.getMessage());
		}
	}

	protected void heartbeatQuery(IDBController dc) throws SQLException {
		// getRepositoryService().createUserCommand(dc, "select * from dual").execQuery();
	}
}
