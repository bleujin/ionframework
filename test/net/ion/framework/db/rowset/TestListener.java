package net.ion.framework.db.rowset;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.RowSetEvent;
import javax.sql.RowSetListener;

import net.ion.framework.db.DBController;
import net.ion.framework.db.Rows;
import net.ion.framework.db.bean.ResultSetHandler;
import net.ion.framework.db.bean.handlers.ListHandler;
import net.ion.framework.db.manager.OracleDBManager;
import net.ion.framework.util.Debug;
import junit.framework.TestCase;

public class TestListener extends TestCase {

	private DBController dc;
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		OracleDBManager dbm = OracleDBManager.test();
		this.dc = new DBController(dbm);
		dc.initSelf();
	}
	
	@Override
	protected void tearDown() throws Exception {
		dc.destroySelf();
		super.tearDown();
	}
	
	public void testAddListener() throws Exception {
		Rows rows = dc.getRows("select * from tabs");
		rows.addRowSetListener(new OutListener());

		while (rows.next()) {
			Debug.debug(rows.getString("table_name"));
		}

	}

	public void testHandler() throws Exception {
		List<String> result = dc.createUserCommand("select * from tabs").execHandlerQuery(new LoggerHandler<List<String>>(new ListHandler(1)));
		Debug.line(result) ;
	}
}




class LoggerHandler<T> implements ResultSetHandler<T> {

	private static final long serialVersionUID = 1L;

	private ResultSetHandler<T> inner ;
	public LoggerHandler(ResultSetHandler<T> inner) {
		this.inner = inner;
	}

	public T handle(ResultSet rs) throws SQLException {
		ResultSet proxy = ProxyHandler.create(rs, new ProxyResultSet.AOPHandler() {
			private int count = 0 ;
			public void pre(Object proxy, Method m, Object[] args) {
			}
			public void after(Object proxy, Method m, Object[] args, Object result) {
				if ("next".equals(m.getName()) && Boolean.TRUE.equals(result)) {
					if ((count++ % 5) == 0)  System.out.println() ;
				}
			}
		}) ;
		return inner.handle(proxy);
	}
}






class OutListener implements RowSetListener {
	private int count = 0;

	public void rowSetChanged(RowSetEvent ev) {
		// System.out.println(ev) ;
	}

	public void rowChanged(RowSetEvent ev) {
		System.out.println(ev);
	}

	public void cursorMoved(RowSetEvent ev) {
		if ((count++ % 5) == 0)
			System.out.println();
	}
}



