package net.ion.framework.db.async;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;

import net.ion.framework.db.procedure.IQueryable;

public class FutureResult implements Result {

	final String NOT_COMPLETE_DAY = "";

	private RealResult realresult = null;
	private InvocationTargetException exception = null;
	private boolean ready = false;

	private IQueryable query;

	private Date startDate = null;
	private Date endDate = null;

	public FutureResult(IQueryable query, Date startDate) {
		this.query = query;
		this.startDate = startDate;
	}

	public synchronized ExecMessage getResultMessage() throws InvocationTargetException {
		if (!ready) { // balk
			return ExecMessage.PROGRESS_MESSAGE;
		}
		if (exception != null) {
			return ExecMessage.FAIL_MESSAGE;
		}
		return realresult.getResultMessage();
	}

	public IQueryable getQuery() {
		return query;
	}

	public int getRowCount() {
		return -1; // not complete..
	}

	public Date getStartDate() {
		return startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public Exception getException() {
		return this.exception;
	}

	synchronized void setRealResult(RealResult realresult) {
		if (ready) {
			return; // balk
		}
		this.realresult = realresult;
		this.endDate = realresult.getEndDate();
		this.ready = true;
	}

	synchronized void setException(Throwable throwable) {
		if (ready) {
			return; // balk
		}
		this.exception = new InvocationTargetException(throwable, throwable.getMessage());
		this.endDate = new Date();
		this.ready = true;
	}

}
