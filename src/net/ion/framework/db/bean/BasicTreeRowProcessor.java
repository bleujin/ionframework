package net.ion.framework.db.bean;

import java.beans.PropertyDescriptor;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BasicTreeRowProcessor extends BasicRowProcessor implements TreeRowProcessor {
	private static final BasicRowProcessor instance = new BasicTreeRowProcessor();

	protected BasicTreeRowProcessor() {
		super();
	}

	public static BasicRowProcessor instance() {
		return instance;
	}

	public Object toTreeBean(ResultSet rs, Class<?> type) throws SQLException {
		List<Object> results = new ArrayList<Object>();

		if (!rs.next()) {
			return results;
		}

		PropertyDescriptor[] props = super.propertyDescriptors(type);
		ResultSetMetaData rsmd = rs.getMetaData();
		int[] columnToProperty = super.mapColumnsToProperties(rsmd, props);
		int cols = rsmd.getColumnCount();

		do {
			results.add(this.createBean(rs, type, props, columnToProperty, cols, rsmd));

		} while (rs.next());

		return results;
	}

	private Object createBean(ResultSet rs, Class<?> type, PropertyDescriptor[] props, int[] columnToProperty, int cols, ResultSetMetaData meta)
			throws SQLException {

		Object bean = super.newInstance(type);

		for (int i = 1; i <= cols; i++) {

			if (columnToProperty[i] == PROPERTY_NOT_FOUND) {
				continue;
			}

			Object value = super.getValue(rs, i, meta);

			PropertyDescriptor prop = props[columnToProperty[i]];
			Class<?> propType = prop.getPropertyType();

			if (propType != null && value == null && propType.isPrimitive()) {
				value = primitiveDefaults.get(propType);
			}

			super.callSetter(bean, prop, value);
		}

		return bean;
	}

}
