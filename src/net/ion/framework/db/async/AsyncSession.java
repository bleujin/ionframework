package net.ion.framework.db.async;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.sql.Connection;
import java.sql.SQLException;

import net.ion.framework.db.IDBController;
import net.ion.framework.db.Rows;
import net.ion.framework.db.bean.ResultSetHandler;
import net.ion.framework.db.procedure.IParameterQueryable;
import net.ion.framework.db.procedure.IQueryable;
import net.ion.framework.db.procedure.IUserCommand;
import net.ion.framework.db.procedure.IUserCommandBatch;
import net.ion.framework.db.procedure.IUserProcedure;
import net.ion.framework.db.procedure.IUserProcedureBatch;
import net.ion.framework.db.procedure.IUserProcedures;

public class AsyncSession {

	private AsyncDBController adc;
	private Connection conn;

	AsyncSession(AsyncDBController adc) {
		this.adc = adc;
	}

	void beginTran() throws SQLException {
		this.conn = adc.dbManager().getConnection();
		conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
		conn.setAutoCommit(false);
	}

	void rollback(Throwable ex) throws SQLException {
		conn.rollback();
	}

	void commit() throws SQLException {
		conn.commit();
	}

	public void fail(String msg) throws SQLException{
		conn.rollback() ;
		throw new RuntimeException(msg) ;
	}
	
	void free() throws SQLException {
		if (conn != null)
			conn.setAutoCommit(false);
		adc.dbManager().freeConnection(conn);
	}

	public IUserCommand createUserCommand(String proc) {
		return new AsyncUserParameterQuery(adc.dbController(), adc.dbController().createUserCommand(proc), conn);
	}

	public IUserProcedure createUserProcedure(String proc) {
		return new AsyncUserParameterQuery(adc.dbController(), adc.dbController().createUserProcedure(proc), conn);
	}
	
	public IUserProcedures createUserProcedures(String proc) {
		return new AsyncUserProcedures(adc.dbController(), proc, conn);
	}

	public IUserCommandBatch createUserCommandBatch(String proc) {
		return new AsyncUserCommandBatch(adc.dbController(), adc.dbController().createUserCommandBatch(proc), conn);
	}

	public IUserProcedureBatch createUserProcedureBatch(String proc) {
		return new AsyncUserProcedureBatch(adc.dbController(), adc.dbController().createUserProcedureBatch(proc), conn);
	}
	

}

class AsyncUserCommandBatch extends AsyncParameterQueryable implements IUserCommandBatch {

	private static final long serialVersionUID = 420552372476172327L;
	private final Connection conn;
	private final IDBController dc;
	public AsyncUserCommandBatch(IDBController dc, IUserCommandBatch upt, Connection conn) {
		super(upt) ;
		this.dc = dc ;
		this.conn = conn ;
	}
	
	protected IUserCommandBatch inner(){
		return (IUserCommandBatch) super.inner() ;
	}
	
	public void addBatchBlob(int paramindex, InputStream val) {
		inner().addBatchBlob(paramindex, val) ;
	}
	public void addBatchClob(String name, CharSequence val) {
		inner().addBatchClob(name, val) ;
	}
	public void addBatchClob(String name, Reader reader) {
		inner().addBatchClob(name, reader) ; 
	}
	public void addBatchParam(String name, boolean val) {
		inner().addBatchParam(name, val) ;
	}
	public void addBatchParam(String name, int val) {
		inner().addBatchParam(name, val) ;
	}
	public void addBatchParam(String name, long val) {
		inner().addBatchParam(name, val) ;
	}
	public void addBatchParam(String name, CharSequence val) {
		inner().addBatchParam(name, val) ;
	}
	public void addBatchParam(String name, Object val) {
		inner().addBatchParam(name, val) ;
	}
	
	
	
	public void addParam(int paramindex, boolean[] objs) {
		inner().addParam(paramindex, objs) ;

	}
	public void addParam(int paramindex, int[] objs) {
		inner().addParam(paramindex, objs) ;
	}
	public void addParam(int paramindex, long[] objs) {
		inner().addParam(paramindex, objs) ;
	}
	public void addParam(int paramindex, CharSequence[] strs) {
		inner().addParam(paramindex, strs) ;
	}
	public void addParam(int paramindex, Object[] objs) {
		inner().addParam(paramindex, objs) ;
	}
	public void addBatchClob(int paramindex, CharSequence val) {
		inner().addBatchClob(paramindex, val) ;
	}
	public void addBatchClob(int paramindex, Reader reader) {
		inner().addBatchClob(paramindex, reader) ;
	}

