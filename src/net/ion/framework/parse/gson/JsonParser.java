/*
 * Copyright (C) 2009 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.ion.framework.parse.gson;

import java.io.EOFException;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.ion.framework.parse.gson.internal.Streams;
import net.ion.framework.parse.gson.reflect.TypeToken;
import net.ion.framework.parse.gson.stream.JsonReader;
import net.ion.framework.parse.gson.stream.JsonToken;
import net.ion.framework.parse.gson.stream.MalformedJsonException;
import net.ion.framework.util.ChainMap;
import net.ion.framework.util.StringUtil;

/**
 * A parser to parse Json into a parse tree of {@link JsonElement}s
 * 
 * @author Inderjeet Singh
 * @author Joel Leitch
 * @since 1.3
 */
public final class JsonParser {

	/**
	 * Parses the specified JSON string into a parse tree
	 * 
	 * @param json
	 *            JSON text
	 * @return a parse tree of {@link JsonElement}s corresponding to the specified JSON
	 * @throws JsonParseException
	 *             if the specified text is not valid JSON
	 * @since 1.3
	 */
	public JsonElement parse(String json) throws JsonSyntaxException {
		return parse(new StringReader(json));
	}

	/**
	 * Parses the specified JSON string into a parse tree
	 * 
	 * @param json
	 *            JSON text
	 * @return a parse tree of {@link JsonElement}s corresponding to the specified JSON
	 * @throws JsonParseException
	 *             if the specified text is not valid JSON
	 * @since 1.3
	 */
	public JsonElement parse(Reader json) throws JsonIOException, JsonSyntaxException {
		try {
			JsonReader jsonReader = new JsonReader(json);
			JsonElement element = parse(jsonReader);
			if (!element.isJsonNull() && jsonReader.peek() != JsonToken.END_DOCUMENT) {
				throw new JsonSyntaxException("Did not consume the entire document.");
			}
			return element;
		} catch (MalformedJsonException e) {
			throw new JsonSyntaxException(e);
		} catch (IOException e) {
			throw new JsonIOException(e);
		} catch (NumberFormatException e) {
			throw new JsonSyntaxException(e);
		}
	}

	/**
	 * Returns the next value from the JSON stream as a parse tree.
	 * 
	 * @throws JsonParseException
	 *             if there is an IOException or if the specified text is not valid JSON
	 * @since 1.6
	 */
	public JsonElement parse(JsonReader json) throws JsonIOException, JsonSyntaxException {
		boolean lenient = json.isLenient();
		json.setLenient(true);
		try {
			return Streams.parse(json);
		} catch (StackOverflowError e) {
			throw new JsonParseException("Failed parsing JSON source: " + json + " to Json", e);
		} catch (OutOfMemoryError e) {
			throw new JsonParseException("Failed parsing JSON source: " + json + " to Json", e);
		} catch (JsonParseException e) {
			if (e.getCause() instanceof EOFException) {
				return JsonNull.INSTANCE;
			}
			throw e;
		} finally {
			json.setLenient(lenient);
		}
	}
	
	public final static JsonObject fromMap(Map<String, ? extends Object> map){
		JsonObject jso = new JsonObject() ;
		for (Entry<String, ? extends Object> entry : map.entrySet()) {
			jso.add(entry.getKey(), fromObject(entry.getValue())) ;
		}
		return jso ;
//		Type mapType = new TypeToken<Map<String, ? extends Object>>() {}.getType();
//		JsonElement jso = new Gson().toJsonTree(map, mapType) ;
//		return jso.getAsJsonObject() ;
	}
	
	public final static JsonArray fromList(List list){
		JsonArray array = new JsonArray() ;
		for (Object object : list) {
			array.add(fromObject(object)) ;
		}
		return array ;
		
		// return new Gson().toJsonTree(list, new TypeToken<List>(){}.getType()).getAsJsonArray() ;
	}

	public final static JsonElement fromString(String json) throws JsonSyntaxException {
		if (StringUtil.isBlank(json)) return new JsonObject() ;
		return new JsonParser().parse(json);
	}

	private static DefaultExclusionStrategy DStrategy = new DefaultExclusionStrategy() ;
	public static JsonElement fromObject(Object src) {
		return fromObject(DStrategy, src) ;
	}

	public static JsonElement fromObject(ExclusionStrategy strategy, Object src) {
		if (src == null) return JsonNull.INSTANCE ;
		if (src instanceof JsonElement) return (JsonElement) src ; 
		if (src instanceof ChainMap) return fromMap(((ChainMap)src).toMap()) ;
		if (src instanceof Map) return fromMap((Map<String, Object>)src) ;
		if (src instanceof Collection) return fromList(new ArrayList((Collection)src)) ;
		if (src instanceof JsonString) return fromString(((JsonString)src).toJsonString()) ;
		//		if (src instanceof String) {
//			if (StringUtil.isBlank((String)src)) return new JsonPrimitive("") ; 
//			return new JsonPrimitive((String)src) ;
//		}
		return new GsonBuilder().addSerializationExclusionStrategy(strategy).create().toJsonTree(src) ;
	}
	
	private static class DefaultExclusionStrategy implements ExclusionStrategy {

		public boolean shouldSkipClass(Class<?> clazz) {
			return false;
		}

		public boolean shouldSkipField(FieldAttributes f) {
			return false;
		}
		
	}
}
