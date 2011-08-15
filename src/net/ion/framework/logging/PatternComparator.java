package net.ion.framework.logging;

import java.text.Collator;
import java.util.Comparator;

/**
 * logger�� ã������ package �̸��� �� �ϴµ� ����
 * 
 * <pre>
 *  *.��Ģ
 *      1. ��Ű�� depth�� ���� ���� ������
 *          z.bc.def.g  -(1)
 *          a.bc.e      -(2)
 *              -> (1)�� z�� �����ؼ� ���������δ� �ڰ� ������ .�� �ܰ谡 (2) ���� �����Ƿ� ������ ����
 * 
 *      2. ��Ű�� depth�� ������ ������ ������ ������.
 *          a.b.c       -(1)
 *          a.b.d       -(2)
 *              -> (1),(2) �� depth�� �����Ƿ� ���� ������ ������.
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
