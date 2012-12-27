package net.ion.framework.db.async;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import net.ion.framework.db.Rows;
import net.ion.framework.db.procedure.IQueryable;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author bleujin
 * @version 1.0
 */

// Future Pattern.virtualData

public interface Data {
	public Rows getRows() throws InvocationTargetException;

	public IQueryable getQuery();

	public Throwable getException();
}


class ConcurrentFutureData implements Data{

	private Future<UnknownFutureData<Rows>> future ;
	private IQueryable query ;
	
	private ConcurrentFutureData(Future<UnknownFutureData<Rows>> future, IQueryable query) {
		this.future = future;
		this.query = query ;
	}

	public static ConcurrentFutureData create(Future<UnknownFutureData<Rows>> future, IQueryable query){
		return new ConcurrentFutureData(future, query) ;
	}
	
	public IQueryable getQuery() {
		return query;
	}

	public Rows getRows() throws InvocationTargetException {
		try {
			return future.get().obj();
		} catch (InterruptedException e) {
			throw new InvocationTargetException(e) ;
		} catch (ExecutionException e) {
			throw new InvocationTargetException(e) ;
		}
	}

	public Throwable getException() {
		try {
			return future.get().ex();
		} catch (InterruptedException e) {
			throw new RuntimeException(e) ;
		} catch (ExecutionException e) {
			throw new RuntimeException(e) ;
		}
	}
	
}