package net.ion.framework.db.manager;

import java.sql.Connection;
import java.sql.SQLException;

import net.ion.framework.db.IDBController;
import net.ion.framework.db.cache.Cache;
import net.ion.framework.db.cache.CacheConfig;
import net.ion.framework.db.cache.CacheManager;
import net.ion.framework.db.cache.CacheRepositoryService;
import net.ion.framework.db.procedure.IQueryable;
import net.ion.framework.db.procedure.RepositoryService;
import net.ion.framework.util.Debug;
import net.ion.framework.util.StringUtil;

public class CacheDBManager extends DBManager implements CacheManager {

	public static final int NOT_FOUND_UPDATE = Integer.MIN_VALUE;

	private CacheConfig config;
	private DBManager dbm;
	private Cache cache;
	private long hitCount = 0L;

	public CacheDBManager(CacheConfig config, DBManager dbm) {
		this.config = config;
		this.dbm = dbm;

		this.cache = config.getCache(this);
	}

	public IDBController getDBController() {
		return getOwner();
	}

	@Override
	public Connection getConnection() throws SQLException {
		return dbm.getConnection();
	}

	@Override
	public int getDBManagerType() {
		return dbm.getDBManagerType();
	}

	@Override
	public String getDBType() {
		return dbm.getDBType();
	}

	@Override
	public RepositoryService getRepositoryService() {
		return new CacheRepositoryService(this, dbm);
	}

	@Override
	protected void myDestroyPool() throws Exception {
		dbm.myDestroyPool();
		cache.clear();
	}

	@Override
	protected void myInitPool() throws SQLException {
		dbm.myInitPool();
	}

	protected void heartbeatQuery(IDBController dc) throws SQLException {
		dbm.heartbeatQuery(dc);
	}

	public Object findCachedResult(IQueryable query) {
		return cache.findCachedValue(query);
	}

	public void putResult(IQueryable query, Object result) {
		cache.putValue(query, result);
	}

	public long getHittingCount() {
		return this.hitCount;
	}

	public void hitCache(Object key) {
		this.hitCount++;
		Debug.debug("Hit Cache", this.hitCount, StringUtil.deleteWhitespace(key.toString()));
		// , cache.countUsageMemory()
	}
}