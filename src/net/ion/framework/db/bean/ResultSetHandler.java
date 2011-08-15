package net.ion.framework.db.bean;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: I-ON Communications
 * </p>
 * 
 * @author bleujin
 * @version 1.0
 */

public interface ResultSetHandler extends Serializable{
	public Object handle(ResultSet rs) throws SQLException;
}
