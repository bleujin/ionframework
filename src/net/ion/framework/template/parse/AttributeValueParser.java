package net.ion.framework.template.parse;


/**
 * 속성 값을 가져온다.
 * 
 * <pre>
 *  예제:
 *  [[--tagname,,Attrname:Value,:Value,:--]]
 * 
 *                        -> null : 이런 경우 주의한다. 속성이 전부 파싱이 되기 전에 null이 생기므로 파싱을 중단하게 될 수 있다.
 *      Attrname:Value    -> Value
 *      :Value            -> Value
 *      :                 -> 빈문자열
 * </pre>
 * 
 * @author Kim Sanghoon wizest@i-on.net
 * @version 1.0
 */

public class AttributeValueParser extends Parser {
	private static final char ATTR_SEPARATOR = AttributeParser.ATTR_SEPARATOR;

	public Marker parseNext() throws ParserException {
		String text = getText();
		int start, end;

		Marker mark = null;

		if (getParsingPoint() == 0) {
			start = text.indexOf(ATTR_SEPARATOR);
			end = text.length();

			// 속성 값이 없을 때
			if (start < 0) {
				if (end > 0) {
					mark = new Marker(0, end, text.trim());
				}
			}

			// 속성 값이 있을 때
			else {
				mark = new Marker(start + 1, end, text.substring(start + 1).trim());
			}
		}

		setParsingPoint(mark);
		return mark;
	}

	// public static void main( String[] args ) throws ParserException
	// {
	//
	// String s =
	// "[[--name,sdsd:,:dds, attr1:ds   ,:, attr2:a ,     attr3: ,attr4  ,,dd--]]";
	//
	// Parser p = new AttributeParser();
	// Parser pp = new AttributeValueParser();
	//
	// p.initialize( s );
	//
	// Mark m;
	// Mark mm;
	//
	// while ( ( m = p.parseNext() ) != null )
	// {
	// pp.initialize( m );
	// while ( ( mm = pp.parseNext() ) != null )
	// System.out.println( mm.getParsedString() );
	//
	// }
	// }
}
