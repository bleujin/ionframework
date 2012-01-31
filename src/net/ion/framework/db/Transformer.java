package net.ion.framework.db;

import java.lang.reflect.Array;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.sql.RowSet;

import net.ion.framework.db.bean.handlers.BeanListHandler;
import net.ion.framework.parse.gson.JsonParser;
import net.ion.framework.util.CaseInsensitiveHashMap;
import net.ion.framework.util.ListUtil;
import net.ion.framework.util.MapUtil;

public class Transformer {

	public static <T> T[] transformIntoBeans(Rows rs, Class<T> beanClass) throws SQLException {
		// Vector<T> objects = new Vector<T>();
		//
		// String[] names = getColumnsNames(rs);
		//
		// rs.beforeFirst();
		// while (rs.next()) {
		// T object = (T)
		// InstanceCreator.createBeanWithPropertyValues(beanClass, names,
		// RowsUtils.getCurrentTuple(rs).toArray());
		// objects.add(object);
		// }
		// return objects.toArray((T[]) Array.newInstance(beanClass, 0));

		List<T> list = (List<T>) rs.toHandle(new BeanListHandler(beanClass));
		return (T[]) list.toArray((T[]) Array.newInstance(beanClass, 0));
	}

	public static synchronized Map<String, Object> fetchFirstToMap(RowSet rows) {
		try {
			if (!rows.first()) {
				throw RepositoryException.throwIt("No Data Found\n");
			}
			return currentRowToMap(rows);
		} catch (SQLException ex) {
			throw RepositoryException.throwIt(ex);
		}
	}

	public static Map<String, Object> currentRowToMap(RowSet rows) {
		try {
			Map<String, Object> result = new CaseInsensitiveHashMap<Object>();
			ResultSetMetaData rsmd = rows.getMetaData();
			int cols = rsmd.getColumnCount();

			for (int i = 1; i <= cols; i++) {
				// result.put(rsmd.getColumnName(i), getValue(rows, i, rsmd));
				result.put(rsmd.getColumnName(i), getNullObjectValue(rows, i, rsmd));
			}

			return result;
		} catch (SQLException ex) {
			throw RepositoryException.throwIt(ex);
		}
	}

	@SuppressWarnings("unchecked")
	public static Map<String, Object>[] toMap(RowSet rows) {
		try {
			rows.beforeFirst();
			Vector<Map<String, Object>> v = new Vector<Map<String, Object>>();
			ResultSetMetaData rsmd = rows.getMetaData();
			int cols = rsmd.getColumnCount();

			while (rows.next()) {
				Map<String, Object> map = new CaseInsensitiveHashMap<Object>();
				for (int i = 1; i <= cols; i++) {
					// map.put(rsmd.getColumnName(i), getValue(rows, i, rsmd));
					map.put(rsmd.getColumnName(i), getNullObjectValue(rows, i, rsmd));
				}
				v.add(map);
			}

			return v.toArray(new Map[0]);
		} catch (SQLException ex) {
			throw RepositoryException.throwIt(ex);
		}
	}

	public static String toJSONObjectString(RowSet rows) {
		try {
			rows.beforeFirst();
			List<Map> jList = ListUtil.newList() ;
			ResultSetMetaData rsmd = rows.getMetaData();
			int cols = rsmd.getColumnCount();

			while (rows.next()) {
				Map<String, Object> map = MapUtil.newMap() ;
				for (int i = 1; i <= cols; i++) {
					map.put(rsmd.getColumnName(i).toUpperCase(), getNullObjectValue(rows, i, rsmd));
				}
				jList.add(map);
			}
			return JsonParser.fromList(jList).toString();
		} catch (SQLException ex) {
			throw RepositoryException.throwIt(ex);
		}
	}

	public static Row fetchFirstToRow(Rows rows) {
		try {
			if (!rows.first()) {
				throw RepositoryException.throwIt("No Data Found\n");
			}
			return new Row(currentRowToMap(rows), getColumnsNames(rows));
		} catch (SQLException ex) {
			throw RepositoryException.throwIt(ex);
		}
	}

	private static Object getValue(RowSet rows, int i, ResultSetMetaData meta) throws SQLException {
		if (meta.getColumnType(i) == Types.CLOB) {
			return RowsUtils.clobToString(rows.getClob(i));
		} else {
			return rows.getObject(i);
		}
	}

	private static Object getNullObjectValue(RowSet rows, int i, ResultSetMetaData meta) throws SQLException {
		Object value = getValue(rows, i, meta);
		int type = meta.getColumnType(i);
		switch (type) {
		case Types.CHAR:
		case Types.VARCHAR:
		case Types.LONGVARCHAR:
		case Types.CLOB:
			if (value == null)
				return "";
		}
		return value;
	}

	private static String[] getColumnsNames(RowSet rs) throws SQLException {
		ResultSetMetaData meta = rs.getMetaData();
		String[] names = new String[meta.getColumnCount()];

		for (int i = 0; i < names.length; ++i) {
			names[i] = meta.getColumnName(i + 1).toUpperCase();
		}

		return names;
	}
}
