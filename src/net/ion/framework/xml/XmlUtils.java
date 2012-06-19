package net.ion.framework.xml;

import java.util.ArrayList;
import java.util.List;

import net.ion.framework.util.StringUtil;

public class XmlUtils {

	private String rootTag;
	private StringBuffer buffer = new StringBuffer();

	public XmlUtils(String rootTag) {
		this.rootTag = rootTag;
	}

	private List<String> attribute = new ArrayList<String>();

	public void addAttribute(String name, String value) {
		attribute.add(name + "=\"" + value + "\"");
	}

	public void addAttribute(String name, int value) {
		addAttribute(name, String.valueOf(value));
	}

	public void makeTag(String tagName, int value) {
		makeTag(tagName, String.valueOf(value));
	}

	public void makeTag(String tagName, String value) {
		// buffer.append("\n\t<" + tagName + ">" + value + "</" + tagName + ">")
		// ;
		buffer.append("\n\t<").append(tagName).append(">").append(value).append("</").append(tagName).append(">");
	}

	public void makeTag(String lowerTag) {
		// buffer.append("\n\t" + lowerTag) ;
		buffer.append("\n\t").append(lowerTag);
	}

	public void makeTag(String tagName, String value, String attrName, String attrValue) {
		makeTag(tagName, value, new String[] { attrName }, new String[] { attrValue });
	}

	public void makeTag(String tagName, String value, String[] attrName, String[] attrValue) {
		// Assert.assertEquals("name length and value length must be equal ! ",
		// attrName.length, attrValue.length);

		String attr = "";
		for (int i = 0, last = attrName.length; i < last; i++) {
			attr += " " + attrName[i] + "=\"" + attrValue[i] + "\"";
		}
		// buffer.append("\n\t<" + tagName + attr + ">" + value + "</" + tagName
		// + ">") ;
		buffer.append("\n\t<").append(tagName).append(attr).append(">").append(value).append("</").append(tagName).append(">");
	}

	public void makeTag(String tagName, String value, int depth) {
		// buffer.append("\n" + StringUtils.repeat("\t", depth)+ "<" + tagName +
		// ">" + value + "</" + tagName + ">") ;
		buffer.append("\n").append(StringUtil.repeat("\t", depth)).append("<").append(tagName).append(">").append(value).append("</").append(tagName).append(
				">");
	}

	public void makeCDATATag(String tagName, String value, int depth) {
		// buffer.append("\n" + StringUtils.repeat("\t", depth)+ "<" + tagName +
		// "><![CDATA[" + value + "]]></" + tagName + ">") ;
		buffer.append("\n").append(StringUtil.repeat("\t", depth)).append("<").append(tagName).append("><![CDATA[").append(value).append("]]></").append(
				tagName).append(">");
	}

	private Object mutex = new Object();

	public StringBuffer toBuffer() {
		if (StringUtil.isBlank(rootTag)) {
			return buffer;
		} else {
			synchronized (mutex) {
				buffer.insert(0, "\n<" + rootTag + getAttribute() + ">");
				for (int i = 0, last = lowerXML.size(); i < last; i++) {
					buffer.append(((XmlUtils) lowerXML.get(i)).toBuffer());
				}
				// buffer.append("\n</" + rootTag + ">\n") ;
				buffer.append("\n</").append(rootTag).append(">\n");
				rootTag = null;
			}

			return buffer;
		}
	}

	private String getAttribute() {
		return (attribute.size() > 0 ? " " : "") + StringUtil.join(attribute.iterator(), ' ');
	}

	public String toString() {
		return toBuffer().toString();
	}

	private List<XmlUtils> lowerXML = new ArrayList<XmlUtils>();

	public void addXML(XmlUtils xml) {
		lowerXML.add(xml);
	}

}
