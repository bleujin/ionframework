package net.ion.framework.rest;

import static net.ion.framework.rest.MyConstant.*;

import java.io.Serializable;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;

import net.ion.framework.rope.RopeWriter;
import net.ion.framework.util.StringUtil;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.ecs.xml.XML;
import org.json.JSONException;
import org.json.JSONObject;

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

	public JSONObject toJSON() throws JSONException {
		return new JSONObject(req);
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
		RopeWriter writer = new RopeWriter();
		toXML().output(writer);
		return writer.getRope();
	}
}
