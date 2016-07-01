package afield;

import junit.framework.TestCase;
import net.ion.framework.db.DBController;
import net.ion.framework.db.manager.DBManager;
import net.ion.framework.db.manager.PostSqlDBManager;


public class TesBaseICS6PG extends TestCase {

	protected DBController dc;
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		DBManager dbm = PostSqlDBManager.test() ;
		this.dc = new DBController(dbm) ;
		dc.initSelf(); 
	}
	
	@Override
	protected void tearDown() throws Exception {
		dc.destroySelf();
		super.tearDown();
	}
}
