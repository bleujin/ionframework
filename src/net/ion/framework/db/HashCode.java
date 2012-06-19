package net.ion.framework.db;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author bleujin
 * @version 1.0
 */

public class HashCode {

	HashCode self = new HashCode();

	private HashCode() {
	}

	public HashCode getInstance() {
		return self;
	}

	public static int getHashCode(Object obj) {
		if (obj == null)
			return "".hashCode();
		else
			return obj.toString().hashCode();
	}

}
