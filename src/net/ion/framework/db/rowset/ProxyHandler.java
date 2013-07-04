package net.ion.framework.db.rowset;

import java.lang.reflect.Proxy;
import java.sql.ResultSet;

import net.ion.framework.db.rowset.ProxyResultSet.AOPHandler;

public class ProxyHandler {

	public static ResultSet create(ResultSet rs, AOPHandler aopHandler) {
		return (ResultSet)Proxy.newProxyInstance(aopHandler.getClass().getClassLoader(), new Class[]{ResultSet.class}, new ProxyResultSet(rs,aopHandler)) ;
	}

}
