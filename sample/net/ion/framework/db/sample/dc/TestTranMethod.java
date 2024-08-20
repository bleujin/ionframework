package net.ion.framework.db.sample.dc;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;

import org.junit.Test;

import junit.framework.TestCase;
import net.ion.framework.db.DBController;
import net.ion.framework.db.async.AsyncDBController;
import net.ion.framework.db.async.AsyncSession;
import net.ion.framework.db.async.AsyncTransactionJob;
import net.ion.framework.db.h2.H2EmbedPoolDBManager;
import net.ion.framework.db.manager.DBManager;
import net.ion.framework.util.Debug;

public class TestTranMethod extends TestCase {

	private DBController dc = null ; 
	
	public void setUp() throws Exception {
		
		DBManager dbm = H2EmbedPoolDBManager.test() ;
		dc = new DBController(dbm) ;
		dc.initSelf() ;
		
		dc.createUserProcedure("emp@createtable").execUpdate() ;
		dc.createUserProcedure("emp@insert(?,?)").addParam(10).addParam("bleujin").execUpdate() ;

		
	}
	
	public void tearDown() {
		dc.destroySelf();
	}
	
	public void testException() throws Exception {

		AsyncDBController async = dc.async();
		String returned = async.execute(ajob -> {
			ajob.createUserProcedure("emp@insert(?,?)").addParam(20).addParam("hero").execUpdate() ;
			ajob.createUserProcedure("emp@select").execQuery().debugPrint();
			if (true) throw new IllegalStateException("expected") ;
			return "not expected" ;
		}).exceptionally(ex -> {
			return "exception" ;
		}).get() ; 
		
		assertEquals("exception", returned);
		dc.createUserProcedure("emp@select").execQuery().debugPrint();
	}
	

}
