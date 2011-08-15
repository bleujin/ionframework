package net.ion.framework.db.procedure;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import junit.framework.TestCase;

import org.apache.commons.io.output.ByteArrayOutputStream;

import net.ion.framework.db.DBController;
import net.ion.framework.db.IDBController;
import net.ion.framework.db.Rows;
import net.ion.framework.db.manager.DBManager;
import net.ion.framework.db.manager.OracleDBManager;
import net.ion.framework.util.Debug;

public class TestQuerySerialize extends TestCase{

	private static IDBController dc;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		DBManager dbm = new OracleDBManager("jdbc:oracle:thin:@dev-test.i-on.net:1521:devTest", "bleu", "redf") ;
		dc = new DBController(dbm) ;
		dc.initSelf() ;
	}
	
	@Override
	protected void tearDown() throws Exception {
		dc.destroySelf() ;
		super.tearDown();
	}
	
	public void testname() throws Exception {
		
		IUserCommand cmd = dc.createUserCommand("select 1 from dual") ;
		
		final ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(bos) ;
		
		oos.writeObject(cmd) ;
		
		ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bos.toByteArray())) ;
		IParameterQueryable query =  (IParameterQueryable) ois.readObject();
		Debug.debug(query);
		
	}

}
