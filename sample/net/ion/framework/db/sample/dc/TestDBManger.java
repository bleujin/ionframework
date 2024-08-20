package net.ion.framework.db.sample.dc;

import junit.framework.TestCase;
import net.ion.framework.db.DBController;
import net.ion.framework.db.cache.CacheConfig;
import net.ion.framework.db.cache.CacheConfigImpl;
import net.ion.framework.db.h2.H2EmbedPoolDBManager;
import net.ion.framework.db.manager.CacheDBManager;
import net.ion.framework.db.manager.DBManager;
import net.ion.framework.db.manager.OracleCacheDBManager;


public class TestDBManger extends TestCase {

	public void testOtherManager() throws Exception {
		
		DBManager dbm = H2EmbedPoolDBManager.test() ;
		DBController dc = new DBController(dbm) ;
		dc.initSelf() ;
		
		dc.createUserProcedure("emp@createtable").execUpdate() ;
		dc.createUserProcedure("emp@insert(?,?)").addParam(10).addParam("bleujin").execUpdate() ;
		
		dc.createUserProcedure("emp@select").execQuery().debugPrint();
		
		
		dc.destroySelf();
		
	}
	

	public void testTwicePool() throws Exception {
		DBManager dbm = OracleCacheDBManager.test() ;
		DBController dc1 = new DBController(dbm) ;
		
		
		CacheConfig config = new CacheConfigImpl("");
		DBController dc2 = new DBController(new CacheDBManager(config, dbm)) ;

		dc1.initSelf();
		dc2.initSelf(); 
		
		dc1.destroySelf();
		dc2.destroySelf(); 
	}
	
}
