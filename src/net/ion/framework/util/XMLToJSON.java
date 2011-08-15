package net.ion.framework.util;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

public class XMLToJSON {
	public static JSONObject toJSONObject(String xmlString) throws IOException {
		try {
			return XML.toJSONObject(xmlString);
		} catch (JSONException e) {
			throw new IOException(e.getMessage());
		}
	}

	public static String toJSONString(String xmlString) throws IOException {
		try {
			return XML.toJSONObject(xmlString).toString();
		} catch (JSONException e) {
			throw new IOException(e.getMessage());
		}
	}
}
