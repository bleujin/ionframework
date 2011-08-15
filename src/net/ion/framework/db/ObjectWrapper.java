package net.ion.framework.db;

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
 * Company:
 * </p>
 * 
 * @author bleujin
 * @version 1.0
 */

public class ObjectWrapper {
	final Object obj;
	ObjectWrapper next = null;

	public ObjectWrapper(Object obj) {
		this.obj = obj;
	}

	public ObjectWrapper setNextObjectWrapper(ObjectWrapper next) {
		this.next = next;
		return next;
	}

	public ObjectWrapper getNextObjectWrapper() {
		return next;
	}

	public boolean hasNextObjectWrapper() {
		return next != null;
	}

	public Object getObject() {
		return obj;
	}

}
