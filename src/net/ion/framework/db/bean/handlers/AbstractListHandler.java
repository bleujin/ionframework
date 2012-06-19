package net.ion.framework.db.bean.handlers;

public class AbstractListHandler extends AbstractXMLHandler {

	private String[] attributeNames;
	private String[] columnNames;

	public AbstractListHandler(String[] attrNames, String[] colNames) {
		super();
		this.attributeNames = attrNames;
		this.columnNames = colNames;
	}

	public int getAttribueSize() {
		return attributeNames.length;
	}

	public String[] getAttributeNames() {
		return this.attributeNames;
	}

	public String[] getColumnNames() {
		return this.columnNames;
	}

	public String getAttributeName(int index) {
		return this.attributeNames[index];
	}

	public String getColumnName(int index) {
		return this.columnNames[index];
	}
}
