package net.ion.framework.db.bean.handlers;

import java.util.List;

import net.ion.framework.db.async.H2TestCase;
import net.ion.framework.util.Debug;
import junit.framework.TestCase;

public class TestResultSetHandlerGeneric extends H2TestCase {
	
	public void testArrayList() throws Exception {
		dc.createUserProcedure("emp@createtable").execUpdate() ;
		
		dc.createUserProcedure("emp@insert(?,?)").addParam("empno", 1).addParam("ename", "bleujin").execUpdate() ;
		dc.createUserProcedure("emp@insert(?,?)").addParam("empno", 2).addParam("ename", "hero").execUpdate() ;
		dc.createUserProcedure("emp@insert(?,?)").addParam("empno", 3).addParam("ename", "jin").execUpdate() ;
		
		
		List<Object[]> list = dc.createUserProcedure("emp@select").execQuery().toHandle(new ArrayListHandler());
		Debug.line(list, list.get(0)) ;
	}

	
	public void testBeanHandler() throws Exception {
		dc.createUserProcedure("emp@createtable").execUpdate() ;
		
		dc.createUserProcedure("emp@insert(?,?)").addParam("empno", 1).addParam("ename", "bleujin").execUpdate() ;
		
		Employee emp = dc.createUserProcedure("emp@select").execQuery().toHandle(new BeanHandler<Employee>(Employee.class));
		assertEquals(1, emp.getEmpno()) ;
		assertEquals("bleujin", emp.getEname()) ;
		
		
		List<Employee> list = dc.createUserProcedure("emp@select").execQuery().toHandle(new BeanListHandler<Employee>(Employee.class));
		emp = list.get(0) ;
		assertEquals(1, emp.getEmpno()) ;
		assertEquals("bleujin", emp.getEname()) ;
	}
	
	public void testStringArray() throws Exception {
		dc.createUserProcedure("emp@createtable").execUpdate() ;
		
		dc.createUserProcedure("emp@insert(?,?)").addParam("empno", 1).addParam("ename", "bleujin").execUpdate() ;
		dc.createUserProcedure("emp@insert(?,?)").addParam("empno", 2).addParam("ename", "hero").execUpdate() ;
		
		Object datas = dc.createUserProcedure("emp@select").execQuery().toHandle(new StringArrayHandler());
	}
}


class Employee {
	private int empno ;
	private String ename ;
	
	public void setEmpno(int empno) {
		this.empno = empno;
	}
	public int getEmpno() {
		return empno;
	}
	public void setEname(String ename) {
		this.ename = ename;
	}
	public String getEname() {
		return ename;
	}
}
