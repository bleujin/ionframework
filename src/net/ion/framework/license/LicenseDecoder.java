package net.ion.framework.license;

/**
 * License Decoder Interface
 * 
 * @author Kim, Sanghoon (wizest@i-on.net)
 * @version 1.0
 */

public interface LicenseDecoder {
	// String encode(final String serialNumber,final License license) throws LicenseException ;
	/**
	 * ����ȭ�� license ��ü�� ��ü�� ȯ����Ų��.
	 * 
	 * @param serialNumber
	 *            String ����ȭ�� �� ����� serial number
	 * @param activationKey
	 *            String ����ȭ�� �� ����� activation key
	 * @throws LicenseException
	 *             license ��ü�� ������ �� ���� ��� ���ܰ� �߻��Ѵ�.
	 * @return License
	 */

	License decode(final String serialNumber, final String activationKey) throws LicenseException;
}
