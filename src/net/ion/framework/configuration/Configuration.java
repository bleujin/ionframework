package net.ion.framework.configuration;

/**
 * 환경변수들을 가져오고 저장하는 기본적인 접근 메소드들을 정의하는 Interface
 *
 * @author Choi sei hwan <a href="mailto:sehan@i-on.net">Choi sei hwan</a>
 * @version 1.0
 */

public interface Configuration
{
    /**
     * 현재 Configuration 에서 childElementName이름의 자식 Element 를 하나가져온다.
     * childElementName을 가지는 자식이 하나이상일경우에는 가장처음에 발견되는 자식만을 가져온다.
     *
     * @param childElementName String   자식 Element 의 이름
     * @throws NotFoundXmlTagException  childElementName이 존재하지 않는 경우 발생
     * @throws NotBuildException    환경설정파일(.xml)이 아직 Build 되어있지 않은 경우 발생
     * @throws ConfigurationException   환경설정파일 처리시 문제가 발생하였을 경우 발생
     * @return Configuration    해당 Child Element의 Configuration 인스턴스
     */
    Configuration getChild( String childElementName ) throws NotFoundXmlTagException, NotBuildException, ConfigurationException;

    /**
     * 현재 Configuration 에서 childElementName이름의 자식 Element 를 모두 가져온다.
     *
     * @param childElementName String   자식 Element 의 이름
     * @throws NotFoundXmlTagException  childElementName이 존재하지 않는 경우 발생
     * @throws NotBuildException    환경설정파일(.xml)이 아직 Build 되어있지 않은 경우 발생
     * @throws ConfigurationException   환경설정파일 처리시 문제가 발생하였을 경우 발생
     * @return Configuration[]
     */
    Configuration[] getChildren( String childElementName ) throws NotFoundXmlTagException, NotBuildException, ConfigurationException;

//    Configuration[] getChildren();


//    String getLocation();

    /**
     * 현재 Configuration 의 Tag 이름을 가져온다.
     *
     * @return 현재 Tag 의 이름
     */
    String getTagName();

    /**
     * Attribute 값을 가져온다.
     *
     * @param attributeName String  Attribute 이름
     * @return String   Attribute 값
     */
    String getAttribute( String attributeName );

    /**
     * Attribute 값을 가져온다.
     *
     * @param attributeName String  Attribute 이름
     * @param defaultValue String   Attribute 값이 설정되어 있지 않은 경우 지정되는 기본값
     * @return String   Attribute 값
     */
    String getAttribute( String attributeName, String defaultValue );

    /**
     * Attribute 값을 int형으로 casting 하여 가져온다.
     *
     * @param attributeName String  Attribute 이름
     * @throws ConfigurationException   Attribute 값이 설정되지 않았거나, 존재하지 않을경우,
     * 또는 값이 int 형으로 casting이 불가할 경우 발생
     * @return int  Attribute 값
     */
    int getAttributeAsInt( String attributeName ) throws ConfigurationException;

    /**
     * Attribute 값을 int형으로 casting 하여 가져온다.
     *
     * @param attributeName String  Attribute 이름
     * @param defaultValue int  Attribute 값이 설정되어 있지 않은 경우 지정되는 기본값
     * @return int  Attribute 값
     */
    int getAttributeAsInt( String attributeName, int defaultValue );

    /**
     * Attribute 값을 float형으로 casting 하여 가져온다.
     *
     * @param attributeName String  Attribute 이름
     * @throws ConfigurationException   Attribute 값이 설정되지 않았거나, 존재하지 않을경우,
     * 또는 값이 float 형으로 casting이 불가할 경우 발생
     * @return float    Attribute 값
     */
    float getAttributeAsFloat( String attributeName ) throws ConfigurationException;

    /**
     * Attribute 값을 float 형으로 casting 하여 가져온다.
     *
     * @param attributeName String  Attribute 이름
     * @param defaultValue float    Attribute 값이 설정되어 있지 않은 경우 지정되는 기본값
     * @return float    Attribute 값
     */
    float getAttributeAsFloat( String attributeName, float defaultValue );

    /**
     * Attribute 값을 Boolean 형으로 casting 하여 가져온다.
     *
     * @param attributeName String  Attribute 이름
     * @throws ConfigurationException   Attribute 값이 설정되지 않았거나, 존재하지 않을경우 발생
     * @return boolean  Attribute 값
     */
    boolean getAttributeAsBoolean( String attributeName ) throws ConfigurationException;

    /**
     * Attribute 값을 Boolean 형으로 casting 하여 가져온다.
     *
     * @param attributeName String  Attribute 이름
     * @param defaultValue boolean  Attribute 값이 설정되어 있지 않은 경우 지정되는 기본값
     * @return boolean Attribute 값
     */
    boolean getAttributeAsBoolean( String attributeName, boolean defalutValue );

    /**
     * 태그의 값을 가져온다.
     *
     * @return String   태그 값
     */
    String getValue();

    String getValue( String defaultValue );

    /**
     * 태그의 값을 int형으로 casting 하여 가져온다.
     *
     * @throws ConfigurationException   값이 설정되어 있지 않은 경우, 또는 값이 int 형으로 casting이 불가할 경우 발생
     * @return int  태그 값
     */
    int getValueAsInt() throws ConfigurationException;

    /**
     * 태그의 값을 int형으로 casting 하여 가져온다.
     *
     * @param defaultValue int  값이 설정되어 있지 않은 경우 지정되는 기본값
     * @return int  태그 값
     */
    int getValueAsInt( int defaultValue );

    /**
     * 태그의 값을 float형으로 casting 하여 가져온다.
     *
     * @throws ConfigurationException    값이 설정되어 있지 않은 경우, 또는 값이 float 형으로 casting이 불가할 경우 발생
     * @return float    태그 값
     */
    float getValueAsFloat() throws ConfigurationException;

    /**
     * 태그의 값을 float형으로 casting 하여 가져온다.
     *
     * @param defaultValue float    값이 설정되어 있지 않은 경우 지정되는 기본값
     * @return float    태그 값
     */
    float getValueAsFloat( float defaultValue );

    /**
     * 태그의 값을 boolean형으로 casting 하여 가져온다.
     *
     * @throws ConfigurationException   값이 설정되어 있지 않은 경우 발생
     * @return boolean  태그 값
     */
    boolean getValueAsBoolean() throws ConfigurationException;

    /**
     * 태그의 값을 boolean형으로 casting 하여 가져온다.
     *
     * @param defaultValue boolean  값이 설정되어 있지 않은 경우 지정되는 기본값
     * @return boolean  태그 값
     */
    boolean getValueAsBoolean( boolean defaultValue );

    /**
     * 현재 Tag 에 attributeName을 가지는 attribute가 존재하면 값을 attributeValue 로 설정하고
     * 존재하지 않으면 attribute 를 추가한다.
     *
     * @param attributeName
     * @param attributeValue
     * @throws ConfigurationException attributeName 에 illegal character가 포함되어있을때
     */
    void setAttribute( String attributeName, String attributeValue ) throws ConfigurationException;


	// void setValue( String value );

	String getXML() throws ConfigurationException;
}
