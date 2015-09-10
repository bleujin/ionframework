package net.ion.framework.configuration;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * Configuration Factory Ŭ����<br>
 * 
 * <pre>
 * Configuration instance�� ������. factory �ν��Ͻ��� static ���� �׻� �ϳ��� instance�� ����
 * �Ѵ�.
 * 
 * Instance�� ���
 * ex>
 *      ConfigurationFactory factory = ConfigurationFactory.newinstance();
 * 
 * Configuration ����ϱ�
 * ex>
 *      factory.getConfiguration( "elementPathString" ); // elementPathString�� <config><sys>... �ϰ�� "config.sys. " �Ͱ��� ǥ���Ѵ�.
 * 
 * 
 * </pre>
 * 
 * @author Choi sei hwan <a href="mailto:sehan@i-on.net">Choi sei hwan</a>
 * @version 1.0
 */

@SuppressWarnings("unchecked")
public class DefaultConfigurationFactory extends ConfigurationFactory {
	private Element documentElement;

	// ���� Configuration �ν��Ͻ��� �����ϴ� hashtable
	private Hashtable configurationCache = new Hashtable();

	private ConfigurationType configurationType = ConfigurationType.DEFAULT;

	/**
	 * instanceKeyName �� ������ �ν��Ͻ��� ���Ѵ�.
	 * 
	 * @param instanceKeyName
	 *            String
	 */
	public DefaultConfigurationFactory(String instanceKeyName) {
		if (instanceKeyName == null) {
			this.instanceKeyName = "default_instanceKeyName";
		} else {
			this.instanceKeyName = instanceKeyName;
		}

		// System.out.println( "Configuration Factory instance Create! ( key : " + instanceKeyName + " )" );
	}

	/**
	 * DocumentBuilder �� ���Ѵ�.
	 * 
	 * @throws ConfigurationBuildException
	 * @return DocumentBuilder
	 */
	private DocumentBuilder createDocumentBuilder() throws ConfigurationBuildException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;

