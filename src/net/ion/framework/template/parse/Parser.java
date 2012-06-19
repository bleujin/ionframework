package net.ion.framework.template.parse;


/**
 * parser 추상 구현 <br/>
 * parser를 만들 때 상속하여 구현한다.
 * 
 * @author Kim Sanghoon wizest@i-on.net
 * @version 1.0
 */
public abstract class Parser {
	private String text = null;
	private int parsingPoint = 0;

	/**
	 * 주의:생성한뒤 initialize()를 invoke한 뒤 사용한다.
	 */
	public Parser() {
	}

	/**
	 * parsing하고자 하는 string을 설정한다. parsingPoint는 reset된다.
	 * 
	 * @param text
	 */
	public void initialize(String text) {
		this.text = text;
		setParsingPoint(0);
	}

	/**
	 * 재 사용을 위해 release한다.
	 */
	public void release() {
		this.text = null;
		setParsingPoint(0);

	}

	/**
	 * parsing하고자 하는 string을 설정한다. parsingPoint는 reset된다.
	 * 
	 * @param mark
	 *            mark.getParsedString()이 parsing 하려는 string으로 설정된다.
	 */
	public void initialize(Marker mark) {
		initialize(mark.getValue());
	}

	/**
	 * parsing 대상 문자열(text) 를 가져온다.
	 * 
	 * @return
	 */
	public String getText() {
		return this.text;
	}

	/**
	 * parsing을 시작할 위치를 지정한다. mark가 null일 경우 text의 마지막 위치+1을 가르킨다.
	 * 
	 * @param mark
	 *            mark의 endIndex+1 를 다음 parsing의 시작 위치로 한다.
	 */
	public void setParsingPoint(Marker mark) {
		if (mark == null) {
			this.parsingPoint = this.text.length();
		} else {
			this.parsingPoint = mark.getEndIndex();
		}
	}

	/**
	 * parsing을 시작할 위치를 지정한다.
	 * 
	 * @param index
	 *            시작 위치
	 */
	public void setParsingPoint(int index) {
		this.parsingPoint = index;
	}

	/**
	 * parsing을 시작할 위치를 리턴한다.
	 * 
	 * @return
	 */
	public int getParsingPoint() {
		return this.parsingPoint;
	}

	/**
	 * 현재 위치에서 parsing을 시작하여 찾은 정보를 리턴한다. (현재 위치는 찾은 위치로 업데이트된다.)
	 * 
	 * @return null if no more parsing data
	 * @throws ParserException
	 */
	public abstract Marker parseNext() throws ParserException;
}
