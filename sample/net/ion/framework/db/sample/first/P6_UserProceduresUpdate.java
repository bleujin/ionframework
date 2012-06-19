package net.ion.framework.db.sample.first;

import java.sql.SQLException;

import net.ion.framework.db.Rows;
import net.ion.framework.db.procedure.IUserCommand;
import net.ion.framework.db.procedure.IUserProcedures;
import net.ion.framework.db.sample.TestBaseSample;


public class P6_UserProceduresUpdate extends TestBaseSample{

	public void setUp() throws Exception {
		super.setUp() ;
		IUserCommand cmd = dc.createUserCommand("delete from update_sample") ;
		cmd.execUpdate() ;
	}

	/*
	 * UserProcedures�� MDL ���� �������� ���ְ�
	 * (IQueryable�� ��ӹ޴� ����̵� �������� �ִ�. ������ �ٸ� UserProcedures�� ������ �� �ִ�.)
	 * execUpdate�� ȣ���ϸ� �ش� MDL �������� �ڵ����� �ϳ��� Transaction���� ���ֵǸ� 
	 * ������ �ϳ��� �����Ұ�� �ڵ� ��ҵȴ�. 
	 */
	
	public void testUserProcedures() throws Exception {
		IUserProcedures upts = dc.createUserProcedures("One Transaction") ; 
		
		IUserCommand cmd = dc.createUserCommand("insert into update_sample values(?, ?)") ;
		cmd.addParam(1).addParam("abc") ;
		
		IUserCommand cmd2 = dc.createUserCommand("update update_sample set b = ? where a = ?") ;
		cmd2.addParam("___").addParam(1) ;  // unique index exception
		
		int count = upts.add(cmd).add(cmd2).execUpdate() ;
		assertEquals(2, count) ;
		
		Rows rows = dc.getRows("select * from update_sample where a = 1") ;
		assertEquals("___", rows.firstRow().getString("b")) ;
	}
	
	
	public void testSimpleTransaction() throws Exception {
		
		IUserProcedures upts = dc.createUserProcedures("One Transaction") ; 
		
		IUserCommand cmd = dc.createUserCommand("insert into update_sample values(?, ?)") ;
		cmd.addParam(1).addParam("abc") ;
		
		IUserCommand cmd2 = dc.createUserCommand("insert into update_sample values(?, ?)") ;
		cmd.addParam(1).addParam("abc") ;  // unique index exception
		
		upts.add(cmd).add(cmd2) ;
		
		try {
			upts.execUpdate() ;
			fail() ;
		}catch(SQLException ignore){
		}
		
		Rows rows = dc.getRows("select count(*) from update_sample") ;
		assertEquals(0, rows.firstRow().getInt(1)) ;
	}
}
