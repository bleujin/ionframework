package net.ion.framework.db.procedure;

import net.ion.framework.db.IDBController;
import net.ion.framework.db.mysql.MySQLUserCommand;
import net.ion.framework.db.mysql.MySQLUserProcedure;

public class MySqlRepositoryService extends RepositoryService {

	public IUserCommand createUserCommand(IDBController dc, String sql) {
		return new MySQLUserCommand(dc, sql);
	}

	public IUserCommandBatch createUserCommandBatch(IDBController dc, String sql) {
		return new UserCommandBatch(dc, sql);
	}

	public IUserProcedure createUserProcedure(IDBController dc, String proc) {
		return new MySQLUserProcedure(dc, proc);
	}

	public IUserProcedureBatch createUserProcedureBatch(IDBController dc, String procSQL) {
		throw new UnsupportedOperationException();
	}

}
