package net.ion.framework.db.bean;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.ion.framework.db.RowsUtils;
import net.ion.framework.util.CaseInsensitiveHashMap;

public class BasicRowProcessor implements RowProcessor {
	protected static final Map<Object, Object> primitiveDefaults = new HashMap<Object, Object>();

	static {
		primitiveDefaults.put(Integer.TYPE, new Integer(0));
		primitiveDefaults.put(Short.TYPE, new Short((short) 0));
		primitiveDefaults.put(Byte.TYPE, new Byte((byte) 0));
		primitiveDefaults.put(Float.TYPE, new Float(0));
		primitiveDefaults.put(Double.TYPE, new Double(0));
		primitiveDefaults.put(Long.TYPE, new Long(0));
		primitiveDefaults.put(Boolean.TYPE, Boolean.FALSE);
		primitiveDefaults.put(Character.TYPE, new Character('\u0000'));
	}

	protected static final int PROPERTY_NOT_FOUND = -1;

	private static final BeanProcessor defaultConvert = new BeanProcessor();
	private static final BasicRowProcessor instance = new BasicRowProcessor();

	public static BasicRowProcessor instance() {
		return instance;
	}
	private final BeanProcessor convert;

	public BasicRowProcessor() {
		this(defaultConvert) ;
	}

	public BasicRowProcessor(BeanProcessor convert) {
		super();
		this.convert = convert ;
	}

	public Object[] toArray(ResultSet rs) throws SQLException {
		ResultSetMetaData meta = rs.getMetaData();
		int cols = meta.getColumnCount();
		Object[] result = new Object[cols];

		for (int i = 0; i < cols; i++) {
			result[i] = getValue(rs, i + 1, meta);
		}

		return result;
	}

	public Object[] toArray(ResultSet rs, String[] colNames) throws SQLException {
		int cols = colNames.length;
		Object[] result = new Object[cols];
		ResultSetMetaData meta = rs.getMetaData();
		for (int i = 0; i < colNames.length; i++) {
			result[i] = getStringValue(rs, i + 1, meta);
		}

		return result;
	}

	public <T> T toBean(ResultSet rs, Class<T> type) throws SQLException {
		return convert.toBean(rs, type);
	}

	public <T> List<T> toBeanList(ResultSet rs, Class<T> type) throws SQLException {
		return convert.toBeanList(rs, type) ;
	}


	protected Object getValue(ResultSet rs, int column, ResultSetMetaData meta) throws SQLException {
		Object value;

		if (meta.getColumnType(column) == Types.CLOB && rs.getClob(column) != null) {
			value = RowsUtils.clobToString(rs.getClob(column));
		} else if (meta.getColumnType(column) == Types.BLOB && rs.getBlob(column) != null) {
			value = RowsUtils.blobToFileName(rs.getBlob(column));
		} else {
			value = rs.getObject(column);
		}
		
		if (value == null){
			int type = meta.getColumnType(column) ;
			if (type == Types.CHAR || type == Types.VARCHAR || type == Types.LONGVARCHAR || type == Types.CLOB) {
					return "";
			}
		}
		
		return value;
	}

	protected Object getStringValue(ResultSet rs, int i, ResultSetMetaData meta) throws SQLException {
		Object value;

		if (meta.getColumnType(i) == Types.CLOB && rs.getClob(i) != null) {
			value = RowsUtils.clobToString(rs.getClob(i));
		} else if (meta.getColumnType(i) == Types.BLOB && rs.getBlob(i) != null) {
			value = RowsUtils.blobToFileName(rs.getBlob(i));
		} else {
			value = rs.getString(i);
		}
		return value;
	}

	protected int[] mapColumnsToProperties(ResultSetMetaData rsmd, PropertyDescriptor[] props) throws SQLException {

		int cols = rsmd.getColumnCount();
		int columnToProperty[] = new int[cols + 1];

		for (int col = 1; col <= cols; col++) {
			String columnName = rsmd.getColumnName(col);
			for (int i = 0; i < props.length; i++) {

				if (columnName.equalsIgnoreCase(props[i].getName())) {
					columnToProperty[col] = i;
					break;

				} else {
					columnToProperty[col] = PROPERTY_NOT_FOUND;
				}
			}
		}

		return columnToProperty;
	}

