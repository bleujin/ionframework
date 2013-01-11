package net.ion.framework.db.rowset;

import java.util.List;
import java.util.Map;

import junit.framework.TestCase;
import net.ion.framework.db.DBController;
import net.ion.framework.db.IDBController;
import net.ion.framework.db.Rows;
import net.ion.framework.db.bean.BasicRowProcessor;
import net.ion.framework.db.bean.BeanProcessor;
import net.ion.framework.db.bean.handlers.BeanListHandler;
import net.ion.framework.db.bean.handlers.MapListHandler;
import net.ion.framework.db.cache.CacheConfig;
import net.ion.framework.db.cache.CacheConfigImpl;
import net.ion.framework.db.manager.CacheDBManager;
import net.ion.framework.db.manager.DBManager;
import net.ion.framework.db.manager.OracleCacheDBManager;
import net.ion.framework.db.procedure.IUserCommand;
import net.ion.framework.util.Debug;

/**
 * <p>Title: TestClobString.java</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2007</p>
 * <p>Company: I-ON Communications</p>
 * <p>Date : 2013. 1. 11.</p>
 * @author novision
 * @version 1.0
 */

public class TestClobString extends TestCase{

	
	public void testClobString() throws Exception {
		String cacheString = "{ cache:[ { groupId:'category_cach', count:5000, add:['Select ARTCONT from article_tblc where artId = 1204636 and rownum = 1'], reset:[] } ]}" ;
		
		DBManager dbm = new OracleCacheDBManager("jdbc:oracle:thin:@dev-oracle.i-on.net:1521:DEV10G", "dev_ics6", "dev_ics6") ;
		CacheConfig config = new CacheConfigImpl(cacheString);
		CacheDBManager cdbm = new CacheDBManager(config, dbm) ;
		
		IDBController dc = new DBController(cdbm) ;
		dc.initSelf();
		
		IUserCommand proc = dc.createUserCommand("Select ARTCONT from article_tblc where artId = 1204636 and rownum = 1");
		for (int i = 0; i < 5; i++) {
			Rows rows = dc.getRows(proc);
			List<ClobContent> list = rows.toHandle(new BeanListHandler<ClobContent>(ClobContent.class));
//			List<Map<String, ? extends Object>> list = rows.toHandle(new MapListHandler());
//			List<ClobContent> list = dc.execHandlerQuery(proc, new BeanListHandler<ClobContent>(ClobContent.class));
			
			Debug.line(list.get(0), list.get(0).getArtCont()) ;
		}
		
		dc.destroySelf() ;
	}
}



class ClobContent {
	private String artCont ;

	public void setArtCont(String artCont) {
		this.artCont = artCont;
	}

	public String getArtCont() {
		return artCont;
	}
	
}
