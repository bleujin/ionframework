package net.ion.framework.db;

import java.io.File;
import java.io.FileInputStream;

import junit.framework.TestCase;
import net.ion.framework.db.cache.CacheConfigImpl;
import net.ion.framework.db.manager.CacheDBManager;
import net.ion.framework.db.manager.DBManager;
import net.ion.framework.db.manager.OracleDBManager;
import net.ion.framework.db.servant.StdOutServant;

import org.apache.commons.io.IOUtils;

public class DBTestCaseByBleujin extends TestCase{

	protected DBController dc ;
	protected DBManager dbm ;
	public void setUp() throws Exception {
		this.dbm = new OracleDBManager("jdbc:oracle:thin:@dev-test.i-on.net:1521:devTest", "bleu", "redf") ;
		final File file = new File("./ics/WEB-INF/cache-config.js");
		if (file.exists()){
			String cacheConfigString = IOUtils.toString(new FileInputStream(file), "UTF-8");
			CacheConfigImpl cacheConfig = new CacheConfigImpl(cacheConfigString);
			this.dbm = new CacheDBManager(cacheConfig, this.dbm);
		}
		this.dc = new DBController("TestCase", dbm, new StdOutServant()) ;
		dc.initSelf() ;
	}
	
	public void tearDown() throws Exception{
		dc.destroySelf() ;
	}
	
	public void xtestRows() throws Exception {
		Rows rows = dc.getRows("select 1 from dual") ;
	}
}