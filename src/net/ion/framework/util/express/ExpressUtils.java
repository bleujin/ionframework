package net.ion.framework.util.express;

import java.util.ArrayList;
import java.util.Stack;

import net.ion.framework.util.StringUtil;

public class ExpressUtils {
	public static String[][][] searchOps = {
	// operand 0
			{ {} },
			// operand 1
			{ { "(", ")" }, { "!" } },
			// operand 2
			{ {}, {}, { "<>", "==", ">=", "<=", "=", ">", "<" }, { "&&", "||" } } };

	public static String[][][] sortOps = {
	// operand 0
			{ {} },
			// operand 1
			{ { "(", ")" } },
			// operand 2
			{ {}, { "=" }, { "&&" } } };

	private static ToInfixExpress ToInfixSearch = new ToInfixExpress(ExpressUtils.searchOps);
	private static ToInfixExpress ToInfixSort = new ToInfixExpress(ExpressUtils.sortOps);

	public static InfixExpress toInfixSearchExpress(String evalString) {
		return ToInfixSearch.toInfixExpress(evalString);
	}

	public static InfixExpress toInfixSortExpress(String evalString) {
		return ToInfixSort.toInfixExpress(evalString);
	}

	public static PostfixExpress toPostfixSearchExpress(String evalString) {
		return ToPostfixEssential.toPostfixExpress(evalString);
	}

	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	private final static String TRUE_EXP = "__TRUE__";
	private final static String[][][] EssentialOps = { { {} }, { { "(", ")" } }, { {}, { "&&", "||" } } };
	private final static ToPostfixExpress ToPostfixEssential = new ToPostfixExpress(EssentialOps, false);

	/**
	 * 많은 조건중 필수 조건들만 추출하는 함수 (상훈이가 만들어줬음.. ^^) &&, || 의 두 연산자만 존재하는 논리식이 있다고 가정하고 || 연산을 TRUE로 하여 2개의 피연산식을 삭제함
	 * 
	 * @note 최종식이 TRUE이어서 리턴할 것이 없을 경우 빈문자열("")을 반환
	 * 
	 * @param eval
	 * @return
	 * @throws UnsupportedOperationException
	 */
	public static InfixExpress filterEssential(String eval) throws UnsupportedOperationException {
		PostfixExpress exp = ToPostfixEssential.toPostfixExpress(eval);
		Stack<String> s = new Stack<String>();
		String[] es = exp.getExpressions();
		int[] esN = exp.getNeedOperandNumber();

		for (int i = 0, len = es.length; i < len; ++i) {
			String e = es[i];
			int n = esN[i];

			if (n == 0) {
				s.push(e);
			} else if (n == 2) {
				String e1 = s.pop();
				String e2 = s.pop();

				String t;
				if (e.equals("||"))
					t = TRUE_EXP;
				// when && operator
				else if (e1.equals(TRUE_EXP))
					if (e2.equals(TRUE_EXP))
						t = TRUE_EXP;
					else
						t = e2;
				else if (e2.equals(TRUE_EXP))
					if (e1.equals(TRUE_EXP))
						t = TRUE_EXP;
					else
						t = e1;
				else
					t = "(" + e2 + " " + e + " " + e1 + ")";
				s.push(t);
			} else {
				// 주의: Operand 2가 아닌 연산자는 지원하지 않음
				throw new UnsupportedOperationException("Only supports the operation which has two operands.");
			}
		}
		String result = s.peek().equals(TRUE_EXP) ? "" : s.pop();
		result = StringUtil.replace(result, "(", "");
		result = StringUtil.replace(result, ")", "");

		String[] arrResult = result.split("&&");
		ArrayList<String> list = new ArrayList<String>();
		for (int i = 0; i < arrResult.length; i++) {
			if (!list.contains(arrResult[i])) {
				list.add(arrResult[i]);
			}
		}

		result = StringUtil.join(list.toArray(new String[0]), "&&");

		return ExpressUtils.toInfixSearchExpress(result);
	}

	// ////////////////////////////////////////////////////////////////////////////////////

}
