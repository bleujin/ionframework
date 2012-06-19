package net.ion.framework.db;


public class ManyDBControllerTest extends DBTestCase{

	public void testStart() throws Exception {
		
		int max = 50000;
		for (int i = 0; i < max; i++) {
			new DBController(dc.getDBManager()).initSelf() ;
		}
		
//		DBController newDc = new DBController("NEW", dc.getDBManager(), new StdOutServant());
//		newDc.initSelf() ;
//		
//		dc.execQuery("select 1 from dual") ;
//		
//		Thread.sleep(2000) ;
//		newDc.destroySelf() ;
	}
}
