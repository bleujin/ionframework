package net.ion.framework.db.bean.handlers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.ion.framework.db.bean.BasicRowProcessor;
import net.ion.framework.db.bean.RowProcessor;
import net.ion.framework.util.MapUtil;
import net.ion.framework.util.ObjectUtil;

public class StringMapListHandler extends AbListHandler<Map<String, String>>{

	private static final long serialVersionUID = 6758822446587422316L;
	private RowProcessor convert ;
	public StringMapListHandler() {
		this(BasicRowProcessor.instance());
	}

	public StringMapListHandler(RowProcessor convert) {
		super();
		this.convert = convert;
	}

	protected Map<String, String> handleRow(ResultSet rs) throws SQLException {
		Map<String, Object> map = this.convert.toMap(rs);
		Map<String, String> result = MapUtil.newCaseInsensitiveMap() ;
		for (Entry<String, Object> entry : map.entrySet()) {
			result.put(entry.getKey(), ObjectUtil.toString(entry.getValue())) ;
		}
		
		return result;
	}


}
