package net.ion.framework.db.bean.handlers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import net.ion.framework.db.RepositoryException;
import net.ion.framework.db.RowsUtils;
import net.ion.framework.db.bean.ResultSetHandler;

public class FirstMapHandler implements ResultSetHandler<Map<String, Object>>{

	private static final long serialVersionUID = -2704812436420290689L;
	public final static FirstMapHandler SELF = new FirstMapHandler() ;
	
	public Map<String, Object> handle(ResultSet rs) {
		try {
			if (!rs.first()) {
				throw RepositoryException.throwIt("No Data Found\n");
			}
			
			return RowsUtils.currentRowToMap(rs);
		} catch (SQLException ex) {
			throw RepositoryException.throwIt(ex);
		}
	}
	
}
