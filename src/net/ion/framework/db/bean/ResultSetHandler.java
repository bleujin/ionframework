package net.ion.framework.db.bean;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;


public interface ResultSetHandler<T> extends Serializable{
	public T handle(ResultSet rs) throws SQLException;
}
