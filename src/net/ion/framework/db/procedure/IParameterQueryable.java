package net.ion.framework.db.procedure;

import java.io.InputStream;
import java.io.Reader;
import java.util.List;

public interface IParameterQueryable extends Queryable {

	public IParameterQueryable addParam(boolean param);

	public IParameterQueryable addParam(int param);

	public IParameterQueryable addParam(long param);

	public IParameterQueryable addParam(CharSequence param);

	public IParameterQueryable addParam(Object param);

	public IParameterQueryable addClob(CharSequence param);

	public IParameterQueryable addClob(Reader param);

	public IParameterQueryable addBlob(InputStream input);

	public IParameterQueryable addParameter(final Object param, final int type);

	public void addParam(int paramindex, boolean obj);

	public void addParam(int paramindex, int obj);

	public void addParam(int paramindex, long obj);

	public void addParam(int paramindex, CharSequence param);

	public void addParam(int paramindex, Object param);

	public void addClob(int paramindex, CharSequence clobString);

	public void addClob(int paramindex, Reader param);

	public void addBlob(int paramindex, InputStream is);

	public void addParameter(final int paramindex, final Object param, final int type);

	public IParameterQueryable addParam(String name, boolean obj);

	public IParameterQueryable addParam(String name, int obj);

	public IParameterQueryable addParam(String name, long obj);

	public IParameterQueryable addParam(String name, CharSequence param);

	public IParameterQueryable addParam(String name, Object param);

	public IParameterQueryable addClob(String name, CharSequence param);

	public IParameterQueryable addClob(String name, Reader param);

	public IParameterQueryable addBlob(String name, InputStream param);

	public IParameterQueryable addParameter(final String name, final Object param, final int type);

	// public void addParamAsString( int paramindex, String param ) ;
	public void setParamValues(List params, List types);

	public boolean isNull(int paramIndex);

	public String getProcFullSQL();

	public String[] getParamsAsString(int index);

	public String getParamAsString(int index);

	public List getParams();

	public List getTypes();

	public int getType(int index);

	public void clearParam();
}
