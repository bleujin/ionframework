package net.ion.framework.promise.multiple;

import java.util.concurrent.atomic.AtomicInteger;

import net.ion.framework.promise.AlwaysCallback;
import net.ion.framework.promise.DoneCallback;
import net.ion.framework.promise.FailCallback;
import net.ion.framework.promise.ProgressCallback;
import net.ion.framework.promise.Promise;
import net.ion.framework.promise.impl.DeferredObject;

/**
 * This will return a special Promise called {@link MasterDeferredObject}. In short,
 * <ul>
 * <li>{@link Promise#done(DoneCallback)} will be triggered if all promises resolves
 * (i.e., all finished successfully) with {@link MultipleResults}.</li>
 * <li>{@link Promise#fail(FailCallback)} will be
 * triggered if any promises rejects (i.e., if any one failed) with {@link OneReject}.</li>
 * <li>{@link Promise#progress(ProgressCallback)} will be triggered whenever one
 * promise resolves or rejects ({#link {@link MasterProgress}), 
 * or whenever a promise was notified progress ({@link OneProgress}).</li>
 * <li>{@link Promise#always(AlwaysCallback)} will be triggered whenever
 * {@link Promise#done(DoneCallback)} or {@link Promise#fail(FailCallback)}
 * would be triggered</li>
 * </ul>
 * 
 */
@SuppressWarnings("rawtypes")
public class MasterDeferredObject extends
		DeferredObject<MultipleResults, OneReject, MasterProgress>
		implements Promise<MultipleResults, OneReject, MasterProgress> {
	private final int numberOfPromises;
	private final AtomicInteger doneCount = new AtomicInteger();
	private final AtomicInteger failCount = new AtomicInteger();
	private final MultipleResults results;

	@SuppressWarnings("unchecked")
	public MasterDeferredObject(Promise... promises) {
		if (promises == null || promises.length == 0)
			throw new IllegalArgumentException("Promises is null or empty");
		this.numberOfPromises = promises.length;
		results = new MultipleResults(numberOfPromises);

		int count = 0;
		for (final Promise promise : promises) {
			final int index = count++;
			promise.fail(new FailCallback<Object>() {
				public void onFail(Object result) {
					synchronized (MasterDeferredObject.this) {
						if (!MasterDeferredObject.this.isPending())
							return;
						
						final int fail = failCount.incrementAndGet();
						MasterDeferredObject.this.notify(new MasterProgress(
								doneCount.get(),
								fail,
								numberOfPromises));
						
						MasterDeferredObject.this.reject(new OneReject(index, promise, result));
					}
				}
			}).progress(new ProgressCallback() {
				public void onProgress(Object progress) {
					synchronized (MasterDeferredObject.this) {
						if (!MasterDeferredObject.this.isPending())
							return;
	
						MasterDeferredObject.this.notify(new OneProgress(
								doneCount.get(),
								failCount.get(),
								numberOfPromises, index, promise, progress));
					}
				}
			}).done(new DoneCallback() {
				public void onDone(Object result) {
					synchronized (MasterDeferredObject.this) {
						if (!MasterDeferredObject.this.isPending())
							return;
	
						results.set(index, new OneResult(index, promise,
								result));
						int done = doneCount.incrementAndGet();
	
						MasterDeferredObject.this.notify(new MasterProgress(
								done,
								failCount.get(),
								numberOfPromises));
						
						if (done == numberOfPromises) {
							MasterDeferredObject.this.resolve(results);
						}
					}
				}
			});
		}
	}
}
