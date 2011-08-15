package net.ion.framework.xml;

import java.io.Reader;
import java.io.StringReader;

/**
 * XML String을 멤버로 가지며, XML String에 대한 java.io.Reader 인스턴스를 제공한다.
 * 
 * @author bleujin
 * @version 1.0
 */

public class XmlDocument {

	private StringBuffer xmlSource;

	public XmlDocument(StringBuffer xmlSource) {
		this.xmlSource = xmlSource;
	}

	public XmlDocument(String fileName) {

	}

	public StringBuffer getXmlString() {
		return xmlSource;
	}

	public Reader getReader() {
		return new StringReader(xmlSource.toString());
	}
}