		try {
			builder = factory.newDocumentBuilder();
		} catch (ParserConfigurationException ex) {
			throw new ConfigurationBuildException(ex.getLocalizedMessage(), ex);
		}
		return builder;

	}

	// /**
	// * ���Ϸ� ���� DocumentElement �� ���´�.
	// *
	// * @param configFile - ȯ�漳���� ���� XML���� �ν��Ͻ�
	// * @throws ConfigurationBuildException
	// */
	// private void createDocumentFromFile( File configFile ) throws ConfigurationBuildException
	// {
	// Document doc = null;
	//
	// try
	// {
	// doc = createDocumentBuilder().parse( configFile );
	// }
	// catch( IOException ex1 )
	// {
	// throw new ConfigurationBuildException( ex1.getLocalizedMessage(), ex1 );
	// }
	// catch( SAXException ex1 )
	// {
	// throw new ConfigurationBuildException( ex1.getLocalizedMessage(), ex1 );
	// }
	//
	// documentElement = doc.getDocumentElement();
	// }

	/**
	 * org.xml.sax.InputSource �� ���� DocumentElement �� ���´�.
	 * 
	 * @param inputsource
	 *            InputSource ȯ�漳���� ���� InputSource �ν��Ͻ�
	 * @throws ConfigurationBuildException
	 */
	private void createDocument(InputSource inputsource) throws ConfigurationBuildException {
		Document doc = null;

		try {
			doc = createDocumentBuilder().parse(inputsource);
		} catch (IOException ex1) {
			throw new ConfigurationBuildException(ex1.getLocalizedMessage(), ex1);
		} catch (SAXException ex1) {
			throw new ConfigurationBuildException(ex1.getLocalizedMessage(), ex1);
		}
		documentElement = doc.getDocumentElement();
	}

	// public void build( File configFile ) throws ConfigurationBuildException
	// {
	// createDocumentFromFile( configFile );
	// }

	/**
	 * inputsource �� ���ؼ� xml config ������ �о���δ�.
	 * 
	 * @param inputsource
	 *            InputSource ȯ�漳���� ���� InputSource Class
	 * @throws ConfigurationBuildException
	 */
	public void build(InputSource inputsource) throws ConfigurationBuildException {
		createDocument(inputsource);
	}

	/**
	 * ã�����ϴ� Element �� Root Element ������ Ȯ��
	 * 
	 * @param elementToken
	 *            ElementInfoTokenizer ã�����ϴ� element �� ������ ������ �ִ� ElementInfoTokenizer
	 * @throws NotFoundXmlTagException
	 * @return boolean ã�������ϴ� element �� root �� ������ true �ƴϸ� false
	 */
	private boolean isRootElement(ElementInfoTokenizer elementToken) throws NotFoundXmlTagException {
		String rootElementName = elementToken.nextToken().getElementName();

		if (!documentElement.getTagName().equalsIgnoreCase(rootElementName)) {
			throw new NotFoundXmlTagException(elementToken.getElementPathString() + ":" + rootElementName);
		}

		if (!elementToken.hasMoreTokens()) {
			// root element
			return true;
		} else {
			// sub element
			return false;
		}
	}

	/**
	 * ó����� Configuration �ν��Ͻ��� Hashtable�� �߰��Ѵ�.�ѹ���� Configuration �ν��Ͻ����� Hashtable�� �߰��ȴ�.
	 * 
	 * @param key
	 *            String Configuration �ν��Ͻ��� Ű��. elementPathString�� ���εȴ�.
	 * @param obj
	 *            Object ��� Configuration �ν��Ͻ�
	 */
	private void addConfigurationCache(String key, Object obj) {
		configurationCache.put(key, obj);
	}

	/**
	 * Hashtable�� �˻��ؼ� Configuration�� return�Ѵ�.
	 * 
	 * @param key
	 *            String Configuration �ν��Ͻ��� Ű��. elementPathString�� ���εȴ�.
	 * @return Object Object( Configuration �ν��Ͻ� )
	 */
	private Object searchConfigurationCache(String key) {
		return configurationCache.get(key);
	}

	/**
	 * ConfigurationFactory�� build �޼ҵ尡 ����Ǿ������ üũ�Ѵ�. build�޼ҵ尡 ����Ǿ� documentElement �� ��Ǿ���� ���θ� üũ.
	 * 
	 * @return boolean build�� ����Ǿ����� true, �׷��� ������ false
	 */
	private boolean isBuild() {
		if (this.documentElement != null)
			return true;
		else
			return false;
	}

	/**
	 * elementPathString�� �ش��ϴ� Configuration �ν��Ͻ��� �����´�.
	 * 
	 * @param elementPathString
	 *            String ������ Configuration �ν��Ͻ��� path ���ڿ�
	 * @throws NotFoundXmlTagException
	 * @throws NotBuildException
	 * @throws ConfigurationException
	 * @return Configuration
	 */
	public Configuration getConfiguration(String elementPathString) throws NotFoundXmlTagException, NotBuildException, ConfigurationException {
		return getConfiguration(elementPathString, null);
	}

	/**
	 * �θ𿡼� �ڽ� Element �� Configuration �� �ϳ� ���´�. �ϳ��̻� �����ϸ� ���� ���� �߰ߵǴ°��� ���´�.
	 * 
	 * @param childElementName
	 *            String �ڽ� element �� �̸�
	 * @param parentConfiguration
	 *            DefaultConfiguration �θ� Configuration �ν��Ͻ�
	 * @throws NotFoundXmlTagException
	 * @throws NotBuildException
	 * @throws ConfigurationException
	 * @return Configuration �ڽ� Element �� Configuration �ν��Ͻ�
	 */
	public Configuration getConfiguration(String childElementName, DefaultConfiguration parentConfiguration) throws NotFoundXmlTagException, NotBuildException, ConfigurationException {
		return (this.getConfigurations(childElementName, parentConfiguration))[0];
	}

	public Configuration configuration(String childElementName, DefaultConfiguration parentConfiguration) throws NotFoundXmlTagException, NotBuildException, ConfigurationException {
		Configuration[] result = this.configurations(childElementName, parentConfiguration);
		if (result.length == 0) throw new NotFoundXmlTagException(parentConfiguration + " : " + childElementName);
		return result[0];
	}


	/**
	 * elementPathString�� �ش��ϴ� Configuration �ν��Ͻ����� �����´�.
	 * 
	 * @param elementPathString
	 *            String ������ Configuration �ν��Ͻ��� path ���ڿ�
	 * @throws NotFoundXmlTagException
	 * @throws NotBuildException
	 * @throws ConfigurationException
	 * @return Configuration[]
	 */
	public Configuration[] getConfigurations(String elementPathString) throws NotFoundXmlTagException, NotBuildException, ConfigurationException {
		return getConfigurations(elementPathString, null);
	}

	/**
	 * �θ𿡼� �ڽ� Element �� Configuration���� ���´�.
	 * 
	 * @param childElementName
	 *            String �ڽ� element �� �̸�
	 * @param parentConfiguration
	 *            DefaultConfiguration ���� Configuration �ν��Ͻ�
	 * @throws NotFoundXmlTagException
	 * @throws NotBuildException
	 * @throws ConfigurationException
	 * @return Configuration[] �ڽ� Element �� Configuration []
	 */
	public Configuration[] getConfigurations(String childElementName, DefaultConfiguration parentConfiguration) throws NotFoundXmlTagException, NotBuildException, ConfigurationException {
		Configuration[] result = configurations(childElementName, parentConfiguration) ;
		if (result.length == 0) throw new NotFoundXmlTagException(parentConfiguration.getElementPathString() + " : " + childElementName) ;
		return result ;
	}

	
	public Configuration[] configurations(String childElementName, DefaultConfiguration parentConfiguration) throws NotBuildException, ConfigurationException {
		Element currentElement = null;
		String elementIndexStringTemp = null;
		String elementIndexString = null;
		String elementPathString = null;
		int elementDepth = 0;

		// �θ� Element �� �����Ѵ�.
		if (parentConfiguration != null) {
			// �θ� ������
			// parentConfiguration �� Element �������� ã�´�.
			currentElement = parentConfiguration.getElement();
			elementPathString = parentConfiguration.getElementPathString() + "." + childElementName;
			elementIndexString = parentConfiguration.getElementIndexString();
			elementIndexStringTemp = elementIndexString;
			elementDepth = parentConfiguration.getElementDepth();
		} else {
			// ���� ������
			// DocumentElement �������� ã�´�.
			currentElement = documentElement;
			elementPathString = childElementName;
			int count = (new StringTokenizer(elementPathString, ".")).countTokens() - 2;
			elementIndexString = "0";
			while (count > 0) {
				elementIndexString += ".0";
				count--;
			}
			// elementIndexString �� "" �̸� �ֻ��� element �� �ϳ��̹Ƿ� "0" �� �⺻���� �Ѵ�.
			elementIndexStringTemp = "";
		}
		// DEBUG
		// System.out.println( "[[ getConfigurations() DEBUG Message Start ]]");
		// System.out.println( "- currentElement : " + currentElement.getTagName());
		// System.out.println( "- elementPathString : " + elementPathString );
		// System.out.println( "- elementIndexString : " + elementIndexString );

		// build �Ǿ������ �˻��Ѵ�.
		if (!isBuild())
			throw new NotBuildException("ConfigurationFactory.build() not execute!");

		ElementInfoTokenizer elementToken = new ElementInfoTokenizer(elementPathString, elementIndexString);

		// Configuration Cache Table �� �˻��Ѵ�.
		// ã������ return �׷�ġ ������ configurations �ʱ�ȭ
		Vector configurations = null;
		if ((configurations = (Vector) searchConfigurationCache(elementPathString + ":" + elementIndexString)) != null)
			return (Configuration[]) configurations.toArray(new Configuration[0]);
		else
			configurations = new Vector();

		Vector elements = new Vector();

		// root element �̸� �������� element �� ã�´�. elememntToken �� next 1ȸ
		if (!isRootElement(elementToken)) {
			// ã�����ϴ� Element �� Root Element �� �ƴҶ�

			NodeList currentNodeList = null;
			boolean isFindElement = false;
			ElementInfo currentToken = null;
			int indexCount = 0;

			for (int i = 0; i < elementDepth - 1; i++)
				elementToken.nextToken();

			while (elementToken.hasMoreTokens()) {
				isFindElement = false;

				currentToken = elementToken.nextToken();
				// DEBUG
				// System.out.println( "currentToken elementName : " + currentToken.getElementName() );
				// System.out.println( "currentToken elementIndex : " + currentToken.getElementIndex() );

				// xml element �� ��ܿ��� ���� �ش� element �� ã�´�.
				currentNodeList = currentElement.getChildNodes();

				indexCount = 0;
				for (int i = 0; i < currentNodeList.getLength(); i++) {
					// DEBUG
					// System.out.println( "element type : " + currentNodeList.item(i).getNodeType() );
					// System.out.println( "element node name : " + currentNodeList.item(i).getNodeName() );

					if (currentNodeList.item(i).getNodeType() == NODETYPE.ELEMENT && currentNodeList.item(i).getNodeName().equalsIgnoreCase(currentToken.getElementName())) {
						if (elementToken.hasMoreTokens()) {
							// DEBUG
							// System.out.println( "element name : " + currentNodeList.item( i ).getNodeName() );
							if (indexCount == currentToken.getElementIndex()) {
								currentElement = (Element) currentNodeList.item(i);
								i = currentNodeList.getLength();

								// elementIndexString �����
								if (elementIndexStringTemp != "")
									elementIndexStringTemp += ".";
								elementIndexStringTemp += currentToken.getElementIndex();

								isFindElement = true;
							}

							indexCount++;
						} else {
							// DEBUG
							// System.out.println( "elements add : " + ( ( Element )currentNodeList.item( i ) ).getTagName() );
							elements.add((Element) currentNodeList.item(i));
							isFindElement = true;
						}

					}
				}

				if (!isFindElement) {
					return new Configuration[0] ;
				}
			}
		} else {
			// ã�����ϴ� Element �� Root Element �϶�
			elements.add(documentElement);
		}

		// DEBUG
		// System.out.println( "elementIndexStringTemp : " + elementIndexStringTemp );

		// Element�� obj�� ������ �ִ� elements Vector�� Configuration �� obj�� ������ Vector�� �����Ѵ�.
		setElementToConfiguration(elementPathString, elementIndexStringTemp, elements, configurations);

		// Configuration Cache Table �� Configurations Object �� ����Ѵ�.
		addConfigurationCache(elementPathString + ":" + elementIndexStringTemp, configurations);

		return (Configuration[]) configurations.toArray(new Configuration[0]);
	}
	
	/**
	 * elements Vector �� Element type�� Object �� Configuration type �� Object �� ��ȯ��Ų��.
	 * 
	 * @param elementPathString
	 *            String ������ Configuration �ν��Ͻ��� path ���ڿ�
	 * @param elementIndexString
	 *            String ����ܰ������ IndexString
	 * @param elements
	 *            Vector Element �� ������ Vector
	 * @param configurations
	 *            Vector Configuration �� ������ Vector
	 * @throws ConfigurationException
	 */
	private void setElementToConfiguration(final String elementPathString, final String elementIndexString, Vector elements, Vector configurations) throws ConfigurationException {
		Class configurationClass = null;
		Constructor configurationConstructor = null;
		// ���� �Ķ���� ���� Class
		Class[] parameterTypes = new Class[] { Element.class, String.class, String.class, String.class };
		Object[] parameterValues = null;
		// elementPathString �� �´� Configuration Instance �� Configurations�� �ִ´�.
		for (int j = 0; j < elements.size(); j++) {
			try {
				configurationClass = Class.forName(configurationType.getClassName());
			} catch (ClassNotFoundException ex) {
				throw new ConfigurationException("Class not found : " + configurationType.getClassName(), ex);
			}

			try {
				configurationConstructor = configurationClass.getConstructor(parameterTypes);
			} catch (SecurityException ex1) {
				throw new ConfigurationException(ex1.getLocalizedMessage(), ex1);
			} catch (NoSuchMethodException ex1) {
				throw new ConfigurationException("No such method : " + configurationType.getClassName() + " Constructor", ex1);
			}

			// ���� �Ķ������ value Object
			String elementIndexStringTemp = null;
			if (elementIndexString == "")
				elementIndexStringTemp = String.valueOf(j);
			else
				elementIndexStringTemp = elementIndexString + "." + String.valueOf(j);

			parameterValues = new Object[] { (Element) elements.get(j), elementPathString, elementIndexStringTemp, this.instanceKeyName };
			try {
				configurations.add(configurationConstructor.newInstance(parameterValues));
			} catch (Exception ex) {
				throw new ConfigurationException(ex.getLocalizedMessage(), ex);
			}

		}
	}

	public Hashtable getConfigurationCache() {
		return this.configurationCache;
	}

}

