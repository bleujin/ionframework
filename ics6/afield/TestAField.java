package afield;

import net.ion.framework.util.Debug;
import junit.framework.TestCase;

public class TestAField extends TesBaseICS6PG {

	
	public void testCreateWith() throws Exception {
		int result = dc.createUserProcedure("afield@createWith(?,?,?,?,?, ?,?,?,?,?, ?,?,?)")
		.addParam("bleujin").addParam("bleujin").addParam("at_t").addParam("bleujin").addParam("String")
		.addParam(50).addParam("T").addParam(1).addParam(400).addParam(1).addParam("ALL").addParam(0).addParam("").execUpdate() ;
		Debug.line(result);
	}
	
	public void testSelect() throws Exception {
		dc.createUserProcedure("AFIELD@allGroupBy()").execQuery().debugPrint();
	}
	
	public void testSelectTnto() throws Exception {
		dc.createUserProcedure("test@listBy(?)").addParam(3).execQuery().debugPrint();
	}
	
	public void testIntoGet() throws Exception {
		for (int i = 0; i < 5; i++) {
			int result = dc.createUserProcedure("test@countBy(?)").addParam(3).execUpdate() ;
			Debug.line(result);
			
		}
	}
}
