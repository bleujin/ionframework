package net.ion.framework.db.procedure;

import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import net.ion.framework.db.IDBController;
import net.ion.framework.db.Rows;

import org.apache.commons.lang.ArrayUtils;

public class UserCommandBatch extends UserCommand implements IUserCommandBatch {

	private int idx = 0;

	protected UserCommandBatch(IDBController dc, String strSQL) {
		super(dc, strSQL, QueryType.USER_COMMAND_BATCH);
	}

	public Rows execQuery(int limitRow) {
		throw new java.lang.UnsupportedOperationException("exception.framework.user_command_batch.exec_query.not_supported_method");
	}

	protected Object[][] getParamsAsArray() {
		return params.toArray(new Object[0][]);
	}

	protected Object[] getParam(int index) {
		return (Object[]) params.get(index);
	}

	public int myUpdate(Connection conn) throws SQLException {
		int updateCount;
		try {
			pstmt = conn.prepareStatement(getProcSQL());
			pstmt.clearBatch();

			Object[][] values = getParamsAsArray(); // [a][b] a : rows, b : column
			for (int row = 0, rlast = values.length > 0 ? values[0].length : 0; row < rlast; row++) {
				List<Object> paramList = new ArrayList<Object>();
				for (int col = 0, clast = values.length; col < clast; col++) {
					Object obj = values[col][row];
					paramList.add(obj);
				}
				MSSQLParamUtils.setCommandParam(pstmt, paramList, getTypes());
				pstmt.addBatch();
			}
			int[] count = pstmt.executeBatch();
			updateCount = count.length;

			pstmt.close();
			pstmt = null;

		} catch (SQLException ex) {
			throw ex;
		} finally {
			closeSilence(pstmt);
		}
		return updateCount;
	}

	public IUserCommandBatch addParam(boolean[] param) {
		addParam(idx++, param);
		return this;
	}

	public IUserCommandBatch addParam(int[] param) {
		addParam(idx++, param);
		return this;
	}

	public IUserCommandBatch addParam(long[] param) {
		addParam(idx++, param);
		return this;
	}

	public IUserCommandBatch addParam(CharSequence[] param) {
		addParam(idx++, param);
		return this;
	}

	public IUserCommandBatch addParam(Object[] param) {
		addParam(idx++, param);
		return this;
	}

	public IUserCommandBatch addClob(CharSequence[] param) {
		addClob(idx++, param);
		return this;
	}

	public IUserCommandBatch addBlob(InputStream[] param) {
		addBlob(idx++, param);
		return this;
	}

	public void addClob(int paramindex, CharSequence clobString) {
		this.addParameter(paramindex, new CharSequence[] { clobString }, Types.CLOB);
	}

	public void addClob(int paramindex, CharSequence[] clobString) {
		this.addParameter(paramindex, clobString, Types.CLOB);
	}

	public void addBlob(int paramindex, InputStream is) {
		this.addParameter(paramindex, new InputStream[] { is }, Types.BLOB);
	}

	public void addBlob(int paramindex, InputStream[] is) {
		addParameter(paramindex, is, Types.BLOB);
	}

	public void addParam(int paramindex, int value) {
		this.addParameter(paramindex, new Integer[] { new Integer(value) }, Types.INTEGER);
	}

	public void addParam(int paramindex, boolean value) {
		this.addParameter(paramindex, new Boolean[] { new Boolean(value) }, Types.BOOLEAN);
	}

	public void addParam(int paramindex, long value) {
		this.addParameter(paramindex, new Long[] { new Long(value) }, Types.BIGINT);
	}

	public void addParam(int paramindex, Object value) {
		this.addParameter(paramindex, new Object[] { value }, Types.OTHER);
	}

	public void addParam(int paramindex, CharSequence param) {
		this.addParameter(paramindex, new CharSequence[] { param }, Types.VARCHAR);
	}

	public void addParameter(int paramindex, Object value, int type) {
		this.addParameter(paramindex, new Object[] { value }, type);
	}

	public void addParamToArray(int paramindex, int value, int size) {
		Integer[] values = new Integer[size];
		for (int i = 0; i < values.length; i++) {
			values[i] = new Integer(value);
		}
		addParameter(paramindex, values, Types.INTEGER);
	}

	public void addParamToArray(int paramindex, long value, int size) {
		Long[] values = new Long[size];
		for (int i = 0; i < values.length; i++) {
			values[i] = new Long(value);
		}
		addParameter(paramindex, values, Types.BIGINT);
	}

	public void addParamToArray(int paramindex, boolean value, int size) {
		Boolean[] values = new Boolean[size];
		for (int i = 0; i < values.length; i++) {
			values[i] = new Boolean(value);
		}
		addParameter(paramindex, values, Types.BOOLEAN);
	}

	public void addParamToArray(int paramindex, CharSequence value, int size) {
		CharSequence[] values = new CharSequence[size];
		for (int i = 0; i < values.length; i++) {
			values[i] = value;
		}
		addParameter(paramindex, values, Types.VARCHAR);
	}

