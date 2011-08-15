package net.ion.framework.db.bean.shaping;

import net.ion.framework.xml.XmlSerializable;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: I-ON Communications
 * </p>
 * 
 * @author bleujin
 * @version 1.0
 */

public interface IsChild extends XmlSerializable {
	Object findPrimaryKey();
}
