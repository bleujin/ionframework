package net.ion.framework.db.async;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.management.RuntimeErrorException;

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

public interface Result {
	public ExecMessage getResultMessage() throws InvocationTargetException;

	public IQueryable getQuery();

	public int getRowCount();

	public Date getStartDate();

	public Date getEndDate();

	public Throwable getException();
}


class ConcurrentFutureResult implements Result {

	private final IQueryable query;
	private final Date startDate;
	private final Date endDate;
	private final Future<UnknownFutureData<Integer>> future;

	private ConcurrentFutureResult(IQueryable query, Date startDate, Future<UnknownFutureData<Integer>> future) {
		this.query = query;
		this.startDate = startDate;
		this.endDate = new Date();
		this.future = future;
	}

	public static ConcurrentFutureResult create(IQueryable query, Date startDate, Future<UnknownFutureData<Integer>> future){
		return new ConcurrentFutureResult(query, startDate, future) ;
	}
	
	public Date getEndDate() {
		return endDate;
	}

	public Throwable getException() {
		try {
			return future.get().ex() ;
		} catch (InterruptedException e) {
			return new RuntimeException(e) ;
		} catch (ExecutionException e) {
			return new RuntimeException(e) ;
		}
	}

	public IQueryable getQuery() {
		return query;
	}

	public ExecMessage getResultMessage() throws InvocationTargetException {
		return ExecMessage.SUCCESS_MESSAGE;
	}

	public int getRowCount() {
		try {
			return future.get().obj();
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
