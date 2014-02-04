package net.ion.framework.db.sample.rows;

import net.ion.framework.db.Page;
import net.ion.framework.db.Rows;
import net.ion.framework.db.procedure.IUserCommand;
import net.ion.framework.db.sample.TestBaseSample;


public class RowsPage extends TestBaseSample{
	
	String query1 = "select * from copy_sample order by no1" ;
	
	public void testDefaultLimit() throws Exception {
		dc.setLimitedRows(20) ;
		IUserCommand cmd = dc.createUserCommand(query1) ;
		
		Rows rows = cmd.execQuery() ;
		assertEquals(20, rows.getRowCount()) ;

		rows.absolute(20) ;
		assertEquals(20, rows.getInt("no2")) ;
	}
	
	
	public void testNextPage() throws Exception {
		IUserCommand cmd = dc.createUserCommand(query1) ;
		cmd.setPage(Page.create(10, 1)) ;  
		
		Rows rows = cmd.execQuery() ; // 1page
		assertEquals(10, rows.getRowCount()) ;
		assertEquals(1, rows.firstRow().getInt("no2")) ;
		
		
		Rows nextRows = rows.nextPageRows() ; // 2page
		assertEquals(10, nextRows.getRowCount()) ;
		assertEquals(11, nextRows.firstRow().getInt("no2")) ;
	}

	
	public void testPrePage() throws Exception {
		IUserCommand cmd = dc.createUserCommand(query1) ;
		cmd.setPage(Page.create(10, 2)) ;  // 2page
		
		Rows rows = cmd.execQuery() ;
		assertEquals(10, rows.getRowCount()) ;
		assertEquals(11, rows.firstRow().getInt("no2")) ;
		
		
		Rows preRows = rows.prePageRows() ;  // 1page
		assertEquals(10, preRows.getRowCount()) ;
		assertEquals(1, preRows.firstRow().getInt("no2")) ;
	}

	
	
}
