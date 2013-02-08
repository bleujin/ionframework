package net.ion.framework.util;

public interface Closure<T> {
	
	public interface Return<T,R> {
		public R execute(T obj) ;
	}
	
	public void execute(T obj);
}