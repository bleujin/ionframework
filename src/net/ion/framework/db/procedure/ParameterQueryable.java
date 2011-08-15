package net.ion.framework.db.procedure;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamException;
import java.io.Reader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.ion.framework.db.IDBController;
import net.ion.framework.db.Rows;
import net.ion.framework.db.RowsUtils;
import net.ion.framework.util.CaseInsensitiveHashMap;
import net.ion.framework.util.Debug;

import org.apache.commons.io.IOUtils;

public abstract class ParameterQueryable extends AbstractQueryable implements IParameterQueryable {

	protected List<Object> params = new ArrayList<Object>();
	protected List<Integer> types = new ArrayList<Integer>();
	protected Map<String, Object> namedParams = new CaseInsensitiveHashMap<Object>();

	private int idx = 0;

	public ParameterQueryable(IDBController dc, String procSQL, int queryType) {
		super(dc, procSQL, queryType);
	}

	protected final Rows populate(ResultSet rs) throws SQLException {
		Rows result = RowsUtils.create(this);
		result.populate(rs, getPage().getStartLoc(), getPage().getListNum());
		return result;
	}

	public IParameterQueryable addParam(boolean param) {
		addParameter(idx++, param, Types.BOOLEAN);
		return this;
	}

	public IParameterQueryable addParam(int param) {
		addParameter(idx++, param, Types.INTEGER);
		return this;
	}

	public IParameterQueryable addParam(long param) {
		addParameter(idx++, param, Types.BIGINT);
		return this;
	}

	public IParameterQueryable addParam(Object param) {
		addParameter(idx++, param, Types.OTHER);
		return this;
	}

	public IParameterQueryable addParam(CharSequence param) {
		addParameter(idx++, param, Types.VARCHAR);
		return this;
	}

	public IParameterQueryable addClob(CharSequence param) {
		addParameter(idx++, param, Types.CLOB);
		return this;
	}

	public IParameterQueryable addClob(Reader param) {
		addParameter(idx++, param, Types.CLOB);
		return this;
	}

	public IParameterQueryable addBlob(InputStream input) {
		addParameter(idx++, input, Types.BLOB);
		return this;
	}

	public IParameterQueryable addParameter(Object param, int type) {
		addParameter(idx++, param, type);
		return this;
	}

	// .................
	public IParameterQueryable addParam(String name, boolean obj) {
		namedParams.put(name, new ParamValue(new Boolean(obj), Types.BOOLEAN));
		return this;
	}

	public IParameterQueryable addParam(String name, int obj) {
		namedParams.put(name, new ParamValue(new Integer(obj), Types.INTEGER));
		return this;
	}

	public IParameterQueryable addParam(String name, long obj) {
		namedParams.put(name, new ParamValue(new Long(obj), Types.BIGINT));
		return this;
	}

	public IParameterQueryable addParam(String name, Object param) {
		if (param instanceof IStringObject) {
			return addParam(name, ((IStringObject) param).getString());
		} else {
			namedParams.put(name, new ParamValue(param, Types.OTHER));
			return this;
		}
	}

	public IParameterQueryable addParam(String name, CharSequence param) {
		namedParams.put(name, new ParamValue(param, Types.VARCHAR));
		return this;
	}

	public IParameterQueryable addParameter(String name, Object param, int type) {
		namedParams.put(name, new ParamValue(param, type));
		return this;
	}

	public IParameterQueryable addClob(String name, CharSequence param) {
		namedParams.put(name, new ParamValue(param, Types.CLOB));
		return this;
	}

	public IParameterQueryable addClob(String name, Reader param) {
		namedParams.put(name, new ParamValue(param, Types.CLOB));
		return this;
	}

	public IParameterQueryable addBlob(String name, InputStream param) {
		namedParams.put(name, new ParamValue(param, Types.BLOB));
		return this;
	}

	public Map getNamedParam() {
		return Collections.unmodifiableMap(namedParams);
	}

	public IParameterQueryable addNamedParam(Map<String, Object> namedParam) {
		this.namedParams = namedParam;
		return this;
	}

	class ParamValue {
		ParamValue(Object value, int type) {
			this.value = value;
			this.type = type;
		}

		Object value;

		int type;

		public String toString() {
			return value == null ? null : value.toString();
		}

	}

	protected final String patternStr = "\\:[a-zA-Z][a-zA-Z0-9]*";

	@Override
	public String getProcSQL() {
//		return super.getProcSQL() ;
		return transNamedSQL(super.getProcSQL());
	}

	protected String transNamedSQL(String _sql) {
		if (namedParams.isEmpty()) {
			return _sql;
		} else if(! params.isEmpty()) {
			return _sql.replaceAll(patternStr, "?");
		} else {
			String baseSQL = _sql;
			// when select ... from table wheren col1 = :name1 and col2 = :name2
			// and col3 = (:name1 + :name2)
			// list will contain names of: name1, name2, name1, name2
			List<String> list = getSettedParam(baseSQL);
			ParamValue[] tempParam = new ParamValue[list.size()];

			for (int i = 0, last = list.size(); i < last; i++) {
				ParamValue paramValue = (ParamValue) namedParams.get(list.get(i));

				if (paramValue == null)
					throw new IllegalArgumentException("unsetted named param : " + list.get(i));
				tempParam[i] = paramValue;
			}
			// now, we have tempParam list which contains values for the list.
			for (int i = 0; i < tempParam.length; i++) {
				this.params.add(tempParam[i].value);
				this.types.add(tempParam[i].type);
			}
			return baseSQL.replaceAll(patternStr, "?");
		}
	}

