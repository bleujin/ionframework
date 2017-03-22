package net.ion.framework.db.manager;

import net.ion.framework.db.DBController;
import junit.framework.TestCase;

public class TestReadOnlyDBManager extends TestCase {

	
	private DBController dc;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		ReadOnlyDBManager dbm = new ReadOnlyDBManager(OracleDBManager.test()) ;
		this.dc = new DBController(dbm) ;
		
		dbm.setUp(dc.createUserCommand("insert into article_tblt values('mybla', 100, 1)")) ;
		
		dc.initSelf();
	}
	
	@Override
	protected void tearDown() throws Exception {
		dc.destroySelf();
		super.tearDown();
	}

	public void testMake() throws Exception {
		dc.createUserCommand("select * from article_tblt").execQuery().debugPrint();
	}
}
