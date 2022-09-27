package net.ion.framework.util;

import java.util.Collection;

import org.apache.commons.collections.CollectionUtils;

public class CollectionUtil extends CollectionUtils{

	public static <T> void each(Collection<T> collection, Closure<T> clos){
		for (T obj : collection) {
			clos.execute(obj) ;
		}
	}
	
	public static <T> Collection<T> add(Collection<T> collection, T newElement){
		collection.add(newElement) ;
		return collection ;
	}
}
