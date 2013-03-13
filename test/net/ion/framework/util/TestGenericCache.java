package net.ion.framework.util;

import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Collections2;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Maps;

import net.ion.framework.exception.ExecutionRuntimeException;
import net.ion.framework.template.ref.GenericContext;

import junit.framework.TestCase;

public class TestGenericCache extends TestCase {

	public void testFirst() throws Exception {
		// GenericCache<String, Object> cache = new GenericCache.Builder<String, Object>(5).build();
		GenericCache<String, Object> cache = new GenericCache.Builder<String, Object>(5).old();

		assertEquals(null, cache.get("bleujin"));
		cache.put("bleujin", "BLEUJIN");
		assertEquals("BLEUJIN", cache.get("bleujin"));

		assertEquals(true, cache.containsKey("bleujin"));
		assertEquals(false, cache.containsKey("hero"));

		Object value = cache.get("hero", new Callable<String>() {
			public String call() throws Exception {
				return "HERO";
			}
		});
		assertEquals("HERO", value);

	}

	public void testThread() throws Exception {
		final GenericCache<String, Object> cache = new GenericCache.Builder<String, Object>(100).build();
		// final GenericCache<String, Object> cache = new GenericCache.Builder<String, Object>(100).old() ;

		int threadCount = 20;
		final CountDownLatch latch = new CountDownLatch(threadCount);
		Runnable runner = new Runnable() {
			public void run() {

				cache.put("123", "123");

				for (int i : ListUtil.rangeNum(100000)) {
					final String str = RandomUtil.nextRandomString(3);
					Object value = cache.get(str, new Callable<Object>() {
						public Object call() throws Exception {
							for (int jj : ListUtil.rangeNum(10)) {
								final String inner = RandomUtil.nextRandomString(3);
								cache.put(inner, inner.toUpperCase());
							}

							Object value = cache.get(str);
							if (value == null) {
								return str.toUpperCase();
							} else
								return value;

							// return cache.get(str, new Callable<String>() {
							// public String call() throws Exception {
							// return str.toUpperCase();
							// }
							// }) ;
						}
					});
					assertEquals(str.toUpperCase(), value);
				}
				latch.countDown();
			}
		};

		for (int i = 0; i < threadCount; i++) {
			new Thread(runner).start();
		}

		latch.await();
	}

	public void testException() throws Exception {

//		final GenericCache<String, Object> cache = new GenericCache.Builder<String, Object>(100).old();
		final GenericCache<String, Object> cache = new GenericCache.Builder<String, Object>(100).build();
		try {
			cache.get("bleujin", new Callable<Object>() {
				public Object call() throws Exception {
					throw new IndexOutOfBoundsException("Hi");
				}
			});
		} catch (ExecutionRuntimeException ex) {
//			ex.printStackTrace();
			assertEquals("Hi", ex.getMessage()) ;
			assertEquals(IndexOutOfBoundsException.class.getName(), ex.getCause().getClass().getName()) ;
		}

	}
	
	
	public void testGuavaMultiMap() throws Exception {
		HashMultimap<String, String> map = HashMultimap.create();
		
		map.put("bleujin", "b1") ;
		map.put("bleujin", "b2") ;
		map.put("hero", null) ;
		map.put("hero", null) ;
		
		Debug.line(map.asMap(), map.containsKey("hero"), map.get("hero"), map.get("hero").toArray(new String[0]).length ) ;
		Debug.line(Collections2.filter(map.get("hero"), Predicates.notNull()).size()) ;
		
	}
	
	public void testGenericContext() throws Exception {
		GenericContext context = new GenericContext();
		String value = context.getAttribute("bleujin", new Callable<String>() {
			public String call() throws Exception {
				return null;
			}
		}) ;
		
		assertEquals(true, value == null) ;
		
	}
	
}
