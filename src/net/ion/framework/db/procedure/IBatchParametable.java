package net.ion.framework.db.procedure;

public interface IBatchParametable {
	public void addBatchParameter(String name, Object obj, int type);

	public void addBatchParameter(final int paramindex, final Object obj, int type);

	public void addBatchParameterToArray(String name, Object obj, int size, int type);

}
