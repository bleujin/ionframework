package net.ion.framework.db.bean.handlers;

import java.sql.ResultSet;
import java.sql.SQLException;

import net.ion.framework.db.bean.ResultSetHandler;

public class ScalarHandler extends ColNameHelper implements ResultSetHandler<Object> {

	private static final long serialVersionUID = 6636753643880285420L;

	public ScalarHandler() {
		super();
	}

	public ScalarHandler(int columnIndex) {
		super(columnIndex);
	}
	
	public ScalarHandler(String columnName) {
		super(columnName);
	}

	public Object handle(ResultSet rs) throws SQLException {

		if (rs.next()) {
			if (this.columnName() == null) {
				return rs.getObject(this.columnIndex());
			} else {
				return rs.getObject(this.columnName());
			}

		} else {
			return null;
		}
	}
}

class ColNameHelper {
	private int columnIndex = 1;
	private String columnName = null;
	
	ColNameHelper(){
		this(1) ;
	}
	
	ColNameHelper(String columnName){
		this.columnName = columnName ;
	}
	
	ColNameHelper(int columnIndex){
		this.columnIndex = columnIndex ;
	}
	
	protected String columnName() {
		return columnName;
	}

	protected int columnIndex() {
		return columnIndex;
	}

	
}

