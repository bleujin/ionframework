package net.ion.framework.license;

import java.util.Date;

import net.ion.framework.util.Debug;
import net.ion.framework.util.SerializedString;
import net.ion.framework.util.SerializedStringException;

/**
 * 임의의 license 객체를 serialization 한후 다시 deserialization 하기 위해선 객체를 구성한 license class를 필요로 한다. 클라이언트가 현재 어떠한 license class를 가지고 있는지 관계없이 자유롭게 license class를 구현하려면 license class 자체를 serialization 하여 별도로 제공해줘야하는데 PortableLicense가 이러한 역할을 한다.
 * 
 * Target License를 이 클래스로 포장하고 serialization 해버리면 별도의 Target License class 의 class loading 처리를 하지 않고(이 클래스 자체가 처리해줌) License를 받은 컴퓨터에서 deserializing할 수 있다.
 * 
 * @author Kim, Sanghoon (wizest@i-on.net)
 * @version 1.0
 */
public final class PortableLicense implements License {
	private final String innerLicenseName;
	private final String innerLicenseCls;
	private final String innerLicenseObj;

	private transient License innerLicense;

	/**
	 * @param targetLicense
	 *            License warpping할 license 객체
	 * @throws LicenseException
	 */
	public PortableLicense(License targetLicense) throws LicenseException {
		try {
			// super
			Class<?> from = targetLicense.getClass();
			innerLicenseName = from.getName();
			
			innerLicenseCls = getLicenseCls(from);
			Debug.warn(innerLicenseCls != null) ;
			
			innerLicenseObj = SerializedString.serialize(targetLicense);
		} catch (Exception ex) {
			throw new LicenseException("could not create a license", ex);
		}
	}

	private String getLicenseCls(Class<?> from) {
		return SerializedString.saveResource(from.getName(), Thread.currentThread().getContextClassLoader());
	}

	private License getInnerLicense() {
		if (innerLicense == null) {

			Class<?> cls = SerializedString.loadClass(innerLicenseName, innerLicenseCls);
			// deserialize licenseObj
			try {
				this.innerLicense = (License) SerializedString.deserialize(cls, innerLicenseObj, false);
			} catch (SerializedStringException ex) {
				throw new RuntimeException("could not deserialize a license", ex);
			}
		}
		return innerLicense;
	}

	public Date getBeginDate() {
		return getInnerLicense().getBeginDate();
	}

	public Date getEndDate() {
		return getInnerLicense().getEndDate();
	}

	public Date getIssueDate() {
		return getInnerLicense().getIssueDate();
	}

	public String getLicenseDescription() {
		return getInnerLicense().getLicenseDescription();
	}

	public String getLicenseFrom() {
		return getInnerLicense().getLicenseFrom();
	}

	public String getLicenseTo() {
		return getInnerLicense().getLicenseTo();
	}

	public String getProduct() {
		return getInnerLicense().getProduct();
	}

	public Object getValue(String key) {
		return getInnerLicense().getValue(key);
	}

	public int getValueAsInt(String key) {
		return getInnerLicense().getValueAsInt(key);
	}

	public String getValueAsString(String key) {
		return getInnerLicense().getValueAsString(key);
	}

	public String[] getValueKeys() {
		return getInnerLicense().getValueKeys();
	}

	public String getVersion() {
		return getInnerLicense().getVersion();
	}

	public boolean hasValue(String key) {
		return getInnerLicense().hasValue(key);
	}

	public void validate() throws LicenseException {
		getInnerLicense().validate();
	}

	public String toString() {
		return getInnerLicense().toString();
	}
}
