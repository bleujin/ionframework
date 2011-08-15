package net.ion.framework.db.procedure;

import java.io.Serializable;
import java.sql.SQLException;

import net.ion.framework.db.Page;
import net.ion.framework.db.Rows;
import net.ion.framework.db.bean.ResultSetHandler;

public interface IQueryable  extends Serializable{

	public final int QUERY_COMMAND = 1;
	public final int UPDATE_COMMAND = 2;

	public void setPage(Page page);

	public Page getPage();

	public Rows execQuery() throws SQLException;

	public int execUpdate() throws SQLException;

	public Rows execPageQuery() throws SQLException;

	public Object execHandlerQuery(ResultSetHandler handler) throws SQLException;

	public void cancel() throws SQLException, InterruptedException;

	// public Rows myQuery(Connection conn) throws SQLException ;
	// public int myUpdate(Connection conn) throws SQLException ;

	public int getQueryType();

	public String getProcSQL();

	public String getProcFullSQL();

	public String getDBType();
}
