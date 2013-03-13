package net.ion.framework.util;

import java.util.concurrent.Callable;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import junit.framework.TestCase;

public class TestGuavaCache extends TestCase {

	public void testSuper() throws Exception {
		Cache<String, CharSequence> cache = CacheBuilder.newBuilder().build();
		
		cache.put("bleujin", "BLEUJIN") ;
		CharSequence result = cache.get("hero", new Callable<String>() {
			public String call() throws Exception {
				return new String("HERO");
			}
		});
		assertEquals("HERO", result) ;
		
		CharSequence find = cache.get("bleujin", new Callable<String>() {
			public String call() throws Exception {
				Debug.line() ;
				return new String("HERO");
			}
		});
		assertEquals("BLEUJIN", find) ;
	}
	
	public void testRecursive() throws Exception {
		final Cache<String, String> cache = CacheBuilder.newBuilder().build();

		String value = cache.get("bleujin", new Callable<String>() {
			public String call() throws Exception {
				cache.put("bleujin", "bleujin".toUpperCase()) ;
				
				return cache.getIfPresent("bleujin");
			}
		}) ;
		
		Debug.line(value) ;
	}
}
