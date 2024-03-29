package net.ion.framework.parse.gson;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import net.ion.framework.util.ListUtil;

/**
 * A class representing an array type in Json. An array is a list of {@link JsonElement}s each of which can be of a different type. This is an ordered list, meaning that the order in which elements are added is preserved.
 * 
 * @author Inderjeet Singh
 * @author Joel Leitch
 */
public final class JsonArray extends JsonElement implements Iterable<JsonElement> {
	private final List<JsonElement> elements;

	/**
	 * Creates an empty JsonArray.
	 */
	public JsonArray() {
		elements = new ArrayList<JsonElement>();
	}

	/**
	 * Adds the specified element to self.
	 * 
	 * @param element
	 *            the element that needs to be added to the array.
	 */
	public JsonArray adds(Object... values) {
		for (Object value : values) {
			add(JsonUtil.toProperElement(value)) ;
		}
		return this ;
	}
	
	public JsonArray addCollection(Collection col){
		for(Object value : col){
			add(JsonUtil.toProperElement(value)) ;
		}
		return this ;
	}
	
	public JsonArray add(JsonElement element) {
		if (element == null) {
			element = JsonNull.INSTANCE;
		}
		elements.add(element);
		return this ;
	}

	/**
	 * Adds all the elements of the specified array to self.
	 * 
	 * @param array
	 *            the array whose elements need to be added to the array.
	 */
	public void addAll(JsonArray array) {
		elements.addAll(array.elements);
	}

	/**
	 * Returns the number of elements in the array.
	 * 
	 * @return the number of elements in the array.
	 */
	public int size() {
		return elements.size();
	}

	/**
	 * Returns an iterator to navigate the elemetns of the array. Since the array is an ordered list, the iterator navigates the elements in the order they were inserted.
	 * 
	 * @return an iterator to navigate the elements of the array.
	 */
	public Iterator<JsonElement> iterator() {
		return elements.iterator();
	}

	/**
	 * Returns the ith element of the array.
	 * 
	 * @param i
	 *            the index of the element that is being sought.
	 * @return the element present at the ith index.
	 * @throws IndexOutOfBoundsException
	 *             if i is negative or greater than or equal to the {@link #size()} of the array.
	 */
	public JsonElement get(int i) {
		return elements.get(i);
	}

	/**
	 * convenience method to get this array as a {@link Number} if it contains a single element.
	 * 
	 * @return get this element as a number if it is single element array.
	 * @throws ClassCastException
	 *             if the element in the array is of not a {@link JsonPrimitive} and is not a valid Number.
	 * @throws IllegalStateException
	 *             if the array has more than one element.
	 */
	@Override
	public Number getAsNumber() {
		if (elements.size() == 1) {
			return elements.get(0).getAsNumber();
		}
		throw new IllegalStateException();
	}

	/**
	 * convenience method to get this array as a {@link String} if it contains a single element.
	 * 
	 * @return get this element as a String if it is single element array.
	 * @throws ClassCastException
	 *             if the element in the array is of not a {@link JsonPrimitive} and is not a valid String.
	 * @throws IllegalStateException
	 *             if the array has more than one element.
	 */
	@Override
	public String getAsString() {
		if (elements.size() == 1) {
			return elements.get(0).getAsString();
		}
		throw new IllegalStateException();
	}

	/**
	 * convenience method to get this array as a double if it contains a single element.
	 * 
	 * @return get this element as a double if it is single element array.
	 * @throws ClassCastException
	 *             if the element in the array is of not a {@link JsonPrimitive} and is not a valid double.
	 * @throws IllegalStateException
	 *             if the array has more than one element.
	 */
	@Override
	public double getAsDouble() {
		if (elements.size() == 1) {
			return elements.get(0).getAsDouble();
		}
		throw new IllegalStateException();
	}

	/**
	 * convenience method to get this array as a {@link BigDecimal} if it contains a single element.
	 * 
	 * @return get this element as a {@link BigDecimal} if it is single element array.
	 * @throws ClassCastException
	 *             if the element in the array is of not a {@link JsonPrimitive}.
	 * @throws NumberFormatException
	 *             if the element at index 0 is not a valid {@link BigDecimal}.
	 * @throws IllegalStateException
	 *             if the array has more than one element.
	 * @since 1.2
	 */
	@Override
	public BigDecimal getAsBigDecimal() {
		if (elements.size() == 1) {
			return elements.get(0).getAsBigDecimal();
		}
		throw new IllegalStateException();
	}

	/**
	 * convenience method to get this array as a {@link BigInteger} if it contains a single element.
	 * 
	 * @return get this element as a {@link BigInteger} if it is single element array.
	 * @throws ClassCastException
	 *             if the element in the array is of not a {@link JsonPrimitive}.
	 * @throws NumberFormatException
	 *             if the element at index 0 is not a valid {@link BigInteger}.
	 * @throws IllegalStateException
	 *             if the array has more than one element.
	 * @since 1.2
	 */
	@Override
	public BigInteger getAsBigInteger() {
		if (elements.size() == 1) {
			return elements.get(0).getAsBigInteger();
		}
		throw new IllegalStateException();
	}

