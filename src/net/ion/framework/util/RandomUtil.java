package net.ion.framework.util;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Random;

import org.apache.commons.lang.math.RandomUtils;

public class RandomUtil extends RandomUtils {

	public static final int START_BY_CHAR_TYPE = 1;

	public static final int UPPERCASE_CHAR_TYPE = 2;

	public static final int DOWNCASE_CHAR_TYPE = 3;

	public static final int NUMBER_CHAR_TYPE = 4;

	private static Random random;

	static {
		random = new Random();
	}

	public static int[] getRandomIntArray(int _minValue, int _maxValue, int size) {
		int[] re = new int[size];
		if (_maxValue - _minValue <= size) {
			for (int i = 0; i < _maxValue - _minValue + 1; i++)
				re[i] = _minValue + i;
			return re;
		}

		int iPos = 0;
		while (iPos < size) {
			int ranValue = nextRandomInt(_minValue, _maxValue);
			if (!isInArray(re, ranValue)) {
				re[iPos] = ranValue;
				iPos++;
			}

		}

		Arrays.sort(re);
		return re;
	}

	public static boolean isInArray(int[] ia, int value) {
		for (int i = 0; i < ia.length; i++) {
			if (ia[i] == value)
				return true;
		}
		return false;
	}

	public static int nextRandomInt(int _minValue, int _maxValue) {
		if (_minValue > _maxValue) {
			throw new IllegalArgumentException("Parameter exception:[" + _minValue + " must < [" + _maxValue + "]");
		}
		if (_minValue == _maxValue)
			return _minValue;
		float randomFloat = random.nextFloat();

		int temp = (_minValue - 1) + (int) ((_maxValue - _minValue + 2) * randomFloat);
		if (temp < _minValue || temp > _maxValue) {
			temp = nextRandomInt(_minValue, _maxValue);
		}
		return temp;
	}

	public static int nextRandomInt(int _maxValue) {
		return nextRandomInt(0, _maxValue);
	}

	public static String nextRandomString(int _length) {
		char[] strChar = new char[_length];
		for (int i = 0; i < _length; i++) {
			int select = nextRandomInt(4);
			switch (select) {
			case 0: {
				strChar[i] = nextRandomNumberChar();
				break;
			}
			case 1: {
				strChar[i] = nextRandomNumberChar();
				break;
			}
			case 2: {
				strChar[i] = nextRandomUpperCaseChar();
				break;
			}
			case 3: {
				strChar[i] = nextRandomDownCaseChar();
				break;
			}
			case 4: {
				strChar[i] = nextRandomDownCaseChar();
				break;
			}
			}
		}
		return new String(strChar);
	}

	public static String nextRandomString(int _length, int _type) {
		String temp = null;
		switch (_type) {
		case START_BY_CHAR_TYPE: {
			temp = nextRandomString(_length);
			if (Character.isDigit(temp.charAt(0))) {
				temp = nextRandomString(_length, START_BY_CHAR_TYPE);
			}
			break;
		}
		case UPPERCASE_CHAR_TYPE: {
			char[] strChar = new char[_length];
			for (int i = 0; i < _length; i++) {
				strChar[i] = nextRandomUpperCaseChar();
			}
			temp = new String(strChar);
			break;
		}
		case DOWNCASE_CHAR_TYPE: {
			char[] strChar = new char[_length];
			for (int i = 0; i < _length; i++) {
				strChar[i] = nextRandomDownCaseChar();
			}
			temp = new String(strChar);
			break;
		}
		case NUMBER_CHAR_TYPE: {
			char[] strChar = new char[_length];
			for (int i = 0; i < _length; i++) {
				strChar[i] = nextRandomNumberChar();
			}
			temp = new String(strChar);
			break;
		}
		default: {
			throw new java.lang.IllegalArgumentException("Not support the type[" + _type + "]");
		}
		}
		return temp;
	}

	public static char nextRandomNumberChar() {
		return (char) nextRandomInt(48, 57);
	}

	public static char nextRandomUpperCaseChar() {
		return (char) nextRandomInt(65, 90);
	}

	public static char nextRandomDownCaseChar() {
		return (char) nextRandomInt(97, 122);
	}

	public static void main(String args[]) {
		int re[] = getRandomIntArray(0, 20, 5);
		Arrays.sort(re);
		for (int i = 0; i < re.length; i++)
			System.out.print("," + re[i]);
		for (int i = 0; i < 10; i++) {
			System.out.println(nextRandomString(10, 3));
		}
	}

	public static Calendar nextCalendar(int n) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, -1 * n);
		cal.add(Calendar.DAY_OF_MONTH, nextInt(n));
		return cal;
	}

	public static <T> T random(T... ts){
		return ts[nextRandomInt(ts.length)] ;
	}
}
