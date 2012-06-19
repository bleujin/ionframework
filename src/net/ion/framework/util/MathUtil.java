package net.ion.framework.util;


public class MathUtil {

	public static int log2(int num){
		return new Double(log(num, 2)).intValue() ;
	}
	
	public static double log(double num, int base){
		return Math.log(num) / Math.log(base) ;
	}
}
