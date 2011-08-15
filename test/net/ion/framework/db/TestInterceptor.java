package net.ion.framework.db;

import net.ion.framework.db.manager.InterceptorDBManger;
import net.ion.framework.db.procedure.IUserCommand;
import net.ion.framework.db.procedure.IUserCommandBatch;
import net.ion.framework.db.procedure.IUserProcedure;
import net.ion.framework.db.procedure.IUserProcedureBatch;

public class TestInterceptor extends DBTestCaseByBleujin{

	InterceptorDBManger idbm = null;

	public void setUp() throws Exception {
		super.setUp() ;
		idbm = new InterceptorDBManger(dbm) ;
	}
	
	
	public void tearDown() throws Exception {
		super.tearDown() ;
	}
	
	
	public void testCreate() throws Exception {
		InterceptorDBManger idbm = new InterceptorDBManger(dbm) ;
		DBController idc = new DBController(idbm) ;
		
		IUserProcedure upt = idc.createUserProcedure("test@test()") ;
		assertEquals(null, upt.execQuery()) ;

		IUserCommand cmd = idc.createUserCommand("test@test()") ;
		assertEquals(null, cmd.execQuery()) ;

		IUserProcedureBatch uptb = idc.createUserProcedureBatch("test@test()") ;
		assertEquals(null, uptb.execQuery()) ;

		IUserCommandBatch cmdb = idc.createUserCommandBatch("test@test()") ;
		assertEquals(null, cmdb.execQuery()) ;
	}

	
	public void testVirtualRow() throws Exception {
		Rows rows = new FakeRows() ;
		
	}
	
	
	
}
