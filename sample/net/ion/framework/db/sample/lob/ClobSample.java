package net.ion.framework.db.sample.lob;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;

import net.ion.framework.db.procedure.IUserCommand;
import net.ion.framework.db.procedure.IUserProcedure;
import net.ion.framework.db.sample.SampleTestBase;
import net.ion.framework.rope.Rope;
import net.ion.framework.rope.RopeBuilder;
import net.ion.framework.rope.RopeReader;
import net.ion.framework.util.Debug;
import net.ion.framework.util.RandomUtil;
import junit.framework.TestCase;

public class ClobSample extends SampleTestBase{

	
	public void testInsertClobString() throws Exception {
		// a number, b varchar2, c clob, d blob, e varchar2
		IUserCommand cmd = dc.createUserCommand("insert into lob_sample(a,b,c,d,e) values(:a,:b,:c,:d,:e)") ;
		cmd.addParam("a", 1).addParam("b", "2").addClob("c", makeLongString(10000)).addBlob("d", makeLongByte(5000)).addParam("e", "e") ;
		assertEquals(1, cmd.execUpdate()) ;
	}

	public void testRopeReader() throws Exception {
		String ran = RandomUtil.nextRandomString(260);
		StringReader str = new StringReader(ran) ;
		Rope rope = RopeBuilder.build(str) ;
		RopeReader rr = new RopeReader(rope) ;
		char[] buffers = new char[256] ;
		do{
			// Debug.debug(new String(buffers)) ;
		} while(rr.read(buffers) == buffers.length) ;
		Debug.debug(ran) ;
	}
	
	public void testInsertClobReader() throws Exception {
		// a number, b varchar2, c clob, d blob, e varchar2
		IUserCommand cmd = dc.createUserCommand("insert into lob_sample(a,b,c,d,e) values(:a,:b,:c,:d,:e)") ;
		cmd.addParam("a", 1).addParam("b", "2").addClob("c", makeLongReader(10000)).addBlob("d", makeLongByte(5000)).addParam("e", "e") ;
		assertEquals(1, cmd.execUpdate()) ;
	}
	
	public void testInsetProcClobString() throws Exception {
		IUserProcedure upt = dc.createUserProcedure("sample@insertLobWith(:a,:c)") ;
		upt.addParam("a", 1).addClob("c", makeLongString(10000)) ;
		assertEquals(1, upt.execUpdate()) ;
	}

	public void testInsetProcClobReader() throws Exception {
		IUserProcedure upt = dc.createUserProcedure("sample@insertLobWith(:a,:c)") ;
		upt.addParam("a", 1).addClob("c", makeLongReader(10000)) ;
		assertEquals(1, upt.execUpdate()) ;
	}

	private String makeLongString(int lenght){
		return RandomUtil.nextRandomString(lenght) ;
	}
	
	private Reader makeLongReader(int length){
		return new StringReader(makeLongString(length)) ;
	}

	private InputStream makeLongByte(int lenght){
		return new ByteArrayInputStream(makeLongString(lenght).getBytes()) ;
	}

}
