package net.ion.framework.template.parse;


/**
 * 범용 parser 구현<br/>
 * <br/>
 * BEGIN 이 [[ 이고 END 가 ]] 이면 다음과 같이 parsing 된다. (trimming됨) abcdefg[[ hijklm ]]nopqrstuvwxyz -> hijklm
 * 
 * @author Kim, Sanghoon (wizest@i-on.net)
 * @version 1.0
 */

public class GenericParser extends Parser {
	private final String BEGIN;
	private final String END;

	private final int beginLen;
	private final int endLen;

	private final boolean overlap;
	private final boolean showTag;

	/**
	 * @param BEGIN
	 *            String
	 * @param END
	 *            String
	 * @param overlap
	 *            boolean true일 경우 END를 다음 parsing\uFFFD parsing의 시작지점에 포함시킨다.
	 */
	public GenericParser(String BEGIN, String END, boolean overlap, boolean showTag) {
		this.BEGIN = BEGIN;
		this.END = END;

		this.beginLen = BEGIN.length();
		this.endLen = END.length();

		this.overlap = overlap;

		if (overlap && (beginLen == 0 || endLen == 0))
			throw new IllegalArgumentException("not allow 0 length mark with peel off mode");

		this.showTag = showTag;
	}

	public GenericParser(String BEGIN, String END) {
		this(BEGIN, END, false, false);
	}

	public Marker parseNext() throws ParserException {
		int begin = getParsingPoint();
		String text = getText();
		if (text.length() == 0)
			return null;

		int p1 = text.indexOf(BEGIN, begin);
		if (p1 < 0)
			return null;

		int p2 = text.indexOf(END, p1 + beginLen);
		if (p2 < 0)
			return null;

		int b, e;
		if (overlap) {
			b = p1 + beginLen;
			e = p2;
		} else {
			b = p1;
			e = p2 + endLen;
		}
		String t = (showTag) ? text.substring(b, e) : text.substring(p1 + beginLen, p2).trim();

		Marker m = new Marker(b, e, t);
		setParsingPoint(m);
		return m;
	}
}
