package net.ion.framework.promise.impl;

import java.io.File;
import java.io.FileNotFoundException;
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
		
//		deferred.reject("oops") ;
		deferred.notify("100%") ;
		deferred.resolve("done") ;
	}
	
	public void testDefault() throws Exception {
		Promise<File, Throwable, Void> promise = new DefaultDeferredManager().when(new Callable<File>(){
			public File call() throws Exception {
				Debug.line("when");
//				return new File(".");
				throw new FileNotFoundException() ;
			}
		}).fail(new FailCallback<Throwable>(){
			public void onFail(Throwable ex) {
				Debug.line("fail");
				ex.printStackTrace(); 
			}
		}).progress(new ProgressCallback<Void>() {
			public void onProgress(Void progress) {
				Debug.line(progress);
			}
		}).always(new AlwaysCallback<File, Throwable>() {
			public void onAlways(State state, File resolved, Throwable ex) {
				Debug.line(state, resolved, ex);
			}
		}) ;
		
		
		Thread.sleep(100);
	}
}