	public void addBatchParam(int paramindex, boolean val) {
		inner().addBatchParam(paramindex, val) ;
	}
	public void addBatchParam(int paramindex, int val) {
		inner().addBatchParam(paramindex, val) ;
	}
	public void addBatchParam(int paramindex, long val) {
		inner().addBatchParam(paramindex, val) ;
	}
	public void addBatchParam(int paramindex, CharSequence val) {
		inner().addBatchParam(paramindex, val) ;
	}
	public void addBatchParam(int paramindex, Object val) {
		inner().addBatchParam(paramindex, val) ;
	}
	public void addBlob(int paramindex, InputStream[] is) {
		inner().addBatchParam(paramindex, is) ;
	}

	
	public void addClob(int paramindex, CharSequence[] clobString) {
		inner().addClob(paramindex, clobString) ;
	}
	public void addClob(String name, CharSequence[] strs) {
		inner().addClob(name, strs) ;
	}
	public void addClobToArray(int paramindex, CharSequence clobString, int size) {
		inner().addClobToArray(paramindex, clobString, size) ;
	}
	public void addClobToArray(String name, CharSequence clobString, int size) {
		inner().addClobToArray(name, clobString, size) ;
	}
	

	public void addParam(String name, boolean[] objs) {
		inner().addParam(name, objs) ;
	}
	public void addParam(String name, int[] objs) {
		inner().addParam(name, objs) ;
	}
	public void addParam(String name, long[] objs) {
		inner().addParam(name, objs) ;
	}
	public void addParam(String name, CharSequence[] strs) {
		inner().addParam(name, strs) ;
	}
	public void addParam(String name, Object[] objs) {
		inner().addParam(name, objs) ;

	}
	public void addParamToArray(int paramindex, boolean value, int size) {
		inner().addParamToArray(paramindex, value, size) ;

	}
	public void addParamToArray(int paramindex, int value, int size) {
		inner().addParamToArray(paramindex, value, size) ;
	}
	public void addParamToArray(int paramindex, long value, int size) {
		inner().addParamToArray(paramindex, value, size) ;
	}
	public void addParamToArray(int paramindex, CharSequence value, int size) {
		inner().addParamToArray(paramindex, value, size) ;
	}
	public void addParamToArray(int paramindex, Object value, int size) {
		inner().addParamToArray(paramindex, value, size) ;
	}
	public void addParamToArray(String name, boolean value, int size) {
		inner().addParamToArray(name, value, size) ;
	}
	public void addParamToArray(String name, int value, int size) {
		inner().addParamToArray(name, value, size) ;
	}
	public void addParamToArray(String name, long value, int size) {
		inner().addParamToArray(name, value, size) ;
	}
	public void addParamToArray(String name, CharSequence value, int size) {
		inner().addParamToArray(name, value, size) ;
	}
	public void addParamToArray(String name, Object value, int size) {
		inner().addParamToArray(name, value, size) ;
	}
	public void addParameter(int paramindex, Object[] objs, int type) {
		inner().addParameter(paramindex, objs, type) ;
	}
	public void addParameter(String name, Object[] objs, int type) {
		inner().addParameter(name, objs, type) ;
	}
	public void addParameterToArray(int paramindex, Object value, int size, int type) {
		inner().addParameterToArray(paramindex, value, size, type) ;
	}
	public void addParameterToArray(String name, Object value, int size, int type) {
		inner().addParameterToArray(name, value, size, type) ;
	}

