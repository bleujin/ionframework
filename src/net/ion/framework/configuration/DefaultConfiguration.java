package net.ion.framework.configuration;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.StringTokenizer;

import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;

/**
 * @author Choi sei hwan <a href="mailto:sehan@i-on.net">Choi sei hwan</a>
 * @version 1.0
 * 
 *          <pre>
 * �⺻ Configuration Ŭ����
 * 
 * XML �� Tag Element �� DefaultConfiguration Ŭ�����ϳ��� ���εȴ�.
 * ex)
 *  <tag attributeName=attributeValue>value</tag>
 * 
 * xml �� attribute�� getAttribute(...) �� value�� getValue()�� �����Ѵ�.
 * 
 * ex)
 *      ConfigurationFactory factory = ConfigurationFactory.newinstance();
 *      factory.build( "ȯ�漳�� XML ����" );
 *      // <config><sys type="unix">Solaris</sys></config> �϶�
 *      DefaultConfiguration config = factory.getConfiguration( "config.sys" );
 *      String attribute =  config.getAttribute( "type" );
 *      String value = config.getValue();
 * 
 * </pre>
 * 
 * 
 */

public class DefaultConfiguration extends AbstractConfiguration {
	private Element element = null;
	private DefaultConfigurationFactory factory = null;
	private String elementPathString = null;
	private String elementIndexString = null;
	private int elementDepth = 0;

	/**
	 * @param element
	 *            ���� Configuration �� ������ Element
	 * @param elementPathString
	 *            ���� Element�� ��ġ�� ��Ÿ���� path ���ڿ�
	 * @param elementIndexString
	 *            ���� Element�� ��ġ�� ��Ÿ���� index ���ڿ� path ���ڿ��� <br>
	 *            aaa.bbb.ccc ��� aaa �� ���° ����, bbb�� ���° ������ ��Ÿ����. 0.0.0 �̶�� aaa ù��°, bbb ù��°, ccc ù��°�� ��Ÿ����.
	 * @param factoryInstanceKeyName
	 * @throws ConfigurationException
	 */
	public DefaultConfiguration(Element element, String elementPathString, String elementIndexString, String factoryInstanceKeyName)
			throws ConfigurationException {
		// System.out.println( "Configuration created : " + elementPathString );
		this.element = element;
		this.elementPathString = elementPathString;
		this.elementIndexString = elementIndexString;
		this.elementDepth = (new StringTokenizer(elementPathString, ".")).countTokens();
		this.factory = (DefaultConfigurationFactory) ConfigurationFactory.getInstance(factoryInstanceKeyName);
		this.tagName = element.getTagName();
	}

	/**
	 * ���� Configuration ���� childElementName�̸��� �ڽ� Element �� �ϳ������´�. childElementName�� ������ �ڽ��� �ϳ��̻��ϰ�쿡�� ����ó���� �߰ߵǴ� �ڽĸ��� �����´�.
	 * 
	 * @param childElementName
	 *            String �ڽ� Element �� �̸�
	 * @throws NotFoundXmlTagException
	 *             childElementName�� �������� �ʴ� ��� �߻�
	 * @throws NotBuildException
	 *             ȯ�漳������(.xml)�� ���� Build �Ǿ����� ���� ��� �߻�
	 * @throws ConfigurationException
	 *             ȯ�漳������ ó���� ������ �߻��Ͽ��� ��� �߻�
	 * @return Configuration �ش� Child Element�� Configuration �ν��Ͻ�
	 */
	public Configuration getChild(String childElementName) throws NotFoundXmlTagException, NotBuildException, ConfigurationException {
		return factory.getConfiguration(childElementName, this);
	}

	/**
	 * ���� Configuration ���� childElementName�̸��� �ڽ� Element �� ��� �����´�.
	 * 
	 * @param childElementName
	 *            String �ڽ� Element �� �̸�
	 * @throws NotFoundXmlTagException
	 *             childElementName�� �������� �ʴ� ��� �߻�
	 * @throws NotBuildException
	 *             ȯ�漳������(.xml)�� ���� Build �Ǿ����� ���� ��� �߻�
	 * @throws ConfigurationException
	 *             ȯ�漳������ ó���� ������ �߻��Ͽ��� ��� �߻�
	 * @return Configuration[]
	 */
	public Configuration[] getChildren(String childElementName) throws NotFoundXmlTagException, NotBuildException, ConfigurationException {
		return factory.getConfigurations(childElementName, this);
	}

	/**
	 * �±��� ���� �����´�.
	 * 
	 * @return String �±� ��
	 */
	public String getValue() {
		if (element.hasChildNodes()) {

			if (element.getFirstChild().getNodeType() == NODETYPE.TEXT || element.getFirstChild().getNodeType() == NODETYPE.CDATA) {
				return (((Text) element.getFirstChild()).getData()).trim();
			} else {
				return new String("");
			}
		} else {
			return new String("");
		}
	}

	/**
	 * Attribute ���� �����´�.
	 * 
	 * @param attributeName
	 *            String Attribute �̸�
	 * @return String Attribute ��
	 */
	public String getAttribute(String attributeName) {
		return element.getAttribute(attributeName).trim();
	}

	/**
	 * ���� Tag �� attributeName�� ������ attribute�� �����ϸ� ���� attributeValue �� �����ϰ� �������� ������ attribute �� �߰��Ѵ�.
	 * 
	 * @param attributeName
	 * @param attributeValue
	 * @throws ConfigurationException
	 *             attributeName �� illegal character�� ���ԵǾ�������
	 */
	public void setAttribute(String attributeName, String attributeValue) throws ConfigurationException {
		try {
			element.setAttribute(attributeName, attributeValue);
		} catch (DOMException ex) {
			throw new ConfigurationException(ex.getLocalizedMessage(), ex);
		}
	}

	/**
	 * ���� Configuration �� Element �� �����´�.
	 * 
	 * @return Element
	 */
	public Element getElement() {
		return element;
	}

	/**
	 * ���� Configuration �� ��ġ�� ��Ÿ���� path ���ڿ�
	 * 
	 * @return String
	 */
	public String getElementPathString() {
		return elementPathString;
	}

	/**
	 * ���� Configuration �� ��ġ�� ��Ÿ���� index ���ڿ�
	 * 
	 * @return String
	 */
	public String getElementIndexString() {
		return elementIndexString;
	}

	/**
	 * ���� Configuration ��ġ�� ����
	 * 
	 * elementPathString �� aaaa.bbb ��� elementDepth �� 2
	 * 
	 * @return String
	 */
	public int getElementDepth() {
		return elementDepth;
	}

	public String getXML() throws ConfigurationException {
		OutputFormat format = new OutputFormat();
		format.setEncoding("UTF-8");
		format.setLineWidth(2000);

		Writer writer = new StringWriter();
		XMLSerializer xs = new XMLSerializer(writer, format);
		try {
			xs.serialize(element);
		} catch (IOException e) {
			throw new ConfigurationException("element serialize error", e);
		}
		
		return writer.toString();
	}
}
