package net.ion.framework.license;

import java.io.Serializable;
import java.util.Date;

/**
 * �� �������̽��� �����Ͽ� ���̼��� Ŭ������ �����.<br/>
 * License ��ü�� ����ȭ �Ǿ� serial number, activation key�� ������ ��ȯ�ȴ�.
 * 
 * @author Kim Sanghoon wizest@i-on.net
 * @version 1.0
 */
public interface License extends Serializable // <-- �̰� ���̼��� Ŭ�������� ������ �߿��� �Ӽ��̴�!!! (��̷� ���� �Ӽ��� �ƴ϶� ���̴�. �ø�����������̼ǿ� ���õ� �޼ҵ带 Ȯ���ϰ� ������������Ѵ�!)
{
	/**
	 * ���� license�� ��ȿ���� �׽�Ʈ �Ѵ�. LicenseEncoder�� License�� decode�� �� invoke�ȴ�.
	 * 
	 * @throws LicenseException
	 *             ��ȿ�� ���̼����� �ƴ� ��� �߻�
	 */
	void validate() throws LicenseException;

	/**
	 * ��ǰ��
	 * 
	 * @return String
	 */
	String getProduct();

	/**
	 * ����
	 * 
	 * @return String
	 */
	String getVersion();

	/**
	 * �߱� ���
	 * 
	 * @return String
	 */
	String getLicenseFrom();

	/**
	 * ���� ���
	 * 
	 * @return String
	 */
	String getLicenseTo();

	/**
	 * ������
	 * 
	 * @return Date
	 */
	Date getIssueDate();

	/**
	 * ���̼����� ���� ��ü���� ������ ���´�. display�� text�� ����Ѵ�.
	 * 
	 * @return String
	 */
	String getLicenseDescription();

	/**
	 * ��ȿ�Ⱓ - ������
	 * 
	 * @return Date
	 */
	Date getBeginDate();

	/**
	 * ��ȿ�Ⱓ - ������
	 * 
	 * @return Date
	 */
	Date getEndDate();

	String[] getValueKeys();

	/**
	 * license�� �ش� �� ���� ����
	 * 
	 * @param key
	 *            String value�� key
	 * @return boolean
	 */
	boolean hasValue(String key);

	/**
	 * key�� �ش��ϴ� value�� ��´�.
	 * 
	 * @param function
	 *            String null if invalid or inaccessable key
	 * @return Object
	 */
	Object getValue(String key);

	/**
	 * getValue()�� �ٸ� ���� - String ������ ����
	 * 
	 * @param key
	 *            String
	 * @return String
	 */
	String getValueAsString(String key);

	/**
	 * getValue()�� �ٸ� ���� - int ������ ����
	 * 
	 * @param key
	 *            String
	 * @return int 0 if error
	 */
	int getValueAsInt(String key);
}
