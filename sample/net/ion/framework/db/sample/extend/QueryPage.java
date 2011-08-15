package net.ion.framework.db.sample.extend;

import net.ion.framework.db.Page;
import net.ion.framework.db.Rows;
import net.ion.framework.db.procedure.IUserCommand;
import net.ion.framework.db.sample.SampleTestBase;


public class QueryPage extends SampleTestBase{
	

	/*
	 * 모든 IQueryable객체에는 Page를 설정할수 있다. 
	 * Page를 어떻게 처리하는가는 DB 마다 조금씩 다르지만 각 DBManager의 종류에 따라 최선의 방법을 선택하도록 되어 있다. 
	 * Page는 설정은 Page.creae(listNum, pageNo)의 Static 메소드로 생성하여
	 * setPage(Page page)로 설정한다. 
	 * listNum이 10이고 pageNo가 2이면 10개씩 했을때 두번째 페이지(11~20)를 뜻한다.
	 * 
	 * 만약 사용자가 아무런 페이지도 설정하지 않았을때는
	 * dc의 setLimit에 설정된 갯수만큼만을 select한다. 
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
	 * 좀더 자세한 page사용법은 com.bleujin.framework.db.sample.rows.RowsPage에 있다. 
	 */

	
}
