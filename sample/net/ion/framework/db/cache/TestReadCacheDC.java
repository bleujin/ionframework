package net.ion.framework.db.cache;

import java.sql.ResultSet;
import java.sql.SQLException;

import junit.framework.TestCase;
import net.ion.framework.db.DBController;
import net.ion.framework.db.Rows;
import net.ion.framework.db.bean.ResultSetHandler;
import net.ion.framework.db.manager.OracleDBManager;
import net.ion.framework.db.procedure.IParameterQueryable;
import net.ion.framework.db.servant.AfterTask;
import net.ion.framework.db.servant.IExtraServant;
import net.ion.framework.util.Debug;
import net.ion.framework.util.ListUtil;

public class TestReadCacheDC extends TestCase{

	private ReadCacheDBController cdc;
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		DBController dc = new DBController("test", OracleDBManager.test());
		dc.initSelf() ;
		
		this.cdc = ReadCacheDBController.create(dc) ;
	}
	
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		cdc.getOrginial().destroySelf() ;
	}
	
	public void testCreate() throws Exception {
		assertEquals(0, cdc.hitcount()) ; 
		
		cdc.getRows("select * from dual") ;
		assertEquals(0, cdc.hitcount()) ;
		
		for (int i = 0; i < 5 ; i++) {
			cdc.getRows("select * from dual") ;
			assertEquals(1 + i, cdc.hitcount()) ;
		}
	}
	
	public void testCallServant() throws Exception {
		final CountServant cs = new CountServant();
		((DBController)cdc.getOrginial()).addServant(cs) ;
		
		for (int ii = 0; ii < 5 ; ii++) {
			cdc.getRows("select * from dual") ;
		}
		assertEquals(5, cs.count) ;
	}
	
	public void testAnother() throws Exception {

		ReadCacheDBController another = ReadCacheDBController.create(cdc.getOrginial()) ; 

		assertEquals(0, cdc.hitcount()) ;
		assertEquals(0, another.hitcount()) ;

		cdc.getRows("select * from dual") ;
		assertEquals(0, cdc.hitcount()) ;
		assertEquals(0, another.hitcount()) ;

		cdc.getRows("select * from dual") ;
		assertEquals(1, cdc.hitcount()) ;
		assertEquals(0, another.hitcount()) ;
	}
	
	public void testParameter() throws Exception {
		cdc.createUserProcedure("Category@infoBy(?)").addParam("abc").execQuery();
		assertEquals(0, cdc.hitcount());

		cdc.createUserProcedure("Category@infoBy(?)").addParam("def").execQuery();
		assertEquals(0, cdc.hitcount());

		cdc.createUserProcedure("Category@infoBy(?)").addParam("def").execQuery();
		assertEquals(1, cdc.hitcount());

		cdc.createUserProcedure("Category@infoBy(?)").addParam("abc").execQuery();
		assertEquals(2, cdc.hitcount());
	}
	
	public void testNotProxyInServerCursorHandler() throws Exception {
		IParameterQueryable other = cdc.createUserProcedure("Category@infoBy(?)").addParam("def");
		for (int ii = 0; ii < 5; ii++) {
			cdc.execHandlerQuery(other, new DummyHandler()) ;
			assertEquals(0, cdc.hitcount()) ;
		}
	}
	
	public void testProxyInServerCursorHandler() throws Exception {
		IParameterQueryable other = cdc.createUserProcedure("Category@infoBy(?)").addParam("def");
		for (int ii = 0; ii < 5; ii++) {
			other.execQuery().toHandle(new DummyHandler()) ;
			assertEquals(ii, cdc.hitcount()) ;
		}
	}
	

	public void testSpeed() throws Exception {
		cdc.createUserProcedure("Category@infoBy(?)").addParam("def").execQuery() ;
		
		long start = System.currentTimeMillis() ;
		for (int ii = 0; ii < 1000; ii++) {
			cdc.createUserProcedure("Category@infoBy(?)").addParam("def").execQuery() ;
		}
		Debug.line(System.currentTimeMillis() - start) ;
	}
	
	public void testNotShareRow() throws Exception {
		ReadCacheDBController another = ReadCacheDBController.create(cdc.getOrginial()) ;
		
		Rows r1 = cdc.getRows("select * from dual") ;
		Rows r2 = another.getRows("select * from dual") ;
		
		assertEquals(true, r1 != r2) ;
		assertEquals(true, r1.isBeforeFirst()) ;
		r1.next() ;
		assertEquals(true, r1.isFirst()) ;
		assertEquals(true, r2.isBeforeFirst()) ;
	}
	
	public void testMaxSize() throws Exception {
		ReadCacheDBController another = ReadCacheDBController.create(cdc.getOrginial(), 1) ;
		
		for (int i : ListUtil.rangeNum(10)) {
			if (i % 2 == 0) another.getRows("select * from dual where 1 = 1") ;
			else another.getRows("select * from dual where 2 = 2") ;
		}
		
		assertEquals(0, another.hitcount()) ;
	}
	
	
	
	
	
	
	
	
	
	
	
	
}

class CountServant implements IExtraServant {

	int count = 0 ;
	public void support(AfterTask atask) {
		count++ ;
	}
}

class DummyHandler implements ResultSetHandler<Void> {

	public Void handle(ResultSet rs) throws SQLException {
		return null;
	}
	
}