	public void addParamToArray(int paramindex, Object value, int size) {
		Object[] values = new Object[size];
		for (int i = 0; i < values.length; i++) {
			values[i] = value;
		}
		addParameter(paramindex, values, Types.OTHER);
	}

	public void addClobToArray(int paramindex, CharSequence clobString, int size) {
		CharSequence[] values = new CharSequence[size];
		for (int i = 0; i < values.length; i++) {
			values[i] = clobString;
		}
		addParameter(paramindex, values, Types.CLOB);
	}

	public void addParameterToArray(int paramindex, Object value, int size, int type) {
		Object[] values = new Object[size];
		for (int i = 0; i < values.length; i++) {
			values[i] = value;
		}
		addParameter(paramindex, values, type);
	}

	public void addParamToArray(String name, boolean value, int size) {
		for (int i = 0; i < size; i++) {
			putNamedParam(name, new ParamValue(new Boolean(value), Types.BOOLEAN));
		}
	}

	public void addParamToArray(String name, int value, int size) {
		for (int i = 0; i < size; i++) {
			putNamedParam(name, new ParamValue(new Integer(value), Types.INTEGER));
		}
	}

	public void addParamToArray(String name, long value, int size) {
		for (int i = 0; i < size; i++) {
			putNamedParam(name, new ParamValue(new Long(value), Types.BIGINT));
		}
	}

	public void addParamToArray(String name, CharSequence value, int size) {
		for (int i = 0; i < size; i++) {
			putNamedParam(name, new ParamValue(value, Types.VARCHAR));
		}
	}

	public void addParamToArray(String name, Object value, int size) {
		for (int i = 0; i < size; i++) {
			putNamedParam(name, new ParamValue(value, Types.OTHER));
		}
	}

	public void addClobToArray(String name, CharSequence clobString, int size) {
		for (int i = 0; i < size; i++) {
			putNamedParam(name, new ParamValue(clobString, Types.CLOB));
		}
	}

	public void addParameterToArray(String name, Object value, int size, int type) {
		for (int i = 0; i < size; i++) {
			putNamedParam(name, new ParamValue(value, type));
		}
	}

	public void addParam(int paramindex, int[] objs) {
		addParameter(paramindex, ArrayUtils.toObject(objs), Types.INTEGER);
	}

	public void addParam(int paramindex, long[] objs) {
		addParameter(paramindex, ArrayUtils.toObject(objs), Types.BIGINT);
	}

	public void addParam(int paramindex, boolean[] objs) {
		addParameter(paramindex, ArrayUtils.toObject(objs), Types.BOOLEAN);
	}

	public void addParam(int paramindex, Object[] objs) {
		addParameter(paramindex, objs, Types.OTHER);
	}

	public void addParam(int paramindex, CharSequence[] strs) {
		addParameter(paramindex, strs, Types.VARCHAR);
	}

	public void addParameter(int paramindex, Object[] objs, int type) {
		checkArraySize(paramindex, objs.length);
		params.add(paramindex, objs);
		types.add(paramindex, new Integer(type));
	}

	public void addParam(String name, int[] objs) {
		addParameter(name, ArrayUtils.toObject(objs), Types.INTEGER);
	}

	public void addParam(String name, long[] objs) {
		addParameter(name, ArrayUtils.toObject(objs), Types.BIGINT);
	}

	public void addParam(String name, boolean[] objs) {
		addParameter(name, ArrayUtils.toObject(objs), Types.BOOLEAN);
	}

	public void addParam(String name, Object[] objs) {
		addParameter(name, objs, Types.OTHER);
	}

	public void addParam(String name, CharSequence[] strs) {
		addParameter(name, strs, Types.VARCHAR);
	}

	public void addClob(String name, CharSequence[] objs) {
		for (int i = 0; i < objs.length; i++) {
			putNamedParam(name, new ParamValue(objs[i], Types.CLOB));
		}
	}

	public void addParameter(String name, Object[] objs, int type) {
		for (int i = 0; i < objs.length; i++) {
			putNamedParam(name, new ParamValue(objs[i], type));
		}
	}

	public void addBatchParam(int paramindex, boolean val) {
		addBatchParameter(paramindex, val ? Boolean.TRUE : Boolean.FALSE, Types.BOOLEAN);
	}

	public void addBatchParam(int paramindex, int val) {
		addBatchParameter(paramindex, new Integer(val), Types.INTEGER);
	}

	public void addBatchParam(int paramindex, long val) {
		addBatchParameter(paramindex, new Long(val), Types.BIGINT);
	}

	public void addBatchParam(int paramindex, CharSequence val) {
		addBatchParameter(paramindex, val, Types.VARCHAR);
	}

	public void addBatchParam(int paramindex, Object val) {
		addBatchParameter(paramindex, val, Types.OTHER);
	}

	public void addBatchClob(int paramindex, CharSequence val) {
		addBatchParameter(paramindex, val, Types.CLOB);
	}

	public void addBatchClob(int paramindex, Reader reader) {
		addBatchParameter(paramindex, reader, Types.CLOB);
	}

	
	public void addBatchBlob(int paramindex, InputStream val) {
		addBatchParameter(paramindex, val, Types.BLOB);
	}

