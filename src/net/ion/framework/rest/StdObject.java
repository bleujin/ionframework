package net.ion.framework.rest;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class StdObject implements Serializable{

	public final static StdObject EMPTY = new StdObject(IRequest.EMPTY_REQUEST, Collections.EMPTY_LIST, IResponse.EMPTY_RESPONSE) ; 
	private static final long serialVersionUID = -2187890007945716396L;

	private IRequest req ;
	private List<Map<String, ? extends Object>> datas ;
	private IResponse res;
	private StdObject(IRequest req, List<Map<String, ? extends Object>> datas, IResponse res) {
		this.req = req ;
		this.datas = datas ;
		this.res = res ;
	}

	public static StdObject create(IRequest req, List<Map<String, ? extends Object>> datas, IResponse res) {
		return new StdObject(req, datas, res);
	}

	public List<Map<String, ? extends Object>> getDatas() {
		return datas ;
	}

	public IRequest getRequest() {
		return req;
	}

	public IResponse getResponse() {
		return res;
	}
	
	public String toString(){
		return "StdObject[" + datas.size() + "]:" + hashCode() ;
	}
	
	

}
