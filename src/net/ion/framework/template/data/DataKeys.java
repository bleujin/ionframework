package net.ion.framework.template.data;

/**
 * data key ����
 * 
 * @author Kim Sanghoon wizest@i-on.net
 * @version 1.0
 * 
 * @see DataBlock
 * @see DataKeys
 * @see DataReader
 * @see DataSpace
 */

public interface DataKeys {
	/**
	 * ��ü key ũ��
	 * 
	 * @return int
	 */
	int size();

	/**
	 * ��ü key �������� �ش� key�� �� ��° index���� �˾Ƴ���.<br/>
	 * ���� �＼�� �ǹǷ� ������ ����Ͽ� �� ������ �Ѵ�.
	 * 
	 * @param key
	 * @return -1 if not found
	 */
	int indexOf(Object key);

	/**
	 * ��ü key���� from���� to���� key�� �迭�� ����<br/>
	 * ���Ͻ� �ݵ�� key�� �ش��ϴ� class�� array ���·� �����ؾ��Ѵ�!
	 * 
	 * @param from
	 * @param to
	 * @return key array
	 */
	Object[] indexRange(int from, int to);
}
