package net.ion.framework.template.data;

/**
 * DataSpace 에서 data를 읽어오는 객체
 * 
 * @author Kim Sanghoon wizest@i-on.net
 * @version 1.0
 * 
 * @see DataBlock
 * @see DataKeys
 * @see DataReader
 * @see DataSpace
 */

public interface DataReader {
	/**
	 * data keys 의 from에서 to까지 인덱스 에 해당하는 data를 dataBlock로 리턴한다.
	 * 
	 * @param keys
	 * @param from
	 * @param to
	 * @return must not null
	 */
	DataBlock read(DataKeys keys, int from, int to);
}
