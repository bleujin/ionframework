package net.ion.framework.db.manager;

import net.ion.framework.db.DBController;
import junit.framework.TestCase;

public class TestReadOnlyDBManager extends TestCase {

	
	private DBController dc;
	private DBController oldDC;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.oldDC = new DBController(OracleDBManager.test());
		oldDC.initSelf(); 
		
		ReadOnlyDBManager dbm = new ReadOnlyDBManager(oldDC.getDBManager()) ;
		this.dc = new DBController(dbm) ;
		dc.initSelf();
	}
	
	@Override
	protected void tearDown() throws Exception {
		dc.destroySelf();
		
		oldDC.destroySelf();
		super.tearDown();
	}

	public void testMake() throws Exception {
		dc.createUserCommand("insert into article_tblt values('mybla', 100, 1)").execUpdate();
		dc.createUserCommand("insert into article_tblt values('mybla', 101, 2)").execUpdate();
		dc.createUserCommand("insert into article_tblt values('mybla', 102, 3)").execUpdate();
		dc.createUserCommand("insert into article_tblt values('mybla', 103, 4)").execUpdate();
		dc.createUserCommand("insert into article_tblt values('mybla', 104, 5)").execUpdate();
		
		dc.createUserCommand("select * from article_tblt").execQuery().debugPrint();
	}
	
	public void testConfirm() throws Exception {
		dc.createUserCommand("select * from article_tblt").execQuery().debugPrint();
	}
}
