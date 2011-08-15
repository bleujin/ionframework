package net.ion.framework.parse.json;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class JSONParser {

	private static final String EMPTY_STRING = "";

	public Map<String, Object> toMap(JSONObject json) {
		Map<String, Object> result = new HashMap<String, Object>();
		recursive(null, result, json);
		return result;
	}

	private void recursive(String prefix, Map<String, Object> paramter, Object obj) {
		if (obj instanceof JSONObject) {
			Set<Map.Entry<String, Object>> entrySet = ((JSONObject) obj).entrySet();
			for (Entry<String, Object> entry : entrySet) {
				recursive((prefix == null ? EMPTY_STRING : prefix + ".") + entry.getKey(), paramter, entry.getValue());
			}
		} else if (obj instanceof JSONArray) {
			JSONArray array = (JSONArray) obj;
			for (int i = 0, last = array.size(); i < last; i++) {
				recursive(prefix + "[" + i + "]", paramter, array.get(i));
			}
		} else {
			paramter.put(prefix, obj);
		}
	}
}
