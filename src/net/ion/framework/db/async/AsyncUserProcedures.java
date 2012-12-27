package net.ion.framework.db.async;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import net.ion.framework.db.IDBController;
import net.ion.framework.db.Page;
import net.ion.framework.db.Rows;
import net.ion.framework.db.bean.ResultSetHandler;
import net.ion.framework.db.procedure.IQueryable;
import net.ion.framework.db.procedure.IUserProcedures;
import net.ion.framework.db.procedure.Queryable;
import net.ion.framework.db.procedure.UserProcedure;
import net.ion.framework.db.procedure.UserProcedures;

public class AsyncUserProcedures implements IUserProcedures {

	
	private Connection conn ;
	private final IUserProcedures inner ;
	private final IDBController dc;
	AsyncUserProcedures(IDBController dc, String proc, Connection conn) {
		this.inner = dc.createUserProcedures(proc) ;
		this.dc = dc ;
		this.conn = conn ;
	}

	public IUserProcedures add(IQueryable query) {
		inner().add(query) ;
		return this;
	}

	private IUserProcedures inner() {
		return inner;
	}

	public IUserProcedures add(Queryable[] queryArray) {
		inner().add(queryArray) ;
		return this;
	}

	public IUserProcedures add(IQueryable query, IDBController dc) {
		inner().add(query, dc) ;
		return this;
	}

	public String getParamAsString(int i, int j) {
		return inner.getParamAsString(i, j);
	}

	public long getCurrentModifyCount() {
		return inner.getCurrentModifyCount();
	}

	public IDBController getDBController() {
		return inner.getDBController();
	}

	public Statement getStatement() throws SQLException {
		return inner.getStatement();
	}

	public <T> T myHandlerQuery(Connection conn, ResultSetHandler<T> handler) throws SQLException {
		return inner.myHandlerQuery(conn, handler);
	}

	public Rows myQuery(Connection conn) throws SQLException {
		return inner.myQuery(conn);
	}

	public int myUpdate(Connection conn) throws SQLException {
		return inner.myUpdate(conn);
	}

	public void cancel() throws SQLException, InterruptedException {
		inner.cancel() ;
	}

	public String getDBType() {
		return inner.getDBType();
	}

	public Page getPage() {
		return inner.getPage();
	}

	public String getProcFullSQL() {
		return inner.getProcFullSQL();
	}

	public String getProcSQL() {
		return inner.getProcSQL();
	}

	public int getQueryType() {
		return inner.getQueryType();
	}

	public void setPage(Page page) {
		inner.setPage(page) ;
	}

	public Queryable getQuery(int i) {
		return inner.getQuery(i);
	}

	public int size() {
		return inner().size();
	}
	

	public Rows execPageQuery() throws SQLException {
		return inner.execPageQuery();
	}


//	public <T> T execHandlerQuery(ResultSetHandler<T> handler) throws SQLException {
//		// TODO Auto-generated method stub
//		return null;
//	}
//	
//	public Rows execQuery() throws SQLException {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	public int execUpdate() throws SQLException {
//		// TODO Auto-generated method stub
//		return 0;
//	}


	
	public <T> T execHandlerQuery(ResultSetHandler<T> handler) throws SQLException {
		long start = System.nanoTime();
		try {
			return inner().myHandlerQuery(conn, handler);
		} finally{
			dc.handleServant(start, System.nanoTime(), inner(), IQueryable.QUERY_COMMAND);		
		}
	}

	public Rows execQuery() throws SQLException {
		long start = System.nanoTime();
		try {
			return inner().myQuery(conn);
		} finally {
			dc.handleServant(start, System.nanoTime(), inner(), IQueryable.QUERY_COMMAND);
		}
	}

	public int execUpdate() throws SQLException {
		long start = System.nanoTime();
		try {
			return inner().myUpdate(conn);
		} finally {
			dc.handleServant(start, System.nanoTime(), inner(), IQueryable.UPDATE_COMMAND);
		}
	}
}
