package net.ion.framework.db.bean.handlers;

import java.beans.PropertyDescriptor;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import net.ion.framework.db.bean.BasicRowProcessor;
import net.ion.framework.db.bean.shaping.IsChild;
import net.ion.framework.db.bean.shaping.IsParent;

public class BasicShapingRowProcessor extends BasicRowProcessor {

	private Hashtable<Object, IsParent> hashParent = new Hashtable<Object, IsParent>();

	BasicShapingRowProcessor() {
		super();
	}

	public List<IsParent> toShapeList(ResultSet rs, Class<?> parent, Class<?> child) throws SQLException {
		if (!rs.next()) {
			return new ArrayList<IsParent>();
		}
		final PropertyDescriptor[] parentProps = super.propertyDescriptors(parent);
		final ResultSetMetaData rmeta = rs.getMetaData();
		final int[] parentColumnToProperty = super.mapColumnsToProperties(rmeta, parentProps);
		final int cols = rmeta.getColumnCount();

		final PropertyDescriptor[] childProps = super.propertyDescriptors(child);
		final int[] childColumnToProperty = super.mapColumnsToProperties(rmeta, childProps);

		do {
			IsParent currentParent = this.getParent(rs, parent, parentProps, parentColumnToProperty, cols, rmeta);
			currentParent.addChild(this.getChild(rs, child, childProps, childColumnToProperty, cols, rmeta));
		} while (rs.next());

		Iterator<IsParent> iterator = hashParent.values().iterator();

		List<IsParent> result = new ArrayList<IsParent>();
		while (iterator.hasNext()) {
			result.add((IsParent) iterator.next());
		}

		return result;
	}

	private IsChild getChild(ResultSet rs, Class<?> child, PropertyDescriptor[] props, int[] columnToProperty, int cols, ResultSetMetaData meta)
			throws SQLException {

		IsChild bean = (IsChild) super.newInstance(child);
		bean = (IsChild) invokeSetter(rs, props, columnToProperty, cols, meta, bean);
		return bean;
	}

	private IsParent getParent(ResultSet rs, Class<?> type, PropertyDescriptor[] props, int[] columnToProperty, int cols, ResultSetMetaData meta)
			throws SQLException {

		IsParent bean = (IsParent) super.newInstance(type);
		bean = (IsParent) invokeSetter(rs, props, columnToProperty, cols, meta, bean);

		IsParent result = (IsParent) hashParent.get(bean.findPrimaryKey());
		if (result == null) {
			hashParent.put(bean.findPrimaryKey(), bean);
			result = (IsParent) hashParent.get(bean.findPrimaryKey());
		}
		return result;
	}

	private Object invokeSetter(ResultSet rs, PropertyDescriptor[] props, int[] columnToProperty, int cols, ResultSetMetaData meta, Object bean)
			throws SQLException {
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

			this.callSetter(bean, prop, value);
		}
		return bean;
	}

}
