package net.ion.framework.db.bean.handlers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import net.ion.framework.db.Page;
import net.ion.framework.db.bean.ResultSetHandler;
import net.ion.framework.util.Debug;
import net.ion.framework.util.ListUtil;

public class DebugOutHandler implements ResultSetHandler{

	private static final long serialVersionUID = -7848441924592515160L;
	private final Page page ;
	public DebugOutHandler(Page page) {
		this.page = page ;
	}
	
	public static DebugOutHandler create(Page page) {
		return new DebugOutHandler(page);
	}

	public Object handle(ResultSet rs) throws SQLException {
		
		int i = 0 ;
		int colCount = rs.getMetaData().getColumnCount() ;
		while(rs.next()){
			if (page.getEndLoc() <= i) break ;
			if (page.getStartLoc() > i++) continue ;
			List<String> list = ListUtil.newList() ;
			for (int k = 1 ; k <= colCount ; k++) {
				list.add(rs.getString(k)) ;
			}
			Debug.debug(list.toString()) ;
		}
		return true;
	}
	
	

}
