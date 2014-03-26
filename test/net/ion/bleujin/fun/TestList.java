package net.ion.bleujin.fun;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import net.ion.framework.util.Debug;
import net.ion.framework.util.ListUtil;
import net.ion.framework.util.RandomUtil;
import junit.framework.TestCase;

public class TestList extends TestCase {

	public void testList() throws Exception {
		ExecutorService es = Executors.newFixedThreadPool(5);
		
		final Names names = new Names();
		for (int i = 0; i < 100; i++) {
			names.addName("bleujin" + i);
		}

		Future<Void> djob = es.submit(new Callable<Void>() {
			public Void call() throws Exception {
				for (int i = 0; i < 100; i++) {
					Thread.sleep(40);
					names.removeName("bleujin" + RandomUtil.nextInt(100));
				}
				return null;
			}
		});

		Future<Void> rjob = es.submit(new Callable<Void>() {
			public Void call() throws Exception {
				for (int i = 0; i < 100; i++) {
					int count = 0 ;
					for (String name : names.names()) {
						Thread.sleep(1);
						count++;
					}
					Debug.line(count);
				}
				return null;
			}
		});
		

		rjob.get() ;
		djob.get() ;

	}
}

class Names {
	private List<String> names = new ArrayList<String>();
	

	public Names addName(String name) {
		names.add(name);
		return this;
	}

	public Names removeName(String name) {
		names.remove(name);
		return this;
	}

	public String[] toNameArray() {
		return names.toArray(new String[0]);
	}

	public List<String> names() {
		return new ArrayList(names);
	}

}