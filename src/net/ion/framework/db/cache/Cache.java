package net.ion.framework.db.cache;

import net.ion.framework.db.procedure.IQueryable;

public interface Cache {

	public long countUsageMemory();

	public Object findCachedValue(IQueryable query);

	public void putValue(IQueryable query, Object result);

	public void clear();
}
