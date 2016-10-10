package net.ion.framework.db;

import java.sql.ResultSetMetaData;
import java.sql.Types;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;
import net.ion.framework.db.bean.handlers.JsonArrayHandler;
import net.ion.framework.db.manager.OracleDBManager;
import net.ion.framework.util.Debug;
import net.ion.framework.util.ListUtil;
import net.ion.framework.util.MapUtil;

public class TestFakeRow extends TestCase {

	
	public void testColumnAlias() throws Exception {
		List<Map> maps = ListUtil.newList() ;
		for (int i = 0; i < 5; i++) {
			Map<String, Object> map = MapUtil.newCaseInsensitiveMap();
			map.put("OPTION1", i + "");
			map.put("OPTION2", i + "");
			map.put("OPTION3", i + "");
			maps.add(map) ;
		}
		
		FakeRows frows = new FakeRows() ;
		
		frows.addColumn("option1", "includefile", Types.VARCHAR); ;
		frows.addColumn("option2", Types.VARCHAR); ;
		frows.addColumn("option3", "viewfile", Types.VARCHAR); ;
		
		for (Map m : maps) {
			frows.addRow(m);
		}
		
		frows.debugPrint();
		
		Object result = frows.toHandle(new JsonArrayHandler()) ;
		Debug.line(result);

	}
	
	public void testColumnLabel() throws Exception {
		
		OracleDBManager dbm = OracleDBManager.test() ;
		DBController dc = new DBController(dbm) ;
		dc.initSelf(); 
		
		
		Rows rows = dc.createUserCommand("select 1 from dual").execQuery() ;
		ResultSetMetaData meta = rows.getMetaData() ;
		
		for(int i = 1 ; i <= meta.getColumnCount(); i++){
			Debug.line(meta.getColumnLabel(i), meta.getColumnName(i)) ;
		}
		
		
		dc.destroySelf();
	}
}
