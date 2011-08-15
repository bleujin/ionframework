package net.ion.framework.db.procedure;

import java.io.ObjectStreamException;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.ion.framework.db.IDBController;
import net.ion.framework.db.Rows;
import net.ion.framework.db.bean.ResultSetHandler;
import net.ion.framework.db.manager.DBManager;
import net.ion.framework.db.manager.DBType;
import net.ion.framework.db.xa.XAUserProcedure;

public class CombinedUserProcedures extends AbstractQueryable implements ICombinedUserProcedures {

	private List<Query> querys = new ArrayList<Query>();
	private Map<String, Object> resultMap = new HashMap<String, Object>();
	private IQueryable currQuery = null;

	public CombinedUserProcedures(IDBController dc, String name) {
		super(dc, name, QueryType.USER_PROCEDURES);
	}

	public ICombinedUserProcedures add(String strSQL, String name, int type) {
		add(getDBController().createParameterQuery(strSQL), name, type);
		return this;
	}

	public ICombinedUserProcedures add(IQueryable query) {
		return this.add(query, query.getProcSQL(), UPDATE_COMMAND);
	}

	public ICombinedUserProcedures add(IQueryable query, int type) {
		return this.add(query, query.getProcSQL(), type);
	}

	public ICombinedUserProcedures add(IQueryable query, String name, int type) {
		if (this == query)
			throw new IllegalArgumentException("exception.framework.db.combined_procedures.infinite_loop");
		if (query instanceof XAUserProcedure)
			throw new IllegalArgumentException("exception.framework.db.combined_procedures.not_supported_type");
		querys.add(new Query(query, name, type));
		return this;
	}
	
	public List<Query> getQuerys() {
		return Collections.unmodifiableList(querys);
	}
	
	public Rows myQuery(Connection conn) throws SQLException {
		IUserProcedures upts = getDBController().createUserProcedures(this.getProcSQL());
		Query[] query = querys.toArray(new Query[0]);
		for (int i = 0; i < query.length; i++) {
			upts.add(query[i].getQuery());
		}
		return upts.execQuery();
	}


	public int myUpdate(Connection conn) throws SQLException {

		for (int i = 0; i < querys.size(); i++) {

			Query cquery = (Query) querys.get(i);
			Queryable query = cquery.getQuery();
			String name = cquery.getName();
			int query_type = cquery.getQueryType();

			setCurrentQuery(query);

			if (query_type == UPDATE_COMMAND) {
				int updateRow = query.myUpdate(conn);
				getResultMap().put(name, new Integer(updateRow));
			} else if (query_type == QUERY_COMMAND) {
				Rows rows = query.myQuery(conn);
				getResultMap().put(name, rows);
			} else {
				throw new IllegalArgumentException(query_type + " is not defined type\n" + query);
			}
		}

		return querys.size();
	}

	public Map<String, Object> getResultMap() {
		return resultMap;
	}

	public Object getResult(IQueryable query) {
		return getResultMap().get(query.getProcSQL());
	}

	public Queryable getQuery(int i) {
		return ((Query) querys.get(i)).getQuery();
	}

	public int size() {
		return querys.size();
	}

	public Statement getStatement() throws SQLException {
		if (currQuery instanceof Queryable) {
			return ((Queryable) currQuery).getStatement();
		} else {
			throw new UnsupportedOperationException("Unknown Query Type : ");
		}
	}

	private void setCurrentQuery(IQueryable query) {
		this.currQuery = query;
	}

	@Override
	public Object myHandlerQuery(Connection conn, ResultSetHandler handler) throws SQLException {
		throw new UnsupportedOperationException();
	}

	public Object myHandlerQuery(ResultSetHandler handler) throws SQLException {
		throw new UnsupportedOperationException();
	}

	// ibr_12548
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(super.getProcSQL()).append("\n");
		for (Query q : querys) {
			sb.append(q.toString()).append("\n");
		}
		return sb.toString();
	}
	
	
	public Object writeReplace() throws ObjectStreamException {
		return SerializedQuery.createCombinedUserProcedures(this) ;
	}

	
}

class Query implements Serializable {

	private IQueryable query;
	private String name;
	private int query_type;
	private ResultSetHandler handler;

	public Query(IQueryable query, String name, int query_type) {
		this(query, name, query_type, null);
	}

	public Query(IQueryable query, String name, int query_type, ResultSetHandler handler) {
		this.query = query;
		this.name = name;
		this.query_type = query_type;
		this.handler = handler;
	}

	Queryable getQuery() {
		if (!(query instanceof Queryable))
			throw new IllegalArgumentException("exception.framework.db.userporcedures.not_supported_query_type");

		return (Queryable) query;
	}

	String getName() {
		return name;
	}

	int getQueryType() {
		return query_type;
	}

	ResultSetHandler getHandler() {
		return handler;
	}

	// ibr_12548
	public String toString() {
		return query.toString();
	}
}

class InnerDBManager extends DBManager {

	private DBManager dbm;
	private Connection conn;

	InnerDBManager(DBManager dbm, Connection conn) {
		this.dbm = dbm;
		this.conn = conn;
	}

	public int getDBManagerType() {
		return DBType.UNKNOWN;
	}

	public String getDBType() {
		return DBType.UnknownDBName;
	}

	public RepositoryService getRepositoryService() {
		try {
			return RepositoryService.makeService(this.conn);
		} catch (SQLException ex) {
			throw new IllegalArgumentException(ex.getMessage());
		}
	}

	public Connection getConnection() {
		return this.conn;
	}

	public void freeConnection(Connection conn) throws SQLException {
	}

	protected void myInitPool() {
	}

	protected void myDestroyPool() throws SQLException {
	}


}
