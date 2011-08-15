package net.ion.framework.rope;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import net.ion.framework.rope.Rope;
import net.ion.framework.util.Debug;

import junit.framework.TestCase;

public class RopeCharSequenceTest extends TestCase {

	public void testRead() throws Exception {
		Rope rope = RopeBuilder.build("abcd");

		assertEquals("abcd", rope.toString());

		
		
		
		long start = System.nanoTime();
//		stringSpeed();
		long end = System.nanoTime();
		Debug.debug("stringSpeed", (end - start) / 1000000);

		start = System.nanoTime();
		stringBufferSpeed();
		end = System.nanoTime();
		Debug.debug("bufferSpeed", (end - start) / 1000000);

		start = System.nanoTime();
		stringRopeSpeed();
		end = System.nanoTime();
		Debug.debug("RopeSpeed", (end - start) / 1000000);
	}

	private void stringSpeed() throws FileNotFoundException, IOException {
		BufferedReader reader = new BufferedReader(new FileReader(new File("./resource/data/AChristmasCarol.txt")));
		String thisLine = null;
		String stringResult = "";
		while ((thisLine = reader.readLine()) != null) {
			stringResult += thisLine;
		}
		reader.close() ;
	}

	private void stringBufferSpeed() throws FileNotFoundException, IOException {
		BufferedReader reader = new BufferedReader(new FileReader(new File("./resource/data/AChristmasCarol.txt")));
		String thisLine = null;
		StringBuffer buffer = new StringBuffer();
		while ((thisLine = reader.readLine()) != null) {
			buffer.insert(0, thisLine);
		}
		reader.close() ;
	}

	private void stringRopeSpeed() throws FileNotFoundException, IOException {
		BufferedReader reader = new BufferedReader(new FileReader(new File("./resource/data/AChristmasCarol.txt")));
		String thisLine = null;
		Rope rope = RopeBuilder.build("");
		while ((thisLine = reader.readLine()) != null) {
			rope.addTo("thisLine") ;
			
			rope = rope.insert(0, thisLine);
		}
		
		reader.close() ;
	}

}
