package net.ion.framework.db;

import java.sql.Connection;
import java.sql.Statement;

import junit.framework.TestCase;
import net.ion.framework.db.manager.OracleDBManager;
import net.ion.framework.db.procedure.IUserCommand;
import net.ion.framework.util.Debug;
import net.ion.framework.util.IOUtil;

public class TestTrigger extends TestCase{

	private DBController dc;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		OracleDBManager dbm = OracleDBManager.test();
		this.dc = new DBController(dbm);
		dc.initSelf();
	}

	@Override
	protected void tearDown() throws Exception {
		dc.destroySelf();
		super.tearDown();
	}

	
	public void testTrigger() throws Exception {
		Connection conn = dc.getDBManager().getConnection() ;
		Statement stmt = conn.createStatement() ;
		int success = stmt.executeUpdate(IOUtil.toStringWithClose(getClass().getResourceAsStream("sample.trigger"))) ;
		Debug.line(success);
		stmt.close(); 
		conn.close();
	}
}
