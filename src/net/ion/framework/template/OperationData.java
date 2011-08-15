package net.ion.framework.template;

import java.util.ArrayList;

/**
 * �ؽ�Ʈ ���ø����� ������ �κи� �и� ǥ���� Ŭ����
 * 
 * @author Kim Sanghoon wizest@i-on.net
 * @version 1.0
 * 
 * @see OperationCode
 */
public class OperationData {
	private ArrayList<Object> data = null;

	public OperationData() {
		this.data = new ArrayList<Object>();
	}

	/**
	 * @param dataObject
	 *            ������
	 * @return dataObject�� ����� address
	 */
	public int add(Object dataObject) {
		data.add(dataObject);
		return getLastAddress();
	}

	/**
	 * @return int ���������� ����� ������ �ּ�
	 */
	public int getLastAddress() {
		return data.size() - 1;
	}

	/**
	 * @return int �� �ּ� ũ��
	 */
	public int getSize() {
		return data.size();
	}

	/**
	 * @return Object[] ����� ������ �迭
	 */
	public final Object[] getDataArray() {
		return data.toArray();
	}

	/**
	 * Ư�� �ּҿ��� �����͸� �����´�.
	 * 
	 * @param address
	 *            int ������ �ּ�
	 * @return Object ������
	 */
	public final Object getDataAt(int address) {
		return data.get(address);
	}

	public String toString() {
		StringBuffer buff = new StringBuffer();
		Object[] os = getDataArray();

		for (int i = 0; i < os.length; ++i) {
			buff.append("Address ");
			buff.append(i);
			buff.append(":  ");
			buff.append(os[i]);
			buff.append("\n");
		}

		return buff.toString();
	}

}
