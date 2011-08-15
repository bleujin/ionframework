package net.ion.framework.template;

import java.util.ArrayList;

/**
 * �ؽ�Ʈ ���ø����� �ڵ�κи� �и� ǥ���� Ŭ����
 * 
 * <pre>
 * // operation code -----------------------------------------------------------------------------
 * operator                        operand (�ǿ����� ��,�ǹ�(Ÿ��))
 * --------------------------------------------------------------------------------------------
 * OPCODE_TERMINATE = 0           0 X ���� ����
 * OPCODE_EXCEPTION = 1           1 �����޼���(data address) ���ܸ� �߻� -> ������ ����
 * OPCODE_GOTO = 2                1 ������ code address
 * OPCODE_NO_OPERATION = 3        0 �ƹ� �ϵ� ���� �ʴ´�.
 * 
 * OPCODE_PRINT = 4               1 ����Ʈ�� ������(String)(data address)
 * 
 * OPCODE_LOAD_HANDLER = 10       2 tag handler name(data address), reflection parameter(data address)
 * OPCODE_LOAD_PAGE_HANDLER = 11  2 tag handler name(data address), reflection parameter(data address)
 * OPCODE_RELEASE_HANDLER = 12    0 X
 * 
 * // conditional jump operation
 * OPCODE_DO_START_TAG = 20       3 SKIP_BODY�� ��� ������ �ּ�(code address),EVAL_BODY_INCLUDE�� ���,EVAL_BODY_BUFFERED�� ���
 * OPCODE_DO_END_TAG = 21         2 SKIP_PAGE�� ��� ������ �ּ�(code address),EVAL_PAGE�� ��� ������ �ּ�(code address),
 * OPCODE_DO_AFTER_BODY = 22      2 SKIP_BODY�� ��� ������ �ּ�(code address),EVAL_BODY_AGAIN�� ��� ������ �ּ�(code address)
 * 
 * OPCODE_DO_START_TEMPLATE = 23  2 SKIP_PAGE�� ��� ������ �ּ�(code address),EVAL_PAGE�� �� ������ �ּ�(code address)
 * OPCODE_DO_END_TEMPLATE = 24    0 X
 * OPCODE_DO_CREATE_PAGEINFO = 25 0 X
 * OPCODE_DO_AFTER_PAGE = 26      2 SKIP_PAGE/SKIP_PAGE_WITHOUT_PAGE_GENERATION�� ��� ������ �ּ�(code address),EVAL_PAGE_AGAIN/EVAL_PAGE_AGAIN_WITHOUT_PAGE_GENERATION�� �� ������ �ּ�(code address)
 * 
 * // evaluating mark for error tracing
 * OPCODE_EVAL_MARK = 30          1 ���� �۾����� tag�� Marker(net.ion.framework.Marker)(data address)
 * </pre>
 * 
 * @author Kim Sanghoon wizest@i-on.net
 * @version 1.0
 * 
 * @see OperationData
 */

public class OperationCode {
	// operation code -----------------------------------------------------------------------------
	// operator operand (�ǿ����� ��,�ǹ�(Ÿ��))
	// --------------------------------------------------------------------------------------------
	public final static int OPCODE_TERMINATE = 0; // 0 X ���� ����
	public final static int OPCODE_EXCEPTION = 1; // 1 �����޼���(data address) ���ܸ� �߻� -> ������ ����
	public final static int OPCODE_GOTO = 2; // 1 ������ code address
	public final static int OPCODE_NO_OPERATION = 3; // 0 �ƹ� �ϵ� ���� �ʴ´�.

	public final static int OPCODE_PRINT = 4; // 1 ����Ʈ�� ������(String)(data address)

	public final static int OPCODE_LOAD_HANDLER = 10; // 2 tag handler name(data address), reflection parameter(data address)
	public final static int OPCODE_LOAD_PAGE_HANDLER = 11; // 2 tag handler name(data address), reflection parameter(data address)
	public final static int OPCODE_RELEASE_HANDLER = 12; // 0 X

	// conditional jump operation
	public final static int OPCODE_DO_START_TAG = 20; // 3 SKIP_BODY�� ��� ������ �ּ�(code address),EVAL_BODY_INCLUDE�� ���,EVAL_BODY_BUFFERED�� ���
	public final static int OPCODE_DO_END_TAG = 21; // 2 SKIP_PAGE�� ��� ������ �ּ�(code address),EVAL_PAGE�� ��� ������ �ּ�(code address),
	public final static int OPCODE_DO_AFTER_BODY = 22; // 2 SKIP_BODY�� ��� ������ �ּ�(code address),EVAL_BODY_AGAIN�� ��� ������ �ּ�(code address)

	public final static int OPCODE_DO_START_TEMPLATE = 23; // 2 SKIP_PAGE�� ��� ������ �ּ�(code address),EVAL_PAGE�� �� ������ �ּ�(code address)
	public final static int OPCODE_DO_END_TEMPLATE = 24; // 0 X
	public final static int OPCODE_DO_CREATE_PAGEINFO = 25; // 0 X
	public final static int OPCODE_DO_AFTER_PAGE = 26; // 2 SKIP_PAGE/SKIP_PAGE_WITHOUT_PAGE_GENERATION�� ��� ������ �ּ�(code address),EVAL_PAGE_AGAIN/EVAL_PAGE_AGAIN_WITHOUT_PAGE_GENERATION�� �� ������ �ּ�(code address)

