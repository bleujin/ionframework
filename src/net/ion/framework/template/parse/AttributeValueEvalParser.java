package net.ion.framework.template.parse;


/**
 * attribute value에서 [% %] 부분을 추출한다.
 * 
 * @author Kim Sanghoon wizest@i-on.net
 * @version 1.0
 */

public class AttributeValueEvalParser extends Parser {
	public static final String EVAL_OPEN = "[%";
	public static final String EVAL_CLOSE = "%]";

	private static final int OPEN_LENGTH = EVAL_OPEN.length();
	private static final int CLOSE_LENTH = EVAL_CLOSE.length();

	public Marker parseNext() throws ParserException {
		String text = getText();
		int parsingPoint = getParsingPoint();

		int start, end;
		Marker mark = null;

		while (true) {
			start = text.indexOf(EVAL_OPEN, parsingPoint);
			if (start >= 0) {
				end = text.indexOf(EVAL_CLOSE, start + OPEN_LENGTH);
				if (end >= 0) {
					end += CLOSE_LENTH;
					mark = new Marker(start, end, text.substring(start + OPEN_LENGTH, end - CLOSE_LENTH).trim());
					setParsingPoint(end);
				}
				break;
			} else {
				break;
			}
		}

		setParsingPoint(mark);
		return mark;
	}

	// public static void main(String[] args) throws Exception
	// {
	// String s="kjggasg[%11111%]sagsga[%2222%]sddssd";
	//
	// Parser p = new AttributeValueEvalParser();
	// p.initialize(s);
	//
	//
	// while(true) {
	// Marker m = p.parseNext();
	// if (m==null) break;
	// System.out.println(m);
	// }
	// }
}
