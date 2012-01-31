package net.ion.framework.rest;

import static net.ion.framework.rest.MyConstant.NAME;
import static net.ion.framework.rest.MyConstant.PROPERTY;
import static net.ion.framework.rest.MyConstant.REQUEST;

import java.io.Serializable;
import java.io.StringWriter;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;

import net.ion.framework.parse.gson.JsonObject;
import net.ion.framework.parse.gson.JsonParser;
import net.ion.framework.util.StringUtil;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.ecs.xml.XML;

public class IRequest implements Serializable {

	private static final long serialVersionUID = -6158715799014756853L;
	private Map<String, ? extends Object> req;

	public final static IRequest EMPTY_REQUEST = new IRequest(Collections.EMPTY_MAP);

	protected IRequest(Map<String, ? extends Object> req) {
		this.req = req;
	}

	public final static IRequest create(Map<String, ? extends Object> req) {
		return new IRequest(req);
	}

	public JsonObject toJSON() {
		return JsonParser.fromMap(req);
	}

	public Map<String, Object> getAttributes() {
		return Collections.unmodifiableMap(req);
	}

	public XML toXML() {
		XML element = new XML(REQUEST);
		for (Entry<String, ? extends Object> entry : req.entrySet()) {
			XML property = new XML(PROPERTY);
			property.addAttribute(NAME, entry.getKey());
			property.addElement(StringEscapeUtils.escapeXml(StringUtil.toString(entry.getValue(), "")));
			element.addElement(property);
		}
		return element;
	}

	public CharSequence toHTML() {
		StringWriter writer = new StringWriter();
		//toXML().output(writer);
		for (Entry<String, ?> entry : req.entrySet()) {
			writer.append(entry.getKey()).append(" : ").append(StringUtil.toString(entry.getValue())).append("<br/>\n");
		}
		return writer.getBuffer();
	}
}
