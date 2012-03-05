package net.ion.framework.parse.gson;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.ion.framework.parse.gson.internal.LazilyParsedNumber;
import net.ion.framework.util.ArrayUtil;
import net.ion.framework.util.ListUtil;
import net.ion.framework.util.ObjectUtil;
import net.ion.framework.util.StringUtil;

import org.apache.commons.lang.ArrayUtils;

public class JsonUtil {

	public static Object toSimpleObject(JsonElement jsonElement) {
		if (jsonElement.isJsonArray()) {
			JsonElement[] jeles = jsonElement.getAsJsonArray().toArray();
			List<Object> list = ListUtil.newList();
			for (JsonElement jele : jeles) {
				list.add(toSimpleObject(jele));
			}
			return list;
		} else if (jsonElement.isJsonObject()) {
			return jsonElement.getAsJsonObject().toMap();
		} else if (jsonElement.isJsonPrimitive()) {
			if (jsonElement.getAsJsonPrimitive().getValue() instanceof LazilyParsedNumber) {
				return ((LazilyParsedNumber) jsonElement.getAsJsonPrimitive().getValue()).estimate() ;
			} else {
				return jsonElement.getAsJsonPrimitive().getValue();
			}
		} else if (jsonElement.isJsonNull()) {
			return ObjectUtil.NULL;
		} else {
			return ObjectUtil.NULL;
		}
	}

	public static JsonElement toProperElement(Object value) {
		if (value == null) {
			return JsonNull.INSTANCE;
		} else if (value.getClass().isPrimitive()) {
			return new JsonPrimitive(value);
		} else if (value instanceof CharSequence || value instanceof Number || value instanceof Boolean || value instanceof Character) {
			return new JsonPrimitive(value);
		} else if (value instanceof List) {
			return JsonParser.fromList((List) value);
		} else if (value instanceof Map) {
			return JsonParser.fromMap((Map) value);
		} else if (value instanceof JsonElement){
			return (JsonElement) value ;
		} else {
			return JsonParser.fromObject(value);
		}
	}

	public static JsonElement findElement(JsonObject json, String path) {
		if (StringUtil.isBlank(path)) return json;
		if (json == null) return null ;
		
		String[] paths = StringUtil.split(path, "./");

		String firstPath = paths[0];
		if (paths.length == 1) {
			return json.getAsJsonObject().get(firstPath);
		} else {
			return findElement(json.getAsJsonObject().asJsonObject(firstPath), StringUtil.join(ArrayUtils.subarray(paths, 1, paths.length), "."));
		}
	}
	
	public static Object findSimpleObject(JsonObject json, String path){
		JsonElement found = findElement(json, path);
		if (found == null) return null ;
		return toSimpleObject(found) ;
	}

	public static boolean hasElement(JsonObject json, String path){
		return findElement(json, path) != null ;
	}

	public static JsonObject arrangeKey(JsonObject jso) {
		JsonObject result = new JsonObject() ;
		
		for (Entry<String, JsonElement> entry : jso.entrySet()) {
			String[] keys = StringUtil.split(entry.getKey(), "./") ;
			if (keys.length == 1) result.add(keys[0], entry.getValue()) ;
			else {
				JsonObject curr = result ;
				for (String key : ArrayUtil.newSubArray(keys, 0, keys.length-1)) {
					if (! curr.has(key)){
						JsonObject newJson = new JsonObject() ;
						curr.put(key, newJson) ;
					}
					curr = curr.asJsonObject(key) ;
				}
				curr.add(keys[keys.length-1], entry.getValue()) ;
			}
		} 
		return result;
	}
	
}
