package net.ion.framework.util;

import java.lang.reflect.Array;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;

public class ArrayUtil extends ArrayUtils{

	public static <T> T[] newSubArray(T[] array, int startIndexInclusive, int endIndexExclusive){
		if(array == null)
            return null;
        if(startIndexInclusive < 0)
            startIndexInclusive = 0;
        if(endIndexExclusive > array.length)
            endIndexExclusive = array.length;
        int newSize = endIndexExclusive - startIndexInclusive;
        Class type = ((Object) (array)).getClass().getComponentType();
        if(newSize <= 0)
        {
            return (T[])Array.newInstance(type, 0);
        } else
        {
            T[] subarray = (T[])Array.newInstance(type, newSize);
            System.arraycopy(((Object) (array)), startIndexInclusive, ((Object) (subarray)), 0, newSize);
            return subarray;
        }
	}
	
	public static <T, S> S[] each(T[] array, Closure.Return<T, S> closure){
		List<S> list = ListUtil.newList() ;
		for (T t : array) {
			list.add(closure.execute(t)) ;
		}
		return (S[]) list.toArray();
	}
	
}
