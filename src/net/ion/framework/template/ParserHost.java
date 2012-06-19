package net.ion.framework.template;

import net.ion.framework.pool.ObjectHost;
import net.ion.framework.pool.SimpleObjectPool;
import net.ion.framework.template.parse.Parser;

/**
 * Parse Pool
 * 
 * @author Kim Sanghoon wizest@i-on.net
 * @version 1.0
 */

public class ParserHost extends ObjectHost {
	/**
	 * @param parserClass
	 *            Class �ļ� Ŭ����
	 * @param maxParser
	 *            int �ʱ� ���� ����
	 */
	public ParserHost(Class<?> parserClass, int maxParser) {
		super(new SimpleObjectPool(maxParser), parserClass);
	}

	/**
	 * �ļ��� Ǯ���� ���� �ش�. �������� ���� ��� �����Ͽ� �ش�.
	 * 
	 * @return Parser
	 */
	public Parser getParser() {
		return (Parser) super.askObject();
	}

	/**
	 * ����� �ļ��� �ݳ��Ѵ�.
	 * 
	 * @param parser
	 *            Parser
	 */
	public void releaseParser(Parser parser) {
		super.releaseObject(parser);
	}

	public void beforeAskObject(Object o) {
	}

	public void beforeReleaseObject(Object o) {
	}
}
