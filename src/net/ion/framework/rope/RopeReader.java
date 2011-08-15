package net.ion.framework.rope;

import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;

import org.apache.commons.lang.ArrayUtils;

// buffer[i] = iter.next() ; <- 요 구문의 autoboxing 처리때문에 StringReader보다 처리가 늦다 =ㅅ=;
// 따라서 Rope를 Reader로 변환시켜 사용하는 것은 그리 권장하지 않는다. 인터페이스를 꼭 맞춰야 할 필요가 아니라면 말이다. 
// 물론 new SringReader(rope.toString())시에는 GC 의 대상이 될 임시메모리가 할당되므로 굳이 StringReader로 변환하는 것은 좋지 않다. 

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
