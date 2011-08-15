package net.ion.framework.rope;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderResult;
import java.nio.charset.CodingErrorAction;

public class RopeInputStream extends InputStream {

	private final CharBuffer charData;

	private final CharsetEncoder conversion;

	private boolean marked = false;
	private byte mark_multiByteChar[];
	private int mark_position;

	private byte multiByteChar[];
	private int position;

	/**
	 * @param rope
	 *            the char sequence
	 * @param encoding
	 *            the charset encoding
	 */
	public RopeInputStream(Rope rope, String encoding) {
		charData = CharBuffer.wrap(rope.toString());

		Charset encodingCharset = Charset.forName(encoding);

		conversion = encodingCharset.newEncoder();
		conversion.onMalformedInput(CodingErrorAction.REPLACE);
		conversion.onUnmappableCharacter(CodingErrorAction.REPLACE);

		int maxBytes = new Float(conversion.maxBytesPerChar()).intValue();

		multiByteChar = new byte[maxBytes];
		position = multiByteChar.length;
	}

	public void mark(int ignored) {
		charData.mark();
		mark_multiByteChar = multiByteChar.clone();
		mark_position = position;
		marked = true;
	}

	public boolean markSupported() {
		return false;
	}

	public void reset() throws IOException {

		if (!marked) {
			throw new IOException("reset() called before mark()");
		}

		charData.reset();
		multiByteChar = mark_multiByteChar.clone();
		position = mark_position;
	}

	public int read() throws IOException {
		// prefill the buffer
		while (multiByteChar.length == position) {
			int readsome = read(multiByteChar, 0, multiByteChar.length);

			if (-1 == readsome) {
				return -1;
			}

			position = multiByteChar.length - readsome;

			if ((0 != position) && (0 != readsome)) {
				System.arraycopy(multiByteChar, 0, multiByteChar, position, readsome);
			}

		}
		return (multiByteChar[position++] & 0xFF);
	}

	public int read(byte[] buffer) throws IOException {
		return read(buffer, 0, buffer.length);
	}

	public int read(byte[] buffer, int offset, int length) throws IOException {
		// handle partial characters;
		if (multiByteChar.length != position) {
			int copying = Math.min(length, multiByteChar.length - position);

			System.arraycopy(multiByteChar, position, buffer, offset, copying);
			position += copying;
			return copying;
		}

		ByteBuffer bb = ByteBuffer.wrap(buffer, offset, length);

		int before = bb.remaining();

		CoderResult result = conversion.encode(charData, bb, true);

		int readin = before - bb.remaining();

		if (CoderResult.UNDERFLOW == result) {
			if (0 == readin) {
				return -1;
			} else {
				return readin;
			}
		}

		if (CoderResult.OVERFLOW == result) {
			return readin;
		}

		return -1;
		// result.throwException();
		//
		// // NEVERREACHED
		// return -1;
	}

}
