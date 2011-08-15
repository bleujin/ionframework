package net.ion.framework.db;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: I-ON Communications</p>
 * @author bleujin
 * @version 1.0
 */

// Imports
import java.io.Writer;
import java.sql.SQLException;

import net.ion.framework.db.rowset.XmlWriter;

public abstract interface IXmlWriter extends XmlWriter {
	// Methods
	void writeXML(Rows rowset, Writer writer) throws SQLException;
}
