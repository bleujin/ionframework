package net.ion.framework.db.bleujin;

import junit.framework.TestCase;
import net.ion.framework.db.DBController;
import net.ion.framework.db.IDBController;
import net.ion.framework.db.manager.DBManager;
import net.ion.framework.db.manager.OracleCacheDBManager;
import net.ion.framework.db.procedure.IUserProcedure;

public class LobTest extends TestCase{

	public void testLobInsert() throws Exception {
		DBManager dbm = new OracleCacheDBManager("jdbc:oracle:thin:@dev-test.i-on.net:1521:devTest", "bleu", "redf") ;
		IDBController dc = new DBController(dbm) ;
		dc.initSelf();
		
		StringBuilder textString = new StringBuilder() ;
		for (int i = 0; i < 200; i++) {
			textString.append("0123456789012345678901234567890123나다라마바사4567890123456789") ; // 10k
		}
		// insert into lob_sample(a, c) values(v_a, v_b) ;
		IUserProcedure upt = dc.createUserProcedure("Sample@insertLobWith(?,?)") ;
		upt.addParam(1).addClob(textString.toString()) ;
		
		assertEquals(1, upt.execUpdate()) ;
		
		dc.destroySelf() ;

	}
}
