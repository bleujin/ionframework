package net.ion.framework.db.sample.extend;

import net.ion.framework.db.Page;
import net.ion.framework.db.Rows;
import net.ion.framework.db.procedure.IUserCommand;
import net.ion.framework.db.sample.TestBaseSample;


public class QueryPage extends TestBaseSample{
	

	/*
	 * ��� IQueryable��ü���� Page�� �����Ҽ� �ִ�. 
	 * Page�� ��� ó���ϴ°��� DB ���� ���ݾ� �ٸ����� �� DBManager�� ���� ��� �ּ��� ����� �����ϵ��� �Ǿ� �ִ�. 
	 * Page�� ������ Page.creae(listNum, pageNo)�� Static �޼ҵ�� ���Ͽ�
	 * setPage(Page page)�� �����Ѵ�. 
	 * listNum�� 10�̰� pageNo�� 2�̸� 10���� ������ �ι�° ������(11~20)�� ���Ѵ�.
	 * 
	 * ���� ����ڰ� �ƹ��� �������� �������� �ʾ�������
	 * dc�� setLimit�� ������ ������ŭ���� select�Ѵ�. 
	 */
	
	public void testRowsPage() throws Exception {
		String query = "select * from copy_sample" ;
		// listNum = 1 -> count per page 
		// No = 1      -> page no 
		Rows rows = dc.getRows(query, Page.create(1, 1)) ;
		assertEquals(1, rows.getRowCount()) ;
	}
	
	public void testRowsPageMethod2() throws Exception {
		String query = "select * from copy_sample" ;
		
		IUserCommand cmd = dc.createUserCommand(query) ;
		cmd.setPage(Page.create(1, 1)) ;
		Rows rows = cmd.execQuery() ;
		assertEquals(1, rows.getRowCount()) ;
	}

	
	
	/*
	 * ���� �ڼ��� page������ com.bleujin.framework.db.sample.rows.RowsPage�� �ִ�. 
	 */

	
}
