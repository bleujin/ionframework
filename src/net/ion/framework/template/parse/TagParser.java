package net.ion.framework.template.parse;

import net.ion.framework.util.StringUtil;


/**
 * 문자열에서 Action Tag를 찾는다.
 * 
 * <pre>
 * action tag는 다음을 말한다.
 *  [[--테그네임,테그속성,...--]]
 * </pre>
 * 
 * @author Kim Sanghoon wizest@i-on.net
 * @version 1.0
 */

public class TagParser extends Parser {
	public static final String TAG_OPEN = "[[--";
	public static final String TAG_CLOSE = "--]]";

	public static final String COMMENT_OPEN = "[[---";
	public static final String COMMENT_CLOSE = "---]]";

	public static final String COMMENT_OPEN_T = "[[\\---";
	public static final String COMMENT_CLOSE_T = "---\\]]";

	private static final int TAG_OPEN_LENGTH = TAG_OPEN.length();
	private static final int TAG_CLOSE_LENGTH = TAG_CLOSE.length();
	private static final int COMMENT_OPEN_LENGTH = COMMENT_OPEN.length();
	private static final int COMMENT_CLOSE_LENGTH = COMMENT_CLOSE.length();

	public Marker parseNext() throws ParserException {
		String text = getText();
		int parsingPoint = getParsingPoint();

		int start, end;
		Marker mark = null;

		while (true) {
			start = text.indexOf(TAG_OPEN, parsingPoint);
			if (start >= 0) {
				// 주석일 경우
				if (text.startsWith(COMMENT_OPEN, start)) {
					end = text.indexOf(COMMENT_CLOSE, start + COMMENT_OPEN_LENGTH);
					if (end >= 0) {
						end += COMMENT_CLOSE_LENGTH;
						parsingPoint = end;
					} else {
						break;
					}
				}

				// 테그일 경우
				else {
					end = text.indexOf(TAG_CLOSE, start + TAG_OPEN_LENGTH);
					if (end >= 0) {
						end += TAG_CLOSE_LENGTH;
						mark = new Marker(start, end, text.substring(start, end).trim());
						setParsingPoint(end);
					}
					break;
				}
			} else {
				break;
			}
		}

		setParsingPoint(mark);
		return mark;
	}

	public static String filterComments(String s) // 주석만 걸러낸다
	{
		StringBuffer buff = new StringBuffer();
		int pivot = 0;
		while (true) {
			int start = s.indexOf(COMMENT_OPEN, pivot);
			int end;
			if (start < 0) {
				buff.append(s.substring(pivot));
				break;
			} else { // 주석을 찾았을때
				end = s.indexOf(COMMENT_CLOSE, start + COMMENT_OPEN_LENGTH);
				if (end < 0) {
					buff.append(s.substring(start));
					break;
				} else {
					buff.append(s.substring(pivot, start));
					pivot = end + COMMENT_CLOSE_LENGTH;
				}
			}
		}
		return StringUtil.replace(StringUtil.replace(buff.toString(),COMMENT_OPEN_T, COMMENT_OPEN),COMMENT_CLOSE_T, COMMENT_CLOSE);
	}

	/**
	 * 현재 Tag(Start테그여야한다)에 해당하는 EndTag의 Marker를 리턴한다.
	 * 
	 * @param text
	 *            String
	 * @param startMark
	 *            Marker
	 * @throws ParserException
	 *             계층구조가 올바르지 않거나 End Tag를 찾을 수 없는 경우(Start Tag는 있는데..)
	 * @return Marker
	 */
	public static Marker getEndTagMarkerOf(String text, Marker startMark) throws ParserException {
		/**
		 * 원리) start tag가 나타나면 depth를 1씩 증가하고 end tag가 나타나면 depth를 1씩 감소한다.
		 * 
		 * 만일 계층 구조가 제대로 되어 있다면 depth가 0이 되는 순간 tag가 닫히는 순간이다.
		 */

		int depth = 1; // 이 메소드를 부른 상태에 이미 열려져 있는 상태(start tag를 지나쳤음) 이므로 1로 설정
		Marker mark = null;

		Parser tagParser = new TagParser();
		tagParser.initialize(text);
		tagParser.setParsingPoint(startMark);

		Parser nameParser = new TagNameParser();
		nameParser.initialize(startMark);
		Marker nameStartMark = nameParser.parseNext();

		// Start Tag가 아니면 할 필요가 없다.
		if (!TagNameParser.isStartTag(nameStartMark))
			return startMark;

		String tagName = TagNameParser.getRealTagName(nameStartMark);

		try {
			do {
				mark = tagParser.parseNext();
				if (mark == null) {
					throw new ParserException("could not find the corresponding end tag.");
				}

				nameParser.initialize(mark);
				Marker nameMark = nameParser.parseNext();

				if (tagName.equals(TagNameParser.getRealTagName(nameMark)))
					if (TagNameParser.isBodyTag(nameMark)) {
						if (TagNameParser.isStartTag(nameMark))
							++depth;
						else
							--depth;
					}
			} while (depth > 0);

			// // end tag의 시작 부분을 다음 parsing point로 설정한다.
			// tagParser.setParsingPoint(mark.getBeginIndex());
			return mark;
		} finally {
			tagParser.release();
			nameParser.release();
		}
	}

	// public static void main( String[] args ) throws ParserException
	// {
	// String s =
	// "가나다라[[--sfafsa--]]    [[---여기는 주석입니다.---]]gsdgsd[[--sddsgsdgsg--]]fdgsgs[[--1--]]--]][[--11144214-]--]][[--2--]]2[[--]]";
	//
	// Parser p = new TagParser();
	// p.initialize( s );
	// Marker m;
	//
	// while ( ( m = p.parseNext() ) != null )
	// {
	// System.out.println( m.getValue() );
	// }
	// }

	// public static void main(String[] args) throws Exception
	// {
	// System.out.println(filterComments("가나다[[---가나다라마바사~~~ㅏㅓ놀ㄴ이ㅏ모ㅓㄴㅇㅎ모--]] 라마[[---[[--주석2---]]바사아자차카"));
	// }
}
