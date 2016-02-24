package net.ion.framework.configuration;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.StringTokenizer;

import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

/**
 * @author Choi sei hwan <a href="mailto:sehan@i-on.net">Choi sei hwan</a>
 * @version 1.0
 * 
 *          <pre>
 * 기본 Configuration 클래스
 * 
 * XML 의 Tag Element 와 DefaultConfiguration 클래스하나가 매핑된다.
 * ex)
 *  <tag attributeName=attributeValue>value</tag>
 * 
 * xml 의 attribute는 getAttribute(...) 로 value는 getValue()로 접근한다.
 * 
 * ex)
 *      ConfigurationFactory factory = ConfigurationFactory.newinstance();
 *      factory.build( "환경설정 XML 파일" );
 *      // <config><sys type="unix">Solaris</sys></config> 일때
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
	 *            현재 Configuration 이 가지는 Element
	 * @param elementPathString
	 *            현재 Element의 위치를 나타내는 path 문자열
	 * @param elementIndexString
	 *            현재 Element의 위치를 나타내는 index 문자열 path 문자열이 <br>
	 *            aaa.bbb.ccc 라면 aaa 가 몇번째 인지, bbb가 면번째 인지를 나타낸다. 0.0.0 이라면 aaa 첫번째, bbb 첫번째, ccc 첫번째를 나타낸다.
	 * @param factoryInstanceKeyName
	 * @throws ConfigurationException
	 */
	public DefaultConfiguration(Element element, String elementPathString, String elementIndexString, String factoryInstanceKeyName) throws ConfigurationException {
		// System.out.println( "Configuration created : " + elementPathString );
		this.element = element;
		this.elementPathString = elementPathString;
		this.elementIndexString = elementIndexString;
		this.elementDepth = (new StringTokenizer(elementPathString, ".")).countTokens();
		this.factory = (DefaultConfigurationFactory) ConfigurationFactory.getInstance(factoryInstanceKeyName);
		this.tagName = element.getTagName();
	}

	/**
	 * 현재 Configuration 에서 childElementName이름의 자식 Element 를 하나가져온다. childElementName을 가지는 자식이 하나이상일경우에는 가장처음에 발견되는 자식만을 가져온다.
	 * 
	 * @param childElementName
	 *            String 자식 Element 의 이름
	 * @throws NotFoundXmlTagException
	 *             childElementName이 존재하지 않는 경우 발생
	 * @throws NotBuildException
	 *             환경설정파일(.xml)이 아직 Build 되어있지 않은 경우 발생
	 * @throws ConfigurationException
	 *             환경설정파일 처리시 문제가 발생하였을 경우 발생
	 * @return Configuration 해당 Child Element의 Configuration 인스턴스
	 */
	public Configuration getChild(String childElementName) throws NotFoundXmlTagException, NotBuildException, ConfigurationException {
		return factory.getConfiguration(childElementName, this);
	}

	public Configuration[] children(String childElementName) throws NotBuildException, ConfigurationException {
		return factory.configurations(childElementName, this);
	}
	public boolean hasChild(String childElementName) throws NotBuildException, ConfigurationException { 
		return children(childElementName).length > 0 ;
	}
	
	public boolean hasAttribute(String attributeName) throws ConfigurationException {
		return getElement().hasAttribute(attributeName) ;
	}

	
	/**
	 * 현재 Configuration 에서 childElementName이름의 자식 Element 를 모두 가져온다.
	 * 
	 * @param childElementName
	 *            String 자식 Element 의 이름
	 * @throws NotFoundXmlTagException
	 *             childElementName이 존재하지 않는 경우 발생
	 * @throws NotBuildException
	 *             환경설정파일(.xml)이 아직 Build 되어있지 않은 경우 발생
	 * @throws ConfigurationException
	 *             환경설정파일 처리시 문제가 발생하였을 경우 발생
	 * @return Configuration[]
	 */
	public Configuration[] getChildren(String childElementName) throws NotFoundXmlTagException, NotBuildException, ConfigurationException {
		return factory.getConfigurations(childElementName, this);
	}

	/**
	 * 태그의 값을 가져온다.
	 * 
	 * @return String 태그 값
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
	 * Attribute 값을 가져온다.
	 * 
	 * @param attributeName
	 *            String Attribute 이름
	 * @return String Attribute 값
	 */
	public String getAttribute(String attributeName) {
		return element.getAttribute(attributeName).trim();
	}

	/**
	 * 현재 Tag 에 attributeName을 가지는 attribute가 존재하면 값을 attributeValue 로 설정하고 존재하지 않으면 attribute 를 추가한다.
	 * 
	 * @param attributeName
	 * @param attributeValue
	 * @throws ConfigurationException
	 *             attributeName 에 illegal character가 포함되어있을때
	 */
	public void setAttribute(String attributeName, String attributeValue) throws ConfigurationException {
		try {
			element.setAttribute(attributeName, attributeValue);
		} catch (DOMException ex) {
			throw new ConfigurationException(ex.getLocalizedMessage(), ex);
		}
	}

	/**
	 * 현재 Configuration 의 Element 를 가져온다.
	 * 
	 * @return Element
	 */
	public Element getElement() {
		return element;
	}

	/**
	 * 현재 Configuration 의 위치를 나타내는 path 문자열
	 * 
	 * @return String
	 */
	public String getElementPathString() {
		return elementPathString;
	}

	/**
	 * 현재 Configuration 의 위치를 나타내는 index 문자열
	 * 
	 * @return String
	 */
	public String getElementIndexString() {
		return elementIndexString;
	}

	/**
	 * 현재 Configuration 위치의 깊이
	 * 
	 * elementPathString 이 aaaa.bbb 라면 elementDepth 는 2
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
