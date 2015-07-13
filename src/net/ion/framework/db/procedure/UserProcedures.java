package net.ion.framework.db.procedure;

import java.io.ObjectStreamException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.ion.framework.db.IDBController;
import net.ion.framework.db.RepositoryException;
import net.ion.framework.db.Rows;
import net.ion.framework.db.bean.ResultSetHandler;
import net.ion.framework.db.xa.XAUserProcedure;

import org.apache.commons.lang.ArrayUtils;

public class UserProcedures extends AbstractQueryable implements IUserProcedures {

	private List<IQueryable> querys = new ArrayList<IQueryable>();
	private IQueryable currQuery = null;

	public UserProcedures(IDBController dc, String name) {
		super(dc, name, QueryType.USER_PROCEDURES);
	}

	public IUserProcedures add(IQueryable query) {
		if (this == query)
			throw new IllegalArgumentException("Cant include self. because of infinite loop");
		if (query instanceof XAUserProcedure)
			throw new IllegalArgumentException("exception.framework.db.userporcedures.not_supported_query_type");

		// if (query instanceof XUserProcedure || query instanceof XUserCommand
		// || query instanceof XUserProcedureBatch) throw new
		// IllegalArgumentException("Cant include XUserProcedure.") ;
		querys.add(query);
		return this;
	}

	public IUserProcedures add(Queryable[] queryArray) {
		for (int i = 0, last = queryArray.length; i < last; i++) {
			Queryable query = queryArray[i];
			add(query);
		}
		return this;
	}

	public IUserProcedures add(IQueryable query, IDBController dc) {
		if (this == query)
			throw new IllegalArgumentException("Cant include self. because of infinite loop");
		if (!rebuildUserProcedureBatch(query, dc)) {
			querys.add(query);
		}
		return this;
	}
	
	public List<IQueryable> getQuerys() {
		return Collections.unmodifiableList(querys);
	}
	
	private static int BATCH_MAX_SIZE = 4000;

	private boolean rebuildUserProcedureBatch(IQueryable query, IDBController dc) {

		if (!(query instanceof IUserProcedureBatch))
			return false;

		IUserProcedureBatch batchQuery = (IUserProcedureBatch) query;
		Object[][] params = batchQuery.getParamsAsArray();

		if (params == null || params.length < 1)
			return false;

		int size = params[0].length;

		if (size <= BATCH_MAX_SIZE)
			return false;

		for (int i = 0; i < size; i += BATCH_MAX_SIZE) {
			IUserProcedureBatch newproc = dc.createUserProcedureBatch(batchQuery.getProcSQL());
			int e = i + BATCH_MAX_SIZE;
			e = (e < size) ? e : size;
			for (int j = 0; j < params.length; j++) {
				Object[] newParam = ArrayUtils.subarray(params[j], i, e);
				newproc.addParam(j, newParam);
			}
			querys.add(newproc);
		}

		return true;
	}

	public int size() {
		return querys.size();
	}

	public Queryable getQuery(int i) {
		return (Queryable) querys.get(i);
	}

	public String getParamAsString(int queryIndex, int paramIndex) {
		return ((IParameterQueryable) getQuery(queryIndex)).getParamAsString(paramIndex);
	}

	public Statement getStatement() {
		throw RepositoryException.throwIt("UserProcedures : Not Supported Operation");
	}

	private void setCurrentQuery(Queryable query) {
		this.currQuery = query;
	}

	@Override
	public <T> T myHandlerQuery(Connection conn, ResultSetHandler<T> handler) throws SQLException {
		for (int i = 0; i < querys.size(); i++) {
			Queryable query = getQuery(i);
			return query.myHandlerQuery(conn, handler);
		}
		throw new SQLException("empty queries");
	}

	public Rows myQuery(Connection conn) throws SQLException {
		Rows result = null;
		Rows nextRows = null;
		for (int i = 0; i < querys.size(); i++) {
			Queryable query = getQuery(i);
			setCurrentQuery(query);
			if (i == 0) {
				result = query.myQuery(conn);
				nextRows = result;
			} else {
				nextRows = nextRows.setNextRows(query.myQuery(conn));
			}
		}
		return result;
	}

	public int myUpdate(Connection conn) throws SQLException {
		Queryable query;
		int sumUpdateRow = 0;

		for (int i = 0; i < querys.size(); i++) {
			query = getQuery(i);
			
			setCurrentQuery(query);
			sumUpdateRow += query.myUpdate(conn);
		}

		return sumUpdateRow;
	}

	// this string's format can modify at future
	public String toString() {
		StringBuffer result = new StringBuffer("UserProcedures : " + getProcSQL() + "\n");
		int querySize = querys.size();
		int maxPrintOut = 10;

		for (int i = 0, last = Math.min(querySize, maxPrintOut); i < last; i++) {
			result.append("Query " + i + " : " + getQuery(i) + "\n");
		}
		if (querySize > maxPrintOut)
			result.append("......Total Query Size : " + querySize);
		return result.toString();
	}
	
	public Object writeReplace() throws ObjectStreamException {
		return SerializedQuery.createUserProcedures(this) ;
	}

}
