package net.ion.framework.db.mongo.jdbc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.ion.framework.util.CaseInsensitiveHashMap;
import net.ion.framework.util.CaseInsensitiveSet;

import com.mongodb.DBCursor;
import com.mongodb.DBObject;

public class ClientResult {

	private String name ;
	private String[] colNames ;
	private List<Map> rows ;
	
	private ClientResult(String name, Set<String> colNames, List<Map> rows){
		this.name = name ;
		this.colNames = colNames.toArray(new String[0]) ;
		this.rows = rows ;
	}
	
	
	public static ClientResult create(DBCursor cursor) {
		Set<String> colNames = new CaseInsensitiveSet<String>() ;
		List<Map> rows = new ArrayList<Map>() ;
		int rowCount = 0 ;
		
		while(cursor.hasNext()){
			DBObject row = cursor.next() ;
			colNames.addAll(row.keySet()) ;
			Map<String, Object> rowMap = new CaseInsensitiveHashMap<Object>();
			rowMap.putAll(row.toMap()) ;
			rows.add(rowMap) ;
			rowCount++ ;
			
		}
		if (colNames.size() == 0) colNames.add("N/A") ;
		
		return new ClientResult(cursor.toString(), colNames, rows) ;
	}
	
	public String[] getColNames(){
		return colNames ;
	}
	
	public String toString(){
		return this.name ;
	}


	public int getRowCount() {
		return rows.size();
	}


	public Map getRow(int i) {
		return rows.get(i);
	}


	public String getColName(int i) {
		return getColNames()[i-1];
	}


	public int getColSize() {
		return getColNames().length;
	}
	
	
	
}
