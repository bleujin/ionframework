package net.ion.framework.db.procedure;

import net.ion.framework.db.IDBController;

public class StdOracleRepositoryService extends RepositoryService {

	public static final RepositoryService SELF = new StdOracleRepositoryService();

	StdOracleRepositoryService() {
	}

	public IUserProcedureBatch createUserProcedureBatch(IDBController dc, String procSQL) {
		return new StdOracleUserProcedureBatch(dc, procSQL);
	}

	public IUserProcedure createUserProcedure(IDBController dc, String proc) {
		return new StdOracleUserProcedure(dc, proc);
	}

	public IUserCommandBatch createUserCommandBatch(IDBController dc, String sql) {
		return new StdOracleUserCommandBatch(dc, sql);
	}

	public IUserCommand createUserCommand(IDBController dc, String sql) {
		return new StdOracleUserCommand(dc, sql);
	}

}
