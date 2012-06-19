package net.ion.framework.rope;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;

import junit.framework.TestCase;
import net.ion.framework.util.Debug;

import org.apache.commons.io.IOUtils;

public class TestRopeReader extends TestCase {

	public void testReader() throws Exception {
		Rope rope = RopeBuilder.build();
		rope = rope.addTo("123°¡³ª´ÙabcABC");

		Reader reader = new RopeReader(rope);
		char[] buffers = new char[4096];
		int length = 0;
		while ((length = reader.read(buffers)) != -1) {
			// Debug.debug(buffers, length) ;
		}

		Rope subRope = rope.subSequence(4, 7);
		Debug.debug("", subRope.length(), subRope.charAt(0));

	}

	public void testSpeed() throws Exception {
		String str = IOUtils.toString(new FileInputStream("./resource/data/Autobiography.txt"), "UTF-8");

		Reader br = new InputStreamReader(new FileInputStream("./resource/data/Autobiography.txt"), "UTF-8");
		Rope rope = RopeBuilder.build(br);

		long startTime = System.nanoTime();
		Reader reader = new RopeReader(rope);
		String read1 = IOUtils.toString(reader);
		Debug.debug(System.nanoTime() - startTime);

		startTime = System.nanoTime();
		reader = new StringReader(str);
		String read2 = IOUtils.toString(reader);
		Debug.debug(System.nanoTime() - startTime);
	}

	public void testSpeedDiff() throws Exception {
		String str = IOUtils.toString(new FileInputStream("./resource/data/Autobiography.txt"), "UTF-8");

		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("./resource/data/Autobiography.txt"), "UTF-8"));
		Rope rope = RopeBuilder.build(str);
		// String thisLine ;
		// while((thisLine = br.readLine()) != null){
		// rope = rope.addTo(thisLine) ;
		// }

		long startTime = System.nanoTime();
		Reader reader = new RopeReader(rope);
		for (int j = 0, jlast = rope.length(); j < jlast; ++j)
			reader.read();
		Debug.debug(System.nanoTime() - startTime);

		startTime = System.nanoTime();
		reader = new StringReader(str);
		for (int j = 0, jlast = rope.length(); j < jlast; ++j)
			reader.read();
		Debug.debug(System.nanoTime() - startTime);
	}

	public void testBuildReader() throws Exception {
		Reader reader = new StringReader("abcd");
		Rope r = RopeBuilder.build(reader);
		assertEquals("abcd", r.toString());
	}

	public void testNull() throws Exception {
		Rope rope = RopeBuilder.build((CharSequence) null);
		Debug.debug(rope);
	}
}
