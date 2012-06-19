package net.ion.framework.template.parse;


/**
 * Tag���� Tag name�� ã���ش�.
 * 
 * <pre>
 *  [[--abc--]] -> abc
 *  [[--   abc --]] -> abc : trimming�ȴ�. ��,startIndex,endIndex�� trim�� �ݿ����� �ʴ´�. ��,(4,10)�̴�.
 *  [[--abc,dd,ee,ff--]] -> abc
 * 
 *  ����:
 *      [[----]] -> null
 *      [[-- --]] -> ���ڿ�
 *      [[--,something--]] -> null
 *      �� �Ľ��Ѵ�.
 * </pre>
 * 
 * @author Kim Sanghoon wizest@i-on.net
 * @version 1.0
 */
public class TagNameParser extends Parser {
	private static final String TAG_OPEN = TagParser.TAG_OPEN;
	private static final String TAG_CLOSE = TagParser.TAG_CLOSE;

	public static final char NAME_SEPARATOR = ',';
	public static final String START_TAG_POSTFIX = "Start".toUpperCase();
	public static final String END_TAG_POSTFIX = "End".toUpperCase();

	private static final int START_TAG_POSTFIX_LENGTH = START_TAG_POSTFIX.length();
	private static final int END_TAG_POSTFIX_LENGTH = END_TAG_POSTFIX.length();

	public Marker parseNext() throws ParserException {
		String text = getText();
		if (!(text.startsWith(TAG_OPEN) && text.endsWith(TAG_CLOSE))) {
			throw new ParserException("invalid tag.");
		}

		int start, end;
		Marker mark = null;

		if (getParsingPoint() < TAG_OPEN.length()) // name �� tag�� �ϳ� �ۿ� �����ϱ� �ѹ��� pasing�Ѵ�. ��.��;;
		{

			start = TAG_OPEN.length();

			// attribute�� ���� ���
			if (!((end = text.indexOf(NAME_SEPARATOR)) > 0)) {
				// attribute�� ���� ���
				end = text.indexOf(TAG_CLOSE);
			}

			if (end > start) {
				String body = text.substring(start, end).trim().toUpperCase();
				mark = new Marker(start, end, body);
			}
		}

		setParsingPoint(mark);
		return mark;
	}

	public static boolean isBodyTag(Marker mark) {
		if (isStartTag(mark) || isEndTag(mark)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * BodyTag�� [[--TagNameStart--]],[[--TagNameEnd--]] �� ���� �ױ� �̸� �� Start,End�� �ٴ´�. Start,End�� ������ Tag�̸��� �����Ѵ�.
	 * 
	 * @param mark
	 * @return
	 */
	public static String getRealTagName(Marker mark) {
		if (isStartTag(mark)) {
			String s = mark.getValue();
			return s.substring(0, s.length() - START_TAG_POSTFIX_LENGTH); // Start �κ��� ����
		} else if (isEndTag(mark)) {
			String s = mark.getValue();
			return s.substring(0, s.length() - END_TAG_POSTFIX_LENGTH); // End �κ��� ����
		} else {
			return mark.getValue();
		}
	}

	public static boolean isStartTag(Marker mark) {
		if (mark.getValue().endsWith(START_TAG_POSTFIX)) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isEndTag(Marker mark) {
		if (mark.getValue().endsWith(END_TAG_POSTFIX)) {
			return true;
		} else {
			return false;
		}
	}

	// public static void main( String[] args ) throws ParserException
	// {
	// String s =
	// "[[-- --]]sdgsdgsd[[----]][[--sddsgsdgsg--]]fdgsgs[[--1--]]--]][[--11144214-]--]][[--2,--]]2[[-- --]][[--,--]]";
	//
	// Parser p = new TagParser();
	// Parser pp = new TagNameParser();
	//
	// p.initialize( s );
	//
	// Marker m;
	// Marker mm;
	//
	// while ( ( m = p.parseNext() ) != null )
	// {
	// pp.initialize( m.getParsedString() );
	// while ( ( mm = pp.parseNext() ) != null )
	// System.out.println( mm.getParsedString() );
	// }
	// }
}
