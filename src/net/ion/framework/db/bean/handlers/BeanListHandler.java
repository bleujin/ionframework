package net.ion.framework.db.bean.handlers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import net.ion.framework.db.bean.BasicRowProcessor;
import net.ion.framework.db.bean.ResultSetHandler;
import net.ion.framework.db.bean.RowProcessor;

public class BeanListHandler<T> implements ResultSetHandler<List<T>> {

	private static final long serialVersionUID = 5157397243183980181L;
	private Class<T> type = null;
	private RowProcessor convert = BasicRowProcessor.instance();
	public BeanListHandler(Class<T> type) {
		this.type = type ;
	}
	public BeanListHandler(Class<T> type, RowProcessor convert) {
		this.type = type;
		this.convert = convert;
	}

	public List<T> handle(ResultSet rs) throws SQLException {
		return this.convert.toBeanList(rs, type);
	}

}
