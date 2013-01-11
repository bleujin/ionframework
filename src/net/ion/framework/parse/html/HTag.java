package net.ion.framework.parse.html;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.htmlparser.jericho.Attribute;
import net.htmlparser.jericho.Attributes;
import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.Source;
import net.htmlparser.jericho.StartTagType;
import net.ion.framework.util.ListUtil;
import net.ion.framework.util.StringUtil;

import org.apache.ecs.xml.XML;

public class HTag {

	public static HTag EMPTY_HTAG = new EmptyHTag();
	private Element element;

	protected HTag(Element element) {
		this.element = element;
	}

	private HTag(Reader content, int index) throws IOException {
		this.element = (Element) new Source(content).getAllElements().get(index);
	}

	static HTag create(Element element) throws IOException {
		return new HTag(element);
	}

	public static HTag createGeneral(Reader reader, String rootName) throws IOException {
		Source source = new Source(reader);
		List<Element> allElements = source.getAllElements();
		StringBuilder buffer = new StringBuilder("<prefix>");

		Element htmlElement = null;

		if (allElements.size() == 0)
			return EMPTY_HTAG;
		for (int i = 0, last = allElements.size(); i < last; i++) {

			if (rootName.equalsIgnoreCase(allElements.get(i).getName())) {
				htmlElement = allElements.get(i);
				buffer.append("</prefix>");
				HTag result = new HTag(htmlElement);
				result.setPrfixTag(buffer);
				return result;
			} else {
				Element ele = allElements.get(i);
				buffer.append(ele.toString());
			}
		}

		htmlElement = (Element) allElements.get(0);
		return new HTag(htmlElement);
	}
	private HTag prefixTag = null;

	private void setPrfixTag(StringBuilder buffer) throws IOException {
		this.prefixTag = new HTag(new StringReader(buffer.toString()), 0);
	}

	public HTag getPrefixTag() {
		return prefixTag;
	}

	static HTag create(Reader content, int index) throws IOException {
		return new HTag(content, index);
	}

	public String getTagName() {
		return element.getName();
	}

	public String getElementValue(String path) throws NotFoundTagException, IOException {
		if (StringUtil.isBlank(path)) {
			String value = element.getContent().toString();
			return isCdataSection(value) ? substringCData(value) : value;
		}
		if (StringUtil.indexOf(path, '@') > -1) {
			String[] eleInfo = StringUtil.split(path, "@");
			return getChild(eleInfo[0]).getAttributeValue(eleInfo[1]);
		}
		return getChild(path).getElementValue("");
	}

	private String substringCData(String value) {
		return value.substring("<![CDATA[".length(), value.length() - "]]>".length());
	}

	private boolean isCdataSection(String value) {
		return value.startsWith("<![CDATA[") && value.endsWith("]]>");
	}

	public String getElementText() throws IOException {
		StringWriter writer = new StringWriter();
		
		if (element.getContent().getFirstElement() == null && element.getAttributes().size() == 0  && element.getStartTag().getTagType().equals(StartTagType.NORMAL)){
			return getTagText() ;
		} else if (element.getContent().getFirstStartTag().getTagType().equals(StartTagType.CDATA_SECTION)) {
			//element.getContent().getFirstStartTag().getTagContent().getTextExtractor().writeTo(writer) ;
			element.getContent().getFirstStartTag().getTagContent();
			writer.append(element.getContent().getFirstStartTag().getTagContent().toString());
		} else {
			return "" ;
		}
		return writer.getBuffer().toString();
	}
	
	public String getOnlyText() throws IOException {
		StringWriter writer = new StringWriter();

		if (element.getContent().getFirstElement() == null && element.getStartTag().getTagType().equals(StartTagType.NORMAL)){
			element.getTextExtractor().writeTo(writer);
		} else if (element.getContent().getFirstStartTag().getTagType().equals(StartTagType.CDATA_SECTION)) {
			//element.getContent().getFirstStartTag().getTagContent().getTextExtractor().writeTo(writer) ;
			element.getContent().getFirstStartTag().getTagContent();
			writer.append(element.getContent().getFirstStartTag().getTagContent().toString());
			writer.flush();
		} else {
			element.getTextExtractor().writeTo(writer);
		}
		return writer.getBuffer().toString();
	}

