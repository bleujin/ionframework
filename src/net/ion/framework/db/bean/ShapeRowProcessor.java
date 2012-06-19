package net.ion.framework.db.bean;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * @author bleujin
 * @version 1.0 bridge pattern
 */

public interface ShapeRowProcessor {
	List<?> toShapeList(ResultSet rs, Class<?> parent, Class<?> child) throws SQLException;
}
