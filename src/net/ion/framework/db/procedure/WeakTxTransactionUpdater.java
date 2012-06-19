package net.ion.framework.db.procedure;

import java.sql.SQLException;

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
 * Company: I-ON Communications
 * </p>
 * 
 * @author wschoi
 * @version 1.0
 */
public interface WeakTxTransactionUpdater {
	public int execUpdate(Queryable queryable) throws SQLException;
}
