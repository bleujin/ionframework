package net.ion.framework.promise.impl;

import net.ion.framework.promise.AlwaysCallback;
import net.ion.framework.promise.Deferred;
import net.ion.framework.promise.DoneCallback;
import net.ion.framework.promise.FailCallback;
import net.ion.framework.promise.ProgressCallback;
import net.ion.framework.promise.Promise;
import net.ion.framework.promise.Promise.State;
import net.ion.framework.util.Debug;
import junit.framework.TestCase;

public class TestDeferred extends TestCase {

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
}
