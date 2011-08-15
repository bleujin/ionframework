package net.ion.framework.db.xa;

import javax.transaction.xa.XAException;

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

public interface Transactionable {
	void init(int branchId) throws XAException;

	void start() throws XAException;

	int perform() throws XAException;

	void end() throws XAException;

	int prepare() throws XAException;

	void commit() throws XAException;

	void rollback() throws XAException;

	void close() throws XAException;
}