	// evaluating mark for error tracing
	public final static int OPCODE_EVAL_MARK = 30; // 1 ���� �۾����� tag�� Marker(net.ion.framework.Marker)(data address)
	// --------------------------------------------------------------------------------------------

	private ArrayList<int[]> code = null;

	public OperationCode() {
		this.code = new ArrayList<int[]>();
	}

	/**
	 * ���ο� opcode�� �߰��Ѵ�.
	 * 
	 * @param opcode
	 *            int operation code
	 * @return int code�� �߰��� �ּ�
	 */
	public int add(int opcode) {
		int[] c = new int[] { opcode };
		code.add(c);
		return getLastAddress();
	}

	/**
	 * ���ο� opcode�� �߰��Ѵ�.
	 * 
	 * @param opcode
	 *            int operation code
	 * @param operand1
	 *            int operand 1
	 * @return int code�� �߰��� �ּ�
	 */
	public int add(int opcode, int operand1) {
		int[] c = new int[] { opcode, operand1 };
		code.add(c);
		return getLastAddress();
	}

	/**
	 * ���ο� opcode�� �߰��Ѵ�.
	 * 
	 * @param opcode
	 *            int operation code
	 * @param operand1
	 *            int operand 1
	 * @param operand2
	 *            int operand 2
	 * @return int code�� �߰��� �ּ�
	 */
	public int add(int opcode, int operand1, int operand2) {
		int[] c = new int[] { opcode, operand1, operand2 };
		code.add(c);
		return getLastAddress();
	}

	/**
	 * ���ο� opcode�� �߰��Ѵ�.
	 * 
	 * @param opcode
	 *            int operation code
	 * @param operand1
	 *            int operand 1
	 * @param operand2
	 *            int operand 2
	 * @param operand3
	 *            int operand 3
	 * @return int code�� �߰��� �ּ�
	 */
	public int add(int opcode, int operand1, int operand2, int operand3) {
		int[] c = new int[] { opcode, operand1, operand2, operand3 };
		code.add(c);
		return getLastAddress();
	}

	/**
	 * Ư�� �ּ��� �ڵ� ���� �����´�.
	 * 
	 * @param address
	 *            int
	 * @return int[] index 0 - operation code , index 1~ - operands
	 */
	public final int[] getCodeAt(int address) {
		return (int[]) code.get(address);
	}

	/**
	 * Ư�� �ּ��� �ڵ� ���� ���ο� ������ ��ü�Ѵ�.
	 * 
	 * @param address
	 *            int ��ü�� �ּ�
	 * @param newCode
	 *            int[] �� �ڵ� ��
	 */
	public void updateCodeAt(int address, int[] newCode) {
		this.code.set(address, newCode);
	}

	/**
	 * Ư�� �ּ��� �ڵ� ���� ���ο� ������ ��ü�Ѵ�.
	 * 
	 * @param address
	 *            int ��ü�� �ּ�
	 * @param index
	 *            int operation code
	 * @param value
	 *            int operand
	 */
	public void updateCodeAt(int address, int index, int value) {
		int[] oldCode = getCodeAt(address);
		oldCode[index] = value;
		updateCodeAt(address, oldCode);
	}

	/**
	 * code�� ����� ������ �ּ�(�ε���)
	 * 
	 * @return
	 */
	public int getLastAddress() {
		return code.size() - 1;
	}

	/**
	 * ���� code�� ����� �ּ� = getLastAddress()+1
	 * 
	 * @return
	 */
	public int getAddressToAdd() {
		return code.size();
	}

	/**
	 * ���� code�� ����ǰ� �� ���� �� ������ �ּ� = getAddressToAdd()+1 = getLastAddress()+2
	 * 
	 * @return
	 */
	public int getAfterAddressToAdd() {
		return code.size() + 1;
	}

	/**
	 * �ڵ� ����
	 * 
	 * @return int
	 */
	public int getSize() {
		return code.size();
	}

	/**
	 * ù��° index -> code address, �ι�° index -> operation code(0) �Ǵ� operand index(1,2,3)
	 * 
	 * <pre>
	 *  int[][] code = cs.getCodeArray();
	 *  code[1][2]  -> address:1 �� operand2
	 *  code[1][0]  -> address:1 �� operation code
	 *  code[1][1]  -> address:1 �� operand1
	 *  code[2][3]  -> address:2 �� operand3
	 * </pre>
	 * 
	 * @return
	 */
	public final int[][] getCodeArray() {
		return code.toArray(new int[0][0]);
	}

	public String toString() {
		StringBuffer buff = new StringBuffer();
		int[][] codeArray = getCodeArray();

		for (int i = 0; i < codeArray.length; ++i) {
			buff.append("Address ");
			buff.append(i);
			buff.append(":  ");

			int[] opcode = codeArray[i];
			for (int j = 0; j < opcode.length; ++j) {
				buff.append("\t");
				buff.append(opcode[j]);
			}
			buff.append("\n");
		}

		return buff.toString();
	}
}
