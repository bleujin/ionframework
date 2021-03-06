package net.ion.framework.util;

import java.io.File;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import junit.framework.TestCase;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.IOFileFilter;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

/**
 * <p>Title: TestJava.java</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2007</p>
 * <p>Company: I-ON Communications</p>
 * <p>Date : 2011. 5. 12.</p>
 * @author novision
 * @version 1.0
 */
public class TestJava extends TestCase{

	
	public void testCollection() throws Exception {
		
		List<String> list = new ArrayList<String>() ;
		
		list.add("ad") ;
		list.add("cd") ;
		
		
		Object[] sa = list.toArray() ;
		Debug.debug(sa) ;
		
	}
	
	
	public void testHostAddress() throws Exception {
		Debug.line(InetAddress.getLocalHost().getHostAddress()) ;
	}

	public void testNumber() throws Exception {
		
		int sum = 0 ;
		for (int i = 1; i < 10000 ; i++) {
			String s = String.valueOf(i) ;
			int mcount = StringUtil.countMatches(s, "0");
			if (mcount > 0) Debug.debug(s) ;
			sum += mcount ;
		}
		Debug.line(sum) ;
	}
	
	public void testObjectId() throws Exception {
		Debug.line(new ObjectId()) ;
	}
	
	
	public void testVisitor() throws Exception {
		IOFileFilter date = FileFilterUtils.ageFileFilter(DateUtil.stringToDate("20110920-111111")) ;
		IOFileFilter dir = new IOFileFilter() {
			
			public boolean accept(File arg0, String arg1) {
				return true;
			}
			
			public boolean accept(File arg0) {
				return true;
			}
		};
		
		Iterator i = FileUtils.iterateFiles(new File("C:/temp"), date, dir) ;
		
		while(i.hasNext()){
			Debug.debug(i.next()) ;
		}
	}
	
	public enum Order {
		ASCEND, DESCEND ; 

	}
	
	public void testEnum() throws Exception {
		String order = "Ascend" ;
		
		Order ord = Order.valueOf(order.toUpperCase()) ;
		Debug.debug(ord) ;
		
	}
	
	public void testList() throws Exception {
		List list = ListUtil.newList() ;
		
		list.add("1") ;
		list.add("2") ;
		list.add("1") ;
		
		assertEquals(3, list.size()) ;
		
		list.remove("1") ;
		assertEquals(2, list.size()) ;
		
	}
	
	public void testURL() throws Exception {
		// Object content = new URL("http://localhost:9000/action?aradon.result.method=delete").getContent();
		
		ExecutorService es = Executors.newFixedThreadPool(10) ;
		List<Sleeping> sls = ListUtil.newList() ;
		for (int i : ListUtil.rangeNum(1000)) {
			final Sleeping sl = new Sleeping();
			sls.add(sl);
			es.submit(sl) ;
		}
		
		Thread.sleep(1000 * 10) ;
		for (Sleeping sl : sls) {
			sl.await() ;
		}
	}
	
	
	public void testNullCatch() throws Exception {
		
		String s = null ;
		try {
			s.toUpperCase() ;
			fail() ;
		} catch(Exception expect){
		}
	}
	
	public void testXor() throws Exception {
		assertEquals(true, true ^ false) ;
		assertEquals(true, false ^ true) ;
		assertEquals(false, true ^ true) ;
		assertEquals(false, false ^ false) ;
	}

	public void testParseInt() throws Exception {
		String str = "2000.0" ;
		assertEquals(2000, (int)Double.parseDouble(str)) ;
		assertEquals(2000L, (long)Double.parseDouble(str)) ;
	}
	
	public void testSortedMap() throws Exception {
	    Map<Float,String> mySortedMap = new TreeMap<Float, String>();
	    // Put some values in it
	    mySortedMap.put(1.0f,"One");
	    mySortedMap.put(0.0f,"Zero");
	    mySortedMap.put(3.0f,"Three");

	    // Iterate through it and it'll be in order!
	    for(Map.Entry<Float,String> entry : mySortedMap.entrySet()) {
	        System.out.println(entry.getValue());
	    } 
	}
	
	
	public void testCacheWrite() throws Exception {
		final Cache<String, String> dcache = CacheBuilder.newBuilder().build() ;
		final Cache<String, String> scache = CacheBuilder.newBuilder().expireAfterWrite(2, TimeUnit.SECONDS).build() ;
		
		for (int i = 0; i < 100; i++) {
			Thread.sleep(500);
			
			String result = scache.get("1", new Callable<String>() {
				public String call() throws Exception {
					
					String cvalue = dcache.getIfPresent("1") ;
					
					Thread thread = new Thread() {
						public void run(){
							try {
								Thread.sleep(1000);
							} catch (InterruptedException e) {
							}
							dcache.put("1", System.currentTimeMillis() + "");
						}
					};
					thread.start(); 
					
					if (cvalue == null) {
						thread.join();
						cvalue = dcache.getIfPresent("1") ;
					}
					return cvalue;
				}
			}) ;
			Debug.debug(System.currentTimeMillis(), result);
		}
	}

	
	public void testFileName() throws Exception {
		File file = new File("./resource/a.txt") ;
		String fname = FilenameUtils.separatorsToUnix(file.getCanonicalPath());
		Debug.line(fname.substring(FilenameUtils.getPrefixLength(fname))) ;
	}

}


class Sleeping implements Runnable {

	private CountDownLatch latch = new CountDownLatch(1);

	public void run() {
		try {
			Thread.sleep(1000) ;
			Debug.line() ;
			latch.countDown() ;
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void await() throws InterruptedException{
		latch.await() ;
	}
	
}