	public String getContent() {
		return element.toString();
	}

	public int getContentLength() {
		return element.getContent().length();
	}

	public Element getElement() {
		return element;
	}

	public String getAttributeValue(String attrName) {
		return element.getAttributeValue(attrName);
	}

	public String getValue(String _name) throws NotFoundTagException, IOException {
		String name = _name;
		if (_name.startsWith("@"))
			name = _name.substring(1);
		String attrValue = element.getAttributeValue(name);
		if (attrValue != null)
			return attrValue;
		return getElementValue(name);
	}

	public String getDefaultValue(String _name) {
		return getDefaultValue(_name, "");
	}

	public String getDefaultValue(String _name, String dValue) {
		try {
			return StringUtil.replace(getValue(_name), "]]/>", "]]>");
		} catch (Exception e) {
			return dValue;
		}
	}

	public int getDefaultIntValue(String _name) {
		return getDefaultIntValue(_name, "0");
	}

	public int getDefaultIntValue(String _name, String dValue) {
		return Integer.parseInt(getDefaultValue(_name, dValue));
	}

	public String getPath() {
		ArrayList<String> store = new ArrayList<String>();

		Element current = element;
		Element parent = element.getParentElement();
		while (parent != null) {
			List<Element> childs = parent.getChildElements();
			for (int i = 0, findex = 0; i < childs.size(); i++) {
				if (childs.get(i).getName().equals(current.getName()) && current == childs.get(i)) {
					store.add(current.getName() + "[" + findex + "]");
					break;
				} else if (childs.get(i).getName().equals(current.getName())) {
					findex++;
				}
			}

			current = parent;
			parent = parent.getParentElement();
		}

		Collections.reverse(store);
		return "/" + StringUtil.join(store.toArray(new String[0]), "/");
	}

	public boolean hasChild() {
		return element.getChildElements().size() > 0;
	}

	public boolean hasChild(String path) throws IOException {
		try {
			getChild(path);
		} catch (NotFoundTagException ex) {
			return false;
		}
		return true;
	}

	public HTag getFirstChild() throws IOException, NotFoundTagException {
		if (!hasChild())
			throw new NotFoundTagException("no child");

		return create((Element) element.getChildElements().get(0));
	}

	private Pattern pattern = Pattern.compile("(\\w*)\\[(\\d*)\\]");

	public HTag getChild(String path) throws IOException, NotFoundTagException {
		if (StringUtil.isBlank(path))
			return this;

		if (isRecursive(path)) {
			String beforePath = StringUtil.substringBefore(path + "/", "/");
			String afterPath = StringUtil.substringAfter(path, "/");
			HTag parent = getChild(beforePath);
			return parent.getChild(afterPath);
		}

		Matcher matcher = pattern.matcher(path);
		if (matcher.matches()) {
			if (StringUtil.contains(path, "/")) {
				return create(new StringReader("<font color=red>!!</font>"), 0);
			}
			return getChild(matcher.group(1), Integer.parseInt(matcher.group(2)));
		} else
			return getChild(path, 0);
	}

	private boolean isRecursive(String path) {
		String afterPath = StringUtil.substringAfter(path, "/");
		return StringUtil.isNotBlank(afterPath);
	}

	public List<HTag> findElements(String type) throws IOException {
		List<HTag> result = new ArrayList<HTag>();
		List<Element> els = element.getAllElements(type);
		for (int i = 0, last = els.size(); i < last; i++) {
			result.add(create(els.get(i)));
		}

		return result;
	}

	public HTag findElementById(String type, String idValue) throws IOException, NotFoundTagException {
		return findElementBy(type, "id", idValue);
	}

	public HTag findElementBy(String type, String attributeName, String attributeValue) throws IOException, NotFoundTagException {
		HTag result = null;
		List<Element> els = element.getAllElements(type);
		for (int i = 0, last = els.size(); i < last; i++) {
			if (attributeValue.equals(els.get(i).getAttributeValue(attributeName))) {
				result = new HTag(els.get(i));
				break;
			}
		}
		if (result == null)
			throw new NotFoundTagException("type:" + type + ",attributeName:" + attributeName + ",attributeValue:" + attributeValue);
		return result;
	}

