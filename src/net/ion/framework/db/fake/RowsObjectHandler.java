package net.ion.framework.db.fake;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import net.ion.framework.db.Rows;

public class RowsObjectHandler implements InvocationHandler {

	private String methodName;
	private Rows fakeRows;

	public RowsObjectHandler(String methodName, Rows fakeRows) {
		this.methodName = methodName;
		this.fakeRows = fakeRows;
	}

	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		if (method.getName().equalsIgnoreCase(this.methodName)) {
			return fakeRows;
		}
		throw new UnsupportedOperationException(this.methodName + " method");
	}

}
