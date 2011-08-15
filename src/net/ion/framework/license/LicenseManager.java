package net.ion.framework.license;

import net.ion.framework.util.StackTrace;

/**
 * license�� �о� �´�.
 * 
 * @author Kim, Sanghoon (wizest@i-on.net)
 * @version 1.0
 */

public abstract class LicenseManager {
	protected LicenseManager() {
	}

	protected abstract LicenseDecoder getLicenseEncoder();

	protected abstract String[] getLicenseActivationKeys();

	/**
	 * activation key �߿��� serial number�� �ش��ϴ� ù��° ���� �����´�.
	 * 
	 * @param serialNumber
	 *            String
	 * @throws LicenseException
	 * @return License
	 */
	protected final License getLicense(String serialNumber) throws LicenseException {
		// System.out.println("sn:"+serialNumber);
		String[] ls = getLicenseActivationKeys();
		LicenseDecoder encoder = getLicenseEncoder();
		License license = null;
		StringBuffer buf = new StringBuffer();
		for (int i = 0, length = ls.length; i < length; ++i) {
			try {
				license = encoder.decode(serialNumber, ls[i]);
				break;
			} catch (LicenseException ex) {
				buf.append(ex.getMessage() + " ");
				// ������ ���� ���� iteration ���� �ٽ� �õ�.
				// ex.printStackTrace();
			}
		}

		if (license == null)
			throw new LicenseException("not found the license: " + buf.toString());
		else
			license.validate();

		return license;
	}

	/**
	 * serialNumber - activationKey�� ��ȿ���� �׽�Ʈ�Ѵ�.
	 * 
	 * @param serialNumber
	 *            String
	 * @param activationKey
	 *            String
	 * @throws LicenseException
	 */
	protected final void validateLicense(String serialNumber, String activationKey) throws LicenseException {
		LicenseDecoder encoder = getLicenseEncoder();
		License license = null;
		try {
			license = encoder.decode(serialNumber, activationKey);
		} catch (LicenseException ex) {
			System.out.println(StackTrace.trace(ex));
			// throw new LicenseException("invalid activation key.",ex);
			throw new LicenseException(ex.getMessage()); // ,ex);
		}
		license.validate();
	}

	// public static void main2(String[] args) throws Exception {
	// Enumeration ni=NetworkInterface.getNetworkInterfaces();
	// while(ni.hasMoreElements()) {
	//
	// NetworkInterface n=(NetworkInterface)ni.nextElement();
	// System.out.println("--");
	// System.out.println(n.getDisplayName());
	// System.out.println(n.getName());
	//
	// Enumeration ie=n.getInetAddresses();
	// while(ie.hasMoreElements()) {
	// System.out.println(((InetAddress)ie.nextElement()));
	// }
	// }
	// }
}
