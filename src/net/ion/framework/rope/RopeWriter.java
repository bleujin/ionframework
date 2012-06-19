package net.ion.framework.rope;

import java.io.IOException;
import java.io.Writer;

public class RopeWriter extends Writer {

	private Rope rope;

	public RopeWriter() {
		this(RopeBuilder.build(new char[0]));
	}

	public RopeWriter(Rope rope) {
		this.rope = rope;
	}

	public void close() throws IOException {

	}

	@Override
	public void flush() throws IOException {

	}

	@Override
	public void write(char[] ac, int i, int j) throws IOException {
		write(new String(ac, i, j));
	}

	public void write(String str) {
		rope = rope.addTo(str);
	}

	public void write(CharSequence cseq) {
		rope = rope.addTo(cseq);
	}

	public void write(CharSequence... cseqs) {
		for (CharSequence cseq : cseqs) {
			write(cseq);
		}
	}

	public Rope getRope() {
		return this.rope;
	}

	public Writer append(CharSequence cseq) {
		rope = rope.addTo(cseq);
		return this;
	}

	public Writer append(CharSequence... cseqs) {
		for (CharSequence cseq : cseqs) {
			rope = rope.addTo(cseq);
		}
		return this;
	}

}