/**
 * <pre>
 * ������(.) �� ���еǾ����� ���ڿ� elementPath �� elementIndex �ι��ڿ��� �޾�
 * ElementInfo ���·� Token �� ������ش�.
 * elementPath �� elementIndex�� ���� ��ġ�� �ִ� token �� �ϳ��� ElementInfo ������ Token�� �ȴ�.
 * 
 * ex)
 *      elementPath = "aaaa.bbbb.cccc";
 *      elementIndex = "0.1.0";
 *      �̶��
 *      {aaaa, 0 }, {bbbb, 1}, {cccc, 0} �� ���� ������ �ϳ��� ElementInfo ���̷��.
 * 
 * </pre>
 * 
 * @author Choi sei hwan <a href="mailto:sehan@i-on.net">Choi sei hwan</a>
 * @version 1.0
 */
class ElementInfoTokenizer {
	private StringTokenizer pathToken, indexToken;
	private String elementPath, elementIndex;

	public ElementInfoTokenizer(String elementPath, String elementIndex) {
		this.elementPath = elementPath;
		pathToken = new StringTokenizer(this.elementPath, ".");

		this.elementIndex = elementIndex;
		indexToken = new StringTokenizer(this.elementIndex, ".");
	}

	/**
	 * ElementIndexString ��������
	 * 
	 * @return String
	 */
	public String getElementIndexString() {
		return this.elementIndex;
	}

