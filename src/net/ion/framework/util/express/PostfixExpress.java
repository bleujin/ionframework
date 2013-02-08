package net.ion.framework.util.express;


/**
 * 후위 표기법
 * 
 * @author Kim Sanghoon wizest@i-on.net
 * @version 1.0
 * 
 * @see ToPostfixExpress
 */

public class PostfixExpress {
	private String origin;

	private String[] express;
	private int[] needOperandNumber;

	/**
	 * 후위 표기법
	 * 
	 * @param origin
	 *            String 원문자열
	 * @param express
	 *            String[] 후위 표기법으로 표현한 개별 문자 배열
	 * @param needOperand
	 *            int[] express 배열의 각 인덱스가 필요로하는 operand 수 (0일 경우 피연산자)
	 */
	public PostfixExpress(String origin, String[] express, int[] needOperand) {
		this.origin = origin;

		this.express = express;
		this.needOperandNumber = needOperand;
	}

	public String getOrigin() {
		return this.origin;
	}

	public String[] getExpressions() {
		return express;
	}

	public int[] getNeedOperandNumber() {
		return needOperandNumber;
	}

	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("{");
		for (int i = 0; i < express.length; ++i) {
			if (needOperandNumber[i] > 0) {
				buf.append("Op:");
			}
			buf.append(express[i]);
			buf.append(",");
		}
		buf.append("}");

		return buf.toString();
	}
}
