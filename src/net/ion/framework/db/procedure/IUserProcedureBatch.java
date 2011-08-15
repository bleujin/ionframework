package net.ion.framework.db.procedure;

import java.io.InputStream;

public interface IUserProcedureBatch extends IUserProcedure, IBatchQueryable {

	public IUserProcedureBatch addParam(int[] param);

	public IUserProcedureBatch addParam(long[] param);

	public IUserProcedureBatch addParam(CharSequence[] param);

	public IUserProcedureBatch addClob(CharSequence[] param);

	public void addParamToArray(int paramindex, int value, int size);

	public void addParamToArray(int paramindex, boolean value, int size);

	public void addParamToArray(int paramindex, CharSequence value, int size);

	public void addClobToArray(int paramindex, CharSequence clobString, int size);

	public void addParam(int paramindex, int[] objs);

	public void addParam(int paramindex, boolean[] objs);

	public void addParam(int paramindex, CharSequence[] strs);

	public void addParam(int paramindex, Object[] objs);

	public void addClob(int paramindex, CharSequence[] clobString);

	public void addBlob(int paramindex, InputStream[] is);

	public void addParameter(int paramindex, Object[] objs, int type);

	public void addBatchParam(int paramindex, boolean val);

	public void addBatchParam(int paramindex, int val);

	public void addBatchParam(int paramindex, CharSequence val);

	public void addBatchParam(int paramindex, Object val);

	public void addBatchClob(int paramindex, CharSequence val);

	public Object[][] getParamsAsArray();

}
