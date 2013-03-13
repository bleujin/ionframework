package net.ion.framework.db.bean.handlers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.ion.framework.db.RepositoryException;
import net.ion.framework.db.bean.BasicRowProcessor;
import net.ion.framework.db.bean.RowProcessor;
import net.ion.framework.util.ListUtil;

public class MapListHandler extends AbListHandler<Map<String, ? extends Object>> {

	private static final long serialVersionUID = 6758822446587422316L;
	private RowProcessor convert;
	public final static MapListHandler SELF = new MapListHandler() ;

	public MapListHandler() {
		this(BasicRowProcessor.instance());
	}

	public MapListHandler(RowProcessor convert) {
		super();
		this.convert = convert;
	}

	protected Map<String, ? extends Object> handleRow(ResultSet rs) throws SQLException {
		return this.convert.toMap(rs);
	}

	public List<Map<String, ? extends Object>> handle(ResultSet rs) {
		try {
			List<Map<String, ? extends Object>> list = super.handle(rs);
			return list;
		} catch (SQLException ex) {
			throw RepositoryException.throwIt(ex);
		}
	}

}
