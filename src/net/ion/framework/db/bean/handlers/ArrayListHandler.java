package net.ion.framework.db.bean.handlers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import net.ion.framework.db.bean.BasicRowProcessor;
import net.ion.framework.db.bean.RowProcessor;
import net.ion.framework.util.ListUtil;

public class ArrayListHandler extends AbListHandler<Object[]> {

	private static final long serialVersionUID = 8253301225397608711L;
	private RowProcessor convert ;

	public ArrayListHandler() {
		this(BasicRowProcessor.instance()) ;
	}

	public ArrayListHandler(RowProcessor convert) {
		super();
		this.convert = convert;
	}


	protected Object[] handleRow(ResultSet rs) throws SQLException {
		return convert.toArray(rs);
	}


	public List<Object[]> handleString(ResultSet rs, String[] colNames) throws SQLException {

		List<Object[]> result = ListUtil.newList() ;
		while (rs.next()) {
			result.add(this.convert.toArray(rs, colNames));
		}

		return result;
	}
	
}
