package net.ion.framework.db.procedure;

import net.ion.framework.db.IDBController;

public class PostgreSqlRepositoryService extends RepositoryService {

	public IUserCommand createUserCommand(IDBController dc, String sql) {
		return new PostgreSQLUserCommand(dc, sql);
	}

	public IUserCommandBatch createUserCommandBatch(IDBController dc, String sql) {
		return new UserCommandBatch(dc, sql);
	}

	public IUserProcedure createUserProcedure(IDBController dc, String proc) {
		throw new UnsupportedOperationException();
	}

	public IUserProcedureBatch createUserProcedureBatch(IDBController dc, String procSQL) {
		throw new UnsupportedOperationException();
	}

}