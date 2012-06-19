package net.ion.framework.util.express;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Stack;

import net.ion.framework.template.parse.Marker;
import net.ion.framework.util.GenericCache;

/**
 * ���� ǥ����� ���� ǥ������� �ٲ۴�.
 * 
 * @author Kim Sanghoon wizest@i-on.net
 * @version 1.0
 */

public class ToPostfixExpress {
	private String[][][] ops;
	private String[] plainOps; // �ǿ����ڼ�,�켱������ �����ϰ� �ܼ� ������ ����Ʈ
	private HashMap<String, Integer> opsPriorityMap;
	private HashMap<String, Integer> opsOperandNumber;

	private GenericCache cache;
	private static final int MAX_CACHE = 100;

	private static final char SLASH_MARK = '\\';
	private boolean USE_SLASH_MARK = true;

	public ToPostfixExpress(String[][][] orderedOperators) {
		this(orderedOperators, true);
	}

	/**
	 * ù��° �ε��� : ���꿡 �ʿ��� �ǿ����ڼ� -> ���׿�����, ���׿�����,... index��ȣ�� �ǿ����ڼ��� �ǹ��Ѵ�. �ι�° �ε��� : �켱���� ����° �ε��� : ������ �켱������ ������ �迭
	 * 
	 * <code>
	 *  ������ ����)
	 *  String[][][] ops = {
	 *      // operand 0
	 *      {{}},
	 *      // operand 1
	 *      {{"(",")"},{"!"}},
	 *      // operand 2
	 *      {{},{},{"<>","==",">=","<=","=",">","<"},{"&&","||"}}};
	 * 
	 *      ���� : {}���� �켱������ �����ش�.
     * </code>
	 * 
	 * @param orderedOperators
	 */
	public ToPostfixExpress(String[][][] orderedOperators, boolean useSlashMark) {
		this.USE_SLASH_MARK = useSlashMark;
		this.ops = orderedOperators;
		this.opsPriorityMap = new HashMap<String, Integer>();
		this.opsOperandNumber = new HashMap<String, Integer>();

		ArrayList<String> ol = new ArrayList<String>();

		// build priority map
		for (int i = 0; i < ops.length; ++i) {
			for (int j = 0; j < ops[i].length; ++j) {
				for (int k = 0; k < ops[i][j].length; ++k) {
					opsPriorityMap.put(ops[i][j][k], new Integer(j));
					opsOperandNumber.put(ops[i][j][k], new Integer(i));

					ol.add(ops[i][j][k]);
				}
			}
		}

		plainOps = ol.toArray(new String[0]);
		// ���̰� ����� �������ؼ� ����
		Arrays.sort(plainOps, new Comparator<String>() {
			public int compare(String o1, String o2) {
				String s1 = (String) o1;
				String s2 = (String) o2;

				return s2.length() - s1.length();
			}

			public boolean equals(Object obj) {
				return true;
			}
		});

		this.cache = new GenericCache(ToPostfixExpress.MAX_CACHE);
	}

	/**
	 * @param evalString
	 * @return null if evalString�� �߸��Ǿ��� ���
	 */
	public PostfixExpress toPostfixExpress(String evalString) {
		if (evalString == null) {
			return new PostfixExpress(null, new String[0], new int[0]);
		}

		// from cache
		PostfixExpress postfixEx = (PostfixExpress) cache.get(evalString);
		if (postfixEx != null) {
			return postfixEx;
		}

		// �˰���
		// 1. �����ڰ� �ƴϸ� ����� ���Ѵ�.
		// 2. �������� ��� ���ð� ���Ͽ� �켱������ ���� ��� ���ÿ� �ִ´�.
		// 3. �켱 ������ ���ų� ���� ��� �켱������ ������ ���� �� ������ �ڽ��� �ִ´�.
		// 4. ��ȣ�� �켱������ ���� ������ �� Ư���ϴ�. -_-
		Stack<String> os = new Stack<String>();

		ArrayList<String> ex = new ArrayList<String>(); // expression
		ArrayList<Integer> no = new ArrayList<Integer>(); // �ʿ��� �ǿ����� �� -> 0 ���� Ŭ��� �����ڴ�. ���׿����� 1, ���׿����� 2, (���� ���� �����ڴ� �������� ������ ��.��")

		Integer OPERAND = new Integer(0);
		// Integer UNARYOP = new Integer(1);
		// Integer BINARYOP = new Integer(2);

		int parenthesisChecker = 0; // ( )�� ���� �´��� üũ

		int parseIdx = 0;
		Marker mark = null;
		String op;
		String value;

		while (true) {
			// parseIdx���� �����ڸ� ã�´�.
			mark = indexOf(parseIdx, evalString);

			// �����ڸ� ã�� ���� ���
			if (mark == null) {
				op = null;
				value = evalString.substring(parseIdx).trim();
				if (value.length() > 0) {
					ex.add(filterSlashMark(value));
					// io.add(Boolean.FALSE);
					no.add(OPERAND);
				}

				// ���ÿ� �ִ� �� �ű��
				while (!os.isEmpty()) {
					ex.add(filterSlashMark((String) os.peek()));
					// io.add(Boolean.TRUE);
					// no.add(BINARYOP);
					no.add(opsOperandNumber.get(os.pop()));
				}
				break;
			}
			// �����ڸ� ã���� ���
			else {
				op = mark.getValue();

				// ��ȣ üũ: ������ ������ ������ ���ƾ���
				if (op.equals("(")) {
					++parenthesisChecker;
				} else if (op.equals(")")) {
					--parenthesisChecker;
				}

				value = evalString.substring(parseIdx, mark.getBeginIndex()).trim();
				if (value.length() > 0) {
					ex.add(filterSlashMark(value));
					// io.add(Boolean.FALSE);
					no.add(OPERAND);
				}

				if (op.equals(")")) { // )�� ������ (���� ��� ������.
					while (true) {
						if (os.empty()) {
							return null; // ( �� �߰��ϱ����� empty�� ���� ����.
						}
						String oldOp = (String) os.pop();
						if (oldOp.equals("(")) {
							break;
						} else {
							ex.add(filterSlashMark(oldOp));
							// io.add(Boolean.TRUE);
							// no.add(BINARYOP);
							no.add(opsOperandNumber.get(oldOp));
						}
					}
				} else {
					while (true) {
						if (os.isEmpty()) {
							os.add(op);
							break;
						} else {
							String oldOp = (String) os.peek();
							if (oldOp.equals("(")) { // (�� ������ ���ÿ��� �����°� ����
								os.add(op);
								break;
							}

							int pri = compareOpsPriority(oldOp, op);
							if (pri < 0) {
								os.add(op);
								break;
							} else { // ���ų� ������
								ex.add(filterSlashMark((String) os.peek()));
								// io.add(Boolean.TRUE);
								// no.add(BINARYOP);
								no.add(opsOperandNumber.get(os.pop()));
							}
						}
					}
				}
				parseIdx = mark.getEndIndex();
			}
		}

		if (parenthesisChecker != 0) {
			return null;
		} else {
			String[] exs = ex.toArray(new String[0]);
			int[] nos = new int[exs.length];

			for (int i = 0; i < nos.length; ++i) {
				nos[i] = no.get(i).intValue();
			}

			postfixEx = new PostfixExpress(evalString, exs, nos);
			cache.put(evalString, postfixEx);

			return postfixEx;
		}
	}

