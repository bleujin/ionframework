package net.ion.framework.db.manager;

import junit.framework.TestCase;
import net.ion.framework.db.DBController;

public class TestLoginCaseInsensitive extends TestCase{
	
	public void testConnect() throws Exception {
		DBManager dbm = new OracleCacheDBManager("jdbc:oracle:thin:@61.250.201.76:1521:TOONTALK", "toontalk", "toon0711");
		DBController dc = new DBController(dbm) ;
		dc.initSelf() ;
		
		dc.getRows("select * from dual").debugPrint() ;
		
		dc.destroySelf() ;		
	}
}
