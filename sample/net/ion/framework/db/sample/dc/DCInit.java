package net.ion.framework.db.sample.dc;

import net.ion.framework.db.DBController;
import net.ion.framework.db.Rows;
import net.ion.framework.db.sample.SampleTestBase;
import net.ion.framework.db.servant.StdOutServant;


public class DCInit extends SampleTestBase{

	/*
	 * IDBController는 IQueryable 구현체를 생성하는 팩토리 역활과 
	 * ServantHandle 역할을 가지고 있다.
	 * 
	 *  좀더 추상적으로 하나의 역할로 줄인다면 DBManager를 Wrapping해서 다양한 상황에 디펜던트한 부분을 처리하기 위해서이다. 
	 *  
	 *  먼저 PooledConnection을 쓴다면 IDBController는 하나만 존재해야 의미가 있다. 
	 *  보통은 프로그램의 init 부분에 dc.initSelf을 호출하고
	 *  프로그램의 종료시에 dc.destorySelf를 호출해야 한다. 
	 *  만약 웹에 사용된다면 servlet init과 destory시에 호출하게 될것이다. 
	 *  PoolConnection은 보통 전역적으로 사용되기 때문에 dc는 일반적으로 Static 형으로 참조가 되지만 항상 그래야 할 필요는 없다.   
	 */
	
	
	
	/*
	 * 아래와 같이 이전의 IDBController가 사용하는 DBManager를 가져와서 새로운  IDBController를 만들수 있다.
	 * 그러나 DBManager는 Owner DBController을 인식하고 있기 때문에
	 * 빌려쓰는 IDBController가 init 혹은 destroy 한다고 해서 기존의 DBController의 DBManger를 해제하지 않는다.
	 * 
	 * DBController의 Init 메소드는 DBManger의 풀을 활성화시키고 Servant 쓰레드를 시작시킨다. 
	 */
	public void testDCInit() throws Exception {
		DBController newDc = new DBController("newDC", dc.getDBManager()) ;
		newDc.initSelf() ;
		
		Rows rows = newDc.getRows("select 1 from copy_sample") ;
		newDc.destroySelf() ;

		assertEquals(1, rows.firstRow().getInt(1)) ;
	}
	
	
	/*
	 * 오직 DBManger의 Owner만이 init과 destory를 시킨다면 
	 * 굳이 같은 DBManger를 공유하는 여러개의 DBController를 허용해야 할까라는 의문을 던질수 있다. 
	 * 어차피 같은 Connection Pool을 사용할텐데 말이다. 
	 * 
	 * 그러나 사용자는 같은 DBManger를 사용해도 다른 Servant를 사용하는 DBController를 정의할수 있다. 
	 * Servant는 쿼리가 실행되고 난뒤에 실행할 어떤것을 구현하는 클래스이다. 
	 * 
	 * 예컨데 StdOutServant는 쿼리를 실행한후 쿼리의 간단한 정보와 실행시간을 StdOut에 찍는다.
	 */
	
	public void testServant() throws Exception {
		DBController newDc = new DBController("newDC", dc.getDBManager()) ;
		newDc.addServant(new StdOutServant(StdOutServant.All)) ; // 모든 IQueryable를 화면에 프린트하는 Servant를 추가한다. 
		newDc.initSelf() ;
		
		newDc.getRows("select 1 from copy_sample") ;
		newDc.destroySelf() ;
	}
	
	
	

	/*
	 * Servant는 Chain으로 연결되기 때문에 새로운 Servant를 정의해서 add시켜주면 
	 * 하나의 퀴리가 실행될때마다 Chain된 Servant들이 처리된다. 
	 * 
	 * 기본적인 DBController는 Sevant를 별도의 Thread로 처리하기 때문에 Servant로 인한 쿼리의 지연이 최소화된다.
	 * 
	 *  Servant의 활용은 다양하다. 
	 *  특정 쿼리가 실행될때마다 모니터링 하거나 어떤 동작을 하는 Servant를 할수 있고..
	 *  얼마 이상의 쿼리 실행시간을 기록하는 쿼리만을 별도로  기록하는 ExtraServant를 만들수도 있다. 
	 *  
	 */

	public void testServantChain() throws Exception {
		DBController newDc = new DBController("newDC", dc.getDBManager()) ;
		newDc.addServant(new StdOutServant(StdOutServant.All)) ; // 모든 IQueryable를 화면에 프린트하는 Servant를 추가한다. 
		newDc.addServant(new StdOutServant(StdOutServant.All)) ; // 한개 더 추가한다. 
		newDc.initSelf() ;
		
		newDc.getRows("select 1 from copy_sample") ;
		newDc.destroySelf() ;
	}

	
}
