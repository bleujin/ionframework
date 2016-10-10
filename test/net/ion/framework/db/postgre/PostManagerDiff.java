package net.ion.framework.db.postgre;

import net.ion.framework.db.DBController;
import net.ion.framework.db.manager.DBManager;
import net.ion.framework.db.manager.PostSqlDBManager;
import net.ion.framework.db.manager.PostSqlDataSource;
import net.ion.framework.db.manager.PostSqlPoolDBManager;
import junit.framework.TestCase;

public class PostManagerDiff extends TestCase {

	
	public void testStd() throws Exception {
		DBManager dbm = PostSqlDBManager.test() ;
		DBController dc = new DBController(dbm) ;
		dc.initSelf(); 
		
		for (int i = 0; i < 1000 ; i++) {
			dc.getRows("select 1") ;
		}
		dc.destroySelf(); 
	}
	
	public void testPool() throws Exception {
		DBManager dbm = PostSqlPoolDBManager.test() ;
		DBController dc = new DBController(dbm) ;
		dc.initSelf(); 
		
		for (int i = 0; i < 1000 ; i++) {
			dc.getRows("select 1") ;
		}
		dc.destroySelf(); 
	}
	
	public void testDatasource() throws Exception {
		DBManager dbm = PostSqlDataSource.test() ;
		DBController dc = new DBController(dbm) ;
		dc.initSelf(); 
		
		for (int i = 0; i < 1000 ; i++) {
			dc.getRows("select 1") ;
		}
		dc.destroySelf(); 
	}
	
	public void testSelect() throws Exception {
		DBManager dbm = PostSqlDataSource.test() ;
		DBController dc = new DBController(dbm) ;
		dc.initSelf(); 
		
		dc.getRows("select * from emp limit 100").debugPrint(); ;
		dc.destroySelf(); 
	}
}