	public void addBatchParameter(String name, Object obj, int type) {
		inner().addBatchParameter(name, obj, type) ;
	}
	public void addBatchParameter(int paramindex, Object obj, int type) {
		inner().addBatchParameter(paramindex, obj, type) ;
	}
	public void addBatchParameterToArray(String name, Object obj, int size, int type) {
		inner().addBatchParameterToArray(name, obj, size, type) ;
	}
	
	
	public IUserCommandBatch addBlob(InputStream[] param) {
		inner().addBlob(param) ;
		return this;
	}
	public IUserCommandBatch addClob(CharSequence[] param) {
		inner().addClob(param) ;
		return this;
	}
	public IUserCommandBatch addParam(boolean[] param) {
		inner().addParam(param) ;
		return this;
	}
	public IUserCommandBatch addParam(int[] param) {
		inner().addParam(param) ;
		return this;
	}
	public IUserCommandBatch addParam(long[] param) {
		inner().addParam(param) ;
		return this;
	}
	public IUserCommandBatch addParam(CharSequence[] param) {
		inner().addParam(param) ;
		return this;
	}
	public IUserCommandBatch addParam(Object[] param) {
		inner().addParam(param) ;
		return this;
	}
	
	
	public void printPlan(OutputStream output) throws SQLException {
		inner().printPlan(output) ;
	}
	
	public <T> T execHandlerQuery(ResultSetHandler<T> handler) throws SQLException {
		throw new SQLException("Curren Type is Batch, Select Method not yet implemented.");
	}

	public Rows execQuery() throws SQLException {
		throw new SQLException("Curren Type is Batch, Select Method not yet implemented.");
	}

	public int execUpdate() throws SQLException {
		long start = System.nanoTime();
		try {
			return inner().myUpdate(conn);
		} finally {
			dc.handleServant(start, System.nanoTime(), inner(), IQueryable.UPDATE_COMMAND);
		}
	}
}

class AsyncUserProcedureBatch extends AsyncParameterQueryable implements IUserProcedureBatch {

	private static final long serialVersionUID = 420552372476172327L;
	private final Connection conn;
	private final IDBController dc;
	public AsyncUserProcedureBatch(IDBController dc, IUserProcedureBatch upt, Connection conn) {
		super(upt) ;
		this.dc = dc ;
		this.conn = conn ;
	}
	
	protected IUserProcedureBatch inner(){
		return (IUserProcedureBatch) super.inner() ;
	}
	
	public void addParam(int paramindex, boolean[] objs) {
		inner().addParam(paramindex, objs) ;

	}
	public void addParam(int paramindex, int[] objs) {
		inner().addParam(paramindex, objs) ;
	}
	public void addParam(int paramindex, long[] objs) {
		inner().addParam(paramindex, objs) ;
	}
	public void addParam(int paramindex, CharSequence[] strs) {
		inner().addParam(paramindex, strs) ;
	}
	public void addParam(int paramindex, Object[] objs) {
		inner().addParam(paramindex, objs) ;
	}
	public void addBatchClob(int paramindex, CharSequence val) {
		inner().addBatchClob(paramindex, val) ;
	}
	public void addBatchParam(int paramindex, boolean val) {
		inner().addBatchParam(paramindex, val) ;
	}
	public void addBatchParam(int paramindex, int val) {
		inner().addBatchParam(paramindex, val) ;
	}
	public void addBatchParam(int paramindex, long val) {
		inner().addBatchParam(paramindex, val) ;
	}
	public void addBatchParam(int paramindex, CharSequence val) {
		inner().addBatchParam(paramindex, val) ;
	}
	public void addBatchParam(int paramindex, Object val) {
		inner().addBatchParam(paramindex, val) ;
	}
	public void addBlob(int paramindex, InputStream[] is) {
		inner().addBatchParam(paramindex, is) ;
	}

	
	public void addClob(int paramindex, CharSequence[] clobString) {
		inner().addClob(paramindex, clobString) ;
	}
	public void addClobToArray(int paramindex, CharSequence clobString, int size) {
		inner().addClobToArray(paramindex, clobString, size) ;
	}


	public void addParam(String name, boolean[] objs) {
		inner().addParam(name, objs) ;
	}
	public void addParam(String name, int[] objs) {
		inner().addParam(name, objs) ;
	}
	public void addParam(String name, long[] objs) {
		inner().addParam(name, objs) ;
	}
	public void addParam(String name, CharSequence[] strs) {
		inner().addParam(name, strs) ;
	}
	public void addParam(String name, Object[] objs) {
		inner().addParam(name, objs) ;

	}
	public void addParamToArray(int paramindex, boolean value, int size) {
		inner().addParamToArray(paramindex, value, size) ;

	}
	public void addParamToArray(int paramindex, int value, int size) {
		inner().addParamToArray(paramindex, value, size) ;
	}
	public void addParamToArray(int paramindex, CharSequence value, int size) {
		inner().addParamToArray(paramindex, value, size) ;
	}
	public void addParameter(int paramindex, Object[] objs, int type) {
		inner().addParameter(paramindex, objs, type) ;
	}
	public void addParameter(String name, Object[] objs, int type) {
		inner().addParameter(name, objs, type) ;
	}

