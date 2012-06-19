package net.ion.framework.db.manager;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import net.ion.framework.db.procedure.RepositoryService;
import net.ion.framework.db.procedure.StdOracleRepositoryService;

public class WASDBManager extends DBManager {

	private String datasourceName = null; // web logic DataSource Name
	private String dBName;
	private DataSource ds;
	private Context ctx;

	public WASDBManager(String dataSourceName, String dBName) {
		super("", "", "", "");

		this.datasourceName = dataSourceName;
		this.dBName = dBName;
	}

	protected void myInitPool() throws SQLException {
		try {
        	ctx = new InitialContext();
        	Context envContext = (Context) ctx.lookup("java:/comp/env");
        	ds = (DataSource)envContext.lookup(datasourceName);
		} catch (NamingException ex) {
			new SQLException(ex.getMessage());
		}

	}

    public Connection getConnection() throws SQLException {
        return ds.getConnection();
    }

    public int getDBManagerType() {
        return DBType.WASDBManager;
    }


	protected void myDestroyPool() throws SQLException {
		try {
			ctx.unbind(datasourceName);
		} catch (NamingException ignore) {
			ignore.printStackTrace();
		}
		ds = null;

		try {
			ctx.close();
		} catch (NamingException ignore) {
			ignore.printStackTrace();
		}
        ds = null ;
	}

    public String getDBType() {
        if (dBName.equals(DBType.OracleDBName)) return DBType.OracleDBName;
        else if (dBName.equals(DBType.MSSQLDBName)) return DBType.MSSQLDBName ;
        else
            throw new IllegalArgumentException("not supported db type") ;
    }

    public RepositoryService getRepositoryService() {
        if (dBName.equals(DBType.OracleDBName)) return StdOracleRepositoryService.SELF;
        else if (dBName.equals(DBType.MSSQLDBName)) return RepositoryService.MSSQL ;
        else
            throw new IllegalArgumentException("not supported db type") ;
    }

}
