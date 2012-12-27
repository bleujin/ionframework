package net.ion.framework.db.async;

import java.sql.SQLException;

import net.ion.framework.db.Rows;
import net.ion.framework.db.bean.handlers.ScalarHandler;
import net.ion.framework.db.manager.DBManager;
import net.ion.framework.db.procedure.H2EmbedDBManager;
import net.ion.framework.db.procedure.IUserCommandBatch;
import net.ion.framework.db.procedure.IUserProcedureBatch;
import net.ion.framework.db.procedure.ProcedureBean;

public class TestUserCommandBatch extends H2TestCase{

	
	// "procedures:{'emp@select':'select * from emp', 'emp@createtable':'create table if not exists emp(empno int, ename varchar(40))', 'emp@insert(?,?)':'insert into emp values(:empno, :ename)'} " +
	public void testBatch() throws Exception {
		adc.dbController().createUserProcedure("emp@createtable").execUpdate() ;
		
		Rows rows = adc.tran(new AsyncTransactionJob<Rows>(){
			public Rows handle(AsyncSession session) throws SQLException {
				IUserCommandBatch cmd =  session.createUserCommandBatch("insert into emp values(?, ?)") ;
				for (int i = 0; i < 5; i++) {
					cmd.addBatchParam(0, i) ;
					cmd.addBatchParam(1, "p" + i) ;
				}
				cmd.execUpdate() ;
				
				Rows rows = session.createUserProcedure("emp@select").execQuery();
				
				session.createUserCommand("delete from emp").execUpdate() ;
				return rows;
			}

			public void fail(Throwable ex) {
				ex.printStackTrace() ;
			}
		}).get();

		assertEquals(5, rows.getRowCount()) ;
		assertEquals(0L, adc.getRows("select count(*) cnt from emp").get().toHandle(new ScalarHandler())) ;
	}
	
	public void testTransaction() throws Exception {
		adc.dbController().createUserProcedure("emp@createtable").execUpdate() ;
		
		Rows rows = adc.tran(new AsyncTransactionJob<Rows>(){
			public Rows handle(AsyncSession session) throws SQLException {
				session.createUserCommand("insert into emp values(?, ?)").addParam(0).addParam("bleujin").execUpdate() ;
				IUserCommandBatch cmd =  session.createUserCommandBatch("insert into emp values(?, ?)") ;
				for (int i = 0; i < 5; i++) {
					cmd.addBatchParam(0, "dd") ;
					cmd.addBatchParam(1, "p" + i) ;
				}
				cmd.execUpdate() ;
				
				Rows rows = session.createUserProcedure("emp@select").execQuery();
				
				session.createUserCommand("delete from emp").execUpdate() ;
				return rows;
			}

			public void fail(Throwable ex) {
				assertEquals(true, ex instanceof SQLException) ;
			}
		}).get();

		assertEquals(true, rows == null) ;
		assertEquals(0L, adc.getRows("select count(*) cnt from emp").get().toHandle(new ScalarHandler())) ;
	}
	
	
	
	public void testProcedureBatch() throws Exception {
		adc.dbController().createUserProcedure("emp@createtable").execUpdate() ;
		H2EmbedDBManager dbm = (H2EmbedDBManager) adc.dbController().getDBManager();
		dbm.hsqlBean().addProcedure(ProcedureBean.create("emp@batch(?,?)", "insert into emp values(?, ?)")) ;
		
		Rows rows = adc.tran(new AsyncTransactionJob<Rows>(){
			public Rows handle(AsyncSession session) throws SQLException {
				IUserProcedureBatch cmd =  session.createUserProcedureBatch("emp@batch(?,?)") ;
				for (int i = 0; i < 5; i++) {
					cmd.addBatchParam(0, i) ;
					cmd.addBatchParam(1, "p" + i) ;
				}
				cmd.execUpdate() ;
				
				Rows rows = session.createUserProcedure("emp@select").execQuery();
				
				session.createUserCommand("delete from emp").execUpdate() ;
				return rows;
			}

			public void fail(Throwable ex) {
				ex.printStackTrace() ;
			}
		}).get();

		assertEquals(5, rows.getRowCount()) ;
		assertEquals(0L, adc.getRows("select count(*) cnt from emp").get().toHandle(new ScalarHandler())) ;
	}	
	
}
