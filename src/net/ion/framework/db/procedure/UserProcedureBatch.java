package net.ion.framework.db.procedure;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import net.ion.framework.db.IDBController;
import net.ion.framework.db.Rows;
import net.ion.framework.db.bean.ResultSetHandler;
import net.ion.framework.util.StringUtil;

import org.apache.commons.lang.ArrayUtils;

public abstract class UserProcedureBatch extends UserProcedure implements IUserProcedureBatch {

	private int idx = 0;

	UserProcedureBatch(IDBController dc, String procSQL) {
		super(dc, procSQL, QueryType.USER_PROCEDURE_BATCH);
	}

	@Override
	public Rows myQuery(Connection conn) throws SQLException {
		throw new SQLException("Curren Type is Batch, Select Method not yet implemented.");
	}

	@Override
	public Object myHandlerQuery(Connection conn, ResultSetHandler handler) throws SQLException {
		throw new SQLException("Curren Type is Batch, Select Method not yet implemented.");
	}

	public Object execHandlerQuery(ResultSetHandler handler) throws SQLException {
		throw new UnsupportedOperationException();
	}

	public IUserProcedureBatch addParam(int param) {
		addParam(idx++, param);
		return this;
	}

	public IUserProcedureBatch addParam(CharSequence param) {
		addParam(idx++, param);
		return this;
	}

	public IUserProcedureBatch addParam(Object param) {
		if (param.getClass().isArray()) {
			addParam(idx++, (Object[]) param);
		} else {
			addParam(idx++, param);
		}
		return this;
	}

	public IUserProcedureBatch addClob(CharSequence param) {
		this.addParameter(idx++, new CharSequence[] { param }, Types.CLOB);
		return this;
	}

	public IUserProcedureBatch addParam(int[] param) {
		addParam(idx++, param);
		return this;
	}

	public IUserProcedureBatch addParam(long[] param) {
		this.addParam(idx++, param);
		return this;
	}

	public IUserProcedureBatch addParam(CharSequence[] param) {
		addParam(idx++, param);
		return this;
	}

	public IUserProcedureBatch addParam(Object[] param) {
		this.addParam(idx++, param);
		return this;
	}

	public IUserProcedureBatch addClob(CharSequence[] param) {
		this.addParameter(idx++, param, Types.CLOB);
		return this;
	}

	public void addParam(int paramindex, int value) {
		this.addParameter(paramindex, new Integer[] { value }, Types.INTEGER);
	}

	public void addParam(int paramindex, boolean value) {
		this.addParameter(paramindex, new Boolean[] { value }, Types.BOOLEAN);
	}

	public void addParam(int paramindex, Object value) {
		this.addParameter(paramindex, new Object[] { value }, Types.OTHER);
	}

	public void addParam(int paramindex, CharSequence param) {
		this.addParameter(paramindex, new CharSequence[] { param }, Types.VARCHAR);
	}

	public void addParam(int paramindex, Object value, Types type) {
		this.addParameter(paramindex, new Object[] { value }, Types.OTHER);
	}

	public void addClob(int paramindex, CharSequence clobString) {
		this.addParameter(paramindex, new CharSequence[] { clobString }, Types.CLOB);
	}

	public void addClob(int paramindex, CharSequence[] clobString) {
		this.addParameter(paramindex, clobString, Types.CLOB);
	}

	public void addBlob(int paramindex, InputStream is) {
		addParameter(paramindex, new InputStream[] { is }, Types.BLOB);
	}

	public void addParamToArray(int paramindex, boolean value, int size) {
		Boolean[] values = new Boolean[size];
		for (int i = 0; i < values.length; i++) {
			values[i] = new Boolean(value);
		}
		addParameter(paramindex, values, Types.BOOLEAN);
	}

	public void addParamToArray(int paramindex, int value, int size) {
		Integer[] values = new Integer[size];
		for (int i = 0; i < values.length; i++) {
			values[i] = new Integer(value);
		}
		addParameter(paramindex, values, Types.INTEGER);
	}

	public void addParamToArray(int paramindex, CharSequence value, int size) {
		CharSequence[] values = new CharSequence[size];
		for (int i = 0; i < values.length; i++) {
			values[i] = value;
		}
		addParameter(paramindex, values, Types.VARCHAR);
	}

	public void addClobToArray(int paramindex, CharSequence clobString, int size) {
		CharSequence[] values = new CharSequence[size];
		for (int i = 0; i < values.length; i++) {
			values[i] = clobString;
		}
		addParameter(paramindex, values, Types.CLOB);
	}

	public void addBlob(int paramindex, InputStream[] is) {
		addParameter(paramindex, is, Types.BLOB);
	}

