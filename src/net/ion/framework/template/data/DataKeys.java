package net.ion.framework.template.data;

/**
 * data key 묶음
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
	 * 전체 key 크기
	 * 
	 * @return int
	 */
	int size();

	/**
	 * 전체 key 묶음에서 해당 key가 몇 번째 index인지 알아낸다.<br/>
	 * 자주 억세스 되므로 성능을 고려하여 잘 만들어야 한다.
	 * 
	 * @param key
	 * @return -1 if not found
	 */
	int indexOf(Object key);

	/**
	 * 전체 key에서 from에서 to까지 key를 배열로 리턴<br/>
	 * 리턴시 반드시 key에 해당하는 class의 array 형태로 리턴해야한다!
	 * 
	 * @param from
	 * @param to
	 * @return key array
	 */
	Object[] indexRange(int from, int to);
}
