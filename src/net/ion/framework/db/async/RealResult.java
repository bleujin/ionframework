package net.ion.framework.db.async;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;

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

public class RealResult implements Result {

	private final IQueryable query;
	private final Date startDate;
	private final Date endDate;
	private final int rowcount;

	public RealResult(IQueryable query, Date startDate, int rowcount) {
		this.query = query;
		this.startDate = startDate;
		this.endDate = new Date();
		this.rowcount = rowcount;
	}

	public ExecMessage getResultMessage() throws InvocationTargetException {
		return ExecMessage.SUCCESS_MESSAGE;
	}

	public IQueryable getQuery() {
		return query;
	}

	public int getRowCount() {
		return rowcount;
	}

	public Date getStartDate() {
		return startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public Exception getException() {
		return null; // no exception
	}

}
