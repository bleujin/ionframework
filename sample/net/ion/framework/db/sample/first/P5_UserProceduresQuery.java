package net.ion.framework.db.sample.first;

import net.ion.framework.db.Page;
import net.ion.framework.db.Rows;
import net.ion.framework.db.procedure.IUserCommand;
import net.ion.framework.db.procedure.IUserProcedures;
import net.ion.framework.db.sample.TestBaseSample;


public class P5_UserProceduresQuery extends TestBaseSample{
	
	
	public void setUp() throws Exception {
		super.setUp() ;
		IUserCommand cmd = dc.createUserCommand("delete from update_sample") ;
		cmd.execUpdate() ;
	}
	
	/**
	 * ���� UserProcedures�� �������� MDL���� �ƴ϶� Select���� ��´ٸ�.
	 * execQuery�� �����Ͽ� ������ �Ѳ��� �����ü� �ִ�.
	 * ������ ������ �����°ͺ��� ��� �����ϴ�. 
	 */
	
	
	public void testUpdate() throws Exception {
		IUserCommand cmd1 = dc.createUserCommand("insert into update_sample values(?, ?)") ;
		cmd1.addParam(1).addParam("abc") ;
		IUserCommand cmd2 = dc.createUserCommand("delete from update_sample where a = ?") ;
		cmd2.addParam(1);
		
		IUserProcedures upts = dc.createUserProcedures("Multi MDL") ;
		upts.add(cmd1).add(cmd2) ;
		int result = upts.execUpdate() ;
		assertEquals(2, result) ;
	}
	
	public void testMultiQuery() throws Exception {
		IUserCommand cmd1 = dc.createUserCommand("select * from copy_sample") ;
		cmd1.setPage(Page.create(10, 1)) ;
		IUserCommand cmd2 = dc.createUserCommand("select 3 from dept_sample") ;
		
		IUserProcedures upts = dc.createUserProcedures("Multi Query") ;
		upts.add(cmd1).add(cmd2) ;
		
		Rows first = upts.execQuery() ;  // first query result ;
		assertEquals(true, first.getRowCount() == 10) ;
		
		Rows second = first.getNextRows() ; // second query result ;
		assertEquals(3, second.firstRow().getInt(1)) ;
		
	}
	
}
