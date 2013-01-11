package net.ion.framework.db.async;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

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

public interface Result<T> {
	public ExecMessage getResultMessage() throws InvocationTargetException;

	public IQueryable getQuery();

	public T get();

	public Date getStartDate();

	public Date getEndDate();
}


class ConcurrentFutureResult<T> implements Result<T> {

	private final IQueryable query;
	private final Date startDate;
	private final Date endDate;
	private final Future<T> future;

	private ConcurrentFutureResult(IQueryable query, Date startDate, Future<T> future) {
		this.query = query;
		this.startDate = startDate;
		this.endDate = new Date();
		this.future = future;
	}

	public static <T> Result<T> create(IQueryable query, Future<T> future){
		return new ConcurrentFutureResult(query, new Date(), future) ;
	}
	
	public Date getEndDate() {
		return endDate;
	}

	public IQueryable getQuery() {
		return query;
	}

	public ExecMessage getResultMessage() throws InvocationTargetException {
		return ExecMessage.SUCCESS_MESSAGE;
	}

	public T get() {
		try {
			return future.get();
		} catch (InterruptedException e) {
			throw new RuntimeException(e) ;
		} catch (ExecutionException e) {
			throw new RuntimeException(e) ;
		}
	}

	public Date getStartDate() {
		return startDate;
	}
	
}
