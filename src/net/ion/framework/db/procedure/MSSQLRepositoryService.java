package net.ion.framework.db.procedure;

import net.ion.framework.db.IDBController;

public class MSSQLRepositoryService extends RepositoryService {
	MSSQLRepositoryService() {
	}

	public IUserProcedureBatch createUserProcedureBatch(IDBController dc, String procSQL) {
		return new MSSQLUserProcedureBatch(dc, procSQL);
	}

	public IUserProcedure createUserProcedure(IDBController dc, String proc) {
		return new MSSQLUserProcedure(dc, proc);
	}

	public IUserCommandBatch createUserCommandBatch(IDBController dc, String sql) {
		return new UserCommandBatch(dc, sql);
	}

	public IUserCommand createUserCommand(IDBController dc, String sql) {
		return new MSSQLUserCommand(dc, sql);
	}

}
