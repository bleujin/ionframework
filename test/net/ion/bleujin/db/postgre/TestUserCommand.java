package net.ion.bleujin.db.postgre;

import net.ion.framework.db.Rows;
import net.ion.framework.db.procedure.IUserCommandBatch;
import net.ion.framework.util.Debug;
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
	
	
	
	
}
