package net.ion.framework.db.procedure;

import net.ion.framework.db.IDBController;
import net.ion.framework.db.RepositoryException;

public class HSQLRepositoryService extends RepositoryService {

	public final static HSQLRepositoryService SELF = new HSQLRepositoryService();

	public UserCommand createUserCommand(IDBController dc, String sql) {
		return new OracleUserCommand(dc, sql);
	}

	public UserCommandBatch createUserCommandBatch(IDBController dc, String sql) {
		return new UserCommandBatch(dc, sql);
	}

	public UserProcedure createUserProcedure(IDBController dc, String proc) {
		throw RepositoryException.throwIt("HSQLRepositoryService : Not Supproted Operation");
	}

	public IUserProcedureBatch createUserProcedureBatch(IDBController dc, String procSQL) {
		throw RepositoryException.throwIt("HSQLRepositoryService : Not Supproted Operation");
	}

}