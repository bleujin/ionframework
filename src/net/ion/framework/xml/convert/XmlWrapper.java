package net.ion.framework.xml.convert;

import net.ion.framework.xml.XmlDocument;
import net.ion.framework.xml.XmlSerializable;
import net.ion.framework.xml.excetion.XmlException;

/**
 * XmlSerializable�� �ν��Ͻ����� �ϳ��� XML�� ����� net.ion.framework.xml.XmlDocument ���·� �����Ѵ�.
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
	 * XmlSerializable�� �ν��Ͻ����� �� �ν��Ͻ��� toXml() �޼ҵ带 �̿��Ͽ� �ϳ��� XML String���� ������ش�.
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
	 * init()�� ������ XML String�� net.ion.framework.xml.XmlDocument ���·� �����´�.
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
