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
	 * 직렬화된 license 객체를 객체로 환원시킨다.
	 * 
	 * @param serialNumber
	 *            String 직렬화할 때 사용한 serial number
	 * @param activationKey
	 *            String 직렬화할 때 사용한 activation key
	 * @throws LicenseException
	 *             license 객체를 복원할 수 없을 경우 예외가 발생한다.
	 * @return License
	 */

	License decode(final String serialNumber, final String activationKey) throws LicenseException;
}
