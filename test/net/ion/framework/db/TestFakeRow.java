package net.ion.framework.db;

import java.sql.Types;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;
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

	}
}
