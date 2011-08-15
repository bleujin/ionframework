package net.ion.framework.template.parse;


/**
 * �Ӽ� �̸��� ��´�.
 * 
 * <pre>
 *  [[--TAGNAME,A1,A2,A3,A4name:A4value--]]
 *      A1,A2,A3,A4name:A4value �� ���� �Ӽ��̸�
 *      ':' �����ڿ� ���ؼ� name�� value�� ��������.
 *      �����ڰ� ���� ��� name�� null�̸� value�� �ִ�.
 * 
 *      ���� : [[--Tag,:value--]] �� ���� ':'�� �Ӽ��� ù��° ĭ�� �� ��� name�� null�� �Ľ̵ȴ�.
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

		if (start == 0 && end > 0) { // attribute name�� ���� ���
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
