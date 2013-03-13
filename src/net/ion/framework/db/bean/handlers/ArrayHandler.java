package net.ion.framework.db.bean.handlers;

import java.sql.ResultSet;
import java.sql.SQLException;

import net.ion.framework.db.bean.BasicRowProcessor;
import net.ion.framework.db.bean.ResultSetHandler;
import net.ion.framework.db.bean.RowProcessor;

public class ArrayHandler implements ResultSetHandler<Object[]> {
	private static final long serialVersionUID = 1330244531886427503L;

	private RowProcessor convert = BasicRowProcessor.instance();

	public ArrayHandler() {
		this(BasicRowProcessor.instance()) ;
	}

	public ArrayHandler(RowProcessor convert) {
		super();
		this.convert = convert;
	}

	public Object[] handle(ResultSet rs) throws SQLException {
		return rs.next() ? this.convert.toArray(rs) : null ;
	}

}
