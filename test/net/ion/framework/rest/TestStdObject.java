package net.ion.framework.rest;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

import javax.swing.plaf.ListUI;

import net.ion.framework.db.Page;
import net.ion.framework.util.Debug;
import net.ion.framework.util.ListUtil;

import junit.framework.TestCase;

public class TestStdObject extends TestCase{

	public void testSerialize() throws Exception {
		
		Map<String, Object> reqmap = new HashMap<String, Object>();
		reqmap.put("page", Page.create(10, 1)) ;
		reqmap.put("nots", new Date(System.currentTimeMillis())) ;
		IRequest req = IRequest.create(reqmap) ;
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("mykey", "myvalue") ;
		
		
		StdObject sto = StdObject.create(req, ListUtil.create(map), IResponse.EMPTY_RESPONSE) ;
		
		ByteArrayOutputStream bout = new ByteArrayOutputStream() ;
		ObjectOutputStream output = new ObjectOutputStream(bout);
		output.writeObject(sto) ;
		
		ObjectInputStream input = new ObjectInputStream(new ByteArrayInputStream(bout.toByteArray())) ;
		StdObject twin = (StdObject) input.readObject() ;
		
		
		assertEquals(1, twin.getDatas().size()) ;
		assertEquals(2, twin.getRequest().getAttributes().size()) ;
		
		Debug.debug(twin.getRequest().getAttributes().get("page")) ;
		
	}
}