	protected List<String> getSettedParam(String baseSQL) {
		List<String> result = new ArrayList<String>();
		Pattern pattern = Pattern.compile(patternStr);
		Matcher matcher = pattern.matcher(baseSQL);

		while (matcher.find()) {
			result.add(baseSQL.substring(matcher.start() + 1, matcher.end()));
		}
		return result;
	}

	public void addParam(int paramindex, boolean obj) {
		addParameter(paramindex, new Boolean(obj), Types.BOOLEAN);
	}

	public void addParam(int paramindex, int obj) {
		addParameter(paramindex, new Integer(obj), Types.INTEGER);
	}

	public void addParam(int paramindex, long obj) {
		addParameter(paramindex, new Long(obj), Types.BIGINT);
	}

	public void addParam(int paramindex, Object param) {
		addParameter(paramindex, param, Types.OTHER);
	}

	public void addParam(int paramindex, CharSequence param) {
		addParameter(paramindex, param, Types.VARCHAR);
	}

	public void addParameter(int paramindex, Object param, int type) {
		if (type == Types.OTHER && param instanceof IStringObject) {
			addParameter(paramindex, (param == null) ? param : ((IStringObject) param).getString(), type);
		} else {
			params.add(paramindex, param);
			types.add(paramindex, new Integer(type));
		}
	}

	// about lob...
	public void addClob(int paramindex, CharSequence clobString) {
		addParameter(paramindex, clobString, Types.CLOB);
	}

	public void addClob(int paramindex, Reader clobReader) {
		addParameter(paramindex, clobReader, Types.CLOB);
	}

	public void addBlob(int paramindex, InputStream is) {
		addParameter(paramindex, is, Types.BLOB);
	}

	public boolean isNull(int paramIndex) {
		return params.get(paramIndex) == null;
	}

	boolean isClobType(int paramindex) {
		return (new Integer(Types.CLOB)).equals(types.get(paramindex));
	}

	boolean isBlobType(int paramindex) {
		return (new Integer(Types.BLOB)).equals(types.get(paramindex));
	}

	int getBlobParamCount() {
		int result = 0;
		for (int i = 0, last = types.size(); i < last; i++) {
			if (isBlobType(i))
				result++;
		}
		return result;
	}

	int getClobParamCount() {
		int result = 0;
		for (int i = 0, last = types.size(); i < last; i++) {
			if (isClobType(i))
				result++;
		}
		return result;
	}

	int getClobParamIndex(int keyIndex) {
		int currIndex = 0;
		for (int i = 0, last = types.size(); i < last; i++) {
			if (isClobType(i)) {
				if (currIndex == keyIndex)
					return i;
				else
					currIndex++;
			}
		}
		throw new IllegalArgumentException("Not Found " + keyIndex + "'th Clob Type");
	}

	int getBlobParamIndex(int keyIndex) {
		int currIndex = 0;
		for (int i = 0, last = types.size(); i < last; i++) {
			if (isBlobType(i)) {
				if (currIndex == keyIndex)
					return i;
				else
					currIndex++;
			}
		}
		throw new IllegalArgumentException("Not Found " + keyIndex + "'th Blob Type");
	}

	boolean hasLobType() {
		return (getClobParamCount() + getBlobParamCount()) > 0;
	}

	// end lob type...

	public void setParamValues(List params, List types) {
		if (params == null || types == null) throw new IllegalArgumentException("Parameter and Types Map must be not null") ;
		
		this.params = params;
		this.types = types;
	}

	public String getProcFullSQL() {
		StringBuffer result = new StringBuffer(getProcSQL() + "\n");
		for (int i = 0; i < params.size(); i++) {
			result.append(String.valueOf(i) + "(" + types.get(i) + ") : " + params.get(i) + "\n");
		}
		return result.toString();
	}

	public String[] getParamsAsString(int index) {
		Object obj = params.get(index);
		if (obj == null)
			return new String[] { "" };
		else if (obj instanceof Object[]) {
			Object[] objs = (Object[]) obj;
			String[] result = new String[objs.length];
			for (int i = 0; i < objs.length; i++) {
				result[i] = String.valueOf(objs[i]);
			}
			return result;
		} else
			return new String[] { String.valueOf(obj) };
	}

	public String getParamAsString(int index) {
		return getParamsAsString(index)[0];
	}

	public List getParams() {
		return params;
	}

	public List getTypes() {
		return types;
	}

	public int getType(int index) {
		return ((Integer) types.get(index)).intValue();
	}

	public void clearParam() {
		params.clear();
		types.clear();
		namedParams.clear();
		idx = 0;
	}

	// input stream && reader close
	protected void cleanThis() {
		int index = 0;
		for (Integer type : types) {
			if (type.intValue() == Types.CLOB || type.intValue() == Types.BLOB) {
				Object param = params.get(index);
				closeLob(param);
			}
			index++;
		}
	}

	private void closeLob(Object param) {
		if (param instanceof Object[]) {
			Object[] params = (Object[]) param;
			for (Object p : params) {
				closeLob(p);
			}
		}

		if (param instanceof Reader) {
			IOUtils.closeQuietly((Reader) param);
		} else if (param instanceof InputStream) {
			IOUtils.closeQuietly((InputStream) param);
		}
	}
	
	
	public Object writeReplace() throws ObjectStreamException {
		return SerializedQuery.createParameterQuery(this) ;
	}
	

}