	public void addBatchParameter(String name, Object obj, int type) {
		inner().addBatchParameter(name, obj, type) ;
	}
	public void addBatchParameter(int paramindex, Object obj, int type) {
		inner().addBatchParameter(paramindex, obj, type) ;
	}
	public void addBatchParameterToArray(String name, Object obj, int size, int type) {
		inner().addBatchParameterToArray(name, obj, size, type) ;
	}
	
	
	public IUserProcedureBatch addClob(CharSequence[] param) {
		inner().addClob(param) ;
		return this;
	}
	public IUserProcedureBatch addParam(boolean[] param) {
		inner().addParam(param) ;
		return this;
	}
	public IUserProcedureBatch addParam(int[] param) {
		inner().addParam(param) ;
		return this;
	}
	public IUserProcedureBatch addParam(long[] param) {
		inner().addParam(param) ;
		return this;
	}
	public IUserProcedureBatch addParam(CharSequence[] param) {
		inner().addParam(param) ;
		return this;
	}
	public IUserProcedureBatch addParam(Object[] param) {
		inner().addParam(param) ;
		return this;
	}
	
	
	public <T> T execHandlerQuery(ResultSetHandler<T> handler) throws SQLException {
		throw new SQLException("Curren Type is Batch, Select Method not yet implemented.");
	}

	public Rows execQuery() throws SQLException {
		throw new SQLException("Curren Type is Batch, Select Method not yet implemented.");
	}

	public int execUpdate() throws SQLException {
		long start = System.nanoTime();
		try {
			return inner().myUpdate(conn);
		} finally {
			dc.handleServant(start, System.nanoTime(), inner(), IQueryable.UPDATE_COMMAND);
		}
	}

	public Object[][] getParamsAsArray() {
		return inner().getParamsAsArray();
	}

	public String getProcName() {
		return inner().getProcName();
	}
}


class AsyncUserParameterQuery extends AsyncParameterQueryable implements IUserCommand, IUserProcedure {

	private static final long serialVersionUID = 3178128909286970014L;
	private final Connection conn;
	private final IDBController dc;

	public AsyncUserParameterQuery(IDBController dc, IParameterQueryable upt, Connection conn) {
		super(upt);
		this.dc = dc;
		this.conn = conn;
	}

	public <T> T execHandlerQuery(ResultSetHandler<T> handler) throws SQLException {
		long start = System.nanoTime();
		try {
			return inner().myHandlerQuery(conn, handler);
		} finally{
			dc.handleServant(start, System.nanoTime(), inner(), IQueryable.QUERY_COMMAND);		
		}
	}

	public Rows execQuery() throws SQLException {
		long start = System.nanoTime();
		try {
			return inner().myQuery(conn);
		} finally {
			dc.handleServant(start, System.nanoTime(), inner(), IQueryable.QUERY_COMMAND);
		}
	}

	public int execUpdate() throws SQLException {
		long start = System.nanoTime();
		try {
			return inner().myUpdate(conn);
		} finally {
			dc.handleServant(start, System.nanoTime(), inner(), IQueryable.UPDATE_COMMAND);
		}
	}

	public void printPlan(OutputStream output) throws SQLException {
		((IUserCommand) inner()).printPlan(output);
	}

	public String getProcName() {
		return ((IUserProcedure)inner()).getProcName();
	}
	
	public String toString(){
		return inner().toString();
	}

	@Override
	public int hashCode(){
		return inner().hashCode() ;
	}
	
	public boolean equals(Object obj){
		if (obj instanceof AsyncUserParameterQuery){
			return this.inner().equals(((AsyncUserParameterQuery) obj).inner()) ;
		}
		return false ;
	}
	

	
}