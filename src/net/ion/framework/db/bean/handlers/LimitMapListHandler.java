package net.ion.framework.db.bean.handlers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.ion.framework.db.bean.BasicRowProcessor;
import net.ion.framework.db.bean.ResultSetHandler;
import net.ion.framework.db.bean.RowProcessor;

public class LimitMapListHandler implements ResultSetHandler {

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

	public Object handle(ResultSet rs) throws SQLException {

		List<Map<?, ?>> results = new ArrayList<Map<?, ?>>();

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