	/**
	 * @param fromIndex
	 * @param text
	 * @param ops
	 * @return fromIndex�� ���� ���� operator�� ã�´�.
	 */
	// private Marker indexOf(int fromIndex,String text)
	// {
	// Marker m=null;
	// int min=Integer.MAX_VALUE;
	// for(int i=0;i < plainOps.length;++i) {
	// int p=text.indexOf(plainOps[i],fromIndex);
	// if(p >= 0 && p < min) {
	// min=p;
	// m=new Marker(p,p + plainOps[i].length(),plainOps[i]);
	// }
	// }
	// return m;
	// }

	private Marker indexOf(int fromIndex, String text) {
		Marker m = null;
		int min = Integer.MAX_VALUE;
		for (int i = 0; i < plainOps.length; ++i) {
			int pivot;
			int begin = fromIndex;
			for (;;) { // SCAN
				pivot = text.indexOf(plainOps[i], begin);
				if (USE_SLASH_MARK && pivot - 1 >= 0) {

					// backward slash count
					// slash�� Ȧ�����̸�
					int slashCnt = 0;
					for (int j = pivot - 1; j > 0 && text.charAt(j) == SLASH_MARK; --j) {
						++slashCnt;
					}
					if (slashCnt % 2 > 0) {
						begin = pivot + 1;
						continue;
					}
				}
				break;
			}

			if (pivot >= 0 && pivot < min) {
				min = pivot;
				m = new Marker(pivot, pivot + plainOps[i].length(), plainOps[i]);
			}
		}
		return m;
	}

	/**
	 * @param op1
	 * @param op2
	 * @return op1�� op2���� �켱������ ������ ���, ������ ����, ������ 0
	 */
	private int compareOpsPriority(String op1, String op2) {
		int p1 = opsPriorityMap.get(op1).intValue();
		int p2 = opsPriorityMap.get(op2).intValue();

		return p2 - p1;
	}

	public static boolean checkVectorOperationSyntax(PostfixExpress ex, String[] vectorOps) {
		// ������ ������ �����Ѱ�
		String[] es = ex.getExpressions();
		int[] no = ex.getNeedOperandNumber();

		if (es.length == 0) {
			return true; // �ƹ��� ���� ������ true
		}

		int vector = 0;
		int scaler = 0;
		int size = es.length;

		for (int i = 0; i < size; ++i) {
			if (no[i] == 0) {
				++scaler;
			} else {
				boolean isVectorOp = false;
				for (int j = 0; j < vectorOps.length; ++j) {
					if (es[i].equals(vectorOps[j])) {
						isVectorOp = true;
						break;
					}
				}
				if (isVectorOp) { // vector�������̸�
					vector -= 2; // vector �ΰ��� ���� �����ѵ� �ϳ��� �����.
					++vector;
				} else {
					scaler -= 2; // scaler �ΰ��� ������ vector�ϳ��� �����.
					++vector;
				}
			}
		}

		// System.out.println(ex+":S="+scaler+",V="+vector); // debug
		if (scaler == 0 && vector == 1) {
			return true;
		} else {
			return false;
		}
	}

	private String filterSlashMark(String s) {
		if (USE_SLASH_MARK) {
			if (s.indexOf(SLASH_MARK) < 0)
				return s;

			StringBuffer s2 = new StringBuffer(s.length());
			int slashCnt = 0;
			for (int i = 0, length = s.length(); i < length; ++i) {
				if (s.charAt(i) == SLASH_MARK)
					++slashCnt;
				else
					slashCnt = 0;

				// ������ ���� Ȧ���̸� �ô´�.
				if ((slashCnt % 2) > 0)
					;
				else
					s2.append(s.charAt(i));
			}
			return s2.toString();
		} else
			return s;
	}

}
