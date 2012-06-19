package net.ion.framework.rope;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import junit.framework.TestCase;
import net.ion.framework.util.Debug;
import net.ion.framework.util.RandomUtil;

public class SimpleRopeTest extends TestCase {

	String s1 = new String();
	StringBuffer s2 = new StringBuffer();
	StringBuilder s3 = new StringBuilder();
	SimpleRopeImpl s4 = new SimpleRopeImpl();
	String data = RandomUtil.nextRandomString(50);
	int loopCount = 50000;

	public void testAppendPerf() throws Exception {
		long start = System.nanoTime();
		// appendString() ;
		// Debug.debug("appendString", System.nanoTime() - start) ;

		start = System.nanoTime();
		appendStringBuffer();
		Debug.debug("appendStringBuffer", elapsedTime(start));

		start = System.nanoTime();
		appendStringBuilder();
		Debug.debug("appendStringBuilder", elapsedTime(start));

		start = System.nanoTime();
		appendRope();
		Debug.debug("appendRope", elapsedTime(start));
	}
	
	
	
	
	private void appendString() throws Exception {
		for (int i = 0; i < loopCount; i++) {
			s1 += data;
		}
	}

	private void appendStringBuffer() throws Exception {
		for (int i = 0; i < loopCount; i++) {
			s2.append(data);
		}
	}

	private void appendStringBuilder() throws Exception {
		for (int i = 0; i < loopCount; i++) {
			s3.append(data);
		}
	}

	private void appendRope() throws Exception {
		for (int i = 0; i < loopCount; i++) {
			s4.append(data);
		}
	}

	

	
	private long elapsedTime(long start) {
		return (System.nanoTime() - start) / 1000000;
	}

}

class SimpleRopeImpl extends Writer implements Appendable {

	private List<CharSequence> store = Collections.synchronizedList(new ArrayList<CharSequence>());

	public void flush() throws IOException {

	}

	public void write(char[] datas, int start, int length) throws IOException {
		store.add(new String(datas, start, length));
	}

	public void write(String str) {
		store.add(str);
	}

	public Writer append(CharSequence seq) {
		store.add(seq) ;
		return this;
	}

	public void close() throws IOException {
	}

	
	public void output(Writer writer) throws IOException{
		for (CharSequence seq : store) {
			writer.write(seq.toString(), 0, seq.length()) ;
		}
	}
}