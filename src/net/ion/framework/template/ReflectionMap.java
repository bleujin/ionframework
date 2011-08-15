package net.ion.framework.template;

/**
 * tag attribute의 value를 handler에 reflect할 때 사용된다.
 * 
 * @author Kim Sanghoon wizest@i-on.net
 * @version 1.0
 */

public class ReflectionMap implements Cloneable {
	private String[] names;
	private Object[] values;

	/**
	 * @param names
	 *            String[] attribute names
	 * @param values
	 *            Object[] attribute values
	 */
	public ReflectionMap(String[] names, Object[] values) {
		this.names = names;
		this.values = values;
	}

	/**
	 * @return String[] attribute names
	 */
	public String[] getNames() {
		return this.names;
	}

	/**
	 * @return Object[] attribute values
	 */
	public Object[] getValues() {
		return this.values;
	}

	public String toString() {
		StringBuffer buff = new StringBuffer();

		buff.append("{");
		for (int i = 0; i < names.length; ++i) {
			buff.append(names[i]);
			buff.append("=");
			buff.append(values[i]);

			if (i + 1 < names.length)
				buff.append(",");
		}
		buff.append("}");

		return buff.toString();
	}

	/**
	 * 새로운 attribute를 추가한다.
	 * 
	 * @param name
	 *            String attribute name
	 * @param value
	 *            Object attribute value
	 */
	protected synchronized void append(String name, Object value) {
		String[] nN = new String[names.length + 1];
		Object[] nV = new Object[nN.length];

		nN[0] = name;
		nV[0] = value;

		System.arraycopy(names, 0, nN, 1, names.length);
		System.arraycopy(values, 0, nV, 1, values.length);

		names = nN;
		values = nV;
	}

	protected ReflectionMap copy() {
		try {
			return (ReflectionMap) this.clone();
		} catch (CloneNotSupportedException ex) {
			throw new RuntimeException(ex);
		}
	}
}
