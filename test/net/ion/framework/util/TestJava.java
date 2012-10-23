package net.ion.framework.util;

import java.io.File;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.swing.filechooser.FileView;

import net.ion.framework.db.DBController;
import net.ion.framework.db.IDBController;
import net.ion.framework.db.manager.OraclePoolDBManager;
import net.ion.framework.parse.gson.JsonObject;
import net.ion.framework.parse.gson.JsonParser;

import org.apache.axis.utils.ByteArrayOutputStream;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.lang.ArrayUtils;

import junit.framework.TestCase;

/**
 * <p>Title: TestJava.java</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2007</p>
 * <p>Company: I-ON Communications</p>
 * <p>Date : 2011. 5. 12.</p>
 * @author novision
 * @version 1.0
 */
public class TestJava extends TestCase{

	
	public void testCollection() throws Exception {
		
		List<String> list = new ArrayList<String>() ;
		
		list.add("ad") ;
		list.add("cd") ;
		
		
		Object[] sa = list.toArray() ;
		Debug.debug(sa) ;
		
	}
	
	
	public void testHostAddress() throws Exception {
		Debug.line(InetAddress.getLocalHost().getHostAddress()) ;
	}

	public void testNumber() throws Exception {
		
		int sum = 0 ;
		for (int i = 1; i < 10000 ; i++) {
			String s = String.valueOf(i) ;
			int mcount = StringUtil.countMatches(s, "0");
			if (mcount > 0) Debug.debug(s) ;
			sum += mcount ;
		}
		Debug.line(sum) ;
	}
	
	public void testObjectId() throws Exception {
		Debug.line(new ObjectId()) ;
	}
	
	
	public void testVisitor() throws Exception {
		IOFileFilter date = FileFilterUtils.ageFileFilter(DateUtil.stringToDate("20110920-111111")) ;
		IOFileFilter dir = new IOFileFilter() {
			
			public boolean accept(File arg0, String arg1) {
				return true;
			}
			
			public boolean accept(File arg0) {
				return true;
			}
		};
		
		Iterator i = FileUtils.iterateFiles(new File("C:/temp"), date, dir) ;
		
		while(i.hasNext()){
			Debug.debug(i.next()) ;
		}
	}
	
	public enum Order {
		ASCEND, DESCEND ; 

	}
	
	public void testEnum() throws Exception {
		String order = "Ascend" ;
		
		Order ord = Order.valueOf(order.toUpperCase()) ;
		Debug.debug(ord) ;
		
	}
	
	public void testList() throws Exception {
		List list = ListUtil.newList() ;
		
		list.add("1") ;
		list.add("2") ;
		list.add("1") ;
		
		assertEquals(3, list.size()) ;
		
		list.remove("1") ;
		assertEquals(2, list.size()) ;
		
	}
	
	public void testURL() throws Exception {
		// Object content = new URL("http://localhost:9000/action?aradon.result.method=delete").getContent();
	}
	

}
