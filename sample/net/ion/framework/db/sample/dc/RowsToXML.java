package net.ion.framework.db.sample.dc;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

import net.ion.framework.db.DBController;
import net.ion.framework.db.ExtXmlWriter;
import net.ion.framework.db.IDBController;
import net.ion.framework.db.IONXmlWriter;
import net.ion.framework.db.IXmlWriter;
import net.ion.framework.db.Rows;
import net.ion.framework.db.cache.CacheConfig;
import net.ion.framework.db.cache.CacheConfigImpl;
import net.ion.framework.db.manager.CacheDBManager;
import net.ion.framework.db.sample.TestBaseSample;
import net.ion.framework.util.Debug;
import net.ion.framework.util.IOUtil;

public class RowsToXML extends TestBaseSample{
	
	
	private IDBController idc ; 
	@Override public void setUp() throws Exception {
		super.setUp();
		CacheConfig config = new CacheConfigImpl(makeCacheConfig());
		this.idc = new DBController(new CacheDBManager(config, dc.getDBManager())) ;
		idc.initSelf() ;
	}
	
	@Override public void tearDown() throws Exception {
		idc.destroySelf() ;
		super.tearDown();
	}
	
	public void testToXML() throws Exception {
		Rows rows = idc.getRows("select 1 from dual") ;
		Rows nrows = idc.getRows("select 1 from dual") ;

		assertEquals(1, rows.getRowCount()) ;
		assertEquals(1, nrows.getRowCount()) ;
		assertEquals(1, rows.firstRow().getInt(1)) ;
		assertEquals(1, nrows.firstRow().getInt(1)) ;
		
		Debug.line(nrows) ; // if not fail
		assertEquals(1, 1) ;
	}
	
	public void testDual() throws Exception {
		CacheConfig config = new CacheConfigImpl(makeCacheConfig());
		IDBController idc = new DBController(new CacheDBManager(config, dc.getDBManager())) ;
		idc.initSelf() ;
		
		IXmlWriter xmlwriter = new ExtXmlWriter();
		Rows rows = idc.getRows("select 1 from dual") ;
		Rows crows = idc.getRows("select 1 from dual") ;
		
	}
	
	private String makeCacheConfig(){
		return "{" + 
			"cache:[" +
			"{" +
			"  groupId:'temp_cach'," +
			"  count:5000," +
			"  add:['select 1 from dual']," +
			"  reset:[]" +
			"}]}" ;
	}


	private Writer getWriter(Rows rows, IXmlWriter xmlwriter) {
		Writer writer = new StringWriter();

		try {
			rows.writeXml(writer, xmlwriter);
			writer.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			IOUtil.close(writer) ;
		}
		return writer;
	}

}
