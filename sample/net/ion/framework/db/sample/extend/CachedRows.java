package net.ion.framework.db.sample.extend;

import net.ion.framework.db.DBController;
import net.ion.framework.db.Rows;
import net.ion.framework.db.cache.CacheConfigImpl;
import net.ion.framework.db.manager.CacheDBManager;
import net.ion.framework.db.procedure.IParameterQueryable;
import net.ion.framework.db.procedure.IUserProcedure;
import net.ion.framework.db.procedure.IUserProcedureBatch;
import net.ion.framework.db.procedure.IUserProcedures;
import net.ion.framework.db.sample.TestBaseSample;


public class CachedRows extends TestBaseSample{

	public void xtestUseCache() throws Exception {
		Rows rows = dc.getRows("select * from copy_tblc") ;
		
		assertEquals(false, rows == rows.refreshRows(false)) ;
		assertEquals(true, rows == rows.refreshRows(true)) ;
		
		
		dc.execUpdate("insert into update_sample values(1, 'aa')"); 
		
		assertEquals(false, rows == rows.refreshRows(true)) ;
		
		Rows cached = rows.refreshRows(true) ;
		assertEquals(true, cached == cached.refreshRows(true)) ;
	}
	
	public void testManagerCache() throws Exception {
		CacheConfigImpl cacheConfig = new CacheConfigImpl(makeCacheConfig()) ;
		CacheDBManager cdb = new CacheDBManager(cacheConfig, dc.getDBManager());
		DBController newDC = new DBController(cdb) ;
		newDC.initSelf() ;

		IUserProcedure upt = newDC.createUserProcedure("Category@infoBy(?)") ;
		IParameterQueryable zz =  upt.addParam("abc") ;
		upt.execQuery();
		assertEquals(0, cdb.getHittingCount()) ;
		
		newDC.createUserProcedure("Category@infoBy(?)").addParam("abc").execQuery();
		assertEquals(1, cdb.getHittingCount()) ;

		IUserProcedures upts = newDC.createUserProcedures("procs") ;
		IUserProcedureBatch ibatch = newDC.createUserProcedureBatch("Category@updateWith(?,?,?)") ;
		ibatch.addParam("abc", 2).addParam("abc", 2).addParam("abc", 2) ;
		upts.add(ibatch) ;
		upts.execUpdate() ;
		
		newDC.createUserProcedure("Category@infoBy(?)").addParam("abc").execQuery();
		assertEquals(1, cdb.getHittingCount()) ;
		
		newDC.createUserProcedure("Category@infoBy(?)").addParam("abc").execQuery();
		assertEquals(2, cdb.getHittingCount()) ;
		
		newDC.destroySelf() ;
	}
	
	private String makeCacheConfig(){
		return "{" + 
			"cache:[" +
			"{" +
			"  groupId:'category_cach'," +
			"  count:5000," +
			"  add:['Category@infoBy', 'Category@retrieveBy', 'Category@pathBy', 'Category@existCatBy', 'SITE_CATEGORY@infoBy', 'SITE_CATEGORY@charSetBy']," +
			"  reset:['Category@updateWith','Category@moveWith','SITE_CATEGORY@updateWith','SITE_CATEGORY@moveWith','Migration@importSiteCategory','Migration@importContentCategory','Migration@importSiteRoot']" +
			"}]}" ;
	}
	
}
