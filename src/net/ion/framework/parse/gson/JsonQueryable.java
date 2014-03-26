package net.ion.framework.parse.gson;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import net.ion.framework.db.IDBController;
import net.ion.framework.db.Page;
import net.ion.framework.db.Rows;
import net.ion.framework.db.bean.ResultSetHandler;
import net.ion.framework.db.procedure.IQueryable;
import net.ion.framework.db.procedure.Queryable;

public class JsonQueryable implements Queryable {

	private static final long serialVersionUID = 7951133219530619588L;
	private JsonElement json ;
	private Page page;
	JsonQueryable(JsonElement json) {
		this.json = json ;
	}

	public void cancel() throws SQLException, InterruptedException {

	}

	public <T> T execHandlerQuery(ResultSetHandler<T> resultsethandler) throws SQLException {
		return resultsethandler.handle(execQuery());
	}

	public Rows execPageQuery() throws SQLException {
		return execQuery();
	}

	public Rows execQuery() throws SQLException {
		return JsonUtil.toRows(json);
	}

	public int execUpdate() throws SQLException {
		return 0;
	}

	public String getDBType() {
		return "json";
	}

	public Page getPage() {
		return page;
	}

	public String getProcFullSQL() {
		return json.toString();
	}

	public String getProcSQL() {
		return json.toString();
	}

	public int getQueryType() {
		return 31;
	}

	public IQueryable setPage(Page page) {
		this.page = page ;
		return this;
	}

	public long getCurrentModifyCount() {
		return 0;
	}

	public IDBController getDBController() {
		throw new UnsupportedOperationException() ;
	}

	public Statement getStatement() throws SQLException {
		throw new UnsupportedOperationException() ;
	}

	public <T> T myHandlerQuery(Connection connection, ResultSetHandler<T> resultsethandler) throws SQLException {
		throw new UnsupportedOperationException() ;
	}

	public Rows myQuery(Connection connection) throws SQLException {
		throw new UnsupportedOperationException() ;
	}

	public int myUpdate(Connection connection) throws SQLException {
		throw new UnsupportedOperationException() ;
	}

}
