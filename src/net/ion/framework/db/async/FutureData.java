package net.ion.framework.db.async;

import java.lang.reflect.InvocationTargetException;

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

// Future Pattern.Future

public class FutureData implements Data {
	private RealData realdata = null;
	private InvocationTargetException exception = null;
	private boolean ready = false;

	private final IQueryable query;

	public FutureData(IQueryable query) {
		this.query = query;
	}

	public synchronized Rows getRows() throws InvocationTargetException {
		while (!ready) {
			try {
				wait();
			} catch (InterruptedException ex) {
			}
		}
		if (exception != null) {
			throw exception;
		}
		return realdata.getRows();
	}

	public IQueryable getQuery() {
		return query;
	}

	synchronized void setRealData(RealData realdata) {
		if (ready) {
			return; // balk
		}
		this.realdata = realdata;
		this.ready = true;
		notifyAll();
	}

	synchronized void setException(Throwable throwable) {
		if (ready) {
			return; // balk
		}
		this.exception = new InvocationTargetException(throwable);
		this.ready = true;
		notifyAll();
	}

}
