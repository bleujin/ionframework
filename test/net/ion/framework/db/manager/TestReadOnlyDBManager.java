package net.ion.framework.db.manager;

import java.sql.Connection;
import java.sql.SQLException;

import net.ion.framework.db.DBController;
import junit.framework.TestCase;

public class TestReadOnlyDBManager extends TestCase {

	private DBController dc;
	private DBController oldDC;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.oldDC = new DBController(new PostSqlDataSource("jdbc:postgresql://127.0.0.1:5432/ics6", "postgres", "redf"));
		oldDC.initSelf();

		ReadOnlyDBManager dbm = new ReadOnlyDBManager(oldDC.getDBManager());
		this.dc = new DBController(dbm);
		dc.initSelf();
	}

	@Override
	protected void tearDown() throws Exception {
		dc.destroySelf();

		oldDC.destroySelf();
		super.tearDown();
	}

	public void testMake() throws Exception {
		dc.createUserCommand("insert into preview_article_tblt values(1, 'mybla', 1000, 1)").execUpdate();
		dc.createUserCommand("insert into preview_article_tblt values(2, 'mybla', 1001, 2)").execUpdate();

		dc.createUserCommand("select * from preview_article_tblt").execQuery().debugPrint();
	}

	public void testConfirm() throws Exception {
		dc.createUserCommand("select * from preview_article_tblt").execQuery().debugPrint();
	}

	public void testTran() throws Exception {
		PostSqlDataSource dbm = new PostSqlDataSource("jdbc:postgresql://127.0.0.1:5432/ics6", "postgres", "redf");

		Connection conn = null ;
		try {
			dbm.myInitPool(); 
			conn = dbm.getConnection();
			conn.setAutoCommit(false);
			for (int i = 0; i < 1; i++) {
				conn.createStatement().execute("insert into preview_article_tblt values(1, 'mybla', 1000, 1)");
			}
			conn.rollback(); 
		} catch(SQLException e){
			e.printStackTrace();
			conn.rollback();
		} finally {
			
			if (conn != null) {
				conn.close();
			}
			dbm.myDestroyPool();
		}

	}
}
