package net.ion.framework.db.bean;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.ion.framework.db.RowsUtils;
import net.ion.framework.util.Debug;

public class BeanProcessor {

	protected static final int PROPERTY_NOT_FOUND = -1;
	private static final Map<Class<?>, Object> primitiveDefaults = new HashMap<Class<?>, Object>();

	static {
		primitiveDefaults.put(Integer.TYPE, 0);
		primitiveDefaults.put(Short.TYPE, (Short) ((short) 0));
		primitiveDefaults.put(Byte.TYPE, (Byte) ((byte) 0));
		primitiveDefaults.put(Float.TYPE, (Float) (float) (0));
		primitiveDefaults.put(Double.TYPE, (Double) (double) (0));
		primitiveDefaults.put(Long.TYPE, (Long) (0L));
		primitiveDefaults.put(Boolean.TYPE, Boolean.FALSE);
		primitiveDefaults.put(Character.TYPE, '\u0000');
	}

	public BeanProcessor() {
		super();
	}

	public <T> T toBean(ResultSet rs, Class<T> type) throws SQLException {

		PropertyDescriptor[] props = this.propertyDescriptors(type);

		ResultSetMetaData rsmd = rs.getMetaData();
		int[] columnToProperty = this.mapColumnsToProperties(rsmd, props);

		return this.createBean(rs, type, props, columnToProperty);
	}

	public <T> List<T> toBeanList(ResultSet rs, Class<T> type) throws SQLException {
		List<T> results = new ArrayList<T>();

		if (!rs.next()) {
			return results;
		}

		PropertyDescriptor[] props = this.propertyDescriptors(type);
		ResultSetMetaData rsmd = rs.getMetaData();
		int[] columnToProperty = this.mapColumnsToProperties(rsmd, props);

		do {
			results.add(this.createBean(rs, type, props, columnToProperty));
		} while (rs.next());

		return results;
	}

	private <T> T createBean(ResultSet rs, Class<T> type, PropertyDescriptor[] props, int[] columnToProperty) throws SQLException {

		T bean = this.newInstance(type);

		for (int i = 1; i < columnToProperty.length; i++) {

			if (columnToProperty[i] == PROPERTY_NOT_FOUND) {
				continue;
			}

			PropertyDescriptor prop = props[columnToProperty[i]];
			Class<?> propType = prop.getPropertyType();

			Object value = this.getValue(rs, i, propType);

			if (propType != null && value == null && propType.isPrimitive()) {
				value = primitiveDefaults.get(propType);
			}

			this.callSetter(bean, prop, value);
		}

		return bean;
	}

