package net.ion.framework.db.cache;

import net.ion.framework.db.DBController;
import net.ion.framework.db.manager.CacheDBManager;
import net.ion.framework.db.procedure.IParameterQueryable;
import net.ion.framework.db.procedure.IUserProcedure;
import net.ion.framework.db.procedure.IUserProcedures;
import net.ion.framework.db.sample.TestBaseSample;
import net.ion.framework.util.Debug;

public class TestCacheQuery extends TestBaseSample {

	public void testUserProcedureBatch() throws Exception {
		CacheConfigImpl config = new CacheConfigImpl(makeCacheConfig());
		CacheDBManager cdbm = new CacheDBManager(config, dc.getDBManager());
		DBController newDC = new DBController(cdbm);
		newDC.initSelf();

		newDC.createUserProcedure("Category@infoBy(?)").addParam("abc").execQuery();
		assertEquals(0, cdbm.getHittingCount());

		newDC.createUserProcedure("Category@infoBy(?)").addParam("abc").execQuery();
		assertEquals(1, cdbm.getHittingCount());

		newDC.createUserProcedureBatch("Category@updateWith(?,?,?)").addParam("abc", 1).addParam("abc", 1).addParam("abc", 1).execUpdate();
		assertEquals(1, cdbm.getHittingCount());

		newDC.createUserProcedure("Category@infoBy(?)").addParam("abc").execQuery();
		assertEquals(1, cdbm.getHittingCount());

		newDC.destroySelf();
	}

	public void testUserProcedures() throws Exception {
		CacheConfigImpl config = new CacheConfigImpl(makeCacheConfig());
		CacheDBManager cdbm = new CacheDBManager(config, dc.getDBManager());
		DBController newDC = new DBController(cdbm);
		newDC.initSelf();

		newDC.createUserProcedure("Category@infoBy(?)").addParam("abc").execQuery();
		assertEquals(0, cdbm.getHittingCount());

		newDC.createUserProcedure("Category@infoBy(?)").addParam("abc").execQuery();
		assertEquals(1, cdbm.getHittingCount());

		IUserProcedures upts = newDC.createUserProcedures("Category");
		upts.add(newDC.createUserProcedureBatch("Category@updateWith(?,?,?)").addParam("abc", 1).addParam("abc", 1).addParam("abc", 1));
		assertEquals(1, cdbm.getHittingCount());
		upts.execUpdate();

		newDC.createUserProcedure("Category@infoBy(?)").addParam("abc").execQuery();
		assertEquals(1, cdbm.getHittingCount());

		newDC.destroySelf();
	}

	public void testReturnThis() throws Exception {
		CacheConfigImpl config = new CacheConfigImpl(makeCacheConfig());
		CacheDBManager cdbm = new CacheDBManager(config, dc.getDBManager());
		DBController newDC = new DBController(cdbm);
		newDC.initSelf();

		IUserProcedure upt = newDC.createUserProcedure("Category@infoBy(?)");
		upt.addParam("abc");

		IParameterQueryable upt2 = newDC.createUserProcedure("Category@infoBy(?)").addParam("abc");
		assertEquals("$Proxy0", upt.getClass().getName());
		assertEquals("$Proxy0", upt2.getClass().getName());

		assertEquals(0, cdbm.getHittingCount());
		upt.execQuery();
		assertEquals(0, cdbm.getHittingCount());
		upt2.execQuery();
		assertEquals(1, cdbm.getHittingCount());

		upt2.execQuery();
		assertEquals(2, cdbm.getHittingCount());

		Debug.debug(upt.getClass().getName(), upt2.getClass().getName());
		newDC.destroySelf();

		Debug.debug(Debug.getRecentMessage());
	}

	public void testEmptyParamQuery() throws Exception {
		CacheConfigImpl config = new CacheConfigImpl(makeCacheConfig());
		CacheDBManager cdbm = new CacheDBManager(config, dc.getDBManager());
		DBController newDC = new DBController(cdbm);
		newDC.initSelf();

		assertEquals(0, cdbm.getHittingCount());

		IUserProcedures upts1 = newDC.createUserProcedures("allSiteOption");
		IUserProcedure upt11 = newDC.createUserProcedure("sITE_CATEGORY@allUseCdBy");
		IUserProcedure upt12 = newDC.createUserProcedure("sITE_CATEGORY@useRssCdBy");
		upts1.add(upt11).add(upt12).execQuery();

		assertEquals(0, cdbm.getHittingCount());

		IUserProcedures upts2 = newDC.createUserProcedures("allSiteOption");
		IUserProcedure upt21 = newDC.createUserProcedure("site_category@allUseCdBy");
		IUserProcedure upt22 = newDC.createUserProcedure("site_category@useRssCdBy");
		upts2.add(upt21).add(upt22).execQuery();

		assertEquals(1, cdbm.getHittingCount());
		newDC.destroySelf();
	}

	private String makeCacheConfig() {
		return ""
				+ "{"
				+ "	cache:["
				+ "	{"
				+ "	  groupId:'category_cach',"
				+ "	  count:5000,"
				+ "	  add:['Category@infoBy', 'Category@retrieveBy', 'Category@pathBy','SITE_CATEGORY@infoBy', 'SITE_CATEGORY@useRssCdBy', 'SITE_CATEGORY@allUseCdBy', 'SITE_CATEGORY@charSetBy'],"
				+ "	  reset:['Category@updateWith','Category@moveWith','SITE_CATEGORY@updateWith','SITE_CATEGORY@moveWith','Migration@importSiteCategory','Migration@importContentCategory','Migration@importSiteRoot']"
				+ "	}" + "	]}";
	}
}
