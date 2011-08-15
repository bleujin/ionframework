package net.ion.framework.db.procedure;

import net.ion.framework.db.IDBController;

public interface IUserProcedures extends Queryable, ICompositeable {

	public IUserProcedures add(IQueryable query);

	public IUserProcedures add(Queryable[] queryArray);;

	public IUserProcedures add(IQueryable query, IDBController dc);

	public String getParamAsString(int i, int j);
}
