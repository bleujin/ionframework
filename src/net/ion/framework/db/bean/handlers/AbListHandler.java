package net.ion.framework.db.bean.handlers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import net.ion.framework.db.bean.ResultSetHandler;

public abstract class AbListHandler<T> implements ResultSetHandler<List<T>> {

	private static final long serialVersionUID = 3494928555797396936L;
	
	public List<T> handle(ResultSet rs) throws SQLException {
        List<T> rows = new ArrayList<T>();
        while (rs.next()) {
            rows.add(this.handleRow(rs));
        }
        return rows;
    }
    
    protected abstract T handleRow(ResultSet rs) throws SQLException;
}
