package net.ion.framework.db.postgre;

import net.ion.framework.db.Rows;
import net.ion.framework.db.procedure.IUserCommandBatch;
import net.ion.framework.util.Debug;
import net.ion.framework.util.RandomUtil;
import net.ion.framework.util.StringUtil;

public class TestUserCommand extends TestBasePG{
	
	public void testUserCommand() throws Exception {
		Rows rows = dc.createUserCommand("select 1 as no union all select 2").execQuery() ;
		while(rows.next()){
			Debug.line(rows.getInt("no"), rows.getInt(1), rows.getString(1), rows.getString("no"));
		}
	}

	
	public void testBatch() throws Exception {
		IUserCommandBatch cmd = dc.createUserCommandBatch("insert into no_temp values(?,?)") ;
		for (int i = 0; i < 10; i++) {
			cmd.addBatchParam(0, i) ;
			cmd.addBatchParam(1, StringUtil.leftPad("" + i, 2, '0'));
		}
		cmd.execUpdate() ;
		
		dc.createUserCommand("select count(*) from no_temp").execQuery().debugPrint();
	}
	
	public void testBoolean() throws Exception {
		Rows rows = dc.createUserCommand("select 'T' as bo").execQuery() ;
		if (rows.next()){
			Debug.line(rows.getBoolean("bo")) ;
		}
	}
	
	
	public void testGlobalTemporary() throws Exception {
		IUserCommandBatch cb = dc.createUserCommandBatch("insert into dummy values(? ,?)") ;
		for (int i = 0; i < 5; i++) {
			cb.addBatchParam(0, i);
			cb.addBatchParam(1, RandomUtil.nextRandomString(10));
		}
		cb.execUpdate() ;
	}
	
	
	
}
