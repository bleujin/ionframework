package net.ion.framework.db.cache;

import java.lang.reflect.Proxy;

import net.ion.framework.db.IDBController;
import net.ion.framework.db.manager.CacheDBManager;
import net.ion.framework.db.manager.DBManager;
import net.ion.framework.db.procedure.ICombinedUserProcedures;
import net.ion.framework.db.procedure.IUserCommand;
import net.ion.framework.db.procedure.IUserCommandBatch;
import net.ion.framework.db.procedure.IUserProcedure;
import net.ion.framework.db.procedure.IUserProcedureBatch;
import net.ion.framework.db.procedure.IUserProcedures;
import net.ion.framework.db.procedure.RepositoryService;
import net.ion.framework.util.StringUtil;

public class CacheRepositoryService extends RepositoryService {

	private CacheDBManager cdbm;
	private DBManager dbm;

	public CacheRepositoryService(CacheDBManager cdbm, DBManager dbm) {
		this.cdbm = cdbm;
		this.dbm = dbm;
	}

	public IUserProcedures createUserProcedures(IDBController dc, String name) {
		IUserProcedures upts = dbm.getRepositoryService().createUserProcedures(dc, name);
		CacheQueryHandler ihan = new CacheQueryHandler(cdbm, upts);
		return (IUserProcedures) Proxy.newProxyInstance(upts.getClass().getClassLoader(), new Class[] { IUserProcedures.class }, ihan);
	}

	public ICombinedUserProcedures createCombinedUserProcedures(IDBController dc, String name) {
		ICombinedUserProcedures upts = dbm.getRepositoryService().createCombinedUserProcedures(dc, name);
		CacheQueryHandler ihan = new CacheQueryHandler(cdbm, upts);

		return (ICombinedUserProcedures) Proxy.newProxyInstance(upts.getClass().getClassLoader(), new Class[] { ICombinedUserProcedures.class }, ihan);
	}

	@Override
	public IUserCommand createUserCommand(IDBController dc, String sql) {
		IUserCommand upt = dbm.getRepositoryService().createUserCommand(dc, sql);
		CacheQueryHandler ihan = new CacheQueryHandler(cdbm, upt);

		return (IUserCommand) Proxy.newProxyInstance(upt.getClass().getClassLoader(), new Class[] { IUserCommand.class }, ihan);
	}

	@Override
	public IUserCommandBatch createUserCommandBatch(IDBController dc, String sql) {
		IUserCommandBatch upt = dbm.getRepositoryService().createUserCommandBatch(dc, sql);
		CacheQueryHandler ihan = new CacheQueryHandler(cdbm, upt);

		return (IUserCommandBatch) Proxy.newProxyInstance(upt.getClass().getClassLoader(), new Class[] { IUserCommandBatch.class }, ihan);
	}

	@Override
	public IUserProcedure createUserProcedure(IDBController dc, String proc) {
		IUserProcedure upt = dbm.getRepositoryService().createUserProcedure(dc, StringUtil.deleteWhitespace(proc));
		CacheQueryHandler ihan = new CacheQueryHandler(cdbm, upt);

		return (IUserProcedure) Proxy.newProxyInstance(upt.getClass().getClassLoader(), new Class[] { IUserProcedure.class }, ihan);
	}

	@Override
	public IUserProcedureBatch createUserProcedureBatch(IDBController dc, String sql) {
		IUserProcedureBatch upt = dbm.getRepositoryService().createUserProcedureBatch(dc, sql);
		CacheQueryHandler ihan = new CacheQueryHandler(cdbm, upt);

		return (IUserProcedureBatch) Proxy.newProxyInstance(upt.getClass().getClassLoader(), new Class[] { IUserProcedureBatch.class }, ihan);
	}

}
