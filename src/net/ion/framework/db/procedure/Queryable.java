package net.ion.framework.db.procedure;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import net.ion.framework.db.IDBController;
import net.ion.framework.db.Page;
import net.ion.framework.db.Rows;
import net.ion.framework.db.bean.ResultSetHandler;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author bleujin
 * @version 1.0
 */
public interface Queryable extends IQueryable {

	public static Queryable Fake = new Queryable(){

		public long getCurrentModifyCount() {
			throw new IllegalStateException("null query") ;
		}

		public IDBController getDBController() {
			throw new IllegalStateException("null query") ;
		}

		public Statement getStatement() throws SQLException {
			throw new IllegalStateException("null query") ;
		}

		public Object myHandlerQuery(Connection conn, ResultSetHandler handler) throws SQLException {
			throw new IllegalStateException("null query") ;
		}

		public Rows myQuery(Connection conn) throws SQLException {
			throw new IllegalStateException("null query") ;
		}

		public int myUpdate(Connection conn) throws SQLException {
			throw new IllegalStateException("null query") ;
		}

		public void cancel() throws SQLException, InterruptedException {
			throw new IllegalStateException("null query") ;
		}

		public Object execHandlerQuery(ResultSetHandler handler) throws SQLException {
			throw new IllegalStateException("null query") ;
		}

		public Rows execPageQuery() throws SQLException {
			throw new IllegalStateException("null query") ;
		}

		public Rows execQuery() throws SQLException {
			throw new IllegalStateException("null query") ;
		}

		public int execUpdate() throws SQLException {
			throw new IllegalStateException("null query") ;
		}

		public String getDBType() {
			throw new IllegalStateException("null query") ;
		}

		public Page getPage() {
			throw new IllegalStateException("null query") ;
		}

		public String getProcFullSQL() {
			throw new IllegalStateException("null query") ;
		}

		public String getProcSQL() {
			throw new IllegalStateException("null query") ;
		}

		public int getQueryType() {
			throw new IllegalStateException("null query") ;
		}

		public void setPage(Page page) {
			throw new IllegalStateException("null query") ;
		}
		
	} ;
	
	public final static int DEFAULT_FETCHSIZE = 10;
	public final static int DEFAULT_QUERY_TIMEOUT = 0;

	// protected abstract Statement getStatement() ;
	//    
	// protected abstract Rows myQuery(Connection conn) throws SQLException ;
	// protected abstract Object myHandlerQuery(Connection conn, ResultSetHandler handler) throws SQLException ;
	// protected abstract int myUpdate(Connection conn) throws SQLException;

	public long getCurrentModifyCount();

	public Rows myQuery(Connection conn) throws SQLException;

	public Object myHandlerQuery(Connection conn, ResultSetHandler handler) throws SQLException;

	public int myUpdate(Connection conn) throws SQLException;

	public IDBController getDBController();

	public Statement getStatement() throws SQLException;
}
