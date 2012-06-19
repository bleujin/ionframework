package net.ion.framework.db.procedure;

import net.ion.framework.db.IDBController;

public class OracleRepositoryService extends RepositoryService {
	public OracleRepositoryService() {
	}

	public IUserProcedureBatch createUserProcedureBatch(IDBController dc, String procSQL) {
		return new OracleUserProcedureBatch(dc, procSQL);
	}

	public IUserProcedure createUserProcedure(IDBController dc, String proc) {
		return new OracleUserProcedure(dc, proc);
	}

	public IUserCommand createUserCommand(IDBController dc, String sql) {
		return new OracleUserCommand(dc, sql);
	}

	public IUserCommandBatch createUserCommandBatch(IDBController dc, String sql) {
		return new OracleUserCommandBatch(dc, sql);
	}

}
