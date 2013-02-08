package net.ion.framework.util.express;

import java.util.ArrayList;

import net.ion.framework.template.parse.Marker;
import net.ion.framework.util.GenericCache;

/**
 * <pre>
 * "우선순위를 고려하지 않은" infix expression으로 변경
 * 
 * '\' 는 연산자를 일반 문자로 바꾸어 주는 기능을 한다.
 *  예를들어 '+' 이 연산자라고 정의될때 \+ 은 일반 문자열 '+'으로 간주한다.
 * infixExpress 의 express 도 '\' 을 고려하여 최종 결과가 들어간다.
 * </pre>
 * 
 * @author Kim Sanghoon wizest@i-on.net
 * @version 1.0
 */

public class ToInfixExpress {
	private String[] plainOps;

	private GenericCache cache;
	private static final int MAX_CACHE = 100;

	public ToInfixExpress(String[] plainOps) {
		init(plainOps);
	}

/**
     * *. 연산자 리스트 예제
     *
     *   private static String[][] searchOps= { {"(",")"}
     *                                        , {"<>","==",">=","<=","=",">","<"}
     *                                        , {"&&","||"}};
     *
     * @param orderedOperators - 내부적으로는 우선순위를 고려하지 않으므로 주의!
     */
	public ToInfixExpress(String[][] orderedOperators) {
		ArrayList<String> al = new ArrayList<String>();
		for (int i = 0; i < orderedOperators.length; ++i) {
			for (int j = 0; j < orderedOperators[i].length; ++j) {
				al.add(orderedOperators[i][j]);
			}
		}

		this.plainOps = al.toArray(new String[0]);

		init(plainOps);
	}

	public ToInfixExpress(String[][][] orderedOperators) {
		ArrayList<String> al = new ArrayList<String>();
		for (int i = 0; i < orderedOperators.length; ++i) {
			for (int j = 0; j < orderedOperators[i].length; ++j) {
				for (int k = 0; k < orderedOperators[i][j].length; ++k) {
					al.add(orderedOperators[i][j][k]);
				}
			}
		}

		this.plainOps = al.toArray(new String[0]);

		init(plainOps);

	}

	private void init(String[] plainOps) {
		this.plainOps = plainOps;
		this.cache = new GenericCache(ToInfixExpress.MAX_CACHE);
	}

	public InfixExpress toInfixExpress(String evalString) {
		if (evalString == null) {
			return new InfixExpress(null, new String[0], new boolean[0]);
		}

		InfixExpress infixEx = null;

		ArrayList<String> ex = new ArrayList<String>();
		ArrayList<Boolean> bs = new ArrayList<Boolean>();

		Marker mark = null;
		int parseIdx = 0;
		String op;
		String value;

		while (true) {
			// parseIdx이후 연산자를 찾는다.
			mark = indexOf(parseIdx, evalString); // ,plainOps);

			// 연산자를 찾지 못할 경우
			if (mark == null) {
				op = null;
				value = evalString.substring(parseIdx).trim();
				if (value.length() > 0) {
					ex.add(filterSlashMark(value));
					bs.add(Boolean.FALSE);
				}
				break;
			}
			// 연산자를 찾았을 경우
			else {
				op = mark.getValue();
				value = evalString.substring(parseIdx, mark.getBeginIndex()).trim();

				if (value.length() > 0) {
					ex.add(filterSlashMark(value));
					bs.add(Boolean.FALSE);
				}
				ex.add(op);
				bs.add(Boolean.TRUE);

			}
			parseIdx = mark.getEndIndex();
		}

		String[] exs = ex.toArray(new String[0]);
		boolean[] bss = new boolean[bs.size()];

		for (int i = 0; i < bss.length; ++i) {
			bss[i] = bs.get(i).booleanValue();
		}
		infixEx = new InfixExpress(evalString, exs, bss);
		cache.put(evalString, infixEx);
		return infixEx;
	}

	/**
	 * @param fromIndex
	 * @param text
	 * @return fromIndex로 부터 다음 operator를 찾는다.
	 */
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

	private static final char SLASH_MARK = '\\';
	private static final boolean USE_SLASH_MARK = true;

	private static String filterSlashMark(String s) {
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

	public static void main(String[] args) {
		// String s="aaa\\=bbbb=\\\\\\(ccc\\)\\cc\\dddddd\\\\\\eeeeee";
		// ToInfixExpress toInfix=new ToInfixExpress(new String[] {"(",")","="});
		// InfixExpress ie=toInfix.toInfixExpress(s);
		// System.out.println(ie);
		//
		// System.out.println(filterSlashMark(s));
		// System.out.println(s);

		String[][][] searchOps = {
		// operand 0
				{ {} },
				// operand 1
				{ { "(", ")" }, { "!" } },
				// operand 2
				{ {}, {}, { "<>", "==", ">=", "<=", "=", ">", "<" }, { "&&", "||" } } };

		String eval = "a>1 && b>3 && ( ( c> d && d == 3 ) || ( x> 1 && d > 3 ) ) && f==7 && (a < 9 || b < 9 ) && (d > 5 && f > 5)";
		ToInfixExpress toPostfix = new ToInfixExpress(searchOps);
		System.out.println(toPostfix.toInfixExpress(eval));
	}
}
