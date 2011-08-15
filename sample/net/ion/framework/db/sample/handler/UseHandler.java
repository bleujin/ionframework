package net.ion.framework.db.sample.handler;

import java.util.List;
import java.util.Map;

import net.ion.framework.db.Page;
import net.ion.framework.db.Rows;
import net.ion.framework.db.bean.handlers.BeanListHandler;
import net.ion.framework.db.bean.handlers.MapHandler;
import net.ion.framework.db.bean.handlers.MapListHandler;
import net.ion.framework.db.bean.handlers.ScalarHandler;
import net.ion.framework.db.sample.SampleTestBase;


public class UseHandler extends SampleTestBase {

	String query = "select * from copy_sample order by no1" ;
	
	public void testMapHandler() throws Exception {
		Rows rows = dc.getRows(query, Page.TEN) ;
		Map results = (Map)rows.toHandle(new MapHandler()) ;
		

		assertEquals(2, results.size()) ;

		assertEquals("1", results.get("no1").toString()) ; 
		// 왜 int 1이 아니냐면 DB마다 숫자를 표현하는 방식이 조금씩 다른데 
		// 오라클의 Number는 java.math.BigDecimial로 치환된다.
		assertEquals("01", results.get("no2")) ;
	}
	
	
	public void testMapList() throws Exception {
		Rows rows = dc.getRows(query, Page.TEN) ;
		
		List<Map> results = (List<Map>)rows.toHandle(new MapListHandler()) ;
		
		assertEquals(10, results.size()) ;
		Map row = results.get(0);
		
		assertEquals("1", row.get("no1").toString()) ;
		assertEquals("01", row.get("no2")) ;
	}
	
	
	public void testBeanList() throws Exception {
		Rows rows = dc.getRows(query, Page.TEN) ;
		
		List<TestBean> results = (List<TestBean>)rows.toHandle(new BeanListHandler(TestBean.class)) ;
		assertEquals(10, results.size()) ;

		TestBean row = results.get(0);
		assertEquals(1, row.getNo1()) ;
		assertEquals("01", row.getNo2()) ;

		assertEquals(2, results.get(1).getNo1()) ;
		assertEquals("02", results.get(1).getNo2()) ;
	
	}
	
	public void testScalar() throws Exception {
		Rows rows = dc.getRows(query, Page.TEN) ;
		Object value = rows.toHandle(new ScalarHandler()) ;
		
		assertEquals("1", value.toString()) ;
		
		
		rows = dc.getRows(query, Page.TEN) ;
		Object secondValue = rows.toHandle(new ScalarHandler(2)) ;
		assertEquals("01", secondValue) ;
	}
	
	
	
	
	
	
	
	
}
