package net.ion.framework.db.bean.handlers;

import java.sql.ResultSet;
import java.sql.SQLException;

import net.ion.framework.db.bean.BasicRowProcessor;
import net.ion.framework.db.bean.ResultSetHandler;
import net.ion.framework.db.bean.RowProcessor;

public class BeanHandler<T> implements ResultSetHandler<T> {

	private static final long serialVersionUID = 7181455350898308023L;

	private Class<T> type ;
	private RowProcessor convert;

	public BeanHandler(Class<T> type) {
		this.type = type;
		convert = BasicRowProcessor.instance();
	}

	public BeanHandler(Class<T> type, RowProcessor convert) {
		this.type = type;
		this.convert = convert;
	}

	public T handle(ResultSet rs) throws SQLException {
		return rs.next() ? this.convert.toBean(rs, this.type) : null;
	}

}
