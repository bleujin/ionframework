/*
 * Copyright (C) 2011 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.ion.framework.parse.gson.internal.bind;

import java.io.IOException;
import java.math.BigInteger;

import net.ion.framework.parse.gson.JsonSyntaxException;
import net.ion.framework.parse.gson.TypeAdapter;
import net.ion.framework.parse.gson.stream.JsonReader;
import net.ion.framework.parse.gson.stream.JsonToken;
import net.ion.framework.parse.gson.stream.JsonWriter;

/**
 * Adapts a BigInteger type to and from its JSON representation.
 * 
 * @author Joel Leitch
 */
public final class BigIntegerTypeAdapter extends TypeAdapter<BigInteger> {

	@Override
	public BigInteger read(JsonReader in) throws IOException {
		if (in.peek() == JsonToken.NULL) {
			in.nextNull();
			return null;
		}
		try {
			return new BigInteger(in.nextString());
		} catch (NumberFormatException e) {
			throw new JsonSyntaxException(e);
		}
	}

	@Override
	public void write(JsonWriter out, BigInteger value) throws IOException {
		out.value(value);
	}
}
