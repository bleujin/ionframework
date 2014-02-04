package net.ion.framework.db.bean.handlers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import net.ion.framework.db.bean.BasicRowProcessor;
import net.ion.framework.db.bean.ResultSetHandler;
import net.ion.framework.db.bean.RowProcessor;
import net.ion.framework.util.ListUtil;

public class LimitMapListHandler implements ResultSetHandler<List<Map<String, Object>>> {

	private static final long serialVersionUID = -2980302280744414373L;
	final int skipRow;
	final int limitRow;

	private RowProcessor convert = BasicRowProcessor.instance();

	public LimitMapListHandler() {
		this(0, 10);
	}

	public LimitMapListHandler(int skipRow, int limitRow) {
		super();
		this.skipRow = skipRow;
		this.limitRow = limitRow;
	}

	public List<Map<String, Object>> handle(ResultSet rs) throws SQLException {

		List<Map<String, Object>> results = ListUtil.newList() ;

		// skip
		for (int i = 0; i < this.skipRow; i++) {
			rs.next();
		}

		for (int i = 0; rs.next() && (this.limitRow == 0 || i < this.limitRow); i++) {
			results.add(this.convert.toMap(rs));
		}

		return results;
	}

}
