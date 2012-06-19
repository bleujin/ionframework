package net.ion.framework.db.bean.wrappers;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.sql.ResultSet;

import net.ion.framework.db.bean.ProxyFactory;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: I-ON Communications
 * </p>
 * 
 * @author bleujin
 * @version 1.0
 */

public class StringTrimmedResultSet implements InvocationHandler {

	/**
	 * The factory to create proxies with.
	 */
	private static final ProxyFactory factory = ProxyFactory.instance();

	/**
	 * Wraps the <code>ResultSet</code> in an instance of this class. This is equivalent to:
	 * 
	 * <pre>
	 * ProxyFactory.instance().createResultSet(new StringTrimmedResultSet(rs));
	 * </pre>
	 * 
	 * @param rs
	 *            The <code>ResultSet</code> to wrap.
	 */
	public static ResultSet wrap(ResultSet rs) {
		return factory.createResultSet(new StringTrimmedResultSet(rs));
	}

	/**
	 * The wrapped result.
	 */
	private final ResultSet rs;

	/**
	 * Constructs a new instance of <code>StringTrimmedResultSet</code> to wrap the specified <code>ResultSet</code>.
	 */
	public StringTrimmedResultSet(ResultSet rs) {
		super();
		this.rs = rs;
	}

	/**
	 * Intercept calls to the <code>getString()</code> and <code>getObject()</code> methods and trim any Strings before they're returned.
	 * 
	 * @throws Throwable
	 * @see java.lang.reflect.InvocationHandler#invoke(java.lang.Object, java.lang.reflect.Method, java.lang.Object[])
	 */
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

		Object result = method.invoke(this.rs, args);

		if (method.getName().equals("getObject") || method.getName().equals("getString")) {

			if (result instanceof String) {
				result = ((String) result).trim();
			}
		}

		return result;
	}

}
