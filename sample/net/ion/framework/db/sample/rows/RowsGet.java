package net.ion.framework.db.sample.rows;

import java.sql.Date;

import net.ion.framework.db.Rows;
import net.ion.framework.db.procedure.IUserCommand;
import net.ion.framework.db.procedure.IUserProcedure;
import net.ion.framework.db.sample.TestBaseSample;


public class RowsGet extends TestBaseSample{


	
	public void testDate() throws Exception {
		String query = "select now() day" ;
		// select a, b, sysdate day from update_sample where a = v_a ;
		Rows rows = dc.createUserProcedure("Sample@selectEmpBy()").execQuery() ;
		
		rows.next() ;
		Date d = rows.getDate("hiredate") ;
		

		assertEquals(true, d.getDate() >= 0) ; // smith
	}

	
	/*
	 * Rows�� �⺻������ ResultSet�� impl�� ��ü�̱� ������ ResultSet�� �޼ҵ忡 �������� ġȯ��Ģ - LSP�� �������� �������� ���������
	 * �� �ϳ��� �ǵ������� �ٸ���. 
	 * 
	 * �ٷ� Clob(or Text)�� �＼�� �ϴ� ����ε�. 
	 * Clob�� ���� �Ⱑ ������ �ؽ�Ʈ�� �ִٸ� ������ ��Ʈ���� ����� ����ϴ°� �´�. 
	 * �׷��� ������ 99.9%�� �����ؾ� ��K ������ �ؽ�Ʈ �����̰� �̴� varchar�������� �Ѱ趧���� ��¿��  ��� �����̱⵵ �ϴ�.
	 * (���� DB ���� Ȥ�� ���� ��� ���ݾ� ���̰� �ִ�. )
	 * 
	 *  
	 * �׷��� Rows�� ���� �����ϱ⿡ ��� �ٸ������� �⺻���� RowsImpl ����ü��
	 * Get�޼ҵ��� ���
	 * �⺻ ������ RowsImpl�� Get�� �Ҷ� Clob�� Varchar�� �������� �ʴ´�. 
	 * �� Clob �÷��� get�Ҷ��� �Ȱ��� getString()���� ���� ���ü� �ִ�. 
	 * 
	 * Set�� ���
	 * �Ϲ������� Get�� �޸� Clob or Text�� insert �ϴ°��� DB���� ���� �ٸ��� ������ �ϳ��� �������̽��� ����ϴ� ���� ��ƴ�.
	 *  
	 * �ܼ��� Message ��ü�� IQueryable ��ü�� ���ڷ� �޴� Param�� Clob���� ó���ؾ� �ϴ��� String(Varchar)�� ó���ؾ� �ϴ°��� 
	 * �Ǵ��ϴ� ���� ��Ʊ� ������ ����� addClobó�� ������ �Ķ���͸� ����ؾ� �ϴ� Oracle���� DB�� �ְ�
	 * ���� ����� DB�� �ִ�. ��� �̰��� IQueryable�� �����ϴ� ��ü�� å���� �ְ� �����ڴ� �̿� ���� �ذ�å�� �־�� �Ѵ�.
	 * ������� Clob(Or Text)�� insert or update�Ҷ� addClob param�� ����ϵ��� �����ؾ� �Ѵ�.  
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
