package net.ion.framework.template.ref;

import java.util.HashMap;
import java.util.Set;

/**
 * 범용 작업 공간
 * 
 * @author Kim Sanghoon wizest@i-on.net
 * @version 1.0
 */

public class GenericContext implements Context {
	private HashMap<String, Object> map;
	private Context parent;

	public GenericContext() {
		this(null);
	}

	public GenericContext(Context parent) {
		map = new HashMap<String, Object>();
		this.parent = parent;
	}

	public synchronized void release() {
		if (map != null) {
			map.clear();
			map = null;
		}
	}

	public Object findAttribute(String name) {
		return getAttribute(name);
	}

	public void setAttribute(String name, Object obj) {
		synchronized (map) {
			map.put(name, obj);
		}
	}

	public Object getAttribute(String name) {
		synchronized (map) {
			return map.get(name);
		}
	}

	public void removeAttribute(String name) {
		synchronized (map) {
			map.remove(name);
		}
	}

	public Context getParent() {
		return parent;
	}
}
