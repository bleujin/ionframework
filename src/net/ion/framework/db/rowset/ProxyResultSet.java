package net.ion.framework.db.rowset;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class ProxyResultSet implements InvocationHandler {

	public interface AOPHandler {
		public void pre(Object proxy, Method m, Object[] args);
		public void after(Object proxy, Method m, Object[] args, Object result);
	}

	public enum Type {
		PRE, AFTER
	}

	private Object target;
	private AOPHandler handler;

	public ProxyResultSet(Object target, AOPHandler handler) {
		this.target = target;
		this.handler = handler;
	}

	public Object invoke(Object proxy, Method m, Object[] args) throws Throwable {
		handler.pre(proxy, m, args);
		final Object result = m.invoke(target, args);
		handler.after(proxy, m, args, result);
		return result;
	}

}