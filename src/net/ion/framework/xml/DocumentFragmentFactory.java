package net.ion.framework.xml;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.digester.Digester;
import org.apache.commons.digester.NodeCreateRule;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 * org.w3c.dom.DoucmentFragment �ν��Ͻ��� ����� �ش�.
 * 
 * @author bleujin
 * @version 1.0
 */

public class DocumentFragmentFactory {
	Digester digester;

	public DocumentFragmentFactory() {
		digester = new Digester();
	}

	/**
	 * xml ���ڿ��� root�� rootName�� org.w3c.dom.DoucmentFragment �ν��Ͻ��� ������ش�.
	 * 
	 * @param xml
	 *            String
	 * @param rootName
	 *            String
	 * @throws SAXException
	 * @throws ParserConfigurationException
	 * @throws IOException
	 * @return DocumentFragment
	 */
	public DocumentFragment toDocumentFragment(String xml, String rootName) throws SAXException, ParserConfigurationException, IOException {

		digester.addRule(rootName, new NodeCreateRule(Node.DOCUMENT_FRAGMENT_NODE));
		Object result = digester.parse(new StringReader(xml));

		if (result != null && result instanceof DocumentFragment)
			return (DocumentFragment) result;
		else
			throw new SAXException("Not Valid xml");
	}

}
