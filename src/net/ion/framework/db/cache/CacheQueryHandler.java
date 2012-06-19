package net.ion.framework.db.cache;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.UndeclaredThrowableException;
import java.sql.SQLException;

import net.ion.framework.db.manager.CacheDBManager;
import net.ion.framework.db.procedure.IQueryable;

public class CacheQueryHandler implements InvocationHandler {

	private CacheDBManager cdbm;
	private IQueryable query;

	public CacheQueryHandler(CacheDBManager cdbm, IQueryable query) {
		this.cdbm = cdbm;
		this.query = query;
	}

	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

		Object result = null;
		long start = 0, end = 0;
		boolean foundCache = false;

		try {
			start = System.nanoTime();
			if (isEXEC_COMMAND(method)) {
				Object value = cdbm.findCachedResult(makeKey(this.query, method, args));
				foundCache = (value != null);
				result = value;
			}

			if (result == null) {
				result = method.invoke(query, args);
			}

			if (isEXEC_COMMAND(method)) {
				cdbm.putResult(makeKey(this.query, method, args), result);
			}

			if (result == query) {
				return proxy;
			}

			return result;
		} catch (UndeclaredThrowableException ex) {
			throw new SQLException(ex.getCause().getMessage());
		} catch (InvocationTargetException ex) {
			throw ex.getCause();
		} finally {
			if (foundCache) {
				end = System.nanoTime();
				cdbm.getDBController().handleServant(start, end, query, IQueryable.QUERY_COMMAND);
			}
		}

	}

	private IQueryable makeKey(IQueryable query, Method method, Object[] args) {
		return query;
	}

	private boolean isEXEC_COMMAND(Method method) {
		return "execUpdate".equalsIgnoreCase(method.getName()) || "execQuery".equalsIgnoreCase(method.getName())
				|| "execPageQuery".equalsIgnoreCase(method.getName());
	}

	public IQueryable getQuery() {
		return this.query;
	}

}
