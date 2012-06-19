package net.ion.framework.db.meta;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import net.ion.framework.util.Debug;
import net.ion.framework.util.ListUtil;
import net.ion.framework.util.MapUtil;

public class DBPackage {

	private String packageName ;
	private List<DBFunction> functions ;
	private DBPackage(String packageName, List<DBFunction> functions){
		this.packageName = packageName ;
		this.functions = functions ;
	}
	
	public static DBPackage create(String packageName, List<DBFunction> functions) {
		return new DBPackage(packageName, functions);
	}

	public boolean hasFunction(Method method) {
		String methodExpression = packageName + "@" + method.getName() ;

		return matchMethod(method, methodExpression) ;
	}

	private boolean matchMethod(Method method, String methodExpression) {
		Class[] paramClasses = method.getParameterTypes() ;
		List<String> paramClassList = getParamClassList(paramClasses);
		
		for (DBFunction fn : functions) {
			List<Integer> fnParamType =  toSQLType(paramClassList);
			
			if (foundFunction(fn, methodExpression, fnParamType)){
				return true ;
			}
		}
		return false ;
	}
	
	
	
	private boolean foundFunction(DBFunction fn, String methodExpression,List<Integer> fnParamType) {
		return fn.toString().equalsIgnoreCase(methodExpression) && fn.getParamListType().toString().equals(fnParamType.toString());
	}

	private List<String> getParamClassList(Class[] paramClasses) {
		List<String> tmp = ListUtil.newList();
		for (Class pc : paramClasses) {
			tmp.add(pc.getSimpleName());
		}
		return tmp;
	}

	
	private static Map<String, Integer> typesMap = MapUtil.newMap() ;
	static {
		typesMap.put("String", 12) ;
		typesMap.put("int", 4) ;
	}
	private List<Integer> toSQLType(List<String> paramClassList) {
		List<Integer> result = ListUtil.newList() ;
		for (String javatype : paramClassList) {
//			Debug.debug(javatype); // Integer ?
			if ("Page".equals(javatype)) {
				result.add(typesMap.get("int")) ;
				result.add(typesMap.get("int")) ;
				result.add(typesMap.get("int")) ;
			} else {
				result.add(typesMap.get(javatype)) ;
			}
		}
		return result ;
	}
	
}
