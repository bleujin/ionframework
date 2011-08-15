package net.ion.framework.template;

import java.util.ArrayList;

/**
 * 텍스트 템플릿에서 데이터 부분만 분리 표현한 클래스
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
	 *            데이터
	 * @return dataObject가 저장된 address
	 */
	public int add(Object dataObject) {
		data.add(dataObject);
		return getLastAddress();
	}

	/**
	 * @return int 마지막으로 저장된 데이터 주소
	 */
	public int getLastAddress() {
		return data.size() - 1;
	}

	/**
	 * @return int 총 주소 크기
	 */
	public int getSize() {
		return data.size();
	}

	/**
	 * @return Object[] 저장된 데이터 배열
	 */
	public final Object[] getDataArray() {
		return data.toArray();
	}

	/**
	 * 특정 주소에서 데이터를 가져온다.
	 * 
	 * @param address
	 *            int 가져올 주소
	 * @return Object 데이터
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
