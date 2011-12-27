package net.ion.framework.db.servant;

import net.ion.framework.db.DBController;
import net.ion.framework.db.IDBController;
import net.ion.framework.db.manager.DBManager;
import net.ion.framework.db.manager.OracleDBManager;
import net.ion.framework.util.ListUtil;
import junit.framework.TestCase;

public class TestChannelServant extends TestCase{
	
	public void testPutTask() throws Exception {
		ChannelServant cs = new ChannelServant(new ExceptionServant(), 10) ;
		DBManager dbm = new OracleDBManager("jdbc:oracle:thin:@dev-oracle.i-on.net:1521:dev10g", "bleu", "redf");
		IDBController dc = new DBController("tst", dbm, cs);
		dc.initSelf();

		for (int i : ListUtil.rangeNum(20)) {
			dc.getRows("select 1 from dual") ;
		}
		
		dc.destroySelf() ;
	}

}

class NoActionServant implements IExtraServant{

	private int i = 0 ;
	public void support(AfterTask atask) {
		if (i++ % 100 == 0) System.out.println() ;
		System.out.print(".") ;
	}
	
}

class ExceptionServant implements IExtraServant {

	public void support(AfterTask atask) {
		throw new NumberFormatException("test exception") ;
	}
	
}