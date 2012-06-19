package net.ion.framework.license;

import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 미리 마련된 메소드를 사용하여 License interface의 구현을 쉽게한다.
 * 
 * @author Kim, Sanghoon (wizest@i-on.net)
 * @version 1.0
 */
public class LicenseSupport implements Serializable {

	private final String product;
	private final String version;
	private final String licenseFrom;
	private final String licenseTo;
	private final Date beginDate;
	private final Date endDate;
	private final String licenseDescription;
	private final Date issueDate;

	public LicenseSupport(String product, String version, String licenseFrom, String licenseTo, Date beginDate, Date endDate, String licenseDescription,
			Map<String, ?> values) {
		this.product = product;
		this.version = version;
		this.licenseFrom = licenseFrom;
		this.licenseTo = licenseTo;
		this.beginDate = beginDate;
		this.endDate = endDate;
		this.licenseDescription = licenseDescription;

		this.issueDate = new Date();
		if (values == null)
			map = new HashMap<String, Object>();
		else
			map = new HashMap<String, Object>(values);
	}

	public final Date getBeginDate() {
		return beginDate;
	}

	public final Date getEndDate() {
		return endDate;
	}

	public final String getLicenseDescription() {
		return licenseDescription;
	}

	public final String getLicenseFrom() {
		return licenseFrom;
	}

	public final String getLicenseTo() {
		return licenseTo;
	}

	public final String getProduct() {
		return product;
	}

	public final String getVersion() {
		return version;
	}

	public final Date getIssueDate() {
		return issueDate;
	}

	// public abstract void validate() throws LicenseException;

	private HashMap<String, Object> map;

	protected final void putValue(String key, Object o) {
		map.put(key, o);
	}

	public final String[] getValueKeys() {
		return map.keySet().toArray(new String[0]);
	}

	public final boolean hasValue(String key) {
		return map.containsKey(key);
	}

	public final Object getValue(String key) {
		return map.get(key);
	}

	public final int getValueAsInt(String key) {
		Object o = getValue(key);
		if (o == null)
			return 0;
		else if (o instanceof Integer)
			return ((Integer) o).intValue();
		else
			return Integer.valueOf(o.toString()).intValue();
	}

	public final String getValueAsString(String key) {
		Object o = getValue(key);
		return o == null ? null : o.toString();
	}

	public String toString() {
		StringWriter writer = new StringWriter();
		PrintWriter out = new PrintWriter(writer);
		out.println("-. License");
		out.println("product: " + this.product);
		out.println("version: " + this.version);
		out.println("from: " + this.licenseFrom);
		out.println("to: " + this.licenseTo);
		out.println("issue: " + this.issueDate);
		out.println("begin: " + this.beginDate);
		out.println("end: " + this.endDate);
		out.println("description: " + this.licenseDescription);
		out.println("values: ");
		String[] keys = getValueKeys();
		for (int i = 0, length = keys.length; i < length; ++i) {
			out.println("   " + keys[i] + "=" + getValue(keys[i]));
		}
		out.println();
		out.flush();

		return writer.toString();
	}
}
