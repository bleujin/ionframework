package net.ion.framework.template.parse;


/**
 * 속성 이름을 얻는다.
 * 
 * <pre>
 *  [[--TAGNAME,A1,A2,A3,A4name:A4value--]]
 *      A1,A2,A3,A4name:A4value 가 각각 속성이며
 *      ':' 구분자에 의해서 name과 value가 정해진다.
 *      구분자가 없을 경우 name은 null이며 value만 있다.
 * 
 *      참고 : [[--Tag,:value--]] 와 같이 ':'이 속성의 첫번째 칸에 올 경우 name은 null로 파싱된다.
 * </pre>
 * 
 * @author Kim Sanghoon wizest@i-on.net
 * @version 1.0
 */

public class AttributeNameParser extends Parser {
	private static final char ATTR_SEPARATOR = AttributeParser.ATTR_SEPARATOR;

	public Marker parseNext() throws ParserException {
		String text = getText();
		Marker marker = null;

		int start = getParsingPoint();
		int end = text.indexOf(ATTR_SEPARATOR);

		if (start == 0 && end > 0) { // attribute name이 있을 경우
			marker = new Marker(start, end, text.substring(0, end).trim().toUpperCase());
		}

		setParsingPoint(marker);
		return marker;
	}

	// public static void main( String[] args ) throws ParserException
	// {
	//
	// String s =
	// "[[--name,sdsd:,:dds,\\\\  attr1\\=fdgdsh:ds\\,\\,\\,\\s   ,:, attr2:a ,     attr3: ,attr4 , --]]";
	//
	// Parser p = new AttributeParser();
	// Parser pp = new AttributeNameParser();
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
