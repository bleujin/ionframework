package net.ion.framework.rest;

import static net.ion.framework.rest.MyConstant.NAME;
import static net.ion.framework.rest.MyConstant.PROPERTY;
import static net.ion.framework.rest.MyConstant.RESPONSE;

import java.io.Serializable;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;

import net.ion.framework.parse.gson.JsonObject;
import net.ion.framework.parse.gson.JsonParser;
import net.ion.framework.rope.RopeWriter;
import net.ion.framework.util.StringUtil;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.ecs.xml.XML;
public class IResponse implements Serializable{

	private static final long serialVersionUID = -6799673946398003577L;
	private Map<String, ? extends Object> res;
	public final static IResponse EMPTY_RESPONSE = new IResponse(Collections.EMPTY_MAP);

	protected IResponse(Map<String, ? extends Object> res) {
		this.res = res;
	}

	public final static IResponse create(Map<String, ? extends Object> res) {
		return new IResponse(res);
	}

	public JsonObject toJSON() {
		return JsonParser.fromMap(res);
	}

	public Map<String, Object> getAttributes() {
		return Collections.unmodifiableMap(res);
	}

	public XML toXML() {
		XML element = new XML(RESPONSE);
		for (Entry<String, ? extends Object> entry : res.entrySet()) {
			XML property = new XML(PROPERTY);
			property.addAttribute(NAME, entry.getKey());
			property.addElement(StringEscapeUtils.escapeXml(StringUtil.toString(entry.getValue(), "")));
			element.addElement(property);
		}
		return element;
	}

	public CharSequence toHTML() {
		RopeWriter writer = new RopeWriter();
		//toXML().output(writer);
		for (Entry<String, ?> entry : res.entrySet()) {
			writer.append(entry.getKey(), " : ", StringUtil.toString(entry.getValue()), "<br/>\n");
		}
		return writer.getRope();
	}
}
