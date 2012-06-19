package net.ion.framework.db.bean.shaping;

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

public interface IsParent extends IsChild {

	void addChild(IsChild obj);

	void removeChild(IsChild obj);

	IsChild[] getChilds();

	int getChildLength();

	IsChild getChild(int index);
}
