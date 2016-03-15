package net.ion.framework.db.async;

import java.sql.SQLException;
import java.util.concurrent.Future;

import junit.framework.TestCase;
import net.ion.framework.db.DBController;
import net.ion.framework.db.Rows;
import net.ion.framework.db.h2.H2EmbedPoolDBManager;
import net.ion.framework.db.procedure.IUserCommand;
import net.ion.framework.db.servant.PrintOutServant;

public class TestAsyncDBController extends TestCase{

    private AsyncDBController adc;
	protected void setUp() throws java.lang.Exception {
        super.setUp();
        this.adc = new AsyncDBController(createDBController()) ;
    }

	private DBController createDBController() throws SQLException {
		final DBController dc = new DBController("testH2", H2EmbedPoolDBManager.test(), new PrintOutServant());
		dc.initSelf() ;
		return dc ;
	}

    protected  void tearDown() throws java.lang.Exception {
        super.tearDown();
        adc.destroySelf() ;
    }
    
    
    public void testDebugPrint() throws Exception {
    	final Future<Rows> future = adc.getRows("select * from dual union all select * from dual");
    	
		future.get().debugPrint() ;
	}
    
	public void testDataFuture() throws Exception {
		Future<Rows> future = adc.getRows("select * from dual");
	}
	
	public void testException() throws Exception {
		final class MyHandler implements ExceptionHandler {
			private Throwable ex ;
			public void handle(Throwable ex) {
				this.ex = ex ;
			}
			public Throwable ex(){
				return ex ;
			}
		};
		MyHandler handler = new MyHandler();
		
		Future<Rows> future = adc.getRows("select * from dualdual", handler);
		assertEquals(true, future.get() == null) ;
		assertEquals(true, handler.ex() instanceof SQLException) ;
	}
	

	public void testTranDesign() throws Exception {
		// "procedures:{'emp@select':'select * from emp', 'emp@createtable':'create table if not exists emp(empno int, ename varchar(40))', 'emp@insert(?,?)':'insert into emp values(:empno, :ename)'} " +
		
		adc.dbController().createUserProcedure("emp@createtable").execUpdate() ;
		
		Rows rows = adc.tran(new AsyncTransactionJob<Rows>(){
			public Rows handle(AsyncSession session) throws SQLException {
				IUserCommand uc = session.createUserCommand("insert into emp values(?, ?)");
				uc.addParam(1).addParam("bleujin").execUpdate() ;
				Rows rows = session.createUserCommand("select /* inner */ * from emp").execQuery();
				
				session.createUserCommand("delete from emp where empno = ?").addParam(1).execUpdate() ;
				return rows;
			}
		}).get();
		
		assertEquals(1, rows.getRowCount()) ;
		assertEquals(0, adc.dbController().createUserCommand("select /* outer */ * from emp").execQuery().getRowCount()) ;
	}
	
	
	public void testTransaction() throws Exception {
		adc.dbController().createUserProcedure("emp@createtable").execUpdate() ;
		
		final boolean isSuccess = false ;
		adc.exceptionHandler(new ExceptionHandler() {
			public void handle(Throwable ignore) {
			}
		}) ;
		Rows rows = adc.tran(new AsyncTransactionJob<Rows>(){
			
			public Rows handle(AsyncSession session) throws SQLException {
				IUserCommand uc = session.createUserCommand("insert into emp values(?, ?)");
				uc.addParam(1).addParam("bleujin").execUpdate() ;
				
				Rows rows = session.createUserCommand("select /* inner */ * from emp").execQuery();
				
				if (! isSuccess) session.fail("user fail") ;
				
				session.createUserCommand("delete from emp where empno = ?").addParam(1).execUpdate() ;
				return rows;
			}
		}).get();
		
		assertEquals(true, rows == null) ;
		assertEquals(0, adc.dbController().createUserCommand("select /* outer */ * from emp").execQuery().getRowCount()) ;
	}
	

}


