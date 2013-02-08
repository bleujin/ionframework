package net.ion.framework.db.sample.dc;

import junit.framework.TestCase;
import net.ion.framework.db.DBController;
import net.ion.framework.db.manager.DBManager;
import net.ion.framework.db.procedure.H2EmbedPoolDBManager;


public class TestDBManger extends TestCase {

	public void testOtherManager() throws Exception {
		
		DBManager dbm = H2EmbedPoolDBManager.test() ;
		DBController dc = new DBController(dbm) ;
		dc.initSelf() ;
		
		
		
		
		dc.destroySelf();
		
	}
	
}
