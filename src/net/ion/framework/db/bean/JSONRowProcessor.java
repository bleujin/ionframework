package net.ion.framework.db.bean;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Map;

import net.ion.framework.db.RowsUtils;
import net.ion.framework.util.CaseInsensitiveHashMap;

// Referenced classes of package net.ion.framework.db.bean:
//            RowProcessor

public class JSONRowProcessor extends BasicRowProcessor {

	public JSONRowProcessor() {
	}

	protected Object getValue(ResultSet rs, int column, ResultSetMetaData meta) throws SQLException {
		Object value;
		if (meta.getColumnType(column) == 2005 && rs.getClob(column) != null)
			value = RowsUtils.clobToString(rs.getClob(column));
		else if (meta.getColumnType(column) == 2004 && rs.getBlob(column) != null)
			value = RowsUtils.blobToFileName(rs.getBlob(column));
		else if (meta.getColumnType(column) == Types.DATE  && rs.getObject(column) != null)
			value =  new java.util.Date(((java.sql.Date)rs.getObject(column)).getTime()) ;
		else 
			value = rs.getObject(column);
		return value;
	}
	
	public Map toMap(ResultSet rs) throws SQLException {
		Map result = new CaseInsensitiveHashMap();
		ResultSetMetaData rsmd = rs.getMetaData();
		int cols = rsmd.getColumnCount();
		for (int i = 1; i <= cols; i++)
			result.put(rsmd.getColumnName(i), getValue(rs, i, rsmd));

		return result;
	}

}
