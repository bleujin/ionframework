package net.ion.framework.parse.html;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EmptyHTag extends HTag {

	EmptyHTag() {
		super(null);
	}

	public String getTagName() {
		return "";
	}

	public String getOnlyText() throws IOException {
		return "";
	}

	public String getContent() {
		return "";
	}

	public String getAttributeValue(String attrName) {
		return null;
	}

	public String getPath() {
		return null;
	}

	public boolean hasChild() {
		return false;
	}

	public boolean hasChild(String path) throws IOException {
		return false;
	}

	public HTag getFirstChild() throws IOException, NotFoundTagException {
		return null;
	}

	public HTag getChild(String path) throws IOException, NotFoundTagException {
		return null;
	}

	public List<HTag> findElements(String type) throws IOException {
		return new ArrayList<HTag>();
	}

	public HTag findElementById(String type, String idValue) throws IOException, NotFoundTagException {
		throw new NotFoundTagException("EMPTY_TAG");
	}

	public HTag findElementBy(String type, String attributeName, String attributeValue) throws IOException, NotFoundTagException {
		throw new NotFoundTagException("EMPTY_TAG");
	}

	public List<HTag> getChildren() throws IOException {
		return getChildren(0);
	}

	public List<HTag> getChildren(int skip) throws IOException {
		return new ArrayList<HTag>();
	}

	public HTag getChild(String tagName, int positionIndex) throws NotFoundTagException, IOException {
		return getChild(this, tagName, positionIndex);
	}

	private HTag getChild(HTag parent, String tagName, int positionIndex) throws NotFoundTagException, IOException {
		return null;
	}

	public String[] findPathByText(String textValue) throws IOException {
		return new String[0];
	}

	public int hashCode() {
		return toString().hashCode();
	}

	public boolean equals(Object that) {
		if (that == null || !(that instanceof EmptyHTag))
			return false;
		return true;
	}

	public String toString() {
		return "EMPTY_TAG";
	}
}
