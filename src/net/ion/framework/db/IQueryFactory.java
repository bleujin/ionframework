package net.ion.framework.db;

import net.ion.framework.db.procedure.IBatchQueryable;
import net.ion.framework.db.procedure.ICombinedUserProcedures;
import net.ion.framework.db.procedure.IParameterQueryable;
import net.ion.framework.db.procedure.IUserCommand;
import net.ion.framework.db.procedure.IUserCommandBatch;
import net.ion.framework.db.procedure.IUserProcedure;
import net.ion.framework.db.procedure.IUserProcedureBatch;
import net.ion.framework.db.procedure.IUserProcedures;

public interface IQueryFactory {

	public IUserProcedure createUserProcedure(String proc);

	public IUserCommand createUserCommand(String proc);

	public IParameterQueryable createParameterQuery(String proc);
	

	
	public IUserCommandBatch createUserCommandBatch(String procSQL);

	public IUserProcedureBatch createUserProcedureBatch(String procSQL);

	public IBatchQueryable createBatchParameterQuery(IDBController dc, String strSQL);
	
	

	public IUserProcedures createUserProcedures(String name);

	public ICombinedUserProcedures createCombinedUserProcedures(String name);

}
