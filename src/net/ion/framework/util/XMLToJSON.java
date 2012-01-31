package net.ion.framework.util;

import java.io.IOException;
import java.io.StringReader;

import net.ion.framework.parse.gson.JsonObject;
import net.ion.framework.parse.html.GeneralParser;
import net.ion.framework.parse.html.HTag;
import net.ion.framework.parse.html.TagAttribute;

public class XMLToJSON {
	public static JsonObject toJSONObject(String xmlString) throws IOException {
		HTag root = GeneralParser.parseXML(new StringReader(xmlString));

		JsonObject result = new JsonObject();
		result.add(root.getTagName(), toJson(root, new JsonObject()));
		return result;
	}

	public static String toJSONString(String xmlString) throws IOException {
		return toJSONObject(xmlString).toString();
	}
	
	private static JsonObject toJson(HTag tag, JsonObject jso) throws IOException {
		if (!tag.hasChild() && tag.getAttributes().size() == 0) {
			jso.addProperty(tag.getTagName(), tag.getElementText());
			return jso;
		}
		for (TagAttribute attr : tag.getAttributes()) {
			jso.addProperty(attr.getKey(), attr.getValue());
		}
		for (HTag child : tag.getChildren()) {
			if (!child.hasChild() && child.getAttributes().size() == 0) {
				jso.addProperty(child.getTagName(), child.getElementText());
			} else {
				JsonObject newJSO = new JsonObject();
				jso.add(child.getTagName(), toJson(child, newJSO));
			}
		}
		return jso;
	}
}
