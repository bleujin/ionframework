package net.ion.framework.db.procedure;

import java.sql.Connection;
import java.sql.SQLException;

import net.ion.framework.db.manager.DBManager;
import net.ion.framework.db.manager.PoolHelper;
import net.ion.framework.parse.gson.JsonParser;

public class H2EmbedPoolDBManager extends H2EmbedDBManager {

	private int maxLimit = 40;

	private PoolHelper pHelper;

	public final static H2EmbedPoolDBManager test(){
		String config = 
			"{address:'jdbc:h2:mem:test', name:'h2db', userId:'sa', userPwd:'sa', " +
			"procedures:{'emp@select':'select * from emp', 'emp@createtable':'create table if not exists emp(empno int, ename varchar(40))', 'emp@insert(?,?)':'insert into emp values(:empno, :ename)'} " +
			"}" ;
	
		HSQLBean hb = JsonParser.fromString(config).getAsJsonObject().getAsObject(HSQLBean.class) ;
		return new H2EmbedPoolDBManager(hb) ;
	}
	
	public H2EmbedPoolDBManager(HSQLBean bean) {
		super(bean);
	}
	
	public H2EmbedPoolDBManager(String configFilePath) {
		super(configFilePath);
	}

	public H2EmbedPoolDBManager(String configFilePath, int maxLimit) {
		super(configFilePath);
		this.maxLimit = maxLimit;
	}

	protected void myInitPool() throws SQLException {
		try {
			Class.forName(getDriverName());
		} catch (ClassNotFoundException ex) {
			throw new SQLException("Driver Class Not Found", ex.toString());
		}
		pHelper = new PoolHelper(getJdbcURL(), getUserId(), getUserPwd(), maxLimit);
		pHelper.initPoolConnection();
	}

	public Connection getConnection() throws SQLException {
		return pHelper.getConnection();
	}

	public void freeConnection(Connection conn) throws SQLException {
		pHelper.freeConnection(conn);
	}

	protected void myDestroyPool() throws SQLException {
		pHelper.destroyPoolConnection();
	}

}