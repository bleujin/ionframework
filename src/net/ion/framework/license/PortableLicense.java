package net.ion.framework.license;

import java.util.Date;

import net.ion.framework.util.Debug;
import net.ion.framework.util.SerializedString;
import net.ion.framework.util.SerializedStringException;

/**
 * ������ license ��ü�� serialization ���� �ٽ� deserialization �ϱ� ���ؼ� ��ü�� ������ license class�� �ʿ�� �Ѵ�. Ŭ���̾�Ʈ�� ���� ��� license class�� ������ �ִ��� ������� �����Ӱ� license class�� �����Ϸ��� license class ��ü�� serialization �Ͽ� ������ ����������ϴµ� PortableLicense�� �̷��� ������ �Ѵ�.
 * 
 * Target License�� �� Ŭ������ �����ϰ� serialization �ع����� ������ Target License class �� class loading ó���� ���� �ʰ�(�� Ŭ���� ��ü�� ó������) License�� ���� ��ǻ�Ϳ��� deserializing�� �� �ִ�.
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
	 *            License warpping�� license ��ü
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
