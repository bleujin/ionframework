package net.ion.framework.db.sample.rows;

import java.sql.Date;

import net.ion.framework.db.Rows;
import net.ion.framework.db.procedure.IUserCommand;
import net.ion.framework.db.procedure.IUserProcedure;
import net.ion.framework.db.sample.SampleTestBase;


public class RowsGet extends SampleTestBase{


	
	public void testDate() throws Exception {
		String query = "select now() day" ;
		// select a, b, sysdate day from update_sample where a = v_a ;
		Rows rows = dc.createUserProcedure("Sample@selectEmpBy()").execQuery() ;
		
		rows.next() ;
		Date d = rows.getDate("hiredate") ;
		

		assertEquals(true, d.getDate() >= 0) ; // smith
	}

	
	/*
	 * Rows는 기본적으로 ResultSet을 impl한 객체이기 때문에 ResultSet의 메소드에 리스코프 치환원칙 - LSP를 위반하지 않으려고 노력했지만
	 * 단 하나는 의도적으로 다르다. 
	 * 
	 * 바로 Clob(or Text)을 억세스 하는 방법인데. 
	 * Clob에 정말 기가 단위의 텍스트가 있다면 기존의 스트리밍 방식을 사용하는게 맞다. 
	 * 그러나 현실의 99.9%는 고작해야 몇K 정도의 텍스트 정보이고 이는 varchar사이즈의 한계때문에 어쩔수  없는 문제이기도 하다.
	 * (물론 DB 벤더 혹은 버전에 따라 조금씩 차이가 있다. )
	 * 
	 *  
	 * 그래서 Rows는 물론 구현하기에 따라 다르겠지만 기본적인 RowsImpl 구현체는
	 * Get메소드의 경우
	 * 기본 구현된 RowsImpl은 Get을 할때 Clob과 Varchar를 구분하지 않는다. 
	 * 즉 Clob 컬럼을 get할때도 똑같이 getString()으로 값을 얻어올수 있다. 
	 * 
	 * Set의 경우
	 * 일반적으로 Get과 달리 Clob or Text를 insert 하는것은 DB마다 많이 다르기 때문에 하나의 인터페이스를 고려하는 것은 어렵다.
	 *  
	 * 단순히 Message 객체인 IQueryable 객체가 인자로 받는 Param을 Clob으로 처리해야 하는지 String(Varchar)로 처리해야 하는건지 
	 * 판단하는 것은 어렵기 때문에 사람이 addClob처럼 별도의 파라미터를 사용해야 하는 Oracle같은 DB도 있고
	 * 별로 상관없는 DB도 있다. 따라서 이것은 IQueryable를 구현하는 객체에 책임이 있고 구현자는 이에 대한 해결책이 있어야 한다.
	 * 구현재는 Clob(Or Text)를 insert or update할때 addClob param을 사용하도록 구현해야 한다.  
	*/
	
	public void testSetClob() throws Exception {
		StringBuilder textString = new StringBuilder() ;
		for (int i = 0; i < 200; i++) {
			textString.append("01234567890123456789012345678901234567890123456789") ; // 10k
		}
		// insert into lob_sample(a, c) values(v_a, v_b) ;
		IUserProcedure upt = dc.createUserProcedure("Sample@insertLobWith(?,?)") ;
		upt.addParam(1).addClob(textString.toString()) ;
		
		assertEquals(1, upt.execUpdate()) ;
	}
	
	
	public void testGetClob0() throws Exception {
		testSetClob() ;

		IUserCommand cmd = dc.createUserCommand("SELECT a, b, c FROM lob_sample where a = :a") ;
		cmd.addParam("a", 1);
		
		Rows rows = cmd.execQuery();
		assertEquals(true, rows.firstRow().getString("c").length() > 9000) ;

		dc.execUpdate("delete from lob_sample") ;
	}	
	
	public void testGetClob() throws Exception {
		testSetClob() ;

		IUserProcedure upt = dc.createUserProcedure("Sample@selectLobBy(:a)") ;
		upt.addParam("a", 1);
		
		Rows rows = upt.execQuery();
		assertEquals(true, rows.firstRow().getString("c").length() > 9000) ;

		dc.execUpdate("delete from lob_sample") ;
	}
}
