package net.ion.framework.template.parse;


/**
 * parser �߻� ���� <br/>
 * parser�� ���� �� ����Ͽ� �����Ѵ�.
 * 
 * @author Kim Sanghoon wizest@i-on.net
 * @version 1.0
 */
public abstract class Parser {
	private String text = null;
	private int parsingPoint = 0;

	/**
	 * ����:�����ѵ� initialize()�� invoke�� �� ����Ѵ�.
	 */
	public Parser() {
	}

	/**
	 * parsing�ϰ��� �ϴ� string�� �����Ѵ�. parsingPoint�� reset�ȴ�.
	 * 
	 * @param text
	 */
	public void initialize(String text) {
		this.text = text;
		setParsingPoint(0);
	}

	/**
	 * �� ����� ���� release�Ѵ�.
	 */
	public void release() {
		this.text = null;
		setParsingPoint(0);

	}

	/**
	 * parsing�ϰ��� �ϴ� string�� �����Ѵ�. parsingPoint�� reset�ȴ�.
	 * 
	 * @param mark
	 *            mark.getParsedString()�� parsing �Ϸ��� string���� �����ȴ�.
	 */
	public void initialize(Marker mark) {
		initialize(mark.getValue());
	}

	/**
	 * parsing ��� ���ڿ�(text) �� �����´�.
	 * 
	 * @return
	 */
	public String getText() {
		return this.text;
	}

	/**
	 * parsing�� ������ ��ġ�� �����Ѵ�. mark�� null�� ��� text�� ������ ��ġ+1�� ����Ų��.
	 * 
	 * @param mark
	 *            mark�� endIndex+1 �� ���� parsing�� ���� ��ġ�� �Ѵ�.
	 */
	public void setParsingPoint(Marker mark) {
		if (mark == null) {
			this.parsingPoint = this.text.length();
		} else {
			this.parsingPoint = mark.getEndIndex();
		}
	}

	/**
	 * parsing�� ������ ��ġ�� �����Ѵ�.
	 * 
	 * @param index
	 *            ���� ��ġ
	 */
	public void setParsingPoint(int index) {
		this.parsingPoint = index;
	}

	/**
	 * parsing�� ������ ��ġ�� �����Ѵ�.
	 * 
	 * @return
	 */
	public int getParsingPoint() {
		return this.parsingPoint;
	}

	/**
	 * ���� ��ġ���� parsing�� �����Ͽ� ã�� ������ �����Ѵ�. (���� ��ġ�� ã�� ��ġ�� ������Ʈ�ȴ�.)
	 * 
	 * @return null if no more parsing data
	 * @throws ParserException
	 */
	public abstract Marker parseNext() throws ParserException;
}
