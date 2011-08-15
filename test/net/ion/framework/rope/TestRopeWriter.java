package net.ion.framework.rope;

import java.io.StringWriter;
import java.io.Writer;

import net.ion.framework.util.Debug;
import net.ion.framework.util.RandomUtil;

import junit.framework.TestCase;

public class TestRopeWriter extends TestCase{

	public void testWriterSpped() throws Exception {
		long startTime = System.nanoTime() ;
		
		StringWriter writer = new StringWriter() ;
		for (int i = 0; i < 10000; i++) {
			writer.write(RandomUtil.nextRandomString(5)) ;
		}
		
		Debug.debug("StringWriter", (System.nanoTime() - startTime)/1000000) ;
	}

	public void testRopeWriterSpped() throws Exception {
		long startTime = System.nanoTime() ;
		
		RopeWriter writer = new RopeWriter(RopeBuilder.build(new char[0])) ;
		for (int i = 0; i < 10000; i++) {
			writer.write(RandomUtil.nextRandomString(5)) ;
		}
		
		Debug.debug("StringWriter", (System.nanoTime() - startTime)/1000000) ;
	}
}
