package net.ion.framework.db.bean.handlers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import net.ion.framework.db.bean.BasicRowProcessor;
import net.ion.framework.db.bean.ResultSetHandler;
import net.ion.framework.parse.gson.JsonArray;
import net.ion.framework.parse.gson.JsonParser;
import net.ion.framework.util.ListUtil;

public class JsonArrayHandler implements ResultSetHandler<JsonArray>{

	public JsonArray handle(ResultSet rs) throws SQLException {
		
        List<Map> rows = ListUtil.newList() ;
        while (rs.next()) {
            rows.add(BasicRowProcessor.instance().toMap(rs));
        }
        return JsonParser.fromList(rows);
	}

}
