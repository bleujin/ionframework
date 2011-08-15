package net.ion.framework.db.cache;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import net.ion.framework.db.procedure.IQueryable;
import net.ion.framework.util.Debug;

public class InvokeQueryHandler implements InvocationHandler {

	private IQueryable query;

	public InvokeQueryHandler(IQueryable query) {
		this.query = query;
	}

	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

		if ("EXECQUERY".equalsIgnoreCase(method.getName()) || "EXECUPDATE".equalsIgnoreCase(method.getName())) {
//			Debug.debug(proxy);
			return method.invoke(query, args);
		}

		return method.invoke(query, args);
	}

	protected IQueryable getQuery() {
		return this.query;
	}
}
