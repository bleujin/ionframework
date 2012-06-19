package net.ion.framework.util.express;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Stack;

import net.ion.framework.template.parse.Marker;
import net.ion.framework.util.GenericCache;

/**
 * 중위 표기법을 후위 표기법으로 바꾼다.
 * 
 * @author Kim Sanghoon wizest@i-on.net
 * @version 1.0
 */

public class ToPostfixExpress {
	private String[][][] ops;
	private String[] plainOps; // 피연산자수,우선순위를 무시하고 단순 연산자 리스트
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
	 * 첫번째 인덱스 : 연산에 필요한 피연산자수 -> 단항연산자, 이항연산자,... index번호가 피연산자수를 의미한다. 두번째 인덱스 : 우선순위 세번째 인덱스 : 동일한 우선순위의 연산자 배열
	 * 
	 * <code>
	 *  연산자 예제)
	 *  String[][][] ops = {
	 *      // operand 0
	 *      {{}},
	 *      // operand 1
	 *      {{"(",")"},{"!"}},
	 *      // operand 2
	 *      {{},{},{"<>","==",">=","<=","=",">","<"},{"&&","||"}}};
	 * 
	 *      참고 : {}으로 우선순위를 맞춰준다.
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
		// 길이가 긴것을 앞으로해서 정렬
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
	 * @return null if evalString이 잘못되었을 경우
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

		// 알고리즘
		// 1. 연산자가 아니면 결과에 더한다.
		// 2. 연사자일 경우 스택과 비교하여 우선순위가 높을 경우 스택에 넣는다.
		// 3. 우선 순위가 같거나 낮을 경우 우선순워가 낮을때 까지 다 꺼내고 자신을 넣는다.
		// 4. 괄호는 우선순위가 가장 높으며 쫌 특이하다. -_-
		Stack<String> os = new Stack<String>();

		ArrayList<String> ex = new ArrayList<String>(); // expression
		ArrayList<Integer> no = new ArrayList<Integer>(); // 필요한 피연산자 수 -> 0 보다 클경우 연산자다. 단항연산자 1, 이항연산자 2, (아직 단항 연산자는 지원하지 않지만 ㅡ.ㅡ")

		Integer OPERAND = new Integer(0);
		// Integer UNARYOP = new Integer(1);
		// Integer BINARYOP = new Integer(2);

		int parenthesisChecker = 0; // ( )의 갯수 맞는지 체크

		int parseIdx = 0;
		Marker mark = null;
		String op;
		String value;

		while (true) {
			// parseIdx이후 연산자를 찾는다.
			mark = indexOf(parseIdx, evalString);

			// 연산자를 찾지 못할 경우
			if (mark == null) {
				op = null;
				value = evalString.substring(parseIdx).trim();
				if (value.length() > 0) {
					ex.add(filterSlashMark(value));
					// io.add(Boolean.FALSE);
					no.add(OPERAND);
				}

				// 스택에 있는 것 옮기기
				while (!os.isEmpty()) {
					ex.add(filterSlashMark((String) os.peek()));
					// io.add(Boolean.TRUE);
					// no.add(BINARYOP);
					no.add(opsOperandNumber.get(os.pop()));
				}
				break;
			}
			// 연산자를 찾았을 경우
			else {
				op = mark.getValue();

				// 괄호 체크: 열린거 닫힌거 개수가 같아야지
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

				if (op.equals(")")) { // )를 만나면 (까지 모두 빼낸다.
					while (true) {
						if (os.empty()) {
							return null; // ( 을 발견하기전에 empty일 수가 없다.
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
							if (oldOp.equals("(")) { // (를 만나면 스택에서 빼내는거 중지
								os.add(op);
								break;
							}

							int pri = compareOpsPriority(oldOp, op);
							if (pri < 0) {
								os.add(op);
								break;
							} else { // 같거나 작으면
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
	 * @return fromIndex로 부터 다음 operator를 찾는다.
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
					// slash가 홀수개이면
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
	 * @return op1이 op2보다 우선순위가 높으면 양수, 낮으면 음수, 같으면 0
	 */
	private int compareOpsPriority(String op1, String op2) {
		int p1 = opsPriorityMap.get(op1).intValue();
		int p2 = opsPriorityMap.get(op2).intValue();

		return p2 - p1;
	}

	public static boolean checkVectorOperationSyntax(PostfixExpress ex, String[] vectorOps) {
		// 집합의 연산이 적절한가
		String[] es = ex.getExpressions();
		int[] no = ex.getNeedOperandNumber();

		if (es.length == 0) {
			return true; // 아무런 식이 없으면 true
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
				if (isVectorOp) { // vector연산자이면
					vector -= 2; // vector 두개를 꺼내 연산한뒤 하나를 만든다.
					++vector;
				} else {
					scaler -= 2; // scaler 두개를 가지고 vector하나를 만든다.
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

				// 슬레시 수가 홀수이면 씹는다.
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
