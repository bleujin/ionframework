package net.ion.framework.db.async;

import java.io.Closeable;
import java.sql.SQLException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import net.ion.framework.db.IDBController;
import net.ion.framework.db.Rows;
import net.ion.framework.db.manager.DBManager;
import net.ion.framework.db.procedure.IQueryable;

// Future Pattern.Host
public class AsyncDBController implements Closeable {
	private final IDBController dc;
	private ExecutorService es ;

	public AsyncDBController(IDBController dc) {
		this(dc, Executors.newCachedThreadPool());
	}

	public AsyncDBController(IDBController dc, ExecutorService es) {
		this.dc = dc;
		this.es = es;
	}


	
	public void setExecutorService(ExecutorService es) {
		this.es = es;
	}

	DBManager dbManager() {
		return dc.getDBManager();
	}

	IDBController dbController() {
		return dc;
	}
	
	
	
	public Future<Rows> getRows(final String query) {
		return getRows(dc.createUserCommand(query));
	}

	public Future<Rows> getRows(final String query, ExceptionHandler handler) {
		return getRows(dc.createUserCommand(query), handler);
	}

	public Future<Rows> getRows(final IQueryable query) {
		return getRows(query, ExceptionHandler.PrintHandler);
	}

	public Future<Rows> getRows(final IQueryable query, final ExceptionHandler handler) {
		Future<Rows> future = es.submit(new Callable<Rows>() {
			public Rows call() {
				try {
					return query.execQuery();
				} catch (Throwable ex) {
					handler.handle(ex);
					return null;
				}
			}
		});

		return future;
	}

	

	
	public Future<Integer> execUpdate(final String query) {
		return execUpdate(dc.createUserCommand(query));
	}

	public Future<Integer> execUpdate(final IQueryable query) {
		return execUpdate(query, ExceptionHandler.PrintHandler ); 
	}

	public Future<Integer> execUpdate(final IQueryable query, final ExceptionHandler handler) {
		Future<Integer> future = es.submit(new Callable<Integer>() {
			public Integer call() throws Exception {
				try {
					return query.execUpdate();
				} catch (Throwable ex) {
					handler.handle(ex) ; 
					return -1;
				}
			}
		});

		return future ;
	}

	public void destroySelf() throws SQLException {
		es.shutdown() ;
		dc.destroySelf();
	}
	
	public void close(){
		es.shutdown() ;
	}

	public <T> Future<T> tran(final AsyncTransactionJob<T> job) {
		final AsyncDBController self = this;
		return es.submit(new Callable<T>() {
			public T call() throws Exception {
				AsyncSession session = new AsyncSession(self);
				try {
					session.beginTran();

					T result = job.handle(session);
					session.commit();
					return result;
				} catch (Throwable ex) {
					session.rollback(ex);
					job.fail(ex);
				} finally {
					session.free();
				}
				return null;
			}

		});
	}

	
	@Deprecated
	public Result<Integer> updateResult(final IQueryable query){
		return ConcurrentFutureResult.create(query, execUpdate(query)) ;
	}

	@Deprecated
	public Result<Rows> queryResult(final IQueryable query){
		return ConcurrentFutureResult.create(query, getRows(query)) ;
	}

	@Deprecated
	public Result<Integer> updateResult(final String query){
		return ConcurrentFutureResult.create(dc.createUserCommand(query), execUpdate(query)) ;
	}

	@Deprecated
	public Result<Rows> queryResult(final String query){
		return ConcurrentFutureResult.create(dc.createUserCommand(query), getRows(query)) ;
	}

}

class UnknownFutureData<T> {
	private T t;
	private Throwable ex;

	UnknownFutureData(T t, Throwable ex) {
		this.t = t;
		this.ex = ex;
	}

	T obj() {
		return t;
	}

	Throwable ex() {
		return ex;
	}
}
