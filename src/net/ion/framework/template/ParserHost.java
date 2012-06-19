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
	 *            Class 파서 클래스
	 * @param maxParser
	 *            int 초기 생성 개수
	 */
	public ParserHost(Class<?> parserClass, int maxParser) {
		super(new SimpleObjectPool(maxParser), parserClass);
	}

	/**
	 * 파서를 풀에서 꺼내 준다. 여유분이 없을 경우 생성하여 준다.
	 * 
	 * @return Parser
	 */
	public Parser getParser() {
		return (Parser) super.askObject();
	}

	/**
	 * 사용한 파서를 반납한다.
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
