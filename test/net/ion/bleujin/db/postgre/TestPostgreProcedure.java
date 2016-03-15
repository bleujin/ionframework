package net.ion.bleujin.db.postgre;

import java.io.StringReader;

import net.ion.framework.db.Rows;
import net.ion.framework.util.Debug;

public class TestPostgreProcedure extends TestBasePG{


	public void testSelectProcedure() throws Exception {
		Rows rows = dc.createUserProcedure("emp@listBy(?)").addParam(5).execQuery() ;
		while(rows.next()){
			for (int i = 1; i <= rows.getMetaData().getColumnCount(); i++) {
				Debug.debug(rows.getString(i), rows.getMetaData().getColumnTypeName(i)) ;
			}
		}
	}
	
	
	public void testInsertProcedure() throws Exception {
		StringBuilder sb = new StringBuilder() ;
		for (int i = 0; i < 1000; i++) {
			sb.append("hello hero\n") ;
		} 
		
		int rowcount = dc.createUserProcedure("emp@registerWith(?,?,?)").addParam(7800).addParam("hero").addClob(sb).execUpdate() ;
		assertEquals(1, rowcount);
	}
	
	public void testUpdateProcedure() throws Exception {
		
		StringBuilder sb = new StringBuilder() ;
		for (int i = 0; i < 1000; i++) {
			sb.append("hello hero\n") ;
		} 
		
		int rowcount = dc.createUserProcedure("emp@modifyWith(?,?,?)").addParam(7800).addParam("hero").addClob(new StringReader(sb.toString())).execUpdate() ;
		assertEquals(1, rowcount);
	}
	
	
	
	
}
