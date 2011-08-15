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
import java.util.ArrayList;
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

	/**
	 * Special array index that indicates there is no bean property that matches a column from a ResultSet.
	 */
	protected static final int PROPERTY_NOT_FOUND = -1;

	/**
	 * The Singleton instance of this class.
	 */
	private static final BasicRowProcessor instance = new BasicRowProcessor();

	/**
	 * Returns the Singleton instance of this class.
	 * 
	 * @return The single instance of this class.
	 */
	public static BasicRowProcessor instance() {
		return instance;
	}

	/**
	 * Protected constructor for BasicRowProcessor subclasses only.
	 */
	protected BasicRowProcessor() {
		super();
	}

	/**
	 * Convert a <code>ResultSet</code> row into an <code>Object[]</code>. This implementation copies column values into the array in the same order they're returned from the <code>ResultSet</code>. Array elements will be set to <code>null</code> if
	 * the column was SQL NULL.
	 * 
	 * @see org.apache.commons.dbutils.RowProcessor#toArray(java.sql.ResultSet)
	 */
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

	/**
	 * Convert a <code>ResultSet</code> row into a JavaBean. This implementation uses reflection and <code>BeanInfo</code> classes to match column names to bean property names. Properties are matched to columns based on several factors: <br/>
	 * <ol>
	 * <li>
	 * The class has a writable property with the same name as a column. The name comparison is case insensitive.</li>
	 * 
	 * <li>
	 * The property's set method parameter type matches the column type. If the data types do not match, the setter will not be called.</li>
	 * </ol>
	 * 
	 * <p>
	 * Primitive bean properties are set to their defaults when SQL NULL is returned from the <code>ResultSet</code>. Numeric fields are set to 0 and booleans are set to false. Object bean properties are set to <code>null</code> when SQL NULL is
	 * returned. This is the same behavior as the <code>ResultSet</code> get* methods.
	 * </p>
	 * 
	 * @see org.apache.commons.dbutils.RowProcessor#toBean(java.sql.ResultSet, java.lang.Class)
	 */
	public Object toBean(ResultSet rs, Class<?> type) throws SQLException {

		PropertyDescriptor[] props = this.propertyDescriptors(type);

		ResultSetMetaData rsmd = rs.getMetaData();

		int[] columnToProperty = this.mapColumnsToProperties(rsmd, props);

		int cols = rsmd.getColumnCount();

		return this.createBean(rs, type, props, columnToProperty, cols, rsmd);
	}

	/**
	 * Convert a <code>ResultSet</code> into a <code>List</code> of JavaBeans. This implementation uses reflection and <code>BeanInfo</code> classes to match column names to bean property names. Properties are matched to columns based on several
	 * factors: <br/>
	 * <ol>
	 * <li>
	 * The class has a writable property with the same name as a column. The name comparison is case insensitive.</li>
	 * 
	 * <li>
	 * The property's set method parameter type matches the column type. If the data types do not match, the setter will not be called.</li>
	 * </ol>
	 * 
	 * <p>
	 * Primitive bean properties are set to their defaults when SQL NULL is returned from the <code>ResultSet</code>. Numeric fields are set to 0 and booleans are set to false. Object bean properties are set to <code>null</code> when SQL NULL is
	 * returned. This is the same behavior as the <code>ResultSet</code> get* methods.
	 * </p>
	 * 
	 * @see org.apache.commons.dbutils.RowProcessor#toBeanList(java.sql.ResultSet, java.lang.Class)
	 */
	public List<Object> toBeanList(ResultSet rs, Class<?> type) throws SQLException {
		List<Object> results = new ArrayList<Object>();

		if (!rs.next()) {
			return results;
		}

		PropertyDescriptor[] props = this.propertyDescriptors(type);
		ResultSetMetaData rsmd = rs.getMetaData();
		int[] columnToProperty = this.mapColumnsToProperties(rsmd, props);
		int cols = rsmd.getColumnCount();

		do {
			results.add(this.createBean(rs, type, props, columnToProperty, cols, rsmd));

		} while (rs.next());

		return results;
	}

	/**
	 * Creates a new object and initializes its fields from the ResultSet.
	 * 
	 * @param rs
	 *            The result set
	 * @param type
	 *            The bean type (the return type of the object)
	 * @param props
	 *            The property descriptors
	 * @param columnToProperty
	 *            The column indices in the result set
	 * @param cols
	 *            The number of columns
	 * @return An initialized object.
	 * @throws SQLException
	 *             If a database error occurs
	 */
	private Object createBean(ResultSet rs, Class<?> type, PropertyDescriptor[] props, int[] columnToProperty, int cols, ResultSetMetaData meta)
			throws SQLException {

		Object bean = this.newInstance(type);

		for (int i = 1; i <= cols; i++) {

			if (columnToProperty[i] == PROPERTY_NOT_FOUND) {
				continue;
			}

			Object value = getValue(rs, i, meta);

			PropertyDescriptor prop = props[columnToProperty[i]];
			Class<?> propType = prop.getPropertyType();

			if (propType != null && value == null && propType.isPrimitive()) {
				value = primitiveDefaults.get(propType);
			}

			this.callSetter(bean, prop, value);
		}

		return bean;
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

	/**
	 * The positions in the returned array represent column numbers. The values stored at each position represent the index in the PropertyDescriptor[] for the bean property that matches the column name. If no bean property was found for a column,
	 * the position is set to PROPERTY_NOT_FOUND.
	 * 
	 * @param rsmd
	 *            The result set meta data containing column information
	 * @param props
	 *            The bean property descriptors
	 * @return An int[] with column index to property index mappings. The 0th element is meaningless as column indexing starts at 1.
	 * 
	 * @throws SQLException
	 *             If a database error occurs
	 */
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

	/**
	 * Convert a <code>ResultSet</code> row into a <code>Map</code>. This implementation returns a <code>Map</code> with case insensitive column names as keys. Calls to <code>map.get("COL")</code> and <code>map.get("col")</code> return the same
	 * value.
	 * 
	 * @see org.apache.commons.dbutils.RowProcessor#toMap(java.sql.ResultSet)
	 */
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

	/**
	 * Calls the setter method on the target object for the given property. If no setter method exists for the property, this method does nothing.
	 * 
	 * @param target
	 *            The object to set the property on.
	 * @param prop
	 *            The property to set.
	 * @param value
	 *            The value to pass into the setter.
	 * @throws SQLException
	 *             if an error occurs setting the property.
	 */
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

	/**
	 * ResultSet.getObject() returns an Integer object for an INT column. The setter method for the property might take an Integer or a primitive int. This method returns true if the value can be successfully passed into the setter method. Remember,
	 * Method.invoke() handles the unwrapping of Integer into an int.
	 * 
	 * @param value
	 *            The value to be passed into the setter method.
	 * @param type
	 *            The setter's parameter type.
	 * @return boolean True if the value is compatible.
	 */
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

	/**
	 * Returns a new instance of the given Class.
	 * 
	 * @param c
	 *            The Class to create an object from.
	 * @return A newly created object of the Class.
	 * @throws SQLException
	 *             if creation failed.
	 */
	protected Object newInstance(Class<?> c) throws SQLException {
		try {
			return c.newInstance();

		} catch (InstantiationException e) {
			throw new SQLException("Cannot create " + c.getName() + ": " + e.getMessage());

		} catch (IllegalAccessException e) {
			throw new SQLException("Cannot create " + c.getName() + ": " + e.getMessage());
		}
	}

	/**
	 * Returns a PropertyDescriptor[] for the given Class.
	 * 
	 * @param c
	 *            The Class to retrieve PropertyDescriptors for.
	 * @return A PropertyDescriptor[] describing the Class.
	 * @throws SQLException
	 *             if introspection failed.
	 */
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
