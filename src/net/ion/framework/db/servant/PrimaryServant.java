package net.ion.framework.db.servant;

import java.sql.SQLException;

import net.ion.framework.db.manager.DBManager;
import net.ion.framework.db.procedure.Queryable;

public interface PrimaryServant {
	public Queryable addQuery(final DBManager dbm, final Queryable query) throws SQLException;
}
