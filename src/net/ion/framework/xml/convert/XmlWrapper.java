package net.ion.framework.xml.convert;

import net.ion.framework.xml.XmlDocument;
import net.ion.framework.xml.XmlSerializable;
import net.ion.framework.xml.excetion.XmlException;

/**
 * XmlSerializable한 인스턴스들을 하나의 XML로 만들어 net.ion.framework.xml.XmlDocument 형태로 제공한다.
 * 
 * @author not attributable
 * @version 1.0
 */

public class XmlWrapper {

	XmlSerializable[] xmlSerializable;
	String rootName;

	private StringBuffer buffer = new StringBuffer();
	private boolean isNotInitialized = true;

	public XmlWrapper(XmlSerializable[] xmlSerializable, String rootName) {
		this.xmlSerializable = xmlSerializable;
		this.rootName = rootName;
	}

	/**
	 * XmlSerializable한 인스턴스들을 각 인스턴스의 toXml() 메소드를 이용하여 하나의 XML String으로 만들어준다.
	 * 
	 * @throws XmlException
	 */
	private void init() throws XmlException {
		buffer.append("<" + rootName + ">\n");
		for (int i = 0; i < xmlSerializable.length; i++) {
			buffer.append(xmlSerializable[i].toXml().getXmlString());
		}
		buffer.append("</" + rootName + ">");
	}

	/**
	 * init()로 생성한 XML String을 net.ion.framework.xml.XmlDocument 형태로 가져온다.
	 * 
	 * @throws XmlException
	 * @return XmlDocument
	 */
	public XmlDocument getXmlDocument() throws XmlException {
		if (isNotInitialized)
			init();

		return new XmlDocument(buffer);
	}
}
