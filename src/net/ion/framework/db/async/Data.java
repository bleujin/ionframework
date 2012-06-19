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

// Future Pattern.virtualData

public interface Data {
	public Rows getRows() throws InvocationTargetException;

	public IQueryable getQuery();
}
