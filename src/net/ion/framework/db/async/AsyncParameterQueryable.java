package net.ion.framework.db.async;

import java.io.InputStream;
import java.io.Reader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import net.ion.framework.db.IDBController;
import net.ion.framework.db.Page;
import net.ion.framework.db.Rows;
import net.ion.framework.db.bean.ResultSetHandler;
import net.ion.framework.db.procedure.IParameterQueryable;
import net.ion.framework.db.procedure.IQueryable;

public abstract class AsyncParameterQueryable implements IParameterQueryable {

	private static final long serialVersionUID = -3745910381421346056L;
	private IParameterQueryable inner ;
	protected AsyncParameterQueryable(IParameterQueryable inner){
		this.inner = inner ;
	} 

	
	protected IParameterQueryable inner(){
		return this.inner ;
	}
	
	
	
	public IParameterQueryable addBlob(InputStream input) {
		inner.addBlob(input) ;
		return this;
	}

	public IParameterQueryable addClob(CharSequence param) {
		inner.addClob(param) ;
		return this;
	}

	public IParameterQueryable addClob(Reader param) {
		inner.addClob(param) ;
		return this ;
	}
	
	public IParameterQueryable addParam(boolean param) {
		inner.addParam(param);
		return this ;
	}

	public IParameterQueryable addParam(int param) {
		inner.addParam(param);
		return this ;
	}

	public IParameterQueryable addParam(long param) {
		inner.addParam(param);
		return this ;
	}

	public IParameterQueryable addParam(CharSequence param) {
		inner.addParam(param);
		return this ;
	}

	public IParameterQueryable addParam(Object param) {
		inner.addParam(param);
		return this ;
	}


	
	
	
	
	public void addBlob(int paramindex, InputStream input) {
		inner.addBlob(paramindex, input) ;
	}

	public void addClob(int paramindex, CharSequence clobString) {
		inner.addClob(paramindex, clobString) ;
	}

	public void addClob(int paramindex, Reader param) {
		inner.addParam(paramindex, param);
	}

	public void addParam(int paramindex, boolean obj) {
		inner.addParam(paramindex, obj);
	}

	public void addParam(int paramindex, int obj) {
		inner.addParam(paramindex, obj);

	}
	public void addParam(int paramindex, long obj) {
		inner.addParam(paramindex, obj);
	}

	public void addParam(int paramindex, CharSequence param) {
		inner.addParam(paramindex, param);
	}

	public void addParam(int paramindex, Object param) {
		inner.addParam(paramindex, param);
	}

	
	
	
	

	
	
	
	
	public IParameterQueryable addClob(String name, CharSequence param) {
		inner.addParam(name, param);
		return this ;
	}

	public IParameterQueryable addClob(String name, Reader param) {
		inner.addParam(name, param);
		return this ;
	}

	public IParameterQueryable addBlob(String name, InputStream param) {
		inner.addBlob(name, param) ;
		return this;
	}

	public IParameterQueryable addParam(String name, boolean obj) {
		inner.addParam(name, obj);
		return this ;
	}

	public IParameterQueryable addParam(String name, int obj) {
		inner.addParam(name, obj);
		return this ;
	}

	public IParameterQueryable addParam(String name, long obj) {
		inner.addParam(name, obj);
		return this ;
	}

	public IParameterQueryable addParam(String name, CharSequence param) {
		inner.addParam(name, param) ;
		return this ;
	}

	public IParameterQueryable addParam(String name, Object param) {
		inner.addParam(name, param);
		return this ;
	}

	
	
	
	
	
	public IParameterQueryable addParameter(Object param, int type) {
		inner.addParameter(param, type);
		return this ;
	}

	public void addParameter(int paramindex, Object param, int type) {
		inner.addParameter(paramindex, param, type) ;
	}

	public IParameterQueryable addParameter(String name, Object param, int type) {
		inner.addParameter(name, param, type) ;
		return this ;
	}

	
	
	
	public void clearParam() {
		inner.clearParam() ;
	}

	public String getParamAsString(int index) {
		return inner.getParamAsString(index);
	}

	public List getParams() {
		return inner.getParams();
	}

	public String[] getParamsAsString(int index) {
		return inner.getParamsAsString(index);
	}

	public String getProcFullSQL() {
		return inner.getProcFullSQL();
	}

	public int getType(int index) {
		return inner.getType(index);
	}

	public List getTypes() {
		return inner.getTypes();
	}

	public boolean isNull(int paramIndex) {
		return inner.isNull(paramIndex);
	}

	public void setParamValues(List params, List types) {
		inner.setParamValues(params, types) ;
	}

	public long getCurrentModifyCount() {
		return inner.getCurrentModifyCount();
	}

	public IDBController getDBController() {
		return inner.getDBController();
	}

	public Statement getStatement() throws SQLException {
		return inner.getStatement();
	}

	public <T> T myHandlerQuery(Connection conn, ResultSetHandler<T> handler)
			throws SQLException {
		return inner.myHandlerQuery(conn, handler);
	}

	public Rows myQuery(Connection conn) throws SQLException {
		return inner.myQuery(conn);
	}

	public int myUpdate(Connection conn) throws SQLException {
		return inner.myUpdate(conn);
	}

	public void cancel() throws SQLException, InterruptedException {
		inner.cancel() ;
	}

	public Rows execPageQuery() throws SQLException {
		return inner.execPageQuery();
	}


	public String getDBType() {
		return inner.getDBType();
	}

	public Page getPage() {
		return inner.getPage();
	}

	public String getProcSQL() {
		return inner.getProcSQL();
	}

	public int getQueryType() {
		return inner.getQueryType();
	}

	public IQueryable setPage(Page page) {
		inner.setPage(page) ;
		return this ;
	}
}