	private void callSetter(Object target, PropertyDescriptor prop, Object value) throws SQLException {

		Method setter = prop.getWriteMethod();

		if (setter == null) {
			return;
		}

		Class<?>[] params = setter.getParameterTypes();
		try {
			// convert types for some popular ones
			if (value != null) {
				if (value instanceof java.util.Date) {
					if (params[0].getName().equals("java.sql.Date")) {
						value = new java.sql.Date(((java.util.Date) value).getTime());
					} else if (params[0].getName().equals("java.sql.Time")) {
						value = new java.sql.Time(((java.util.Date) value).getTime());
					} else if (params[0].getName().equals("java.sql.Timestamp")) {
						value = new java.sql.Timestamp(((java.util.Date) value).getTime());
					}
				}
			}

			// Don't call setter if the value object isn't the right type
			if (this.isCompatibleType(value, params[0])) {
				setter.setAccessible(true) ;
				setter.invoke(target, new Object[] { value });
			} else {
				throw new SQLException("Cannot set " + prop.getName() + ": incompatible types.");
			}

		} catch (IllegalArgumentException e) {
			throw new SQLException("Cannot set " + prop.getName() + ": " + e.getMessage());

		} catch (IllegalAccessException e) {
			throw new SQLException("Cannot set " + prop.getName() + ": " + e.getMessage());

		} catch (InvocationTargetException e) {
			throw new SQLException("Cannot set " + prop.getName() + ": " + e.getMessage());
		}
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

	protected <T> T newInstance(Class<T> c) throws SQLException {
		try {
			Constructor<T> constructor = c.getDeclaredConstructor();
			constructor.setAccessible(true) ;
			return  constructor.newInstance();
			// return c.newInstance();
		} catch (InstantiationException e) {
			throw new SQLException("Cannot create " + c.getName() + ": " + e.getMessage());
		} catch (IllegalAccessException e) {
			throw new SQLException("Cannot create " + c.getName() + ": " + e.getMessage());
		} catch (IllegalArgumentException e) {
			throw new SQLException("Cannot create " + c.getName() + ": " + e.getMessage());
		} catch (SecurityException e) {
			throw new SQLException("Cannot create " + c.getName() + ": " + e.getMessage());
		} catch (InvocationTargetException e) {
			throw new SQLException("Cannot create " + c.getName() + ": " + e.getMessage());
		} catch (NoSuchMethodException e) {
			throw new SQLException("Cannot create " + c.getName() + ": " + e.getMessage());
		}
	}

	private PropertyDescriptor[] propertyDescriptors(Class<?> c) throws SQLException {
		// Introspector caches BeanInfo classes for better performance
		BeanInfo beanInfo = null;
		try {
			beanInfo = Introspector.getBeanInfo(c);

		} catch (IntrospectionException e) {
			throw new SQLException("Bean introspection failed: " + e.getMessage());
		}

		return beanInfo.getPropertyDescriptors();
	}

	protected int[] mapColumnsToProperties(ResultSetMetaData rsmd, PropertyDescriptor[] props) throws SQLException {

		int cols = rsmd.getColumnCount();
		int columnToProperty[] = new int[cols + 1];
		Arrays.fill(columnToProperty, PROPERTY_NOT_FOUND);

		for (int col = 1; col <= cols; col++) {
			String columnName = rsmd.getColumnLabel(col);
			if (null == columnName || 0 == columnName.length()) {
				columnName = rsmd.getColumnName(col);
			}
			for (int i = 0; i < props.length; i++) {

				if (columnName.equalsIgnoreCase(props[i].getName())) {
					columnToProperty[col] = i;
					break;
				}
			}
		}

		return columnToProperty;
	}
	
	protected Object getValue(ResultSet rs, int column, Class<?> propType) throws SQLException {
		ResultSetMetaData meta = rs.getMetaData() ;
		Object value;

		if (meta.getColumnType(column) == Types.CLOB && rs.getClob(column) != null) {
			value = RowsUtils.clobToString(rs.getClob(column));
		} else if (meta.getColumnType(column) == Types.BLOB && rs.getBlob(column) != null) {
			value = RowsUtils.blobToFileName(rs.getBlob(column));
		} else {
			value = processColumn(rs, column, propType);
		}
		return value;
	}

	private Object processColumn(ResultSet rs, int index, Class<?> propType) throws SQLException {

		if (!propType.isPrimitive() && rs.getObject(index) == null) {
			return null;
		}

		if (!propType.isPrimitive() && rs.getObject(index) == null) {
			return null;
		}

		if (propType.equals(String.class)) {
			Debug.debug(rs.getClass(), rs.getMetaData().getColumnTypeName(index), rs.getString(index)) ;
			return rs.getString(index);

		} else if (propType.equals(Integer.TYPE) || propType.equals(Integer.class)) {
			return (rs.getInt(index));

		} else if (propType.equals(Boolean.TYPE) || propType.equals(Boolean.class)) {
			return (rs.getBoolean(index));

		} else if (propType.equals(Long.TYPE) || propType.equals(Long.class)) {
			return (rs.getLong(index));

		} else if (propType.equals(Double.TYPE) || propType.equals(Double.class)) {
			return (rs.getDouble(index));

		} else if (propType.equals(Float.TYPE) || propType.equals(Float.class)) {
			return (rs.getFloat(index));

		} else if (propType.equals(Short.TYPE) || propType.equals(Short.class)) {
			return (rs.getShort(index));

		} else if (propType.equals(Byte.TYPE) || propType.equals(Byte.class)) {
			return (rs.getByte(index));

		} else if (propType.equals(Timestamp.class)) {
			return rs.getTimestamp(index);

		} else {
			return rs.getObject(index);
		}

	}

}
