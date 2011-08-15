package net.ion.framework.db;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import net.ion.framework.db.bean.ResultSetHandler;
import net.ion.framework.db.manager.DBManager;
import net.ion.framework.db.procedure.IQueryable;

public interface IDBController extends IQueryFactory {
	public String getName();

	public DBManager getDBManager();

	public void initSelf() throws SQLException;

	public void destroySelf() throws SQLException;

	public void handleServant(long start, long end, IQueryable queryable, int queryCommand);

	public long getModifyCount();

	// for patch.. throw no sqlexception
	public int execUpdate(IQueryable upt);

	public Rows getRows(IQueryable queryable);

	public Object execHandlerQuery(IQueryable query, ResultSetHandler handler);

	public Rows getRows(String proc);

	public DatabaseMetaData getDatabaseMetaData() throws SQLException;
}
