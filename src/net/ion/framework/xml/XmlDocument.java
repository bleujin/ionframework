package net.ion.framework.xml;

import java.io.Reader;
import java.io.StringReader;

/**
 * XML String�� ����� ������, XML String�� ���� java.io.Reader �ν��Ͻ��� �����Ѵ�.
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