	public void addParam(int paramindex, boolean[] objs) {
		addParameter(paramindex, ArrayUtils.toObject(objs), Types.BOOLEAN);
	}

	public void addParam(int paramindex, int[] objs) {
		addParameter(paramindex, ArrayUtils.toObject(objs), Types.INTEGER);
	}

	public void addParam(int paramindex, long[] objs) {
		addParameter(paramindex, ArrayUtils.toObject(objs), Types.BIGINT);
	}

	public void addParam(int paramindex, Object[] objs) {
		addParameter(paramindex, objs, Types.OTHER);
	}

	public void addParam(int paramindex, CharSequence[] strs) {
		addParameter(paramindex, strs, Types.VARCHAR);
	}

	public void addParameter(int paramindex, Object[] objs, int type) {
		checkArraySize(paramindex, objs.length);
		if ((type == Types.OTHER || type == Types.VARCHAR) && objs instanceof IStringObject[]) {
			List<String> temp = new ArrayList<String>();
			for (IStringObject obj : (IStringObject[]) objs) {
				temp.add(obj.getString());
			}
			addParameter(paramindex, temp.toArray(new String[0]), Types.VARCHAR);
		} else {
			params.add(paramindex, objs);
			types.add(paramindex, new Integer(type));
		}
	}

	public void addBatchParam(int paramindex, CharSequence val) {
		addBatchParameter(paramindex, val, Types.VARCHAR);
	}

	public void addBatchParam(int paramindex, boolean val) {
		addBatchParameter(paramindex, val ? Boolean.TRUE : Boolean.FALSE, Types.BOOLEAN);
	}

	public void addBatchParam(int paramindex, int val) {
		addBatchParameter(paramindex, new Integer(val), Types.INTEGER);
	}

	public void addBatchParam(int paramindex, Object val) {
		addBatchParameter(paramindex, val, Types.OTHER);
	}

	public void addBatchClob(int paramindex, CharSequence val) {
		addBatchParameter(paramindex, val, Types.CLOB);
	}

	public void addBatchParameter(final int paramindex, final Object obj, int type) {
		if ((type == Types.OTHER || type == Types.VARCHAR) && obj instanceof IStringObject[]) {
			List<String> temp = new ArrayList<String>();
			for (IStringObject so : (IStringObject[]) obj) {
				temp.add(so.getString());
			}
			addBatchParameter(paramindex, temp.toArray(new String[0]), Types.VARCHAR);
			return;
		}

		if ((type == Types.OTHER || type == Types.VARCHAR) && (obj instanceof IStringObject)) {
			addBatchParameter(paramindex, ((IStringObject) obj).getString(), Types.VARCHAR);
			return;
		}

		if (params.size() <= paramindex) {// first value.. is empty
			params.add(paramindex, new Object[] { obj });
			types.add(paramindex, new Integer(type));
		} else {
			Integer beforeType = (Integer) types.get(paramindex);
			if (beforeType.intValue() != type)
				throw new IllegalArgumentException("Not Match Array Type. before Type:" + beforeType + ", currentType:" + type);

			Object[] src = (Object[]) params.get(paramindex);
			Object[] dst = new Object[src.length + 1];
			System.arraycopy(src, 0, dst, 0, Math.min(src.length, dst.length));
			dst[dst.length - 1] = obj;

			params.remove(paramindex);
			params.add(paramindex, dst);
		}
	}

	private void checkArraySize(int paramindex, int thisLength) {
		if (paramindex == 0)
			return;
		else {
			Object[] sObjs = (Object[]) params.get(0);
			if (sObjs.length != thisLength)
				throw new IllegalArgumentException("Not Match Array Size");
		}
	}

	public String toString() {
		StringBuilder result = new StringBuilder(getProcSQL() + "\n");

		for (int i = 0; i < params.size(); i++) {
			Object[] objects = getParam(i);
			if (objects.length > 0) {
				result.append(String.valueOf(i) + " : " + ((objects[0] == null) ? "" : StringUtil.substring(objects[0].toString(), 0, 50)) + "..."
						+ objects.length + "\n");
			}
		}
		return result.toString();
	}

	public void setParamValues(List params, List types) {
		this.params = params;
		this.types = types;
	}

	public Object[][] getParamsAsArray() {
		return params.toArray(new Object[0][]);
	}

	protected Object[] getParam(int index) {
		return (Object[]) params.get(index);
	}

	// ............
	public void addBatchParameter(String name, Object obj, int type) {
		namedParams.put(name, new ParamValue(obj, type));
	}

	public void addBatchParameterToArray(String name, Object obj, int size, int type) {
		Object[] values = new Object[size];

		for (int i = 0; i < values.length; i++) {
			values[i] = obj;
		}

		addBatchParameter(name, values, type);
	}

}
