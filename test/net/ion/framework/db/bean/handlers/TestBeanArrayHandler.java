package net.ion.framework.db.bean.handlers;

import net.ion.framework.db.DBTestCase;
import net.ion.framework.db.RowsImpl;

public class TestBeanArrayHandler extends DBTestCase{

	
	public void testNotNull() throws Exception {
		BeanArrayHandler<Person> handler = new BeanArrayHandler<Person>(Person.class);
		Person[] ps = handler.handle(dc.getRows("select * from dual where 1 = 2"));
		assertEquals(true, ps != null) ;
		assertEquals(true, ps.length == 0) ;
		
		assertEquals(true, BeanArrayHandler.handle(dc.getRows("select * from dual where 1 = 2"), Person.class) != null) ;
	}
	
	public void testOutNull() throws Exception {
		System.out.println(java.sql.Timestamp.class.getName()) ;
	}
}


class Person {
	
}