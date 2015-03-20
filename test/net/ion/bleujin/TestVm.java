package net.ion.bleujin;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import junit.framework.TestCase;
import net.ion.framework.util.MapUtil;

public class TestVm extends TestCase {

	public void testVMMonitor() throws Exception {
		final Map<String, Object> map = MapUtil.<String, Object>newSyncMap() ;
		
		
//		final Cache<String, Object> map = CacheBuilder.newBuilder().maximumSize(50000).build() ;
		
		
		ExecutorService es = Executors.newFixedThreadPool(100) ;
		while(true) {
			es.submit(new Callable<Void>(){
				public Void call() throws Exception {
					map.put("" + new Date().getTime(), new Date()) ;
					return null;
				}
			}) ;
			Thread.sleep(1);
		}
		
		
	}
}
