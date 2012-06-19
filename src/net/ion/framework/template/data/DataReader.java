package net.ion.framework.template.data;

/**
 * DataSpace ���� data�� �о���� ��ü
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
	 * data keys �� from���� to���� �ε��� �� �ش��ϴ� data�� dataBlock�� �����Ѵ�.
	 * 
	 * @param keys
	 * @param from
	 * @param to
	 * @return must not null
	 */
	DataBlock read(DataKeys keys, int from, int to);
}