	public Map<String, Object> toMap(ResultSet rs) throws SQLException {
		Map<String, Object> result = new CaseInsensitiveHashMap<Object>();
		ResultSetMetaData rsmd = rs.getMetaData();
		int cols = rsmd.getColumnCount();

		for (int i = 1; i <= cols; i++) {
			result.put(rsmd.getColumnName(i), getValue(rs, i, rsmd));
		}

		return result;
	}

	public Map<String, Object> toStringMap(ResultSet rs, String[] attributeNames, String[] columnNames) throws SQLException {
		Map<String, Object> result = new CaseInsensitiveHashMap<Object>();
		ResultSetMetaData meta = rs.getMetaData();

		for (int i = 0; i < meta.getColumnCount(); i++) {
			int column = i + 1;
			// if(! wantToView(meta, column)) continue ;
			for (int k = 0; k < attributeNames.length; k++) {
				if (columnNames[k].equalsIgnoreCase(meta.getColumnName(column))) {
					result.put(meta.getColumnName(column), getStringValue(rs, column, meta));
				}
			}
		}
		return result;
	}

	protected void callSetter(Object target, PropertyDescriptor prop, Object value) throws SQLException {

		Method setter = prop.getWriteMethod();

		if (setter == null) {
			return;
		}

		Class<?>[] params = setter.getParameterTypes();
		try {
			// oracle number type....
			if (BigDecimal.class.isInstance(value)) {
				value = (Object) (new Integer(((BigDecimal) value).intValue()));
				params[0] = Integer.class;
			}

			// Don't call setter if the value object isn't the right type
			if (this.isCompatibleType(value, params[0])) {
				setter.invoke(target, new Object[] { value });
			} else if (params[0].equals(Boolean.TYPE) && compatibleBooleanValue(value)) { // oracle have not boolean type.
				setter.invoke(target, new Object[] { booleanValue(value) });
			}

		} catch (IllegalArgumentException e) {
			throw new SQLException("Cannot set " + prop.getName() + ": " + e.getMessage());

		} catch (IllegalAccessException e) {
			throw new SQLException("Cannot set " + prop.getName() + ": " + e.getMessage());

		} catch (InvocationTargetException e) {
			throw new SQLException("Cannot set " + prop.getName() + ": " + e.getMessage());
		}
	}

	private boolean compatibleBooleanValue(Object value) {
		return "T".equals(value) || "F".equals(value) || "true".equals(value) || "false".equals(value);
	}

	private Boolean booleanValue(Object value) {
		return ("T".equals(value) || "true".equals(value)) ? Boolean.TRUE : Boolean.FALSE;
	}

	private boolean isCompatibleType(Object value, Class<?> type) {

		// Do object check first, then primitives
		if (value == null || type.isInstance(value)) {
			return true;

		} else if (type.equals(Integer.TYPE) && Integer.class.isInstance(value)) {
			return true;

		} else if (type.equals(Long.TYPE) && Long.class.isInstance(value)) {
			return true;

		} else if (type.equals(Double.TYPE) && Double.class.isInstance(value)) {
			return true;

		} else if (type.equals(Float.TYPE) && Float.class.isInstance(value)) {
			return true;

		} else if (type.equals(Short.TYPE) && Short.class.isInstance(value)) {
			return true;

		} else if (type.equals(Byte.TYPE) && Byte.class.isInstance(value)) {
			return true;

		} else if (type.equals(Character.TYPE) && Character.class.isInstance(value)) {
			return true;

		} else if (type.equals(Boolean.TYPE) && Boolean.class.isInstance(value)) {
			return true;

		} else {
			return false;
		}

	}

	protected Object newInstance(Class<?> c) throws SQLException {
		try {
			return c.newInstance();

		} catch (InstantiationException e) {
			throw new SQLException("Cannot create " + c.getName() + ": " + e.getMessage());

		} catch (IllegalAccessException e) {
			throw new SQLException("Cannot create " + c.getName() + ": " + e.getMessage());
		}
	}

	protected PropertyDescriptor[] propertyDescriptors(Class<?> c) throws SQLException {
		// Introspector caches BeanInfo classes for better performance
		BeanInfo beanInfo = null;
		try {
			beanInfo = Introspector.getBeanInfo(c);

		} catch (IntrospectionException e) {
			throw new SQLException("Bean introspection failed: " + e.getMessage());
		}

		return beanInfo.getPropertyDescriptors();
	}

}
