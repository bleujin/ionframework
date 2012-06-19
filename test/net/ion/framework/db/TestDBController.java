package net.ion.framework.db;

import java.io.File;

import junit.framework.TestCase;
import net.ion.framework.configuration.ConfigurationFactory;
import net.ion.framework.util.Debug;
import net.ion.framework.util.ListUtil;

public class TestDBController extends TestCase {

	public void testReadConfig() throws Exception {
		ConfigurationFactory instance = ConfigurationFactory.getInstance("my");
		instance.build(new File("./resource/data/config.tmp")) ;
		
		 ;
		new DBController(instance.getConfiguration("root.database-controller")) ;
	}
	
	/*
CONTENT@retrieveBy(?)
AFIELDCONT@listBy(?,?,?)
	 */
	
	public void testTempTablespace() throws Exception {
		ConfigurationFactory instance = ConfigurationFactory.getInstance("my");
		instance.build(new File("./resource/data/config.tmp")) ;
		IDBController dc = new DBController(instance.getConfiguration("root.database-controller")) ;
		dc.initSelf() ;
		
		for (int i  : ListUtil.rangeNum(10000)) {
			// dc.createUserCommand("alter session set events '60025 trace name context forever'").execUpdate() ;
			
			Rows rows = dc.createUserProcedure("AFIELDCONT@listBy(?,?,?)").addParam("mq_info").addParam(1187661).addParam(2).execQuery() ; 
			//Rows rows = dc.createUserProcedure("AFIELDCONT@listBy(?,?,?)").addParam("ac_movieq").addParam(1187661).addParam(2).execQuery() ;
			// rows.firstRow();
			
			if(i%100 == 0) {
				Rows rows3 = dc.createUserCommand("select sum(blocks)  blockSize from v$sort_usage").execQuery();
				Row row3 = rows3.firstRow();
				Debug.line(row3.getString("blockSize"));
			}
			
		}
		 
	}
	
	
}
