package net.ion.framework.db.procedure;

import java.util.Map;

public interface ICombinedUserProcedures extends Queryable, ICompositeable {

	public ICombinedUserProcedures add(String strSQL, String name, int type);

	public ICombinedUserProcedures add(IQueryable query);

	public ICombinedUserProcedures add(IQueryable query, int type);

	public ICombinedUserProcedures add(IQueryable query, String name, int type);

	public Map getResultMap();

	public Object getResult(IQueryable query);
	
}
