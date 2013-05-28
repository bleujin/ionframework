package net.ion.framework.parse.gson;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.ion.framework.parse.gson.internal.$Gson$Preconditions;
import net.ion.framework.util.LinkedCaseInsensitiveMap;
import net.ion.framework.util.MapUtil;

/**
 * A class representing an object type in Json. 
 * An object consists of name-value pairs where names are strings, and values are any other type of {@link JsonElement}. 
 * This allows for a creating a tree of JsonElements. The member elements of this object are maintained in order they were added.
 * 
 * @author Inderjeet Singh
 * @author Joel Leitch
 */
public final class JsonObject extends JsonElement {
	// We are using a linked hash map because it is important to preserve
	// the order in which elements are inserted. This is needed to ensure
	// that the fields of an object are inserted in the order they were
	// defined in the class.
	private final Map<String, JsonElement> members = new LinkedCaseInsensitiveMap<JsonElement>();

	/**
	 * Creates an empty JsonObject.
	 */
	public JsonObject() {
	}

	public final static JsonObject create() {
		return new JsonObject();
	}

	public void add(String property, JsonElement value) {
		if (value == null) {
			value = JsonNull.INSTANCE;
		}
		members.put($Gson$Preconditions.checkNotNull(property), value);
	}

	public JsonElement remove(String property) {
		return members.remove(property);
	}

	public void addProperty(String property, String value) {
		add(property, createJsonElement(value));
	}

	public void addProperty(String property, Number value) {
		add(property, createJsonElement(value));
	}

	public void addProperty(String property, Boolean value) {
		add(property, createJsonElement(value));
	}

	public void addProperty(String property, Character value) {
		add(property, createJsonElement(value));
	}

	private JsonElement createJsonElement(Object value) {
		return value == null ? JsonNull.INSTANCE : new JsonPrimitive(value);
	}

	public Set<Map.Entry<String, JsonElement>> entrySet() {
		return members.entrySet();
	}

	public boolean has(String memberName) {
		return members.containsKey(memberName);
	}

	public JsonElement get(String memberName) {
		if (members.containsKey(memberName)) {
			JsonElement member = members.get(memberName);
			return member == null ? JsonNull.INSTANCE : member;
		}
		return null;
	}

	public JsonPrimitive getAsJsonPrimitive(String memberName) {
		return (JsonPrimitive) members.get(memberName);
	}

	public JsonArray getAsJsonArray(String memberName) {
		return (JsonArray) members.get(memberName);
	}

	public <T> T getAsObject(Class<T> clz) {
		return new Gson().fromJson(this, clz);
	}

	public JsonObject getAsJsonObject(String memberName) {
		return (JsonObject) members.get(memberName);
	}

	@Override
	public boolean equals(Object o) {
		return (o == this) || (o instanceof JsonObject && ((JsonObject) o).members.equals(members));
	}

	@Override
	public int hashCode() {
		return members.hashCode();
	}

	private JsonElement getIfNotExist(String memberName) {
		JsonElement result = get(memberName);
		return result == null ? NotFoundJsonElement.NOT_FOUND : result;
	}

	public String asString(String key) {
		return getIfNotExist(key).getAsString();
	}

	public long asLong(String key) {
		return getIfNotExist(key).getAsLong();
	}

	public JsonObject asJsonObject(String key) {
		return getIfNotExist(key).getAsJsonObject();
	}

	public JsonArray asJsonArray(String key) {
		return getIfNotExist(key).getAsJsonArray();
	}

	public BigDecimal asBigDecimal(String key) {
		return getIfNotExist(key).getAsBigDecimal();
	}

	public byte asByte(String key) {
		return getIfNotExist(key).getAsByte();
	}

	public char asChar(String key) {
		return getIfNotExist(key).getAsCharacter();
	}

	public double asDouble(String key) {
		return getIfNotExist(key).getAsDouble();
	}

	public float asFloat(String key) {
		return getIfNotExist(key).getAsFloat();
	}

	public Number asNumber(String key) {
		return getIfNotExist(key).getAsNumber();
	}

	public short asShort(String key) {
		return getIfNotExist(key).getAsShort();
	}

	public Iterator<JsonElement> asJsonArrayElement(String key) {
		return getIfNotExist(key).getAsJsonArray().iterator();
	}

	public boolean asBoolean(String key) {
		return getIfNotExist(key).getAsBoolean();
	}

	public int asInt(String key) {
		return getIfNotExist(key).getAsInt();
	}

	public int asInt(String key, int dftvalue) {
		JsonElement ele = getIfNotExist(key);
		if (ele == NotFoundJsonElement.NOT_FOUND)
			return dftvalue;
		return ele.getAsInt();
	}

	public <T> T asObject(String key, Class<T> clz) {
		return new Gson().fromJson(get(key), clz);
	}

	public Map<String, Object> toMap() {
		Map<String, Object> result = MapUtil.newCaseInsensitiveMap();

		for (Entry<String, JsonElement> entry : members.entrySet()) {
			Object simpleObject = JsonUtil.toSimpleObject(entry.getValue());
			result.put(entry.getKey(), simpleObject);
		}

		return result;
	}

	public int childSize() {
		return members.size();
	}

	public JsonObject put(String property, Object value) {
		add(property, JsonUtil.toProperElement(value));
		return this;
	}

	public JsonObject accumulate(String property, Object value) {
		JsonElement properElement = JsonUtil.toProperElement(value);
		if (has(property)) {
			JsonElement ele = get(property);
			if (ele.isJsonArray()) {
				ele.getAsJsonArray().add(properElement);
			} else {
				JsonArray newArray = new JsonArray();
				newArray.add(get(property));
				newArray.add(properElement);
				add(property, newArray);
			}
		} else {
			add(property, properElement);
		}
		return this;
	}

	public final static JsonObject fromString(String jsonString) {
		return JsonParser.fromString(jsonString).getAsJsonObject();
	}

	public final static JsonObject fromObject(Object obj) {
		return JsonParser.fromObject(obj).getAsJsonObject();
	}

}
