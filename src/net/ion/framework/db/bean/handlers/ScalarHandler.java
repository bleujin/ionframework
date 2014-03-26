package net.ion.framework.db.bean.handlers;

import java.sql.ResultSet;
import java.sql.SQLException;

import net.ion.framework.db.bean.ResultSetHandler;

public class ScalarHandler<T> extends ColNameHelper implements ResultSetHandler<T> {

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
	
	public T handle(ResultSet rs) throws SQLException {

		if (rs.next()) {
			if (this.columnName() == null) {
				return (T)rs.getObject(this.columnIndex());
			} else {
				return (T)rs.getObject(this.columnName());
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

