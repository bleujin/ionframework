package net.ion.framework.template.parse;


/**
 * �Ӽ� ���� �����´�.
 * 
 * <pre>
 *  ����:
 *  [[--tagname,,Attrname:Value,:Value,:--]]
 * 
 *                        -> null : �̷� ��� �����Ѵ�. �Ӽ��� ���� �Ľ��� �Ǳ� ���� null�� ����Ƿ� �Ľ��� �ߴ��ϰ� �� �� �ִ�.
 *      Attrname:Value    -> Value
 *      :Value            -> Value
 *      :                 -> ���ڿ�
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

			// �Ӽ� ���� ���� ��
			if (start < 0) {
				if (end > 0) {
					mark = new Marker(0, end, text.trim());
				}
			}

			// �Ӽ� ���� ���� ��
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
