package net.ion.framework.util;

import java.util.concurrent.Callable;

import org.apache.commons.lang.ObjectUtils;

public class ObjectUtil extends ObjectUtils{

	public final static <T> T coalesce(T... objs) {
		for (T object : objs) {
			if (object != null) return object ;
		}
		return null ;
	}
	
	public final static <T> T coalesce(Callable<T> call, T... objs){
		for (T object : objs) {
			if (object != null) return object ;
		}
		try {
			return call.call() ;
		} catch (Exception e) {
			return null ;
		}
	}
	
}