	/**
	 * convenience method to get this array as a float if it contains a single element.
	 * 
	 * @return get this element as a float if it is single element array.
	 * @throws ClassCastException
	 *             if the element in the array is of not a {@link JsonPrimitive} and is not a valid float.
	 * @throws IllegalStateException
	 *             if the array has more than one element.
	 */
	@Override
	public float getAsFloat() {
		if (elements.size() == 1) {
			return elements.get(0).getAsFloat();
		}
		throw new IllegalStateException();
	}

	/**
	 * convenience method to get this array as a long if it contains a single element.
	 * 
	 * @return get this element as a long if it is single element array.
	 * @throws ClassCastException
	 *             if the element in the array is of not a {@link JsonPrimitive} and is not a valid long.
	 * @throws IllegalStateException
	 *             if the array has more than one element.
	 */
	@Override
	public long getAsLong() {
		if (elements.size() == 1) {
			return elements.get(0).getAsLong();
		}
		throw new IllegalStateException();
	}

	/**
	 * convenience method to get this array as an integer if it contains a single element.
	 * 
	 * @return get this element as an integer if it is single element array.
	 * @throws ClassCastException
	 *             if the element in the array is of not a {@link JsonPrimitive} and is not a valid integer.
	 * @throws IllegalStateException
	 *             if the array has more than one element.
	 */
	@Override
	public int getAsInt() {
		if (elements.size() == 1) {
			return elements.get(0).getAsInt();
		}
		throw new IllegalStateException();
	}

	@Override
	public byte getAsByte() {
		if (elements.size() == 1) {
			return elements.get(0).getAsByte();
		}
		throw new IllegalStateException();
	}

	@Override
	public char getAsCharacter() {
		if (elements.size() == 1) {
			return elements.get(0).getAsCharacter();
		}
		throw new IllegalStateException();
	}

	/**
	 * convenience method to get this array as a primitive short if it contains a single element.
	 * 
	 * @return get this element as a primitive short if it is single element array.
	 * @throws ClassCastException
	 *             if the element in the array is of not a {@link JsonPrimitive} and is not a valid short.
	 * @throws IllegalStateException
	 *             if the array has more than one element.
	 */
	@Override
	public short getAsShort() {
		if (elements.size() == 1) {
			return elements.get(0).getAsShort();
		}
		throw new IllegalStateException();
	}

	/**
	 * convenience method to get this array as a boolean if it contains a single element.
	 * 
	 * @return get this element as a boolean if it is single element array.
	 * @throws ClassCastException
	 *             if the element in the array is of not a {@link JsonPrimitive} and is not a valid boolean.
	 * @throws IllegalStateException
	 *             if the array has more than one element.
	 */
	@Override
	public boolean getAsBoolean() {
		if (elements.size() == 1) {
			return elements.get(0).getAsBoolean();
		}
		throw new IllegalStateException();
	}

	@Override
	public boolean equals(Object o) {
		return (o == this) || (o instanceof JsonArray && ((JsonArray) o).elements.equals(elements));
	}

	@Override
	public int hashCode() {
		return elements.hashCode();
	}

	public JsonElement[] toArray() {
		return elements.toArray(new JsonElement[0]);
	}

	public String asString(int key) {
		return get(key).getAsString();
	}

	public long asLong(int key) {
		return get(key).getAsLong();
	}

	public JsonObject asJsonObject(int key) {
		return get(key).getAsJsonObject();
	}

	public JsonArray asJsonArray(int key) {
		return get(key).getAsJsonArray();
	}

	public BigDecimal asBigDecimal(int key) {
		return get(key).getAsBigDecimal();
	}

	public byte asByte(int key) {
		return get(key).getAsByte();
	}

	public char asChar(int key) {
		return get(key).getAsCharacter();
	}

	public double asDouble(int key) {
		return get(key).getAsDouble();
	}

	public float asFloat(int key) {
		return get(key).getAsFloat();
	}

	public Number asNumber(int key) {
		return get(key).getAsNumber();
	}

	public short asShort(int key) {
		return get(key).getAsShort();
	}

	public <T> T asObject(int key, Class<T> clz) {
		return new Gson().fromJson(get(key), clz);
	}

	public <T> List<T> asList(Class<T> clz) {
		List<T> result = ListUtil.newList();
		for (JsonElement ele : toArray()) {
			result.add(ele.getAsJsonObject().getAsObject(clz));
		}
		return result;
	}

	public Iterator<JsonElement> asJsonArrayElement(int key) {
		return get(key).getAsJsonArray().iterator();
	}

	public boolean asBoolean(int key) {
		return get(key).getAsBoolean();
	}

	public int asInt(int key) {
		return get(key).getAsInt();
	}

	public Object[] toObjectArray() {
		List result = ListUtil.newList();
		for (JsonElement ele : toArray()) {
			result.add(JsonUtil.toSimpleObject(ele));
		}
		return result.toArray();
	}

}
