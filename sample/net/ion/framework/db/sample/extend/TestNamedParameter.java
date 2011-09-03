package net.ion.framework.db.sample.extend;

import java.sql.Types;

import net.ion.framework.db.Rows;
import net.ion.framework.db.procedure.IUserCommand;
import net.ion.framework.db.procedure.IUserCommandBatch;
import net.ion.framework.db.procedure.IUserProcedure;
import net.ion.framework.db.sample.TestBaseDB;
import net.ion.framework.util.RandomUtil;


public class TestNamedParameter extends TestBaseDB{


	public void setUp() throws Exception {
		super.setUp() ;
		dc.execUpdate("delete from update_sample") ;
		dc.execUpdate("truncate table performance_sample") ;
	}
	
	public void testNamedParameter() throws Exception {
		IUserCommand cmd = dc.createUserCommand("select * from copy_sample where no1 >= :no1 and no1 <= :no1") ;
		cmd.addParam("no1", 20) ;
		
		Rows rows = cmd.execQuery() ;
		assertEquals(1, rows.getRowCount()) ;
	
	}

	public void testNamedParameterDupl() throws Exception {
		try {
			IUserCommand cmd = dc.createUserCommand("select ':abc' from copy_sample where no1 >= :no1 and no1 <= :no1") ;
			cmd.addParam("no1", 20) ;
			cmd.execQuery() ;
			fail() ;
		} catch (IllegalArgumentException ignore){}

		IUserCommand cmd = dc.createUserCommand("select 'a:1' from copy_sample where no1 >= :no1 and no1 <= :no1") ;
		cmd.addParam("no1", 20) ;

		Rows rows = cmd.execQuery() ;
		assertEquals(1, rows.getRowCount()) ;
	}

	public void xNotUseThisConflictPattern() throws Exception {
		IUserCommand cmd = dc.createUserCommand("select * from copy_sample where no1 >= ? and no1 <= :no1") ;
		cmd.addParam(0, 19) ;
		cmd.addParam("no1", 20) ;
		
		Rows rows = cmd.execQuery() ;
		assertEquals(2, rows.getRowCount()) ;
	}
	
	
	
	
	public void testNamedProcedure() throws Exception {
		IUserProcedure upt = dc.createUserProcedure("sample@insertWith(:a, :b)") ;
		upt.addParameter("a", "1", Types.INTEGER) ;
		upt.addParameter("b", "", Types.VARCHAR) ;
		
		//upt.getProcFullSQL();
		
		upt.execUpdate() ;
		
		Rows rows = dc.getRows("select * from update_sample") ;
		assertEquals(1, rows.getRowCount()) ;
	}
	
	
	public void testBatch() throws Exception {
		
		IUserCommandBatch cmd = dc.createUserCommandBatch("insert into performance_sample(a, b, c) values(?, ?, ?)") ;
		for (int i = 0; i < 10; i++) {
			cmd.addBatchParam(0, i) ;
			cmd.addBatchParam(1, "No." + i + ".....") ;
			cmd.addBatchParam(2, RandomUtil.nextRandomString(RandomUtil.nextRandomInt(10, 50), RandomUtil.NUMBER_CHAR_TYPE)) ;
		}
	
		cmd.execUpdate() ;
	}
	
	public void testNamedBatch() throws Exception {
		
		IUserCommandBatch cmd = dc.createUserCommandBatch("insert into performance_sample(a, b, c) values(:a, :b, :c)") ;
		for (int i = 0; i < 10; i++) {
			cmd.addBatchParam("a", i) ;
			cmd.addBatchParam("b", "No." + i + ".....") ;
			cmd.addBatchParam("c", RandomUtil.nextRandomString(RandomUtil.nextRandomInt(10, 30), RandomUtil.NUMBER_CHAR_TYPE)) ;
		}
		
		cmd.execUpdate() ;
	}


}
