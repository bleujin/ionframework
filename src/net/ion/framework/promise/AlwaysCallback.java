package net.ion.framework.promise;

import net.ion.framework.promise.Promise.State;

public interface AlwaysCallback<D, R> {
	public void onAlways(final State state, final D resolved, final R rejected);
}
