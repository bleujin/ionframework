package net.ion.bleujin.db.postgre;

import java.io.StringReader;
import java.sql.Array;

import net.ion.framework.db.Rows;
import net.ion.framework.util.Debug;
import net.ion.framework.util.StringUtil;

public class TestPostgreProcedure extends TestBasePG{


	public void testSelectProcedure() throws Exception {
		Rows rows = dc.createUserProcedure("emp@listBy(?)").addParam(5).execQuery() ;
		while(rows.next()){
			for (int i = 1; i <= rows.getMetaData().getColumnCount(); i++) {
				Debug.debug(StringUtil.left(rows.getString(i), 20), rows.getMetaData().getColumnTypeName(i)) ;
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
	

	public void testWith() throws Exception {
		Rows rows = dc.createUserCommand("with emptree as (select * from emp_tree(7839)) select * from emptree x1, emp x2 where x1.empno = x2.empno").execQuery();
		while(rows.next()){
			final Array array = rows.getArray("path");
			Object[] rs = (Object[]) array.getArray() ;
			Debug.line(rs.length, rs[0].getClass());
		}
	}
	
	
	
}
