package net.ion.framework.template.data;

import java.util.Arrays;

/**
 * 대용량 데이터를 분할하여 관리한다. <br/>
 * DataSpace 외부에서 보는 입장에서 data의 실 저장위치나 접근 방법에 관계없이 간단하고 쉬운 방법으로 접근하게 한다.<br/>
 * <br/>
 * dataSpace는 data를 block이라 하는 단위 묶음으로 관리하며 각 block은 이것을 여러개의 slot이라는 공간 cache되어 배치한다.
 * 
 * @author Kim Sanghoon wizest@i-on.net
 * @version 1.0
 * 
 * @see DataBlock
 * @see DataKeys
 * @see DataReader
 * @see DataSpace
 */

public class DataSpace<K, D> {
	private DataKeys keys;
	private DataReader reader;

	private int blockSize;
	private Slots<K, D> slots;

	DataSpace() {
	}

	public DataSpace(DataKeys keys, DataReader reader, int blockSize, int slotSize) {
		this.keys = keys;
		this.reader = reader;
		this.blockSize = blockSize;
		this.slots = new Slots<K, D>(slotSize);
	}

	public DataKeys getDataKeys() {
		return this.keys;
	}

	/**
	 * 각 slot에 저장되어 있는 block들을 검색하여 key에 해당하는 값을 가져온다.
	 * 
	 * @param key
	 * @return null if not found
	 */
	public/* synchronized */D find(K key) {
		int keyIndex = keys.indexOf(key);
		if (keyIndex < 0)
			return null;

		int blkIndex = keyIndex / blockSize;
		DataBlock<K, D> blk = slots.getSlot(blkIndex);

		// load data block into data space ; 해당 블럭이 없으면 DB에서 로드한다.
		if (blk == null) {
			if (keyIndex < 0) {
				return null;
			}

			// 범위지정
			int from = keyIndex - (keyIndex % blockSize);
			int to = from + blockSize - 1;
			if (to >= keys.size()) {
				to = keys.size() - 1; // 최대 범위를 넘어가는 것을 제한

			}
			blk = reader.read(keys, from, to);
			slots.feedNewSlot(blkIndex, blk);
		}
		return blk.find(key);
	}
}

class Slots<K, D> {
	private int size;
	private DataBlock<K, D>[] dbSlots;
	private int[] blkIdxSlots;
	private int slotPtr;

	private int cachePtr;
	private DataBlock<K, D> cacheSlot;

	public Slots(int size) {
		this.size = size;
		this.dbSlots = new DataBlock[size];
		this.blkIdxSlots = new int[size];
		Arrays.fill(blkIdxSlots, -Integer.MIN_VALUE); // has no block.

		this.slotPtr = 0;
		this.cachePtr = -Integer.MIN_VALUE;
		cacheSlot = null;
	}

	public synchronized void feedNewSlot(int blkIndex, DataBlock<K, D> blk) {
		dbSlots[slotPtr] = blk;
		blkIdxSlots[slotPtr] = blkIndex;
		slotPtr = ++slotPtr % size;
	}

	/**
	 * @param blkIndex
	 * @return not if not found
	 */
	public synchronized DataBlock<K, D> getSlot(int blkIndex) {
		if (cachePtr == blkIndex) {
			return cacheSlot;
		} else {
			// 해당 slot을 찾는다.
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
