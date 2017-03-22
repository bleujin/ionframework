package net.ion.framework.db;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.ion.framework.db.bean.handlers.MapListHandler;
import net.ion.framework.db.manager.DBManager;
import net.ion.framework.db.manager.OracleCacheDBManager;
import net.ion.framework.db.manager.OracleDBManager;
import net.ion.framework.util.ArrayUtil;
import net.ion.framework.util.Debug;
import net.ion.framework.util.ListUtil;
import junit.framework.TestCase;

public class TestPage extends TestCase {

	public void testRows() throws Exception {
		Page page = Page.create(10, 2) ;
		Debug.line(page.getStartLoc(), page.getEndLoc()) ;
		
		
		DBManager dbm = OracleDBManager.test();
        //DBManager dbm = new MSSQLDBManager("jdbc:microsoft:sqlserver://dev-sql.i-on.net:1435;DatabaseName=pubs", "bleu", "redf") ;
        DBController dc = new DBController("Default", dbm);
        dc.initSelf() ;
        
        Rows rows = dc.getRows("select * from dual") ;
        Debug.line(rows) ;
        dc.destroySelf() ;
	}
	
	
	public void testMax() throws Exception {
		
		
	}
	
	
	public void testPage() throws Exception {
		Page page = Page.create(2, 2) ;
		Debug.line(page.getStartLoc(), page.getEndLoc()) ;
	}
	
	
	public void testSkipOnScreen() throws Exception {
		Page page = Page.create(10, 2, 10);
		assertEquals(0, page.getSkipOnScreen() ) ;

		page = Page.create(10, 12, 10);
		assertEquals(100, page.getSkipOnScreen() ) ;
	}
	

	public void testOffsetOnScreen() throws Exception {
		assertEquals(101, Page.create(10, 2, 10).getOffsetOnScreen()) ;
		assertEquals(101, Page.create(10, 12, 10).getOffsetOnScreen()) ;

		assertEquals(21, Page.create(2, 2, 10).getOffsetOnScreen()) ;
		assertEquals(21, Page.create(2, 12, 10).getOffsetOnScreen()) ;
	}

	
	public void testSubIterator() throws Exception {
		Iterator<Integer> iter = ListUtil.rangeNum(20).iterator();
		assertEquals(true, Arrays.equals(new Integer[]{5,6,7,8,9}, Page.create(5, 2).subList(iter).toArray(new Integer[0]) )) ;
		
		iter = ListUtil.rangeNum(20).iterator();
		assertEquals(true, Arrays.equals(new Integer[]{6,7,8}, Page.create(3, 3).subList(iter).toArray(new Integer[0]) )) ;
	}

	
	public void testHandlerPage() throws Exception {
		DBController dc = new DBController(OracleDBManager.test()) ;
		dc.initSelf();
		
		final Page page = Page.create(3, 4, 3);
		List<Map<String, ? extends Object>> brows = dc.createUserCommand("select table_name from tabs order by table_name").setPage(page).execHandlerQuery(new MapListHandler()) ;
		Debug.line(brows);
		
		Rows arows = dc.createUserCommand("select table_name from tabs order by table_name").setPage(page).execQuery() ;
		Debug.line(arows.toHandle(new MapListHandler())) ;
		dc.close();
	}
	
	
	
}
