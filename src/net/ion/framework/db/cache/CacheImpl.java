package net.ion.framework.db.cache;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.ion.framework.db.Rows;
import net.ion.framework.db.cache.CacheConfig.FoundType;
import net.ion.framework.db.procedure.ICompositeable;
import net.ion.framework.db.procedure.IQueryable;
import net.ion.framework.db.procedure.Queryable;
import net.ion.framework.db.rowset.ProxyRows;
import net.ion.framework.util.CaseInsensitiveHashMap;
import net.ion.framework.util.Debug;
import net.ion.framework.util.StringUtil;

import org.apache.commons.collections.map.LRUMap;

public class CacheImpl implements Cache {

	private CacheConfigImpl config;
	private CacheManager cm;
	private CaseInsensitiveHashMap<Map<String, Object>> stores = new CaseInsensitiveHashMap<Map<String, Object>>();
	private ReadsWriteLock myLock = new ReadsWriteLock();

	public CacheImpl(CacheConfigImpl config, List<CacheGroup> groups, CacheManager cm) {
		this.config = config;
		for (CacheGroup group : groups) {
			stores.put(group.getId(), new LRUMap(group.getCacheSize()));
		}
		this.cm = cm;
	}

	public Object findCachedValue(IQueryable query) {
		IQueryable[] subquerys = includeSubQuery(query);
		for (IQueryable squery : subquerys) {
			List<Object> result = new ArrayList<Object>();
			for (CacheGroup group : config.getGroups()) {

				FoundType foundType = group.contains(getProcName(squery));
				if (!foundType.isAdd())
					continue;
				try {
					myLock.readLock();
					if (foundType.isAdd()) {
						Map<String, Object> cached = stores.get(group.getId());
						Object value = cached.get(makeCacheKey(squery));
						if (cached == null)
							Debug.line("MAY BE... CACHE DESTORYED BY OTHER CALL", group.getId(), stores.keySet(), stores.size());
						if (value != null) {
							cm.hitCache(squery);
							if (value instanceof Rows) {
								return ((ProxyRows) value).toClone(query);
								// return new ProxyRows((Rows) value, (Queryable)query) ;
								// return value ;
							} else {
								return value;
							}
						}
					}
				} catch (SQLException ignore) {
					ignore.printStackTrace();
				} catch (InterruptedException ignore) {
					ignore.printStackTrace();
				} finally {
					myLock.readUnLock();
				}
			}

		}
		return null; // not found..
	}

	private String getProcName(IQueryable squery) {
		String result = StringUtil.substringBefore(squery.getProcSQL().toUpperCase(), "(");
		return result;
	}

	private String makeCacheKey(IQueryable query) {
		return StringUtil.deleteWhitespace(query.getProcFullSQL() + query.getPage().toString()).toUpperCase();
	}

	public void putValue(IQueryable query, Object value) {
		IQueryable[] subquerys = includeSubQuery(query);
		for (IQueryable squery : subquerys) {
			for (CacheGroup group : config.getGroups()) {
				FoundType foundType = group.contains(getProcName(squery));
				if (foundType.isNotDefined())
					continue;
				try {
					myLock.writeLock();
					if (foundType.isAdd()) {
						if (value instanceof Rows) {
							final Rows rows = (Rows) value;
							rows.clearQuery();
							stores.get(group.getId()).put(makeCacheKey(squery), ProxyRows.create(rows, (Queryable) squery));
						} else {
							stores.get(group.getId()).put(makeCacheKey(squery), value);
						}
					} else if (foundType.isReset()) {
						stores.get(group.getId()).clear();
					}
				} catch (InterruptedException ignore) {
				} catch (SQLException ignore) {
					ignore.printStackTrace();
				} finally {
					myLock.writeUnLock();
				}
			}
		}
	}

	private IQueryable[] includeSubQuery(IQueryable query) {
		if (query instanceof ICompositeable) {
			ICompositeable upts = (ICompositeable) query;
			IQueryable[] result = new IQueryable[upts.size()];
			for (int i = 0, last = result.length; i < last; i++) {
				result[i] = upts.getQuery(i);
			}
			return result;
		}
		return new IQueryable[] { query };
	}

	public long countUsageMemory() {
		MemoryCounter mc = new MemoryCounter();
		return mc.estimate(stores);
	}

	public void clear() {
		stores.clear();
	}

}

final class ReadsWriteLock {
	private int readingReader = 0;
	private int waitingWriter = 0;
	private int writingWriter = 0;
	private boolean preferWriter = true;

	private Thread owner = null;

	public synchronized void readLock() throws InterruptedException {
		while (writingWriter > 0 || (preferWriter && waitingWriter > 0)) {
			wait();
		}
		readingReader++;
	}

	public synchronized void readUnLock() {
		readingReader--;
		preferWriter = true;
		notifyAll();
	}

	public synchronized void writeLock() throws InterruptedException {
		waitingWriter++;
		Thread me = Thread.currentThread();
		try {
			while ((readingReader > 0 || writingWriter > 0) && owner != me) {
				wait();
			}
		} finally {
			waitingWriter--;
			owner = me;
		}
		writingWriter++;
	}

	public synchronized void writeUnLock() {
		Thread me = Thread.currentThread();
		if (owner != me)
			return;

		owner = null;
		writingWriter--;
		preferWriter = false;
		notifyAll();
	}

}