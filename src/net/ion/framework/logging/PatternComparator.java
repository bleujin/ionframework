package net.ion.framework.logging;

import java.text.Collator;
import java.util.Comparator;

/**
 * logger를 찾기위해 package 이름을 비교 하는데 쓰임
 * 
 * <pre>
 *  *.규칙
 *      1. 패키지 depth가 높을 수록 앞으로
 *          z.bc.def.g  -(1)
 *          a.bc.e      -(2)
 *              -> (1)은 z로 시작해서 사전식으로는 뒤가 맞지만 .의 단계가 (2) 보다 깊으므로 앞으로 간다
 * 
 *      2. 패키지 depth가 같으면 사전식 정렬을 따른다.
 *          a.b.c       -(1)
 *          a.b.d       -(2)
 *              -> (1),(2) 는 depth가 같으므로 사전 정렬을 따른다.
 * 
 *      ex)
 *          xyz.a.b.c.*
 *          xyz.a.b.*
 *          xyz.a.b.c
 *          a.b.c
 *          a.b.z
 *          d.a.a
 *          z.b.c
 *          xyz.a.*
 *          ...
 * </pre>
 * 
 * @author Kim Sanghoon wizest@i-on.net
 * @version 1.0
 */

public class PatternComparator implements Comparator<String> {
	private static Collator c = Collator.getInstance();

	final static int LESS = -1;
	final static int EQUAL = 0;
	final static int MORE = 1;

	public int compare(String s1, String s2) {
		try {
			int cnt1 = countDot(s1);
			int cnt2 = countDot(s2);

			if (cnt1 > cnt2)
				return LESS;
			if (cnt1 < cnt2)
				return MORE;

			return c.compare(s1, s2);
		} catch (Exception e) {
			throw new ClassCastException("String only.");
		}
	}

	public boolean equals(Object that) {
		if (this == that)
			return true;
		else
			return false;
	}

	private int countDot(String s) {
		int length = s.length();
		int cnt = 0;

		for (int i = 0; i < length; ++i) {
			if (s.charAt(i) == '.')
				cnt++;
		}

		return cnt;
	}
}
