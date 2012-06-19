package net.ion.framework.db;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

public class Row implements Serializable {
	final Map<String, ?> fields;
	final String[] fieldNames;

	// only created in same package
	protected Row(Map<String, ?> fields, String[] sycnFieldNames) {
		this.fields = fields;
		this.fieldNames = sycnFieldNames;
	}

	public String getString(String fieldName) {
		return fields.get(fieldName) == null ? null : fields.get(fieldName).toString();
	}

	public Date getDate(String fieldName) {
		return (Date) fields.get(fieldName);
	}

	public int getInt(String fieldName) {
		if (getString(fieldName) == null)
			throw RepositoryException.throwIt("Not Number Format");
		return Integer.parseInt(getString(fieldName));
	}

	public Integer getInteger(String fieldName) {
		return (Integer) fields.get(fieldName);
	}

	public String getString(int fieldIndex) {
		return fields.get(getFieldName(fieldIndex)) == null ? null : fields.get(getFieldName(fieldIndex)).toString();
	}

	public Object getObject(int fieldIndex) {
		return fields.get(getFieldName(fieldIndex)) == null ? null : fields.get(getFieldName(fieldIndex)).toString();
	}

	public Object getObject(String name) {
		return fields.get(name);
	}

	public Date getDate(int fieldIndex) {
		return (Date) fields.get(getFieldName(fieldIndex));
	}

	public int getInt(int fieldIndex) {
		return Integer.parseInt(getString(getFieldName(fieldIndex)));
	}

	public Integer getInteger(int fieldIndex) {
		return (Integer) fields.get(getFieldName(fieldIndex));
	}

	public boolean getBoolean(String fieldName) {
		return ("T".equals(fields.get(fieldName)) || "true".equals(fields.get(fieldName))) ? true : false;
	}

	public boolean getBoolean(int fieldIndex) {
		return getBoolean(getFieldName(fieldIndex));
	}

	private String getFieldName(int index) {
		return fieldNames[index - 1]; // column counted from 1
	}

	public Map<String, ?> toMap(){
		return fields ;
	}
	
}
