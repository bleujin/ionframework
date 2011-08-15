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

public interface Result {
	public ExecMessage getResultMessage() throws InvocationTargetException;

	public IQueryable getQuery();

	public int getRowCount();

	public Date getStartDate();

	public Date getEndDate();

	public Exception getException();
}
