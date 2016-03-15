package net.ion.framework.db.procedure;

import net.ion.framework.db.IDBController;
import net.ion.framework.db.postgre.PostgreSQLUserCommand;
import net.ion.framework.db.postgre.PostgreUserProcedure;

public class PostgreSqlRepositoryService extends RepositoryService {

	public IUserCommand createUserCommand(IDBController dc, String sql) {
		return new PostgreSQLUserCommand(dc, sql);
	}

	public IUserCommandBatch createUserCommandBatch(IDBController dc, String sql) {
		return new UserCommandBatch(dc, sql);
	}

	public IUserProcedure createUserProcedure(IDBController dc, String proc) {
		return new PostgreUserProcedure(dc, proc);
	}

	public IUserProcedureBatch createUserProcedureBatch(IDBController dc, String procSQL) {
		throw new UnsupportedOperationException();
	}

}