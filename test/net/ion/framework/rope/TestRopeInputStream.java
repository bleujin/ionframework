package net.ion.framework.rope;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;

import net.ion.framework.util.Debug;

import org.apache.commons.io.IOUtils;

import junit.framework.TestCase;

public class TestRopeInputStream extends TestCase{
	
	public void testRead() throws Exception {
		Rope rope = RopeBuilder.build("가나다라") ;
		RopeInputStream ri = new RopeInputStream(rope, "UTF-8") ;
		
		String str = IOUtils.toString(ri, "UTF-8") ;
		Debug.debug(str) ;
		assertEquals("가나다라", str) ;
	}
	
	public void testSpeed() throws Exception {
		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream("./resource/data/AChristmasCarol.txt"), "UTF-8")) ;
		String thisLine ;
		Rope rope = RopeBuilder.build() ; 
		while( (thisLine = reader.readLine()) != null){
			rope = rope.addTo(thisLine) ;
		}
		
		long startTime = System.nanoTime() ;
		for( final char c : rope){
		}
		Debug.debug("iterator char", (System.nanoTime() - startTime) / 1000000) ;
		
		startTime = System.nanoTime() ;
		RopeInputStream ris = new RopeInputStream(rope, "UTF-8") ;
		while(ris.read() != -1){
		}
		Debug.debug("charbuffer stream", (System.nanoTime() - startTime) / 1000000) ;
		
		startTime = System.nanoTime() ;
		RopeByteInputStream ris2 = new RopeByteInputStream(rope, "UTF-8") ;
		while(ris2.read() != -1){
		}
		Debug.debug("getByte stream", (System.nanoTime() - startTime) / 1000000) ;
		
		startTime = System.nanoTime() ;
		byte[] bytes = rope.toString().getBytes("UTF-8") ;
		for (byte b : bytes) {
		}
		Debug.debug("getByte ", (System.nanoTime() - startTime) / 1000000) ;
		
		startTime = System.nanoTime() ;
		for (int i=0, last=rope.length() ; i < last ; i++) {
			rope.charAt(i) ;
		}
		Debug.debug("char at", (System.nanoTime() - startTime) / 1000000) ;
		
	}
	
	public void testCharAt() throws Exception {
		
		Rope rope = RopeBuilder.build("123가나다abc") ;
		RopeInputStream ris = new RopeInputStream(rope, "UTF-8") ;
		while(ris.read() != -1){
		}
		Debug.debug(rope.toString()) ;
	}

}
