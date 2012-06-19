package net.ion.framework.db.constant;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class RuntimeService {

	private Map<String, Object> property = Collections.synchronizedMap(new HashMap<String, Object>());

	public void init() {

	}

	public void setProperty(String key, Object value) {
		property.put(key, value);
	}

	public Object getProperty(String key) {
		return property.get(key);
	}

	public ILogger getLogger() {
		Object property = getProperty(RuntimeConstants.RUNTIME_LOG_LOGSYSTEM);
		if (property == null || (!(property instanceof ILogger)))
			return new DefaultLogger();
		return (ILogger) property;
	}
}