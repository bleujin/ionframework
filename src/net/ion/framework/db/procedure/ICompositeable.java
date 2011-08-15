package net.ion.framework.db.procedure;

public interface ICompositeable {
	public Queryable getQuery(int i);

	public int size();
}
