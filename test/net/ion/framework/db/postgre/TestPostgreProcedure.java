package net.ion.framework.db.postgre;

import java.io.StringReader;
import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;

import net.ion.framework.db.Rows;
import net.ion.framework.db.bean.ResultSetHandler;
import net.ion.framework.db.procedure.IUserCommandBatch;
import net.ion.framework.db.procedure.IUserProcedureBatch;
import net.ion.framework.util.Debug;
import net.ion.framework.util.StringUtil;

public class TestPostgreProcedure extends TestBasePG{


	
	public void testQuery() throws Exception {
		Debug.line() ;

		Rows rows = dc.createUserProcedure("test@copyby(?)").addParam(5).execQuery() ; 
		rows.debugPrint();
		// dc.createUserProcedure("test@copyby(5)").execQuery().debugPrint();
	}
	
	public void testSelectProcedure() throws Exception {
		Rows rows = dc.createUserProcedure("test@listBy(?)").addParam(5).execQuery() ;
		while(rows.next()){
			for (int i = 1; i <= rows.getMetaData().getColumnCount(); i++) {
				Debug.debug(StringUtil.left(rows.getString(i), 20), rows.getMetaData().getColumnTypeName(i)) ;
			}
		}
	}
	
	public void testHandlerQuery() throws Exception {
		dc.createUserProcedure("test@listBy(?)").addParam(5).execHandlerQuery(new ResultSetHandler<Void>() {
			public Void handle(ResultSet rs) throws SQLException {
				while(rs.next()){
					Debug.line(rs.getString(1));
				}
				return null;
			}
		}) ;
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
	
	
	public void testUserCommandBatch() throws Exception {
		IUserCommandBatch cmd = dc.createUserCommandBatch("insert into dummy values(?,?)") ;
		int count = cmd.addParam(new int[]{1,2}).addParam(new String[]{"bleujin", "hero"}).execUpdate() ;
		Debug.line(count);
	}
	
	
	public void testBatchProcedure() throws Exception {
		IUserProcedureBatch upt = dc.createUserProcedureBatch("test@add_dummy(?,?)") ;
		int count = upt.addParam(new int[]{1,2}).addParam(new String[]{"bleujin", "hero"}).execUpdate() ;
	}
	
	public void testSelectTable() throws Exception {
		dc.createUserCommand("select afieldid, afield_down(afieldid, true) from afield_tblc").execQuery().debugPrint();
	}
	
	
	public void testBatch() throws Exception {
		final IUserProcedureBatch proc = dc.createUserProcedureBatch("thoth@initTrigger(?,?,?)");
		proc.addParam(0, new String[0]) ;
		proc.addParam(1, new String[0]) ;
		proc.addParam(2, new String[0]) ;
		
		dc.execUpdate(proc) ;
	}
	
	
	
	
	
}
