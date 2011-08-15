package net.ion.framework.rope;

import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;

import org.apache.commons.lang.ArrayUtils;

// buffer[i] = iter.next() ; <- �� ������ autoboxing ó�������� StringReader���� ó���� �ʴ� =��=;
// ���� Rope�� Reader�� ��ȯ���� ����ϴ� ���� �׸� �������� �ʴ´�. �������̽��� �� ����� �� �ʿ䰡 �ƴ϶�� ���̴�. 
// ���� new SringReader(rope.toString())�ÿ��� GC �� ����� �� �ӽø޸𸮰� �Ҵ�ǹǷ� ���� StringReader�� ��ȯ�ϴ� ���� ���� �ʴ�. 

public class RopeReader extends Reader {

	private Rope rope;
	private Iterator<Character> iter;
	private final int totalLength;

	public RopeReader(CharSequence cseq) {
		this(RopeBuilder.build(cseq));
	}

	public RopeReader(Rope rope) {
		this.rope = rope;
		this.iter = rope.iterator();
		this.totalLength = rope.length();
	}

	public int read() throws IOException {
		if (iter.hasNext()) {
			return iter.next();
		} else {
			return -1;
		}

	}

	public Rope getRope() {
		return rope;
	}

	public int read(char[] buffer, int begin, int length) throws IOException {
		if (begin < 0 || length < 0 || begin > totalLength)
			throw new IndexOutOfBoundsException();
		// if(length == 0) return 0;
		if (!iter.hasNext())
			return -1;

		while (begin > 0) {
			if (iter.hasNext()) {
				iter.next();
				begin--;

			} else
				return -1;
		}
		int i = 0;
		for (i = 0; i < length && iter.hasNext(); i++) {
			buffer[i] = iter.next();
		}

		buffer = ArrayUtils.subarray(buffer, 0, i);
		return i;
	}

	public void reset() throws IOException {
		iter = rope.iterator();
	}

	public void close() throws IOException {
	}

}
