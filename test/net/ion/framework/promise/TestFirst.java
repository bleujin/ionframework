package net.ion.framework.promise;

import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;

import net.ion.framework.util.Debug;
import net.ion.framework.util.RandomUtil;
import junit.framework.Assert;
import net.ion.framework.promise.Promise.State;
import net.ion.framework.promise.impl.*; 

public class TestFirst extends TestBasePromise{

	public void testDoneWait() {
		final ValueHolder<Integer> holder = new ValueHolder<Integer>();
		final AtomicInteger failCount = new AtomicInteger();
		deferredManager.when(successCallable(100, 1000))
				.done(new DoneCallback() {
					public void onDone(Object result) {
						Assert.assertEquals(result, 100);
						holder.set((Integer) result);
					}
				}).fail(new FailCallback() {
					public void onFail(Object result) {
						failCount.incrementAndGet();
					}
				}).always(new AlwaysCallback<Integer, Throwable>() {
					public void onAlways(State state, Integer resolved, Throwable rejected) {
						Debug.line(state, resolved, rejected);
					}
				});

		waitForCompletion();
		holder.assertEquals(100);
		Assert.assertEquals(0, failCount.get());
	}
	
	public void testThen() throws Exception {
		final boolean result = false ;
		deferredManager.when(new Callable<Boolean>(){
			public Boolean call() throws Exception {
				throw new IllegalStateException("false") ;
				// return result;
			}
		}).then(new DoneCallback<Boolean>() {
			public void onDone(Boolean result) {
				if (! result) throw new IllegalStateException("false") ;
			}
		}).done(new DoneCallback<Boolean>() {
			public void onDone(Boolean result) {
				Debug.line(result);
			}
		}).fail(new FailCallback<Throwable>() {
			public void onFail(Throwable result) {
				Debug.line(result.getMessage());
			}
		}) ;
		
		waitForCompletion(); 
	}
	
	public void testDoneRewireToFail() {
		final ValueHolder<Integer> preRewireValue = new ValueHolder<Integer>();
		final ValueHolder<Integer> postRewireValue = new ValueHolder<Integer>();
		final ValueHolder<String> failed = new ValueHolder<String>();
		
		deferredManager.when(new Callable<Integer>() {
			public Integer call() {
				return 10;
			}
		}).then(new DonePipe<Integer, Integer, String, Void>() {
			public Promise<Integer, String, Void> pipeDone(Integer result) {
				preRewireValue.set(result);
				if (result < 100) {
					return new DeferredObject<Integer, String, Void>().reject("less than 100");
				} else {
					return new DeferredObject<Integer, String, Void>().resolve(result);
				}
			}
		}).done(new DoneCallback<Integer>() {
			public void onDone(Integer result) {
				postRewireValue.set(result);
			}
		}).fail(new FailCallback<String>() {
			public void onFail(String result) {
				failed.set(result);
			}
		});
		
		waitForCompletion();
		preRewireValue.assertEquals(10);
		postRewireValue.assertEquals(null);
		failed.assertEquals("less than 100");
	}
	
	
}
