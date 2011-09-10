package net.ion.framework.db.sample.servant;

import junit.framework.TestCase;
import net.ion.framework.db.DBController;
import net.ion.framework.db.manager.DBManager;
import net.ion.framework.db.procedure.H2EmbedDBManager;
import net.ion.framework.db.servant.StdOutServant;

public class TestServant extends TestCase{

	
	public void testInit() throws Exception {
		DBManager dbm = H2EmbedDBManager.testMem() ;
		DBController dc = new DBController(dbm) ;
		dc.initSelf() ;
		dc.addServant(StdOutServant.ALL) ;
		
		
		dc.createUserCommand("create table abc(a int, b varchar(200))").execUpdate() ;
		
		
		
	}
}
