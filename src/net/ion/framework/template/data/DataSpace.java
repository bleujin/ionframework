package net.ion.framework.template.data;

import java.util.Arrays;

/**
 * ��뷮 �����͸� �����Ͽ� �����Ѵ�. <br/>
 * DataSpace �ܺο��� ���� ���忡�� data�� �� ������ġ�� ���� ����� ������� �����ϰ� ���� ������� �����ϰ� �Ѵ�.<br/>
 * <br/>
 * dataSpace�� data�� block�̶� �ϴ� ���� �������� �����ϸ� �� block�� �̰��� �������� slot�̶�� ���� cache�Ǿ� ��ġ�Ѵ�.
 * 
 * @author Kim Sanghoon wizest@i-on.net
 * @version 1.0
 * 
 * @see DataBlock
 * @see DataKeys
 * @see DataReader
 * @see DataSpace
 */

public class DataSpace {
	private DataKeys keys;
	private DataReader reader;

	private int blockSize;
	private Slots slots;

	DataSpace() {
	}

	public DataSpace(DataKeys keys, DataReader reader, int blockSize, int slotSize) {
		this.keys = keys;
		this.reader = reader;
		this.blockSize = blockSize;
		this.slots = new Slots(slotSize);
	}

	public DataKeys getDataKeys() {
		return this.keys;
	}

	/**
	 * �� slot�� ����Ǿ� �ִ� block���� �˻��Ͽ� key�� �ش��ϴ� ���� �����´�.
	 * 
	 * @param key
	 * @return null if not found
	 */
	public/* synchronized */Object find(Object key) {
		int keyIndex = keys.indexOf(key);
		if (keyIndex < 0)
			return null;

		int blkIndex = keyIndex / blockSize;
		DataBlock blk = slots.getSlot(blkIndex);

		// load data block into data space ; �ش� ���� ������ DB���� �ε��Ѵ�.
		if (blk == null) {
			if (keyIndex < 0) {
				return null;
			}

			// ��������
			int from = keyIndex - (keyIndex % blockSize);
			int to = from + blockSize - 1;
			if (to >= keys.size()) {
				to = keys.size() - 1; // �ִ� ������ �Ѿ�� ���� ����

			}
			blk = reader.read(keys, from, to);
			slots.feedNewSlot(blkIndex, blk);
		}
		return blk.find(key);
	}
}

class Slots {
	private int size;
	private DataBlock[] dbSlots;
	private int[] blkIdxSlots;
	private int slotPtr;

	private int cachePtr;
	private DataBlock cacheSlot;

	public Slots(int size) {
		this.size = size;
		this.dbSlots = new DataBlock[size];
		this.blkIdxSlots = new int[size];
		Arrays.fill(blkIdxSlots, -Integer.MIN_VALUE); // has no block.

		this.slotPtr = 0;
		this.cachePtr = -Integer.MIN_VALUE;
		cacheSlot = null;
	}

	public synchronized void feedNewSlot(int blkIndex, DataBlock blk) {
		dbSlots[slotPtr] = blk;
		blkIdxSlots[slotPtr] = blkIndex;
		slotPtr = ++slotPtr % size;
	}

	/**
	 * @param blkIndex
	 * @return not if not found
	 */
	public synchronized DataBlock getSlot(int blkIndex) {
		if (cachePtr == blkIndex) {
			return cacheSlot;
		} else {
			// �ش� slot�� ã�´�.
			for (int i = 0; i < this.size; ++i) {
				if (blkIdxSlots[i] == blkIndex) {
					cachePtr = blkIndex;
					cacheSlot = dbSlots[i];
					return cacheSlot;
				}
			}
			return null;
		}
	}
}
