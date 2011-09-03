package net.ion.framework.db.sample.extend;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import net.ion.framework.db.Rows;
import net.ion.framework.db.procedure.IUserCommand;
import net.ion.framework.db.sample.TestBaseDB;
import net.ion.framework.util.Debug;
import net.ion.framework.util.IOUtil;

import org.apache.commons.io.FileUtils;


public class LobHandle extends TestBaseDB{

	File k100File = null ;
	public void setUp() throws Exception {
		super.setUp();
		k100File = IOUtil.createTempFile("framework_sample") ;
		
		Writer writer = new OutputStreamWriter(new FileOutputStream(k100File), "UTF-8");
		for (int i = 0; i < 10240; i++) {
			writer.write("0123456789") ;
		}
		
		writer.flush() ;
		writer.close() ;
		
	}
	
	@Override
	public void tearDown() throws Exception {
		super.tearDown();
	}

	public void testParam() throws Exception {
		dc.createUserCommand("truncate table lob_sample");
		IUserCommand cmd = dc.createUserCommand("insert into lob_sample(a, b,c,e) values(:a, :b, :c, :e)");
		cmd.addParam("a", 1);
		cmd.addParam("b", "abcdefg");
		cmd.addClob("c", FileUtils.readFileToString(k100File, "UTF-8"));
		cmd.addParam("e", "12345") ;

		int result = cmd.execUpdate();
	}
	
	public void testInsert() throws Exception {
		dc.createUserCommand("truncate table lob_sample").execUpdate() ;
		IUserCommand cmd = dc.createUserCommand("insert into lob_sample(a, b, c, d, e) values(:a, :b, :c, :d, :e)") ;
		cmd.addParam("a", 1) ;
		cmd.addParam("b", "abcdefg") ;
		cmd.addClob("c", FileUtils.readFileToString(k100File, "UTF-8")) ;
		cmd.addBlob("d", new FileInputStream(k100File)) ;
		cmd.addParam("e", "12345") ;
		
		int result = cmd.execUpdate() ;
		
		assertEquals(1, result) ;
		
		Rows rows = dc.getRows("select a, b, c, d, e from lob_sample where a = 1") ;
		
		rows.next() ;
		
		long fileSize = k100File.length();
		Debug.debug(fileSize, rows.getString("c").length(), rows.getString("c")) ;
		assertEquals(true, rows.getString("c").length() >= 102400) ;
		assertEquals(true, fileSize == rows.firstRow().getString("c").length()) ;
		
		InputStream input = rows.getBinaryStream("d") ;
		int bcount = 0;
		while(input.read() != -1) bcount++ ; 
		assertEquals(true, fileSize == bcount) ;
		input.close() ;
	}
	
	
}
