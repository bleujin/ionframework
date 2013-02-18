package net.ion.framework.db.cache;

import java.lang.reflect.Proxy;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.commons.collections.map.LRUMap;

import net.ion.framework.db.DBController;
import net.ion.framework.db.IDBController;
import net.ion.framework.db.RepositoryException;
import net.ion.framework.db.Rows;
import net.ion.framework.db.bean.ResultSetHandler;
import net.ion.framework.db.cache.CacheConfig.FoundType;
import net.ion.framework.db.manager.DBManager;
import net.ion.framework.db.procedure.IBatchQueryable;
import net.ion.framework.db.procedure.ICombinedUserProcedures;
import net.ion.framework.db.procedure.ICompositeable;
import net.ion.framework.db.procedure.IParameterQueryable;
import net.ion.framework.db.procedure.IQueryable;
import net.ion.framework.db.procedure.IUserCommand;
import net.ion.framework.db.procedure.IUserCommandBatch;
import net.ion.framework.db.procedure.IUserProcedure;
import net.ion.framework.db.procedure.IUserProcedureBatch;
import net.ion.framework.db.procedure.IUserProcedures;
import net.ion.framework.db.procedure.Queryable;
import net.ion.framework.db.rowset.ProxyRows;
import net.ion.framework.util.CaseInsensitiveHashMap;
import net.ion.framework.util.Debug;
import net.ion.framework.util.MapUtil;
import net.ion.framework.util.StringUtil;

public class ReadCacheDBController implements IDBController {

	private IDBController real;
	private AtomicInteger confirmCount = new AtomicInteger(); 
	private final Map<String, Object> stores ;
	private ReadWriteLock myLock = new ReentrantReadWriteLock();

	private ReadCacheDBController(IDBController dc, int maxSize) {
		this.real = dc;
		stores = new LRUMap(maxSize) ;
	}

	public static ReadCacheDBController create(IDBController dc) throws SQLException{
		return create(dc, 1000) ;
	}

	public static ReadCacheDBController create(IDBController dc, int maxSize) throws SQLException{
		final ReadCacheDBController result = new ReadCacheDBController(dc, maxSize);
		
		return result ;
	} 

	public void destroySelf() throws SQLException {
		stores.clear() ;
	}

	public void initSelf() throws SQLException {
		
	}

	public DBManager getDBManager() {
		return real.getDBManager();
	}

	public DatabaseMetaData getDatabaseMetaData() throws SQLException {
		return real.getDatabaseMetaData();
	}

	public long getModifyCount() {
		return real.getModifyCount();
	}

	public String getName() {
		return "[READCACHE]" + real.getName();
	}

	public void handleServant(long start, long end, IQueryable queryable, int queryCommand) {
		real.handleServant(start, end, queryable, queryCommand);
	}

	
	
	public <T> T execHandlerQuery(IQueryable query, ResultSetHandler<T> handler) {
		try {
			return toReadCacheQuery(IQueryable.class, query).execHandlerQuery(handler);
		} catch (SQLException e) {
			throw RepositoryException.throwIt(e);
		}
	}

	public int execUpdate(IQueryable upt) {
		try {
			return toReadCacheQuery(IQueryable.class, upt).execUpdate();
		} catch (SQLException e) {
			throw RepositoryException.throwIt(e);
		}
	}

	public Rows getRows(IQueryable queryable) {
		try {
			return toReadCacheQuery(IQueryable.class, queryable).execQuery();
		} catch (SQLException e) {
			throw RepositoryException.throwIt(e);
		}
	}

	
	
	
	public Rows getRows(String proc) {
		try {
			return createParameterQuery(proc).execQuery();
		} catch (SQLException e) {
			throw RepositoryException.throwIt(e);
		}
	}

	public IBatchQueryable createBatchParameterQuery(String strSQL) {
		return toReadCacheQuery(IBatchQueryable.class, real.createBatchParameterQuery(strSQL));
	}

	private <T extends IQueryable> T toReadCacheQuery(Class<T> clz, T upt) {
		if (upt instanceof Proxy && Proxy.getInvocationHandler(upt) instanceof ReadCacheQueryProxy) {
			return upt ;
		}
		
		ReadCacheQueryProxy ihan = new ReadCacheQueryProxy(this, upt);
		return (T) Proxy.newProxyInstance(clz.getClassLoader(), new Class[] { clz }, ihan);
	}

	public ICombinedUserProcedures createCombinedUserProcedures(String name) {
		return toReadCacheQuery(ICombinedUserProcedures.class, real.createCombinedUserProcedures(name));
	}

	public IParameterQueryable createParameterQuery(String proc) {
		return toReadCacheQuery(IParameterQueryable.class, real.createParameterQuery(proc));
	}

	public IUserCommand createUserCommand(String proc) {
		return toReadCacheQuery(IUserCommand.class, real.createUserCommand(proc));
	}

	public IUserCommandBatch createUserCommandBatch(String procSQL) {
		return toReadCacheQuery(IUserCommandBatch.class, real.createUserCommandBatch(procSQL));
	}

	public IUserProcedure createUserProcedure(String proc) {
		return toReadCacheQuery(IUserProcedure.class, real.createUserProcedure(proc));
	}

	public IUserProcedureBatch createUserProcedureBatch(String procSQL) {
		return toReadCacheQuery(IUserProcedureBatch.class, real.createUserProcedureBatch(procSQL));
	}

	public IUserProcedures createUserProcedures(String name) {
		return toReadCacheQuery(IUserProcedures.class, real.createUserProcedures(name));
	}

	public Object findCachedResult(IQueryable query) {
		IQueryable[] subquerys = includeSubQuery(query);
		for (IQueryable squery : subquerys) {
			Lock lock = null;
			try {
				lock = myLock.readLock();
				lock.lock() ;
				Object value = stores.get(makeCacheKey(squery));
				if (value != null) {
					hitCache(squery);
					if (value instanceof ProxyRows) {
						return ((ProxyRows) value).toClone(query);
					} else {
						return value;
					}
				}
			} catch (SQLException ignore) {
				ignore.printStackTrace();
			} finally {
				if (lock != null) 
					lock.unlock();
			}

		}
		return null; // not found in cache..
	}

	private void hitCache(IQueryable squery) {
		confirmCount.incrementAndGet() ;
	}
	
	private String makeCacheKey(IQueryable query) {
		return StringUtil.deleteWhitespace(query.getProcFullSQL() + query.getPage().toString()).toUpperCase();
	}

	public void putResult(IQueryable query, Object value) {
		IQueryable[] subquerys = includeSubQuery(query);
		for (IQueryable squery : subquerys) {
			Lock lock = null;
			try {
				lock = myLock.writeLock();
				lock.lock() ;
				if (value instanceof Rows) {
					final Rows rows = (Rows) value;
					rows.clearQuery();
					stores.put(makeCacheKey(squery), ProxyRows.create(rows, (Queryable) squery));
				} else {
					stores.put(makeCacheKey(squery), value);
				}
			} catch (SQLException ignore) {
				ignore.printStackTrace();
			} finally {
				if (lock != null)
					lock.unlock();
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

	// Use For Test
	IDBController getOrginial() {
		return real ;
	}

	// Use For Test
	int hitcount(){
		return confirmCount.intValue() ;
	}


}
