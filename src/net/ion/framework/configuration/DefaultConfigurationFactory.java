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
 * Configuration Factory 클래스<br>
 * 
 * <pre>
 * Configuration instance를 만들어낸다. factory 인스턴스는 static 으로 항상 하나의 instance를 유지
 * 한다.
 * 
 * Instance를 얻기
 * ex>
 *      ConfigurationFactory factory = ConfigurationFactory.newinstance();
 * 
 * Configuration 생산하기
 * ex>
 *      factory.getConfiguration( "elementPathString" ); // elementPathString은 <config><sys>... 일경우 "config.sys. " 와같이 표현한다.
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

	// 생성한 Configuration 인스턴스를 보관하는 hashtable
	private Hashtable configurationCache = new Hashtable();

	private ConfigurationType configurationType = ConfigurationType.DEFAULT;

	/**
	 * instanceKeyName 을 가지는 인스턴스를 생성한다.
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
	 * DocumentBuilder 를 생성한다.
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
	// * 파일로 부터 DocumentElement 를 얻어온다.
	// *
	// * @param configFile - 환경설정에 사용될 XML파일 인스턴스
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
	 * org.xml.sax.InputSource 로 부터 DocumentElement 를 얻어온다.
	 * 
	 * @param inputsource
	 *            InputSource 환경설정에 사용될 InputSource 인스턴스
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
	 * inputsource 를 통해서 xml config 정보를 읽어들인다.
	 * 
	 * @param inputsource
	 *            InputSource 환경설정에 사용될 InputSource Class
	 * @throws ConfigurationBuildException
	 */
	public void build(InputSource inputsource) throws ConfigurationBuildException {
		createDocument(inputsource);
	}

	/**
	 * 찾고자하는 Element 가 Root Element 인지를 확인
	 * 
	 * @param elementToken
	 *            ElementInfoTokenizer 찾고자하는 element 의 정보를 가지고 있는 ElementInfoTokenizer
	 * @throws NotFoundXmlTagException
	 * @return boolean 찾을려고하는 element 가 root 가 맞으면 true 아니면 false
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
	 * 처음생성된 Configuration 인스턴스를 Hashtable에 추가한다.한번생성한 Configuration 인스턴스들은 Hashtable에 추가된다.
	 * 
	 * @param key
	 *            String Configuration 인스턴스의 키값. elementPathString과 매핑된다.
	 * @param obj
	 *            Object 생성된 Configuration 인스턴스
	 */
	private void addConfigurationCache(String key, Object obj) {
		configurationCache.put(key, obj);
	}

	/**
	 * Hashtable을 검색해서 Configuration을 return한다.
	 * 
	 * @param key
	 *            String Configuration 인스턴스의 키값. elementPathString과 매핑된다.
	 * @return Object Object( Configuration 인스턴스 )
	 */
	private Object searchConfigurationCache(String key) {
		return configurationCache.get(key);
	}

	/**
	 * ConfigurationFactory의 build 메소드가 실행되었는지를 체크한다. build메소드가 수행되어 documentElement 가 생성되었는지 여부를 체크.
	 * 
	 * @return boolean build가 수행되었으면 true, 그렇지 않으면 false
	 */
	private boolean isBuild() {
		if (this.documentElement != null)
			return true;
		else
			return false;
	}

	/**
	 * elementPathString에 해당하는 Configuration 인스턴스를 가져온다.
	 * 
	 * @param elementPathString
	 *            String 가져올 Configuration 인스턴스의 path 문자열
	 * @throws NotFoundXmlTagException
	 * @throws NotBuildException
	 * @throws ConfigurationException
	 * @return Configuration
	 */
	public Configuration getConfiguration(String elementPathString) throws NotFoundXmlTagException, NotBuildException, ConfigurationException {
		return getConfiguration(elementPathString, null);
	}

	/**
	 * 부모에서 자식 Element 의 Configuration 을 하나 얻어온다. 하나이상 존재하면 가장 먼저 발견되는것을 얻어온다.
	 * 
	 * @param childElementName
	 *            String 자식 element 의 이름
	 * @param parentConfiguration
	 *            DefaultConfiguration 부모 Configuration 인스턴스
	 * @throws NotFoundXmlTagException
	 * @throws NotBuildException
	 * @throws ConfigurationException
	 * @return Configuration 자식 Element 의 Configuration 인스턴스
	 */
	public Configuration getConfiguration(String childElementName, DefaultConfiguration parentConfiguration) throws NotFoundXmlTagException, NotBuildException,
			ConfigurationException {
		return (this.getConfigurations(childElementName, parentConfiguration))[0];

	}

	/**
	 * elementPathString에 해당하는 Configuration 인스턴스들을 가져온다.
	 * 
	 * @param elementPathString
	 *            String 가져올 Configuration 인스턴스의 path 문자열
	 * @throws NotFoundXmlTagException
	 * @throws NotBuildException
	 * @throws ConfigurationException
	 * @return Configuration[]
	 */
	public Configuration[] getConfigurations(String elementPathString) throws NotFoundXmlTagException, NotBuildException, ConfigurationException {
		return getConfigurations(elementPathString, null);
	}

	/**
	 * 부모에서 자식 Element 의 Configuration들을 얻어온다.
	 * 
	 * @param childElementName
	 *            String 자식 element 의 이름
	 * @param parentConfiguration
	 *            DefaultConfiguration 보모 Configuration 인스턴스
	 * @throws NotFoundXmlTagException
	 * @throws NotBuildException
	 * @throws ConfigurationException
	 * @return Configuration[] 자식 Element 의 Configuration []
	 */
	public Configuration[] getConfigurations(String childElementName, DefaultConfiguration parentConfiguration) throws NotFoundXmlTagException,
			NotBuildException, ConfigurationException {
		Element currentElement = null;
		String elementIndexStringTemp = null;
		String elementIndexString = null;
		String elementPathString = null;
		int elementDepth = 0;

		// 부모 Element 를 설정한다.
		if (parentConfiguration != null) {
			// 부모가 있을때
			// parentConfiguration 의 Element 에서부터 찾는다.
			currentElement = parentConfiguration.getElement();
			elementPathString = parentConfiguration.getElementPathString() + "." + childElementName;
			elementIndexString = parentConfiguration.getElementIndexString();
			elementIndexStringTemp = elementIndexString;
			elementDepth = parentConfiguration.getElementDepth();
		} else {
			// 보모가 없을때
			// DocumentElement 에서부터 찾는다.
			currentElement = documentElement;
			elementPathString = childElementName;
			int count = (new StringTokenizer(elementPathString, ".")).countTokens() - 2;
			elementIndexString = "0";
			while (count > 0) {
				elementIndexString += ".0";
				count--;
			}
			// elementIndexString 이 "" 이면 최상위 element 는 하나이므로 "0" 을 기본으로 한다.
			elementIndexStringTemp = "";
		}
		// DEBUG
		// System.out.println( "[[ getConfigurations() DEBUG Message Start ]]");
		// System.out.println( "- currentElement : " + currentElement.getTagName());
		// System.out.println( "- elementPathString : " + elementPathString );
		// System.out.println( "- elementIndexString : " + elementIndexString );

		// build 되었는지를 검사한다.
		if (!isBuild())
			throw new NotBuildException("ConfigurationFactory.build() not execute!");

		ElementInfoTokenizer elementToken = new ElementInfoTokenizer(elementPathString, elementIndexString);

		// Configuration Cache Table 을 검색한다.
		// 찾았으면 return 그렇치 않으면 configurations 초기화
		Vector configurations = null;
		if ((configurations = (Vector) searchConfigurationCache(elementPathString + ":" + elementIndexString)) != null)
			return (Configuration[]) configurations.toArray(new Configuration[0]);
		else
			configurations = new Vector();

		Vector elements = new Vector();

		// root element 이름 다음부터 element 를 찾는다. elememntToken 을 next 1회
		if (!isRootElement(elementToken)) {
			// 찾고자하는 Element 가 Root Element 가 아닐때

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

				// xml element 의 상단에서 부터 해당 element 를 찾는다.
				currentNodeList = currentElement.getChildNodes();

				indexCount = 0;
				for (int i = 0; i < currentNodeList.getLength(); i++) {
					// DEBUG
					// System.out.println( "element type : " + currentNodeList.item(i).getNodeType() );
					// System.out.println( "element node name : " + currentNodeList.item(i).getNodeName() );

					if (currentNodeList.item(i).getNodeType() == NODETYPE.ELEMENT
							&& currentNodeList.item(i).getNodeName().equalsIgnoreCase(currentToken.getElementName())) {
						if (elementToken.hasMoreTokens()) {
							// DEBUG
							// System.out.println( "element name : " + currentNodeList.item( i ).getNodeName() );
							if (indexCount == currentToken.getElementIndex()) {
								currentElement = (Element) currentNodeList.item(i);
								i = currentNodeList.getLength();

								// elementIndexString 만들기
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
					throw new NotFoundXmlTagException(elementPathString + ":" + currentToken.getElementName() + "( " + (currentToken.getElementIndex() + 1)
							+ " )");
				}
			}
		} else {
			// 찾고자하는 Element 가 Root Element 일때
			elements.add(documentElement);
		}

		// DEBUG
		// System.out.println( "elementIndexStringTemp : " + elementIndexStringTemp );

		// Element를 obj로 가지고 있는 elements Vector를 Configuration 을 obj로 가지는 Vector로 설정한다.
		setElementToConfiguration(elementPathString, elementIndexStringTemp, elements, configurations);

		// Configuration Cache Table 에 Configurations Object 를 등록한다.
		addConfigurationCache(elementPathString + ":" + elementIndexStringTemp, configurations);

		return (Configuration[]) configurations.toArray(new Configuration[0]);
	}

	/**
	 * elements Vector 에 Element type의 Object 를 Configuration type 의 Object 로 변환시킨다.
	 * 
	 * @param elementPathString
	 *            String 가져올 Configuration 인스턴스의 path 문자열
	 * @param elementIndexString
	 *            String 이전단계까지의 IndexString
	 * @param elements
	 *            Vector Element 를 가지는 Vector
	 * @param configurations
	 *            Vector Configuration 을 가지는 Vector
	 * @throws ConfigurationException
	 */
	private void setElementToConfiguration(final String elementPathString, final String elementIndexString, Vector elements, Vector configurations)
			throws ConfigurationException {
		Class configurationClass = null;
		Constructor configurationConstructor = null;
		// 생성자 파라미터 형의 Class
		Class[] parameterTypes = new Class[] { Element.class, String.class, String.class, String.class };
		Object[] parameterValues = null;
		// elementPathString 에 맞는 Configuration Instance 를 Configurations에 넣는다.
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

			// 생성자 파라미터의 value Object
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
 * 구분자(.) 로 구분되어지는 문자열 elementPath 와 elementIndex 두문자열을 받아
 * ElementInfo 형태로 Token 을 만들어준다.
 * elementPath 와 elementIndex의 같은 위치에 있는 token 이 하나의 ElementInfo 형태의 Token이 된다.
 * 
 * ex)
 *      elementPath = "aaaa.bbbb.cccc";
 *      elementIndex = "0.1.0";
 *      이라면
 *      {aaaa, 0 }, {bbbb, 1}, {cccc, 0} 와 같이 묶여서 하나의 ElementInfo 를이룬다.
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
	 * ElementIndexString 가져오기
	 * 
	 * @return String
	 */
	public String getElementIndexString() {
		return this.elementIndex;
	}

	/**
	 * ElementPathString 가져오기
	 * 
	 * @return String
	 */
	public String getElementPathString() {
		return this.elementPath;
	}

	/**
	 * ElementInfoToken 의 개수를 얻어온다.
	 * 
	 * @return ElementInfoToken의 개수
	 */
	public int countTokens() {
		return pathToken.countTokens();
	}

	/**
	 * 다음 ElementInfoToken 을 가져온다.
	 * 
	 * @return ElementInfo 다음 ElementInfoToken
	 */
	public ElementInfo nextToken() {
		int index = 0;
		if (indexToken.hasMoreTokens()) {
			index = Integer.parseInt(indexToken.nextToken());
		}
		return new ElementInfo(pathToken.nextToken(), index);
	}

	/**
	 * Token 이 남아있는지를 체크한다.
	 * 
	 * @return boolean 남아있으면 true 그렇지 않으면 false
	 */
	public boolean hasMoreTokens() {
		return pathToken.hasMoreTokens();
	}

}

/**
 * elementName 과 elementIndex 한쌍의 멤버로 구성된다. elementName, elementIndex를 가지는 구조체
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
	 * ElementName을 가져온다.
	 * 
	 * @return String
	 */
	public String getElementName() {
		return this.elementName;
	}

	/**
	 * ElementIndex를 가져온다.
	 * 
	 * @return int
	 */
	public int getElementIndex() {
		return this.elementIndex;
	}

}
