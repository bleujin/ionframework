package net.ion.framework.db.procedure;

import java.lang.reflect.Proxy;

import net.ion.framework.db.IDBController;
import net.ion.framework.db.cache.InvokeQueryHandler;
import net.ion.framework.db.manager.DBManager;
import net.ion.framework.db.manager.InterceptorDBManger;

public class InterceptorRepositoryService extends RepositoryService {

	private final InterceptorDBManger idbm;
	private final DBManager dbm;

	public InterceptorRepositoryService(InterceptorDBManger idbm, DBManager dbm) {
		this.idbm = idbm;
		this.dbm = dbm;
	}

	public IUserProcedures createUserProcedures(IDBController dc, String sql) {
		if (!(idbm.find(sql)))
			throw new IllegalArgumentException("not defined sql : " + sql);
		IUserProcedures upts = super.createUserProcedures(dc, sql);
		InvokeQueryHandler ihan = createInvokeQuery(upts);

		return (IUserProcedures) Proxy.newProxyInstance(upts.getClass().getClassLoader(), new Class[] { IUserProcedures.class }, ihan);
	}

	public ICombinedUserProcedures createCombinedUserProcedures(IDBController dc, String name) {
		ICombinedUserProcedures upts = super.createCombinedUserProcedures(dc, name);
		InvokeQueryHandler ihan = createInvokeQuery(upts);

		return (ICombinedUserProcedures) Proxy.newProxyInstance(upts.getClass().getClassLoader(), new Class[] { ICombinedUserProcedures.class }, ihan);
	}

	@Override
	public IUserCommand createUserCommand(IDBController dc, String sql) {
		if (!(idbm.find(sql)))
			throw new IllegalArgumentException("not defined sql : " + sql);

		IUserCommand upt = dbm.getRepositoryService().createUserCommand(dc, sql);
		InvokeQueryHandler ihan = createInvokeQuery(upt);

		return (IUserCommand) Proxy.newProxyInstance(upt.getClass().getClassLoader(), new Class[] { IUserCommand.class }, ihan);
	}

	@Override
	public IUserCommandBatch createUserCommandBatch(IDBController dc, String sql) {
		if (!(idbm.find(sql)))
			throw new IllegalArgumentException("not defined sql : " + sql);

		IUserCommandBatch upt = dbm.getRepositoryService().createUserCommandBatch(dc, sql);
		InvokeQueryHandler ihan = createInvokeQuery(upt);

		return (IUserCommandBatch) Proxy.newProxyInstance(upt.getClass().getClassLoader(), new Class[] { IUserCommandBatch.class }, ihan);
	}

	@Override
	public IUserProcedure createUserProcedure(IDBController dc, String proc) {
		if (!(idbm.find(proc)))
			throw new IllegalArgumentException("not defined proc : " + proc);

		IUserProcedure upt = dbm.getRepositoryService().createUserProcedure(dc, proc);
		InvokeQueryHandler ihan = createInvokeQuery(upt);

		return (IUserProcedure) Proxy.newProxyInstance(upt.getClass().getClassLoader(), new Class[] { IUserProcedure.class }, ihan);
	}

	@Override
	public IUserProcedureBatch createUserProcedureBatch(IDBController dc, String sql) {
		if (!(idbm.find(sql)))
			throw new IllegalArgumentException("not defined sql : " + sql);

		IUserProcedureBatch upt = dbm.getRepositoryService().createUserProcedureBatch(dc, sql);
		InvokeQueryHandler ihan = createInvokeQuery(upt);

		return (IUserProcedureBatch) Proxy.newProxyInstance(upt.getClass().getClassLoader(), new Class[] { IUserProcedureBatch.class }, ihan);
	}

	protected InvokeQueryHandler createInvokeQuery(Queryable upt) {
		return new InvokeQueryHandler(upt);
	}

}
