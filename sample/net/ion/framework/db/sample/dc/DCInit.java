package net.ion.framework.db.sample.dc;

import net.ion.framework.db.DBController;
import net.ion.framework.db.Rows;
import net.ion.framework.db.sample.TestBaseDB;
import net.ion.framework.db.servant.StdOutServant;


public class DCInit extends TestBaseDB{

	/*
	 * IDBController�� IQueryable ����ü�� ���ϴ� ���丮 ��Ȱ�� 
	 * ServantHandle ������ ������ �ִ�.
	 * 
	 *  ���� �߻������� �ϳ��� ���ҷ� ���δٸ� DBManager�� Wrapping�ؼ� �پ��� ��Ȳ�� �����Ʈ�� �κ��� ó���ϱ� ���ؼ��̴�. 
	 *  
	 *  ���� PooledConnection�� ���ٸ� IDBController�� �ϳ��� �����ؾ� �ǹ̰� �ִ�. 
	 *  ������ ���α׷��� init �κп� dc.initSelf�� ȣ���ϰ�
	 *  ���α׷��� ����ÿ� dc.destorySelf�� ȣ���ؾ� �Ѵ�. 
	 *  ���� ���� ���ȴٸ� servlet init�� destory�ÿ� ȣ���ϰ� �ɰ��̴�. 
	 *  PoolConnection�� ���� �������� ���Ǳ� ������ dc�� �Ϲ������� Static ������ ������ ������ �׻� �׷��� �� �ʿ�� ���.   
	 */
	
	
	
	/*
	 * �Ʒ��� ���� ������ IDBController�� ����ϴ� DBManager�� �����ͼ� ���ο�  IDBController�� ����� �ִ�.
	 * �׷��� DBManager�� Owner DBController�� �ν��ϰ� �ֱ� ������
	 * �������� IDBController�� init Ȥ�� destroy �Ѵٰ� �ؼ� ������ DBController�� DBManger�� �������� �ʴ´�.
	 * 
	 * DBController�� Init �޼ҵ�� DBManger�� Ǯ�� Ȱ��ȭ��Ű�� Servant �����带 ���۽�Ų��. 
	 */
	public void testDCInit() throws Exception {
		DBController newDc = new DBController("newDC", dc.getDBManager()) ;
		newDc.initSelf() ;
		
		Rows rows = newDc.getRows("select 1 from copy_sample") ;
		newDc.destroySelf() ;

		assertEquals(1, rows.firstRow().getInt(1)) ;
	}
	
	
	/*
	 * ���� DBManger�� Owner���� init�� destory�� ��Ų�ٸ� 
	 * ���� ���� DBManger�� �����ϴ� �������� DBController�� ����ؾ� �ұ��� �ǹ��� ����� �ִ�. 
	 * ������ ���� Connection Pool�� ������ٵ� ���̴�. 
	 * 
	 * �׷��� ����ڴ� ���� DBManger�� ����ص� �ٸ� Servant�� ����ϴ� DBController�� �����Ҽ� �ִ�. 
	 * Servant�� ������ ����ǰ� ���ڿ� ������ ����� �����ϴ� Ŭ�����̴�. 
	 * 
	 * ������ StdOutServant�� ������ �������� ������ ������ ������ ����ð��� StdOut�� ��´�.
	 */
	
	public void testServant() throws Exception {
		DBController newDc = new DBController("newDC", dc.getDBManager()) ;
		newDc.addServant(new StdOutServant(StdOutServant.All)) ; // ��� IQueryable�� ȭ�鿡 ����Ʈ�ϴ� Servant�� �߰��Ѵ�. 
		newDc.initSelf() ;
		
		newDc.getRows("select 1 from copy_sample") ;
		newDc.destroySelf() ;
	}
	
	
	

	/*
	 * Servant�� Chain���� ����Ǳ� ������ ���ο� Servant�� �����ؼ� add�����ָ� 
	 * �ϳ��� �� ����ɶ����� Chain�� Servant���� ó���ȴ�. 
	 * 
	 * �⺻���� DBController�� Sevant�� ������ Thread�� ó���ϱ� ������ Servant�� ���� ������ ������ �ּ�ȭ�ȴ�.
	 * 
	 *  Servant�� Ȱ���� �پ��ϴ�. 
	 *  Ư�� ������ ����ɶ����� ����͸� �ϰų� � ������ �ϴ� Servant�� �Ҽ� �ְ�..
	 *  �� �̻��� ���� ����ð��� ����ϴ� �������� ������  ����ϴ� ExtraServant�� ������� �ִ�. 
	 *  
	 */

	public void testServantChain() throws Exception {
		DBController newDc = new DBController("newDC", dc.getDBManager()) ;
		newDc.addServant(new StdOutServant(StdOutServant.All)) ; // ��� IQueryable�� ȭ�鿡 ����Ʈ�ϴ� Servant�� �߰��Ѵ�. 
		newDc.addServant(new StdOutServant(StdOutServant.All)) ; // �Ѱ� �� �߰��Ѵ�. 
		newDc.initSelf() ;
		
		newDc.getRows("select 1 from copy_sample") ;
		newDc.destroySelf() ;
	}

	
}
