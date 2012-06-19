package net.ion.framework.db.bean.handlers;

import java.sql.ResultSet;
import java.sql.SQLException;

import net.ion.framework.db.Page;
import net.ion.framework.db.RowsImpl;
import net.ion.framework.db.RowsUtils;
import net.ion.framework.db.bean.ResultSetHandler;
import net.ion.framework.db.procedure.IQueryable;
import net.ion.framework.db.procedure.Queryable;

public class PageRowsHandler implements ResultSetHandler {

	private IQueryable query;
	private Page page;

	public PageRowsHandler(IQueryable query) {
		this.query = query;
		this.page = query.getPage();
	}

	public Object handle(ResultSet rs) throws SQLException {
		RowsImpl result = RowsUtils.create((Queryable) query);
		result.populate(rs, page.getStartLoc(), page.getListNum());

		// 
		int skipSize = (page.getPageNo() - page.getMinPageNoOnScreen()) * page.getListNum();
		int remainMaxSize = (page.getMaxPageNoOnScreen() - page.getPageNo() + 1) * page.getListNum() - result.getRowCount();
		int remainCount = 0;
		while (remainMaxSize-- > 0 && rs.next()) {
			remainCount++;
		}
		int rowCountOfScreen = skipSize + remainCount + Math.min(1, remainCount) + result.getRowCount();

		result.setScreenInfo(ScreenInfoImpl.create(query.getPage(), rowCountOfScreen));
		return result;
	}

}
