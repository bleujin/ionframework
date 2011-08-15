package net.ion.framework.util;

import org.apache.commons.lang.ObjectUtils;

public class ObjectUtil extends ObjectUtils{

	public final static <T> T coalesce(T... objs) {
		for (T object : objs) {
			if (object != null) return object ;
		}
		return null ;
	}
}
