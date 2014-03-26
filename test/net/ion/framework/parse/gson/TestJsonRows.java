package net.ion.framework.parse.gson;

import java.sql.ResultSetMetaData;

import junit.framework.TestCase;
import net.ion.framework.db.Rows;

public class TestJsonRows extends TestCase {
	
	public void testJsonToRowsMeta() throws Exception {
		JsonObject json = JsonObject.fromString("{'name':'bleujin', 'age':20}") ;
		Rows rows = JsonUtil.toRows(json) ;
		
		ResultSetMetaData meta = rows.getMetaData() ;
		assertNotNull(meta);
		assertEquals(2, meta.getColumnCount());
		assertEquals("name", meta.getColumnLabel(1)) ;
		assertEquals("age", meta.getColumnLabel(2)) ;
	}
	
	
	public void testEmptyJsonToRowsMeta() throws Exception {
		JsonObject json = JsonObject.fromString("{}") ;
		Rows rows = JsonUtil.toRows(json, "name", "age") ;
		
		ResultSetMetaData meta = rows.getMetaData() ;
		assertNotNull(meta);
		assertEquals(2, meta.getColumnCount());
		assertEquals("name", meta.getColumnLabel(1)) ;
		assertEquals("age", meta.getColumnLabel(2)) ;
	}

	public void testEmptyJsonToRowsMeta2() throws Exception {
		JsonArray json = JsonParser.fromString("[]").getAsJsonArray() ;
		Rows rows = JsonUtil.toRows(json, "name", "age") ;

		ResultSetMetaData meta = rows.getMetaData() ;
		assertNotNull(meta);
		assertEquals(2, meta.getColumnCount());
		assertEquals("name", meta.getColumnLabel(1)) ;
		assertEquals("age", meta.getColumnLabel(2)) ;
	}

	
	
	public void testColumnValue() throws Exception {
		JsonObject json = JsonObject.fromString("{'name':'bleujin', 'age':20}") ;
		Rows rows = JsonUtil.toRows(json) ;
		
		assertEquals(1, rows.getRowCount());
		assertEquals(true, rows.next()) ;
		assertEquals("bleujin", rows.getString("name")); 
		assertEquals(20, rows.getInt("age")); 
		assertEquals(20L, rows.getLong("age")); 
		assertEquals("20", rows.getString("age")); 
	}
	
	public void testListRow() throws Exception {
		JsonArray json = JsonParser.fromString("[{'name':'bleujin', 'age':20}, {'name':'hero', 'age':30, 'address':'seoul'}]").getAsJsonArray() ;
		Rows rows = JsonUtil.toRows(json) ;
		
		assertEquals(2, rows.getRowCount());
		assertEquals(2, rows.getMetaData().getColumnCount()) ;
		
		rows.debugPrint(); 
	}

	
	public void testListRow2() throws Exception {
		JsonArray json = JsonParser.fromString("[{'name':'bleujin', 'age':20}, {'name':'hero', 'age':30, 'address':'seoul'}]").getAsJsonArray() ;
		Rows rows = JsonUtil.toRows(json, "name", "age", "address") ;
		
		assertEquals(2, rows.getRowCount());
		assertEquals(3, rows.getMetaData().getColumnCount()) ;
		
		rows.debugPrint();
		
		
	}

}
