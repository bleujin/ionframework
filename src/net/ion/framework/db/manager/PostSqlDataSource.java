package net.ion.framework.db.manager;

import java.sql.Connection;
import java.sql.SQLException;

import net.ion.framework.util.StringUtil;

import org.postgresql.jdbc3.Jdbc3PoolingDataSource;

public class PostSqlDataSource extends PostSqlDBManager {

	private Jdbc3PoolingDataSource source;
	private int psize;

	public PostSqlDataSource(String jdbcURL, String user, String passwd) {
		this(jdbcURL, user, passwd, 15);
	}

	public PostSqlDataSource(String jdbcURL, String user, String passwd, int psize) {
		super(jdbcURL, user, passwd);
		this.psize = psize ;
	}

	
	protected void myDestroyPool() throws SQLException {
		source.close(); 
	}

	public Connection getConnection() throws SQLException {
		return source.getConnection();
	}

	protected void myInitPool() throws SQLException {
		try {
			
			Class.forName(getDriverName());
			this.source = new Jdbc3PoolingDataSource();
			source.setDataSourceName("PostSqlDataSource$" + this.hashCode());
			source.setServerName(StringUtil.substringBetween(getJdbcURL(), "://", "/"));
			source.setDatabaseName(StringUtil.substringAfterLast(getJdbcURL(), "/"));
			source.setUser(getUserId());
			source.setPassword(getUserPwd());
			source.setMaxConnections(this.psize);
			
		} catch (ClassNotFoundException e) {
			throw new SQLException(e.getMessage());
		}
	}

	public int getDBManagerType() {
		return DBType.PostgreSQLPoolDBManager;
	}

	public static DBManager test() {
		return new PostSqlDataSource("jdbc:postgresql://127.0.0.1:5432/postgres", "postgres", "redf");
	}
	
}