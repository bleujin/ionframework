package net.ion.framework.util.express;

/**
 * 연산자 중위 표기법
 * 
 * @author Kim Sanghoon wizest@i-on.net
 * @version 1.0
 * 
 * @see ToInfixExpress
 */

public class InfixExpress {
	private String origin;

	private String[] express;
	private boolean[] operator;

	/**
	 * 중위 표기법으로 나타낸다.
	 * 
	 * @param origin
	 *            String 원 문자열
	 * @param express
	 *            String[] 중위 표기법으로 나타낸 문자열 배열
	 * @param operator
	 *            boolean[] 각 문자열 배열 인덱스에서 연산자 여부
	 */
	public InfixExpress(String origin, String[] express, boolean[] operator) {
		this.origin = origin;

		this.express = express;
		this.operator = operator;
	}

	public String getOrigin() {
		return this.origin;
	}

	public String[] getExpressions() {
		return express;
	}

	public boolean[] getIsOperator() {
		return operator;
	}

	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("{");
		for (int i = 0; i < express.length; ++i) {
			if (operator[i]) {
				buf.append("Op:");
			}
			buf.append(express[i]);
			buf.append(",");
		}
		buf.append("}");

		return buf.toString();
	}

}
