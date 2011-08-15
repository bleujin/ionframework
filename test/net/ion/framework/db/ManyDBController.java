package net.ion.framework.db;


/**
 * <p>Title: ManyDBController.java</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2007</p>
 * <p>Company: I-ON Communications</p>
 * <p>Date : 2010. 05. 18</p>
 * @author novision
 * @version 1.0
 */
public class ManyDBController extends DBTestCase{

	
	public void testname() throws Exception {
		
		int max = 10000;
		
		for (int i = 0; i < max ; i++) {
			new DBController(dc.getDBManager()).initSelf() ;
		}
		
	}
	
}
