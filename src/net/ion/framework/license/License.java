package net.ion.framework.license;

import java.io.Serializable;
import java.util.Date;

/**
 * 이 인터페이스를 구현하여 라이센스 클래스를 만든다.<br/>
 * License 객체는 직렬화 되어 serial number, activation key의 쌍으로 변환된다.
 * 
 * @author Kim Sanghoon wizest@i-on.net
 * @version 1.0
 */
public interface License extends Serializable // <-- 이거 라이센스 클래스에서 굉장히 중요한 속성이다!!! (재미로 붙인 속성이 아니란 말이다. 시리얼라이이제이션에 관련된 메소드를 확실하게 구현시켜줘야한다!)
{
	/**
	 * 현재 license가 유효한지 테스트 한다. LicenseEncoder가 License를 decode한 후 invoke된다.
	 * 
	 * @throws LicenseException
	 *             유효한 라이센스가 아닐 경우 발생
	 */
	void validate() throws LicenseException;

	/**
	 * 제품명
	 * 
	 * @return String
	 */
	String getProduct();

	/**
	 * 버전
	 * 
	 * @return String
	 */
	String getVersion();

	/**
	 * 발급 기관
	 * 
	 * @return String
	 */
	String getLicenseFrom();

	/**
	 * 발행 기관
	 * 
	 * @return String
	 */
	String getLicenseTo();

	/**
	 * 발행일
	 * 
	 * @return Date
	 */
	Date getIssueDate();

	/**
	 * 라이센스에 대한 전체적인 설명을 적는다. display용 text로 사용한다.
	 * 
	 * @return String
	 */
	String getLicenseDescription();

	/**
	 * 유효기간 - 시작일
	 * 
	 * @return Date
	 */
	Date getBeginDate();

	/**
	 * 유효기간 - 마감일
	 * 
	 * @return Date
	 */
	Date getEndDate();

	String[] getValueKeys();

	/**
	 * license에 해당 값 존재 여부
	 * 
	 * @param key
	 *            String value의 key
	 * @return boolean
	 */
	boolean hasValue(String key);

	/**
	 * key에 해당하는 value를 얻는다.
	 * 
	 * @param function
	 *            String null if invalid or inaccessable key
	 * @return Object
	 */
	Object getValue(String key);

	/**
	 * getValue()의 다른 형태 - String 형으로 리턴
	 * 
	 * @param key
	 *            String
	 * @return String
	 */
	String getValueAsString(String key);

	/**
	 * getValue()의 다른 형태 - int 형으로 리턴
	 * 
	 * @param key
	 *            String
	 * @return int 0 if error
	 */
	int getValueAsInt(String key);
}