	public void addBatchParam(String name, boolean val) {
		putNamedParam(name, new ParamValue(new Boolean(val), Types.BOOLEAN));
	}

	public void addBatchParam(String name, int val) {
		putNamedParam(name, new ParamValue(new Integer(val), Types.INTEGER));
	}

	public void addBatchParam(String name, long val) {
		putNamedParam(name, new ParamValue(new Long(val), Types.BIGINT));
	}

	public void addBatchParam(String name, CharSequence val) {
		putNamedParam(name, new ParamValue(val, Types.VARCHAR));
	}

	public void addBatchParam(String name, Object val) {
		putNamedParam(name, new ParamValue(val, Types.OTHER));
	}

	public void addBatchClob(String name, CharSequence val) {
		putNamedParam(name, new ParamValue(val, Types.CLOB));
	}

	public void addBatchClob(String name, Reader reader) {
		putNamedParam(name, new ParamValue(reader, Types.CLOB)) ;
	}

	public void addBatchParameter(String name, Object val, int type) {
		putNamedParam(name, new ParamValue(val, type));
	}

	private void putNamedParam(String name, ParamValue value) {
		List<ParamValue> list = (List<ParamValue>) namedParams.get(name);
		if (list == null) {
			list = new ArrayList<ParamValue>();
		}
		list.add(value);

		namedParams.put(name, list);
	}

	@Override 
	protected String transNamedSQL(String _sql) {
		if (namedParams.isEmpty()) {
			return _sql;
		} else if (! params.isEmpty()) {
			return _sql.replaceAll(patternStr, "?");
		} else {

			String baseSQL = _sql;
			// when select ... from table wheren col1 = :name1 and col2 = :name2 and col3 = (:name1 + :name2)
			// list will contain names of: name1, name2, name1, name2
			List list = getSettedParam(baseSQL);
			List<ParamValue>[] tempParams = new ArrayList[list.size()];

			for (int i = 0, last = list.size(); i < last; i++) {
				List<ParamValue> paramValues = (List<ParamValue>) namedParams.get(list.get(i));
				if (paramValues == null)
					throw new IllegalArgumentException("unsetted named param : " + list.get(i));
				tempParams[i] = paramValues;
			}
			// now, we have tempParam list which contains values for the list.

			for (int i = 0; i < tempParams.length; i++) {
				ParamValue[] values = (ParamValue[]) tempParams[i].toArray(new ParamValue[0]);
				Object[] objs = new Object[values.length];
				for (int j = 0; j < values.length; j++) {
					objs[j] = values[j].value;
				}

				this.params.add(objs);
				this.types.add(tempParams[i].get(0).type);
			}
			return baseSQL.replaceAll(patternStr, "?");
		}
	}

	public void addBatchParameter(final int paramindex, final Object obj, int type) {
		if (params.size() <= paramindex) {// first value.. is empty
			params.add(paramindex, new Object[] { obj });
			types.add(paramindex, new Integer(type));
		} else {
			Integer beforeType = (Integer) types.get(paramindex);
			if (beforeType.intValue() != type)
				throw new IllegalArgumentException("Not Match Array Type");

			Object[] src = (Object[]) params.get(paramindex);
			Object[] dst = new Object[src.length + 1];
			System.arraycopy(src, 0, dst, 0, Math.min(src.length, dst.length));
			dst[dst.length - 1] = obj;

			params.remove(paramindex);
			params.add(paramindex, dst);
		}
	}

	public void addBatchParameterToArray(String name, Object val, int size, int type) {
		Object[] values = new Object[size];
		for (int i = 0; i < values.length; i++) {
			values[i] = val;
		}
		addBatchParameter(name, values, type);
	}

	private void checkArraySize(int paramindex, int thisLength) {
		if (paramindex == 0)
			return;
		else {
			Object[] sObjs = (Object[]) params.get(0);
			if (sObjs.length != thisLength) {
				throw new IllegalArgumentException("Not Match Array Size");
			}
		}
	}

	public String toString() {
		StringBuffer result = new StringBuffer("UserCommandBatch : " + getProcSQL() + "\n");

		int paramsize = params.size();
		int maxPrintOut = 10;

		for (int i = 0, last = paramsize > maxPrintOut ? maxPrintOut : paramsize; i < last; i++) {
			Object[] objects = (Object[]) params.get(i);
			if (objects.length > 0) {
				result.append(String.valueOf(i) + " : " + objects[0] + ".....\n");
			}
		}
		if (paramsize > maxPrintOut)
			result.append(".....Total Query Size : " + paramsize);

		return result.toString();
	}

	public void setParamValues(List params, List types) {
		this.params = params;
		this.types = types;
	}

	public Statement getStatement() {
		return pstmt;
	}

	public void clearParam() {
		params.clear();
		types.clear();
	}

	private Reader[] toReaders(String str) {
		return new Reader[] { new StringReader(str) };
	}

	private Reader toReader(String str) {
		return new StringReader(str);
	}

	private Reader[] toReaders(String[] strs) {
		Reader[] readers = new Reader[0];
		for (int i = 0, last = readers.length; i < last; i++) {
			readers[i] = new StringReader(strs[i]);
		}

		return readers;
	}

}
