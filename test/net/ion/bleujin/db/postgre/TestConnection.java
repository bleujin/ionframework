package net.ion.bleujin.db.postgre;

import java.sql.Connection;
import java.util.List;

import junit.framework.TestCase;
import net.ion.framework.db.DBController;
import net.ion.framework.db.manager.DBManager;
import net.ion.framework.db.manager.PostSqlPoolDBManager;
import net.ion.framework.util.Debug;
import net.ion.framework.util.ListUtil;

public class TestConnection extends TestCase {

	public void testPoolConnection() throws Exception {
		DBManager dbm = PostSqlPoolDBManager.test() ;
		DBController dc = new DBController(dbm) ;
		dc.initSelf();
		
		long start = System.currentTimeMillis();
		List<String> cons = ListUtil.newList();
		
		for (int i = 0; i < 500; i++) {
			dc.getRows("select 1") ;
		}
		Debug.line(System.currentTimeMillis() - start);
		dc.destroySelf(); 
	}
	
	
}