	public List<HTag> getChildren() throws IOException {
		return getChildren(0);
	}

	
	public List<HTag> getChildren(String childname) throws IOException {
		if ("param".equals(childname)) throw new IllegalArgumentException("not permitted name") ; // not use param
		List<HTag> result = ListUtil.newList();

		List<HTag> children = getChildren(0);
		for (HTag child : children) {
			if (child.getTagName().equals(childname)) {
				result.add(child);
			}
		}

		return result;
	}

	public List<HTag> getChildren(int skip) throws IOException {
		List<HTag> result = new ArrayList<HTag>();
		List<Element> childElement = element.getChildElements();
		for (int i = skip, last = childElement.size(); i < last; i++) {
			result.add(create(childElement.get(i)));
		}

		return result;
	}

	public HTag getChild(String tagName, int positionIndex) throws NotFoundTagException, IOException {
		return getChild(this, tagName, positionIndex);
	}

	private HTag getChild(HTag parent, String tagName, int positionIndex) throws NotFoundTagException, IOException {
		List<HTag> children = parent.getChildren();
		int foundedIndex = 0;
		for (int i = 0, last = children.size(); i < last; i++) {
			HTag tag = children.get(i);
			if (tagName.equalsIgnoreCase(tag.getTagName())) {
				if (foundedIndex == positionIndex) {
					return create(tag.element);
				} else
					foundedIndex++;
			}
		}

		throw new NotFoundTagException("NOT FOUND TAG: " + tagName);
	}

	public String[] findPathByText(String textValue) throws IOException {

		List<String> store = new ArrayList<String>();
		storePathByText(this, textValue, store, "");

		return store.toArray(new String[0]);
	}

	private void storePathByText(HTag tag, String textValue, List<String> store, String prePath) throws IOException {

		List<HTag> children = tag.getChildren();
		if (textValue.trim().equals(tag.getOnlyText())) {
			store.add(prePath + "/" + tag.getTagName());
		}

		if (children.size() <= 0)
			return;
		for (int i = 0, last = children.size(); i < last; i++) {
			storePathByText(children.get(i), textValue, store, prePath + "/" + tag.getTagName());
		}

	}

	public int hashCode() {
		return toString().hashCode();
	}

	public boolean equals(Object that) {
		if (that == null || !(that instanceof HTag))
			return false;
		return (((HTag) that).getContent()).equals(this.getContent());
	}

	public String toString() {
		return element.toString();
	}

	public String getPathName() {
		StringBuilder builder = new StringBuilder(getTagName());
		Element parent = element.getParentElement();
		while (parent != null) {
			builder.insert(0, parent.getName() + "/");
			parent = parent.getParentElement();
		}
		return builder.toString();
	}

	public int getDepth() {
		return element.getDepth();
	}

	public String getTagText() throws IOException {
		return element.getContent().toString();
	}

	public XML toXML() throws IOException {
		XML xml = new XML(getTagName());
		if (element != null) {
			Attributes attributes = element.getAttributes();
			if (attributes != null) {
				Attribute[] attrs = element.getAttributes().toArray(new Attribute[0]);

				for (Attribute attr : attrs) {
					xml.addAttribute(attr.getName(), attr.getValue());
				}
			}

			List<HTag> children = getChildren();
			for (HTag child : children) {
				if (child.getTagName().equals("![cdata[")) {
					xml.addElement(this.getElementValue(""));
				} else if (child.getElement().getEndTag() == null) { // not closed tag
					XML tag = new XML(child.getTagName());
					tag.addElement(child.getElementValue(""));
					xml.addElement(tag);
				} else {
					xml.addElement(child.toXML());
				}
			}
		}

		return xml;
	}

	public void visit(TagVisitor visitor) throws IOException {
		visitor.visit(this);
		List<HTag> children = getChildren();
		for (HTag tag : children) {
			tag.visit(visitor);
		}
	}

	public List<TagAttribute> getAttributes() {
		List<TagAttribute> result = ListUtil.newList() ;
		for (Attribute attr : element.getAttributes().toArray(new Attribute[0])) {
			result.add(new TagAttribute(attr)) ;
		}
		return result;
	}

}
