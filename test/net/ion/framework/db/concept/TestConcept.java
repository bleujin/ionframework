package net.ion.framework.db.concept;

import java.sql.SQLException;

import net.ion.framework.db.DBController;
import net.ion.framework.db.IDBController;
import net.ion.framework.db.Rows;
import net.ion.framework.db.bean.ResultSetHandler;
import net.ion.framework.db.bean.handlers.JSONHandler;
import net.ion.framework.db.bean.handlers.ScalarHandler;
import net.ion.framework.db.manager.DBManager;
import net.ion.framework.db.servant.StdOutServant;
import net.ion.framework.parse.gson.JsonObject;
import net.ion.framework.util.ListUtil;
import junit.framework.TestCase;

public class TestConcept extends TestCase{

	private NewDBController dc;

	public void setUp() throws Exception{
		DBManager dbm = null ;
		this.dc = new NewDBController(dbm) ;
		dc.initSelf() ;
	}
	
	public void tearDown(){
		dc.destroySelf() ;
	}

	public void testFirst() throws Exception {
		dc.addServant(new StdOutServant()) ;
		
		Rows rows = dc.tran(new TransactionJob<Rows>(){
			public Rows handle(WriteSession wsession) {
				wsession.createProcedure("select@dual(:name, :age)").param("name", "bleujin").param("age", 20).execQuery() ;
				return null;
			}
		}) ;
		JsonObject jso = rows.toHandle(new JSONHandler()) ;
	}
	
	public void testAdvInSessionContext() throws Exception {
		dc.tran(new TransactionJob<Void>(){
			public Void handle(WriteSession wsession) throws SQLException{
				
				ResultSetHandler<Integer> rsh = new ScalarHandler<Integer>("age");
				int val = wsession.createProcedure("viewAge@dual(:name)").param("name", "bleujin").execQuery().toHandle(rsh) ;
				wsession.createProcedure("mergeOld(:name, :older)").param("name", "bleujin").param("older", val > 20) ;

				return null;
			}
			
		}) ;
		
	}
	
	
	public void testStmtType() throws Exception {
		Rows rows = dc.tran(new TransactionJob<Rows>(){
			public Rows handle(WriteSession wsession) throws SQLException {
				WBatchProcedure batch = wsession.createBatchProcedure("insert into dual values(:name)") ;
				for(int i : ListUtil.rangeNum(5)) {
					batch.param("name", "x" + i) ;
				}
				batch.execUpdate() ;
				
				
				
				Rows rows = wsession.createProcedure("select 1 from dual").execQuery() ;
				wsession.createProcedure("delete from dual").execUpdate() ;
				return rows;
			}
		}) ;
		
		
		
	}
	
	
	
}
