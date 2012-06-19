package net.ion.framework.util;

import java.io.IOException;
import java.io.Writer;

/* ------------------------------------------------------------ */
/**
 * A Writer to a StringBuffer.
 * 
 * @author bleujin
 */
public class StringBuilderWriter extends Writer {

	private StringBuilder _buffer;

	public StringBuilderWriter() {
		_buffer = new StringBuilder();
	}

	public StringBuilderWriter(StringBuilder buffer) {
		_buffer = buffer;
	}

	public StringBuilder getStringBuilder() {
		return _buffer;
	}

	public void write(char c) throws IOException {
		_buffer.append(c);
	}

	public void write(char[] ca) throws IOException {
		_buffer.append(ca);
	}

	public void write(char[] ca, int offset, int length) throws IOException {
		_buffer.append(ca, offset, length);
	}

	public void write(String s) throws IOException {
		_buffer.append(s);
	}

	public void write(String s, int offset, int length) throws IOException {
		for (int i = 0; i < length; i++)
			_buffer.append(s.charAt(offset + i));
	}

	public void flush() {
	}

	public void reset() {
		_buffer.setLength(0);
	}

	public void close() {
	}

}
