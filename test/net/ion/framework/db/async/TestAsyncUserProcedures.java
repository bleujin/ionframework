package net.ion.framework.db.async;

import java.sql.SQLException;

import net.ion.framework.db.DBController;
import net.ion.framework.db.Rows;
import net.ion.framework.db.procedure.H2EmbedPoolDBManager;
import net.ion.framework.db.procedure.IUserCommand;
import net.ion.framework.db.servant.PrintOutServant;
import junit.framework.TestCase;

public class TestAsyncUserProcedures extends H2TestCase{

	public void testTranDesign() throws Exception {
		// "procedures:{'emp@select':'select * from emp', 'emp@createtable':'create table if not exists emp(empno int, ename varchar(40))', 'emp@insert(?,?)':'insert into emp values(:empno, :ename)'} " +
		
		adc.dbController().createUserProcedure("emp@createtable").execUpdate() ;
		
		Rows rows = adc.tran(new AsyncTransactionJob<Rows>(){
			public Rows handle(AsyncSession session) throws SQLException {
				session.createUserProcedures("two")
					.add(session.createUserProcedure("emp@insert(?,?)").addParam("empno", 1).addParam("ename", "bleujin"))
					.add(session.createUserProcedure("emp@insert(?,?)").addParam("empno", 2).addParam("ename", "hero")).execUpdate() ;
				
				Rows rows = session.createUserProcedure("emp@select").execQuery();
				
				session.createUserCommand("delete from emp where empno in (?, ?)").addParam(1).addParam(2).execUpdate() ;
				return rows;
			}
		}).get();
		
		assertEquals(2, rows.getRowCount()) ;
		assertEquals(0, adc.dbController().createUserCommand("select /* outer */ * from emp").execQuery().getRowCount()) ;
	}
	
	public void testTransaction() throws Exception {
		adc.dbController().createUserProcedure("emp@createtable").execUpdate() ;
		
		Rows rows = adc.tran(new AsyncTransactionJob<Rows>(){
			public Rows handle(AsyncSession session) throws SQLException {
				
				session.createUserProcedure("emp@insert(?,?)").addParam("empno", 1).addParam("ename", "bleujin").execUpdate() ;
				
				session.createUserProcedures("two")
					.add(session.createUserProcedure("emp@insert(?,?)").addParam("empno", 1).addParam("ename", "bleujin"))
					.add(session.createUserProcedure("emp@insert(?,?)").addParam("empno", "not allowed").addParam("ename", "hero")).execUpdate() ;
				
				Rows rows = session.createUserProcedure("emp@select").execQuery();
				return rows;
			}
		}).get();
		
		assertEquals(true, rows == null) ;
		assertEquals(0, adc.dbController().createUserCommand("select /* outer */ * from emp").execQuery().getRowCount()) ;
	}
	
}
