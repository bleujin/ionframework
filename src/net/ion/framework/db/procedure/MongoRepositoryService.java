package net.ion.framework.db.procedure;

import net.ion.framework.db.IDBController;

public class MongoRepositoryService extends RepositoryService {

	@Override
	public IUserCommand createUserCommand(IDBController idc, String sql) {
		return new MongoUserCommand(idc, sql);
	}

	@Override
	public IUserCommandBatch createUserCommandBatch(IDBController idc, String sql) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IUserProcedure createUserProcedure(IDBController idc, String sql) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IUserProcedureBatch createUserProcedureBatch(IDBController idc, String sql) {
		// TODO Auto-generated method stub
		return null;
	}

}
