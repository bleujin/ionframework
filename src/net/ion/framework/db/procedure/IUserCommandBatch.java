package net.ion.framework.db.procedure;

import java.io.InputStream;
import java.io.Reader;

public interface IUserCommandBatch extends IUserCommand, IBatchQueryable {

	public IUserCommandBatch addParam(boolean[] param);

	public IUserCommandBatch addParam(int[] param);

	public IUserCommandBatch addParam(long[] param);

	public IUserCommandBatch addParam(CharSequence[] param);

	public IUserCommandBatch addParam(Object[] param);

	public IUserCommandBatch addClob(CharSequence[] param);

	public IUserCommandBatch addBlob(InputStream[] param);

	public void addParam(int paramindex, boolean[] objs);

	public void addParam(int paramindex, int[] objs);

	public void addParam(int paramindex, long[] objs);

	public void addParam(int paramindex, CharSequence[] strs);

	public void addParam(int paramindex, Object[] objs);

	public void addClob(int paramindex, CharSequence[] clobString);

	public void addBlob(int paramindex, InputStream[] is);

	public void addParameter(int paramindex, Object[] objs, int type);

	public void addParamToArray(int paramindex, boolean value, int size);

	public void addParamToArray(int paramindex, int value, int size);

	public void addParamToArray(int paramindex, long value, int size);

	public void addParamToArray(int paramindex, CharSequence value, int size);

	public void addParamToArray(int paramindex, Object value, int size);

	public void addClobToArray(int paramindex, CharSequence clobString, int size);

	// InputStream은 DeepCopy가 아니면 ToArray가 의미없음.
	public void addParameterToArray(int paramindex, Object value, int size, int type);

	public void addParamToArray(String name, boolean value, int size);

	public void addParamToArray(String name, int value, int size);

	public void addParamToArray(String name, long value, int size);

	public void addParamToArray(String name, CharSequence value, int size);

	public void addParamToArray(String name, Object value, int size);

	public void addClobToArray(String name, CharSequence clobString, int size);

	public void addParameterToArray(String name, Object value, int size, int type);

	public void addParam(String name, boolean[] objs);

	public void addParam(String name, int[] objs);

	public void addParam(String name, long[] objs);

	public void addParam(String name, CharSequence[] strs);

	public void addParam(String name, Object[] objs);

	public void addClob(String name, CharSequence[] strs);

	public void addParameter(String name, Object[] objs, int type);

	public void addBatchParam(String name, boolean val);

	public void addBatchParam(String name, int val);

	public void addBatchParam(String name, long val);

	public void addBatchParam(String name, CharSequence val);

	public void addBatchParam(String name, Object val);

	public void addBatchClob(String name, CharSequence val);

	public void addBatchClob(String name, Reader reader);

	public void addBatchParam(int paramindex, boolean val);

	public void addBatchParam(int paramindex, int val);

	public void addBatchParam(int paramindex, long val);

	public void addBatchParam(int paramindex, CharSequence val);

	public void addBatchParam(int paramindex, Object val);

	public void addBatchClob(int paramindex, CharSequence val);

	public void addBatchClob(int paramindex, Reader reader);

	public void addBatchBlob(int paramindex, InputStream val);

}
