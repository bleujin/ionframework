package net.ion.framework.db.sample;

import junit.framework.TestCase;
import net.ion.framework.db.DBController;
import net.ion.framework.db.Rows;
import net.ion.framework.db.manager.DBManager;
import net.ion.framework.db.manager.OracleDBManager;
import net.ion.framework.db.procedure.H2EmbedPoolDBManager;
import net.ion.framework.db.servant.PrintOutServant;
import net.ion.framework.db.servant.StdOutServant;
import net.ion.framework.util.Debug;

public class TestBaseSample extends TestCase {

	protected DBController dc;

	public void setUp() throws Exception {
		// DBManager dbm = new MySQLDBManager("jdbc:mysql://wcmdb.i-on.net:3300/im", "im", "im") ;
		// DBManager dbm = new MySQLPoolDBManager("jdbc:mysql://wcmdb.i-on.net:3300/im", "im", "im") ;
		// DBManager dbm = new MSSQLPoolDBManager("jdbc:microsoft:sqlserver://dev-sql.i-on.net:1435;DatabaseName=pubs", "bleu", "redf") ;
		// DBManager dbm = new OraclePoolDBManager("jdbc:oracle:thin:@novision:1521:bleujin", "setuptest", "setuptest") ;
		// DBManager dbm = new JTDSDBManager("jdbc:jtds:sqlserver://localhost:1433/test;useLOBs=false", "bleu", "redf") ;
		// DBManager dbm = new MSSQLPoolDBManager("net.sourceforge.jtds.jdbc.Driver", "jdbc:jtds:sqlserver://localhost:1433/test;useLOBs=false", "bleu", "redf") ;
		// DBManager dbm = new OracleCacheDBManager("jdbc:oracle:thin:@novision:1521:bleujin", "setuptest", "setuptest") ;
		DBManager dbm = OracleDBManager.test();
//		DBManager dbm = H2EmbedPoolDBManager.test() ;
		dc = new DBController(dbm);
		dc.initSelf();
	}

	public void tearDown() throws Exception {
		dc.destroySelf();
	}
	
	public void xtestRow() throws Exception {
		Rows rows = dc.getRows("select * from dual");
		Debug.line(rows.toString()) ;
	}

	public void xtestExecQuery() throws Exception {
		dc.addServant(new PrintOutServant());

		// Rows rows = dc.getRows("SELECT CONCAT('notice',no) id, no, memo, name, reg_date, concat('http://im.i-on.net/zeroboard/view.php?id=notice&no=', no) url FROM zetyx_board_notice") ;
		Rows rows = dc.getRows("SELECT * FROM emp_sample");
		Debug.debug(rows.getRowCount(), rows);
		// Thread.sleep(4000) ;
	}
}