	/**
	 * ElementPathString ��������
	 * 
	 * @return String
	 */
	public String getElementPathString() {
		return this.elementPath;
	}

	/**
	 * ElementInfoToken �� ������ ���´�.
	 * 
	 * @return ElementInfoToken�� ����
	 */
	public int countTokens() {
		return pathToken.countTokens();
	}

	/**
	 * ���� ElementInfoToken �� �����´�.
	 * 
	 * @return ElementInfo ���� ElementInfoToken
	 */
	public ElementInfo nextToken() {
		int index = 0;
		if (indexToken.hasMoreTokens()) {
			index = Integer.parseInt(indexToken.nextToken());
		}
		return new ElementInfo(pathToken.nextToken(), index);
	}

	/**
	 * Token �� �����ִ����� üũ�Ѵ�.
	 * 
	 * @return boolean ���������� true �׷��� ������ false
	 */
	public boolean hasMoreTokens() {
		return pathToken.hasMoreTokens();
	}

}

/**
 * elementName �� elementIndex �ѽ��� ����� �����ȴ�. elementName, elementIndex�� ������ ����ü
 * 
 * @author Choi sei hwan <a href="mailto:sehan@i-on.net">Choi sei hwan</a>
 * @version 1.0
 */
class ElementInfo {
	private String elementName;
	private int elementIndex;

	public ElementInfo(String curElementName, int curElementIndex) {
		this.elementName = curElementName;
		this.elementIndex = curElementIndex;
	}

	/**
	 * ElementName�� �����´�.
	 * 
	 * @return String
	 */
	public String getElementName() {
		return this.elementName;
	}

	/**
	 * ElementIndex�� �����´�.
	 * 
	 * @return int
	 */
	public int getElementIndex() {
		return this.elementIndex;
	}

}
