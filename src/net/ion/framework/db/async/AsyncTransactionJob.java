package net.ion.framework.db.async;

import java.sql.SQLException;

public interface AsyncTransactionJob<T> {

	public T handle(AsyncSession session) throws SQLException ;
}
