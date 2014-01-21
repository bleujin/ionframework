package net.ion.framework.parse.gson.internal;

import java.math.BigInteger;

import net.ion.framework.util.StringUtil;

@SuppressWarnings("serial")
public final class LazilyParsedNumber extends Number {
	private final String value;

	public LazilyParsedNumber(String value) {
		this.value = value;
	}

	@Override
	public int intValue() {
		try {
			return (int) Double.parseDouble(value);
		} catch (NumberFormatException e) {
			return new BigInteger(value).intValue();
		}
	}

	@Override
	public long longValue() {
		try {
			return (long) Double.parseDouble(value);
		} catch (NumberFormatException e) {
			return new BigInteger(value).longValue();
		}
	}

	@Override
	public float floatValue() {
		return Float.parseFloat(value);
	}

	@Override
	public double doubleValue() {
		return Double.parseDouble(value);
	}

	public Object estimate() {
		if (StringUtil.isBlank(StringUtil.substringAfter(value, "."))) {
			return longValue();
		} else {
			return doubleValue();
		}
	}

	@Override
	public String toString() {
		return value;
	}

}