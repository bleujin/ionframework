package net.ion.framework.db.concept;

import java.sql.SQLException;

public interface TransactionJob<T> {

	public T handle(WriteSession wsession) throws SQLException ;
}
