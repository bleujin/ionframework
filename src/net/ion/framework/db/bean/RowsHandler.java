package net.ion.framework.db.bean;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

import net.ion.framework.db.Rows;
import net.ion.framework.db.RowsUtils;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: I-ON Communications
 * </p>
 * 
 * @author bleujin
 * @version 1.0
 */

public class RowsHandler {

	private static final int PROPERTY_NOT_FOUND = -1;
	private static final Map<?, ?> primitiveDefaults = new HashMap<Object, Object>();

	public Object saveFormBean(Rows rows, Object formBean) throws SQLException {
		PropertyDescriptor[] props = this.propertyDescriptors(formBean.getClass());
		ResultSetMetaData rsmd = rows.getMetaData();

		int[] columnToProperty = this.mapColumnsToProperties(rsmd, props);
		int cols = rsmd.getColumnCount();
		return this.saveBean(rows, formBean, props, columnToProperty, cols, rsmd);
	}

	private Object saveBean(Rows rows, Object bean, PropertyDescriptor[] props, int[] columnToProperty, int cols, ResultSetMetaData meta) throws SQLException {
		if (rows.first()) {
			for (int i = 1; i <= cols; i++) {
				if (columnToProperty[i] == PROPERTY_NOT_FOUND) {
					continue;
				}

				Object value = getValue(rows, i, meta);

				PropertyDescriptor prop = props[columnToProperty[i]];
				Class<?> propType = prop.getPropertyType();

				if (propType != null && value == null && propType.isPrimitive()) {
					value = primitiveDefaults.get(propType);
				}

				if (value != null) {
					this.callSetter(bean, prop, value);
				}
			}
		} // no data, no action

		return bean;
	}

	private Object getValue(Rows rs, int i, ResultSetMetaData meta) throws SQLException {
		Object value;

		if (meta.getColumnType(i) == Types.CLOB && rs.getClob(i) != null) {
			value = RowsUtils.clobToString(rs.getClob(i));
		} else if (meta.getColumnType(i) == Types.BLOB && rs.getBlob(i) != null) {
			value = RowsUtils.blobToFileName(rs.getBlob(i));
		} else {
			value = rs.getObject(i);
		}
		return value;
	}

	private int[] mapColumnsToProperties(ResultSetMetaData rsmd, PropertyDescriptor[] props) throws SQLException {

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

	// private Object newInstance(Class<?> c) throws SQLException {
	// try {
	// return c.newInstance();
	//
	// } catch(InstantiationException e) {
	// throw new SQLException(
	// "Cannot create " + c.getName() + ": " + e.getMessage());
	//
	// } catch(IllegalAccessException e) {
	// throw new SQLException(
	// "Cannot create " + c.getName() + ": " + e.getMessage());
	// }
	// }

	private void callSetter(Object target, PropertyDescriptor prop, Object value) throws SQLException {

		Method setter = prop.getWriteMethod();

		if (setter == null) {
			return;
		}

		Class<?>[] params = setter.getParameterTypes();
		try {
			// oracle number type is bigDecimal
			if (BigDecimal.class.isInstance(value)) {
				value = (Object) (new Integer(((BigDecimal) value).intValue()));
				params[0] = Integer.class;
			}

			// because of oracle not supported boolean, but this behavior is not general
			if ("boolean".equals(params[0].toString()) && String.class.isInstance(value)) {
				if (value == null)
					value = Boolean.FALSE;
				else
					value = ("T".equalsIgnoreCase(value.toString()) || "true".equalsIgnoreCase(value.toString())) ? Boolean.TRUE : Boolean.FALSE;
			}

			// Don't call setter if the value object isn't the right type
			if (this.isCompatibleType(value, params[0])) {
				setter.invoke(target, new Object[] { value });
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

}
