package net.ion.framework.db.bean.handlers;

import java.lang.reflect.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import net.ion.framework.db.RepositoryException;
import net.ion.framework.db.bean.BasicRowProcessor;
import net.ion.framework.db.bean.ResultSetHandler;
import net.ion.framework.db.bean.RowProcessor;

public class BeanArrayHandler<T> implements ResultSetHandler<T[]> {

	private static final long serialVersionUID = 5157397243183980181L;
	private Class<T> type = null;
	private RowProcessor convert = BasicRowProcessor.instance();

	public BeanArrayHandler(Class<T> type) {
		this.type = type;
	}

	public BeanArrayHandler(Class<T> type, RowProcessor convert) {
		this.type = type;
		this.convert = convert;
	}

	public T[] handle(ResultSet rs) throws SQLException {
		rs.beforeFirst() ;
		List<T> list = this.convert.toBeanList(rs, type);
		return (T[]) list.toArray((T[]) Array.newInstance(type,0));
	}

	public static <T> T[] handle(ResultSet rs, Class<T> type) {
		try {
			rs.beforeFirst() ;
			List<T> list = BasicRowProcessor.instance().toBeanList(rs, type);
			return (T[]) list.toArray((T[]) Array.newInstance(type,0));
		} catch (SQLException ex) {
			throw RepositoryException.throwIt(ex);
		}
	}

}
