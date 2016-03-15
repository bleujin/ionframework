package net.ion.framework.db.async;

import java.sql.SQLException;

import net.ion.framework.db.DBController;
import net.ion.framework.db.Rows;
import net.ion.framework.db.h2.H2EmbedPoolDBManager;
import net.ion.framework.db.servant.PrintOutServant;
import net.ion.framework.util.Debug;
import junit.framework.TestCase;

public class TestOldAsync extends TestCase {

	
	public void testOldMethod() throws Exception {
		final DBController dc = createDBController();
		final AsyncDBController adc = dc.async();
		adc.dbController().createUserProcedure("emp@createtable").execUpdate() ;
		Result<Integer> iresult = adc.updateResult("insert into emp values(1, 'bleujin')");
		
		Result<Rows> sresult = adc.queryResult("select * from emp") ;
		assertEquals(1, iresult.get().intValue()) ;
		sresult.get().debugPrint() ;
		
		
		assertEquals(dc, adc.dbController()) ;
	}
	

	private DBController createDBController() throws SQLException {
		final DBController dc = new DBController("testH2", H2EmbedPoolDBManager.test(), new PrintOutServant());
		dc.initSelf() ;
		return dc ;
	}

}
