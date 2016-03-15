package net.ion.framework.promise.impl;

import java.io.File;
import java.util.List;
import java.util.concurrent.Callable;

import net.ion.framework.promise.AlwaysCallback;
import net.ion.framework.promise.Deferred;
import net.ion.framework.promise.DoneCallback;
import net.ion.framework.promise.FailCallback;
import net.ion.framework.promise.ProgressCallback;
import net.ion.framework.promise.Promise;
import net.ion.framework.promise.Promise.State;
import net.ion.framework.util.Debug;
import junit.framework.TestCase;

public class TestPromise extends TestCase {

	public void testCreate() throws Exception {
		Deferred deferred = new DeferredObject() ;
		
		Promise promise = deferred.promise() ;
		promise.done(new DoneCallback() {
			public void onDone(Object result) {
				Debug.line(result);
			}
		}).fail(new FailCallback() {
			public void onFail(Object result) {
				Debug.line(result);
			}
		}).progress(new ProgressCallback() {
			public void onProgress(Object progress) {
				Debug.line(progress);
			}
		}).always(new AlwaysCallback() {
			public void onAlways(State state, Object resolved, Object rejected) {
				Debug.line(state, resolved, rejected);
			}
		}) ;
		
//		deferred.resolve("done") ;
//		deferred.reject("oops") ;
		deferred.notify("100%") ;
	}
	
	public void testDefault() throws Exception {
		Promise<File, Throwable, Void> promise = new DefaultDeferredManager().when(new Callable<File>(){
			public File call() throws Exception {
				// TODO Auto-generated method stub
				return null;
			}
		}).fail(new FailCallback<Throwable>(){
			public void onFail(Throwable result) {
			}
		}).always(new AlwaysCallback<File, Throwable>() {
			public void onAlways(State state, File resolved, Throwable rejected) {
			}
		}) ;
		
		
	}
}
