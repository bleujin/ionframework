package net.ion.framework.util.express;


/**
 * ���� ǥ���
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
	 * ���� ǥ���
	 * 
	 * @param origin
	 *            String �����ڿ�
	 * @param express
	 *            String[] ���� ǥ������� ǥ���� ���� ���� �迭
	 * @param needOperand
	 *            int[] express �迭�� �� �ε����� �ʿ���ϴ� operand �� (0�� ��� �ǿ�����)
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
