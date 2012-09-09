package net.ion.framework.db;

import net.ion.framework.db.manager.DBManager;
import net.ion.framework.db.manager.OracleCacheDBManager;
import net.ion.framework.db.manager.OracleDBManager;
import net.ion.framework.util.Debug;
import junit.framework.TestCase;

public class TestPage extends TestCase {

	public void testRows() throws Exception {
		Page page = Page.create(10, 2) ;
		Debug.line(page.getStartLoc(), page.getEndLoc()) ;
		
		
		DBManager dbm = new OracleDBManager("jdbc:oracle:thin:@dev-test.i-on.net:1521:devTest", "bleu", "redf") ;
        //DBManager dbm = new MSSQLDBManager("jdbc:microsoft:sqlserver://dev-sql.i-on.net:1435;DatabaseName=pubs", "bleu", "redf") ;
        DBController dc = new DBController("Default", dbm);
        dc.initSelf() ;
        
        Rows rows = dc.getRows("select * from dual") ;
        Debug.line(rows) ;
        dc.destroySelf() ;
	}
	
	
	public void testPage() throws Exception {
		Page page = Page.create(2, 2) ;
		Debug.line(page.getStartLoc(), page.getEndLoc()) ;
	}
}
