package net.ion.framework.template.tagext;

import java.io.StringWriter;

import net.ion.framework.util.ConcurrentVector;

/**
 * ActionTag를 사용한 page의 모든 action과 data는 PageWriter로 쓰여진다. PageWriter의 instance는 각 page를 생성할 때 사용되는 pageContext에 의해 out이라는 이름으로 관리된다.
 * 
 * @author Kim Sanghoon wizest@i-on.net
 * @version 1.0
 * 
 * @see BodyContent
 * 
 * @see java.io.Writer
 * @see java.io.StringWriter
 * @see java.io.PrintWriter
 * 
 * @see javax.servlet.jsp.JspWriter
 */

public class PageWriter extends StringWriter {
	static String lineSeparator = System.getProperty("line.separator");
	private transient ConcurrentVector<PageWritingListener> pageWritingListeners;

	public PageWriter() {
		super();
		this.pageWritingListeners = new ConcurrentVector<PageWritingListener>(2); 
	}

	public void clear() {
		StringBuffer buff = getBuffer();
		buff.setLength(0);
	}

	private void _write(String s) {
		super.write(s);
		fireWrite(s);
	}

	public void newLine() {
		_write(lineSeparator);
	}

	public void print(boolean b) {
		_write(b ? "true" : "false");
	}

	public void print(char c) {
		_write(String.valueOf(c));
	}

	public void print(int i) {
		_write(String.valueOf(i));
	}

	public void print(long l) {
		_write(String.valueOf(l));
	}

	public void print(float f) {
		_write(String.valueOf(f));
	}

	public void print(double d) {
		_write(String.valueOf(d));
	}

	public void print(char s[]) {
		_write(new String(s));
	}

	public void print(String s) {
		if (s == null) {
			s = "null";
		}
		_write(s);
	}

	public void print(Object obj) {
		_write(String.valueOf(obj));
	}

	public void println() {
		newLine();
	}

	public void println(boolean x) {
		print(x);
		newLine();
	}

	public void println(char x) {
		print(x);
		newLine();
	}

	public void println(int x) {
		print(x);
		newLine();
	}

	public void println(long x) {
		print(x);
		newLine();
	}

	public void println(float x) {
		print(x);
		newLine();
	}

	public void println(double x) {
		print(x);
		newLine();
	}

	public void println(char x[]) {
		print(x);
		newLine();
	}

	public void println(String x) {
		print(x);
		newLine();
	}

	public void println(Object x) {
		print(x);
		newLine();
	}

	public PageWriter cprint(CharSequence cq){
		print(cq) ;
		return this ;
	}
	
	public PageWriter cprintln(CharSequence cq){
		println(cq);
		return this ;
	}
	
	
	public String toString() {
		return super.toString();
	}

	public synchronized void removePageWritingListener(PageWritingListener l) {
		pageWritingListeners.remove(l);
	}

	public synchronized void addPageWritingListener(PageWritingListener l) {
		pageWritingListeners.add(l);
	}

	protected void fireWrite(String e) {
		for (PageWritingListener listener : pageWritingListeners.toCollection()) {
			listener.write(e);
		}
	}
}
