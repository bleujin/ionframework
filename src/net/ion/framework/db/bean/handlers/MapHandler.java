package net.ion.framework.db.bean.handlers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import net.ion.framework.db.bean.BasicRowProcessor;
import net.ion.framework.db.bean.ResultSetHandler;
import net.ion.framework.db.bean.RowProcessor;


public class MapHandler implements ResultSetHandler<Map<String, Object>> {

	private static final long serialVersionUID = -5014656870724722737L;
	private final RowProcessor convert ;

	public MapHandler() {
		this(BasicRowProcessor.instance()) ;
	}

	public MapHandler(RowProcessor convert) {
		super();
		this.convert = convert;
	}

	public Map<String, Object> handle(ResultSet rs) throws SQLException {
		return rs.next() ? this.convert.toMap(rs) : null;
	}

}
